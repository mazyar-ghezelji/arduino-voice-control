package mazyar.arduino;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity {
//    private final String DEVICE_NAME="MyBTBee";

    private final String DEVICE_ADDRESS="68:07:15:83:41:67";// write your evices mac here!
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    Button startButton, sendButton,clearButton,stopButton;
    TextView textView;
    TextView rv;
    EditText editText;
    ImageButton mic;
    TextToSpeech toSpeech;
    int result;

    boolean deviceConnected=false;
    Thread thread;
    byte buffer[];
    int bufferPosition;
    boolean stopThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button) findViewById(R.id.buttonStart);
        sendButton = (Button) findViewById(R.id.buttonSend);
        clearButton = (Button) findViewById(R.id.buttonClear);
        stopButton = (Button) findViewById(R.id.buttonStop);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        rv=(TextView)findViewById(R.id.responseView);
        mic = (ImageButton)findViewById(R.id.mic);

        toSpeech=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS)
                {
                    result=toSpeech.setLanguage(Locale.UK);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Feature not supported in your device",Toast.LENGTH_SHORT).show();
                }
            }
        });

        setUiEnabled(false);

    }

    public void setUiEnabled(boolean bool)
    {
        startButton.setEnabled(!bool);
        sendButton.setEnabled(bool);
        stopButton.setEnabled(bool);
        textView.setEnabled(bool);
        mic.setEnabled(bool);

    }

    public boolean BTinit()
    {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
        }
        else
        {
          //  for (BluetoothDevice iterator : bondedDevices)
           // {

             //   Toast.makeText(this,iterator.getAddress(),Toast.LENGTH_LONG).show();

           // }
            for (BluetoothDevice iterator : bondedDevices)
            {

                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                   //  Toast.makeText(this,iterator.getAddress(),Toast.LENGTH_LONG).show();
                    device=iterator;
                    found=true;
                    break;
                }
            }
        }
        return found;
    }

    public boolean BTconnect()
    {
        boolean connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"socket doesnt connect",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try {
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(connected==true)
            Toast.makeText(getApplicationContext(),"connected ",Toast.LENGTH_SHORT).show();
        return connected;
    }

    public void onClickStart(View view) {
        if(BTinit())
        {
            if(BTconnect())
            {
                setUiEnabled(true);
                deviceConnected=true;
                beginListenForData();
                textView.append("\nConnection Opened!\n");
            }

        }
    }

    void beginListenForData()
    {
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread  = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {
                    try
                    {
                        int byteCount = inputStream.available();
                        if(byteCount > 0)
                        {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                           final String string=new String(rawBytes,"UTF-8");
                            handler.post(new Runnable() {
                                        public void run()
                                        {
                                            rv.append(string);
                                            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                                            {
                                                Toast.makeText(getApplicationContext(),"Feature not supported in your device",Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                toSpeech.speak(rv.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                                            }
                                        }
                                    });

                        }

                    }
                    catch (IOException ex)
                    {
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }

    public void onClickSend(View view) {
        rv.setText("");
        String string = editText.getText().toString();
        string.concat("\n");
        try {
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView.append("\nSent Data:"+string+"\n");

    }

    public void onClickStop(View view) throws IOException {
        stopThread = true;
        outputStream.close();
        inputStream.close();
        socket.close();
        setUiEnabled(false);
        deviceConnected=false;
        textView.append("\nConnection Closed!\n");
    }

    public void onClickClear(View view) {
        textView.setText("");
    }

    public void getSpeechInput(View view) {
        rv.setText("");

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText.setText(result.get(0));
                    String string = editText.getText().toString();
                    string.concat("\n");
                    try {
                        outputStream.write(string.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    textView.append("\nSent Data:"+string+"\n");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toSpeech!=null)
        {
            toSpeech.stop();
            toSpeech.shutdown();
        }
    }
}

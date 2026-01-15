# Arduino Voice Control

A bidirectional communication system between Android devices and Arduino using Bluetooth, featuring voice command recognition and text-to-speech capabilities for IoT applications.

## Overview

This project enables wireless control of Arduino-based devices through voice commands from an Android smartphone. The system uses Bluetooth (HC-05/HC-06 module) for communication and includes both voice recognition for sending commands and text-to-speech for receiving feedback from the Arduino.

## Features

- **Voice Command Recognition**: Control Arduino devices using natural voice commands
- **Text-to-Speech Feedback**: Receive audio feedback from Arduino operations
- **Bluetooth Communication**: Wireless connection via HC-05/HC-06 Bluetooth module
- **Bidirectional Data Transfer**: Send commands to Arduino and receive status updates
- **Android Integration**: Custom Android app for seamless interaction

## Hardware Requirements

- Arduino board (Uno, Mega, or compatible)
- HC-05 or HC-06 Bluetooth module
- Android smartphone (with Bluetooth and Google Speech Recognition)
- LEDs, relays, or other output devices (based on your application)
- Connecting wires and breadboard
- Power supply for Arduino

## Software Requirements

- Arduino IDE (latest version)
- Android device with:
  - Bluetooth capability
  - Google Text-to-Speech (TTS)
  - Google Speech Recognition
  - Internet connection (for voice recognition)

## Hardware Setup

### Bluetooth Module Connections

Connect the HC-05/HC-06 Bluetooth module to Arduino:

```
HC-05/HC-06    →    Arduino
VCC            →    5V
GND            →    GND
TXD            →    RX (Pin 0)
RXD            →    TX (Pin 1)
```

**Important**: Disconnect the Bluetooth module from pins 0 and 1 (TX/RX) before uploading code to the Arduino to avoid upload conflicts.

### Output Devices

Connect your output devices (LEDs, relays, etc.) to digital pins 2-13 or analog pins A0-A5, depending on your application requirements.

## Installation

### Arduino Setup

1. Install the Arduino IDE from [arduino.cc](https://www.arduino.cc/en/software)
2. Clone this repository:
   ```bash
   git clone https://github.com/mazyar-ghezelji/arduino-voice-control.git
   ```
3. Open `sketch_jan05a.ino` in Arduino IDE
4. Select your Arduino board under **Tools → Board**
5. Select the correct serial port under **Tools → Port**
6. **Disconnect the Bluetooth module** from pins 0 and 1
7. Upload the sketch to your Arduino
8. **Reconnect the Bluetooth module** after upload completes

### Android Setup

1. Navigate to the `arduino` directory in the repository
2. Build the Android app using Android Studio or install the provided APK
3. Enable Bluetooth on your Android device
4. Install Google Text-to-Speech if not already available
5. Ensure Google Speech Recognition is set up on your device

## Usage

### Pairing the Bluetooth Module

1. Power on your Arduino
2. Open Bluetooth settings on your Android device
3. Search for available devices
4. Pair with the HC-05/HC-06 module (default PIN is usually `1234` or `0000`)

### Using the Application

1. Launch the Android app
2. Connect to the paired Bluetooth module
3. Speak voice commands clearly into your device
4. The app will:
   - Convert your voice to text using Google Speech Recognition
   - Send the command to Arduino via Bluetooth
   - Arduino processes the command and executes the corresponding action
   - Arduino can send feedback which is converted to speech

### Example Voice Commands

Customize these commands in the Arduino sketch based on your application:

- "Turn on light"
- "Turn off light"
- "Start motor"
- "Stop motor"
- "Open door"
- "Close door"

## How It Works

### Communication Flow

1. **Voice Input**: User speaks a command into the Android app
2. **Speech-to-Text**: Google Speech Recognition converts voice to text
3. **Bluetooth Transmission**: Text command is sent via Bluetooth to Arduino
4. **Arduino Processing**: Arduino receives and parses the command
5. **Action Execution**: Arduino controls connected devices based on the command
6. **Feedback (Optional)**: Arduino sends status back to Android
7. **Text-to-Speech**: Android app converts Arduino response to voice feedback

### Protocol

The system uses serial communication over Bluetooth with a simple text-based protocol. Commands are typically terminated with a special character (like `#`) to indicate end of transmission.

## Customization

### Adding New Commands

In the Arduino sketch, add your command handling in the main loop:

```cpp
if (voice == "*your command") {
    // Your action here
    digitalWrite(yourPin, HIGH);
}
```

### Adjusting Baud Rate

Ensure the baud rate matches between Arduino code and the Bluetooth module (default is usually 9600):

```cpp
Serial.begin(9600);
```

## Applications

This voice control system can be used for:

- Smart home automation
- Robotic control
- Industrial IoT applications
- Accessibility devices
- Educational projects
- Remote device monitoring and control

## Troubleshooting

**Bluetooth won't connect:**

- Verify the module is powered and in pairing mode
- Check if the default PIN is correct
- Ensure Bluetooth is enabled on your Android device

**Commands not recognized:**

- Check internet connection (required for Google Speech Recognition)
- Speak clearly and reduce background noise
- Verify command strings match in both Arduino and Android app

**Arduino not responding:**

- Ensure Bluetooth module connections are correct
- Check serial baud rate settings
- Verify the code uploaded successfully

**Upload fails:**

- Disconnect Bluetooth module from pins 0 and 1 before uploading
- Select the correct board and port in Arduino IDE

## Contributing

Contributions are welcome! Feel free to:

- Report bugs
- Suggest new features
- Submit pull requests
- Improve documentation

## License

This project is open source and available for educational and personal use.

## Acknowledgments

- Uses Android Speech Recognition API
- Compatible with HC-05 and HC-06 Bluetooth modules
- Built for Arduino platform

## Contact

For questions or support, please open an issue in the GitHub repository.

---

**Note**: This project requires an active internet connection on the Android device for voice recognition to function properly, as it uses Google's cloud-based speech recognition service.

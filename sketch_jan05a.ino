#include <SimpleDHT.h>

#define pinDHT11 7
String voice; 
SimpleDHT11 dht11(pinDHT11);

void setup() {
pinMode(13, OUTPUT);  
Serial.begin(9600);
} 
void loop() {
  while (Serial.available()){ 
    delay(10); 
    char c = Serial.read(); 
    voice += c; 
  }  
  if (voice.length() > 0){
     if(voice=="what is the temprature" ||voice=="what is the weather" ||voice=="tell me the weather" ||voice=="tell me the temprature"){
     byte temperature = 0;
     byte humidity = 0;
     int err = SimpleDHTErrSuccess;
     if ((err = dht11.read(&temperature, &humidity, NULL)) != SimpleDHTErrSuccess) {
     Serial.write(""); Serial.write(err);delay(1000);
     return;
     }
  
      //Serial.write("response : ");
      int t=(int)temperature;
      String st=String(t);
      int h=(int)humidity;
      String sh=String(h);
      
      Serial.write(" the temprature is "); writeString(st);Serial.write(" degrees "); 
      Serial.write(" the humidity is "); writeString(sh);Serial.write(" percent ");
     }

     /////////////////////////////////////////

 if(voice=="what should I wear" || voice=="tell me the best clothing"){
     byte temperature = 0;
     byte humidity = 0;
     int err = SimpleDHTErrSuccess;
     if ((err = dht11.read(&temperature, &humidity, NULL)) != SimpleDHTErrSuccess) {
     Serial.write(""); Serial.write(err);delay(1000);
     return;
     }
  
      //Serial.write("response : ");
      int t=(int)temperature;
      String st=String(t);
      int h=(int)humidity;
      String sh=String(h);

    if(t<10 && h<50){

      Serial.write("Temperature may drop below freezing . A jacket, heavier clothing, and a coat may be necessary");
    }
    if(t>10 && t<20 && h<50){

      Serial.write("Temperatures vary from warm to cool. A jacket may be necessary");
    }
    if(t>20 && h<50){

      Serial.write("Temperatures are generally hot. Light clothing is preferred. This is usually dry");
    }
    if(t<10 && h>50){

      Serial.write(" Temperature may drop below freezing with occasional snow and sleet. A jacket, heavier clothing, and a coat may be necessary.");
    }
    if(t>10 && t<20 && h>50){

      Serial.write(" Temperatures vary from warm to cool. A jacket may be necessary. Rain occurs occasionally.");
    }
    if(t>20 && h>50){

      Serial.write("Temperatures are generally hot and humid. Light clothing is preferred. you may need an umbrella");
    }
    
     }

     /////////////////////////////////////

   
     if(voice == "on"){
      digitalWrite(13, HIGH);
     }  
     if(voice == "off")
     {
      digitalWrite(13, LOW);
     } 
    voice="";
  }
  
}
void writeString(String sd)
{
  for(int i=0;i<sd.length();i++)
  {
    Serial.write(sd[i]);
  }
}

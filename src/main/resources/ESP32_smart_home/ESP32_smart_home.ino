#define LED 2
#include <Wire.h>
#include <Adafruit_BMP085.h>
#include "BluetoothSerial.h"

Adafruit_BMP085 bmp;
BluetoothSerial SerialBT;

void setup() {
  Serial.begin(9600);
  pinMode(LED, OUTPUT);
  digitalWrite(LED, HIGH);

  if (bmp.begin ()) {
  Serial.println ( "Sensor BMP085 not found, check the connections!" );
  }

Serial.println("Begin");

if (!SerialBT.begin("ESP32")) {
    Serial.println("An error occurred initializing Bluetooth");
  }
  //digitalWrite(LED_BUILTIN, HIGH);
}

void loop() {
  delay(100);

  while (SerialBT.available()) {

    char val = SerialBT.read();
    if (val == '1') {
      blink_led(3);
    }
    if (val == '2') {
      digitalWrite(LED, HIGH);
    }
    if (val == '3') {
      digitalWrite(LED, LOW);
    }
  }
}

// преобразование в HEX
void view_data (byte *buf, byte size) {
  for (byte j = 0; j < size; j++) {
    //Serial.print(buf [j]);
    Serial.print(buf [j], HEX);
  }
}

void blink_led (int count) {
  for (int i = 0; i < count; i++) {
      digitalWrite(LED, HIGH);
      delay(50);
      digitalWrite(LED, LOW);
      delay(100);
  }

    SerialBT.print(bmp.readTemperature(), 1);
    SerialBT.print(" ");
    SerialBT.println(bmp.readPressure());
}

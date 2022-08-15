#define LED 13

void setup() {
  Serial.begin(9600);
  pinMode(LED, OUTPUT);

  //digitalWrite(LED_BUILTIN, HIGH);
}

void loop() {
  delay(100);

  while (Serial.available()) {

    char val = Serial.read();
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
}

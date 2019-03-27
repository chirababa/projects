 #include <VirtualWire.h>  
 const int led_pin = 13;  

 //digital
const int SwitchPin = 10;

//analog
const int XPin =A0;
const int YPin =A1;

//const int XPinRL =A2;
//const int YPinRL =A3;
 void setup()  
 {  
 // Initialize the IO and ISR  
   Serial.begin(9600);
  digitalWrite(SwitchPin,HIGH);
 vw_setup(2000); // Bits per sec  
 }  
 void loop()  
 {  
 digitalWrite(led_pin, HIGH); // Flash a light to show transmitting  
 int xValue =analogRead(XPin);
  int yValue =analogRead(YPin);
  int switchValue=digitalRead(SwitchPin);

  //int xValueRL =analogRead(XPinRL);
   //int yValueRL =analogRead(YPinRL);

   //Serial.println(xValueRL);
   //Serial.println(yValueRL);
  char directionBuffer[1];
  
  //stop
  if(xValue>490 && xValue<502 && yValue>490 && yValue<512)
  {
    directionBuffer[0]='S';
  }
  
  //fata
  else if (yValue>=0 && yValue<400)
  {
      directionBuffer[0]='L';
  }
  
  //spate
   else if (yValue>=600 && yValue<=1023)
  {
      directionBuffer[0]='R';
  }
  
  //stanga
   else if (xValue>=0 && xValue<=400)
  {
      directionBuffer[0]='F';
  }
  
  //dreapta
   else if (xValue>=600 && xValue<=1023)
  {
      directionBuffer[0]='B';
  }
  
  Serial.println(directionBuffer);
  send(directionBuffer);    
 }  

 
 void send (char *message)  
 {
 vw_send((uint8_t *)message, 1);  
 vw_wait_tx(); // Wait until the whole message is gone  
 digitalWrite(led_pin, LOW); // Flash a light to show transmitting  
 }  

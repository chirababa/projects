 #include <VirtualWire.h>
 #include <String.h>
 byte message[VW_MAX_MESSAGE_LEN]; // a buffer to store the incoming messages  
 byte messageLength = VW_MAX_MESSAGE_LEN; // the size of the message

// Pins of motor 1
#define mpin00 6
#define mpin01 5

// Pins of motor 2

#define mpin10 3
#define mpin11 4

//servo stuff
#define ECHOPIN 8        // Pin to receive echo pulse
#define TRIGPIN 12        // Pin to send trigger pulse


int switchVar=1;

 void setup()  
 {  

 digitalWrite(mpin00, 0);
 digitalWrite(mpin01, 0);
 digitalWrite(mpin10, 0);
 digitalWrite(mpin11, 0);
 pinMode (mpin00, OUTPUT);
 pinMode (mpin01, OUTPUT);
 pinMode (mpin10, OUTPUT);
 pinMode (mpin11, OUTPUT);

 // sevo stuff
 
  pinMode(ECHOPIN, INPUT);
  pinMode(TRIGPIN, OUTPUT);

 
 Serial.begin(9600);  
 Serial.println("Receptorul e pregatit sa primeasca mesaje...");  
 // Initialize the IO and ISR  
 vw_setup(2000); // Bits per sec  
 vw_rx_start(); // Start the receiver  
 }  
  
int computeDistance(int CYCLES, float error){
  float total_nr_read=1;
  float distance_in_cm;
  float medie = 0;
  digitalWrite(TRIGPIN, LOW);
    delayMicroseconds(2);
    digitalWrite(TRIGPIN, HIGH);
    delayMicroseconds(2);
    digitalWrite(TRIGPIN, LOW);
  float distance_reference = pulseIn(ECHOPIN, HIGH);
  distance_reference = distance_reference; 
  Serial.println(distance_reference);
  for (int i=1; i <= CYCLES; i++){ 
    digitalWrite(TRIGPIN, LOW);
    delayMicroseconds(2);
    digitalWrite(TRIGPIN, HIGH);
    delayMicroseconds(2);
    digitalWrite(TRIGPIN, LOW);
  
    distance_in_cm = pulseIn(ECHOPIN, HIGH);
    distance_in_cm = distance_in_cm;
    if(abs(distance_in_cm - distance_reference)< error){
      medie = medie + distance_in_cm;
      total_nr_read++;
    }
    //delay(1);
  }
  if ((medie/total_nr_read)>=error)return 1;
  else return 0;
}
 void loop()  
 {    
     if(vw_get_message(message, &messageLength))// Non-blocking
     {
      vw_wait_rx(); 
       Serial.write(message[0]);  
       Serial.println(message[0]);  
      }

     //stop
    if(message[0]==83)
    {
      //vw_rx_stop();
      Serial.println(message[0]);
      //delayStopped(10);
      StartMotor (mpin00, mpin01, 0, 0);
     StartMotor (mpin10, mpin11, 0, 0);
     //delayStopped(10);
    
    }
    
    //computeDistance ne da un rezultat pozitiv daca avem posibilitatea sa mergem drept in fata
    // primul parametru: CYCLES == numar de pasi pentru detectarea marjei de eroare
    // al doilea parametru error == distanta minima la care are voie sa ajunga, in centrimetri 
    if(computeDistance(10,15.0f)==1){
      
      switchVar=1;//resetam 
      //left
      if(message[0]==76)
      {
        //vw_rx_stop();
        Serial.println(message[0]);
        StartMotor (mpin00, mpin01, 0, 128);
       StartMotor (mpin10, mpin11, 0, 128);;
       //delayStopped(10);
      
      }
      //right
      else if(message[0]==82){
        Serial.println(message[0]);
      //vw_rx_stop();
       StartMotor (mpin00, mpin01, 1, 128);
       StartMotor (mpin10, mpin11, 1, 128);
      }
      
      //spate
      else if(message[0]==66){
        Serial.println(message[0]);
      //vw_rx_stop();
       StartMotor (mpin00, mpin01, 0, 128);
       StartMotor (mpin10, mpin11, 1, 128);
      }
      
      //fata
      else if(message[0]==70){
        Serial.println(message[0]);
        //vw_rx_stop();
       StartMotor (mpin00, mpin01, 1, 128); 
       StartMotor (mpin10, mpin11, 0, 128);
      }
    }
    else {
      if(switchVar==1)delayStopped(10);//pentru a nu face de mai multe ori delay
      switchVar=0;
      ///posibilitate de a da doar cu spatele 
      if(message[0]==66){
        Serial.println(message[0]);
        //vw_rx_stop();
       StartMotor (mpin00, mpin01, 0, 128);
       StartMotor (mpin10, mpin11, 1, 128);
      }
    }
 }

 
 void StartMotor (int m1, int m2, int forward, int speed)
{
   //Serial.println("debug1231314");
     if (speed==0) // stop
     {
      //Serial.println("debug1");
     digitalWrite(m1, 0); 
     digitalWrite(m2, 0);
     
     }
     else
     {
     if (forward)
     {
      //Serial.println("debug2");
     digitalWrite(m2, 0);
     analogWrite (m1, speed); // use PWM
     
     }
     else
     {
       //Serial.println("debug3");
     digitalWrite(m1, 0);
     analogWrite(m2, speed);
    
     }
     }
}

// Safety function
// Commands motors to stop, then delays

void delayStopped(int ms)
{
 StartMotor (mpin00, mpin01, 0, 0);
 StartMotor (mpin10, mpin11, 0, 0);
 delay(ms);
}

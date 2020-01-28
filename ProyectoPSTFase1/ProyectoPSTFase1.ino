#include <SoftwareSerial.h>//libreria para analizar

//CORRESPONDENCIA PINES
int PinPiezo = A1;
#define PinLedVerde 6
#define PinLedRojo 7
#define PinBoton 13

//INICIALIZACION (Tiempo en ms)
#define TiempoPulsoMin 100 // Tiempo minimo entre pulsos consecutivos
#define ErrorMax 200 //Error máximo entre pulsos
#define TimeOut 1500 // Tiempo máximo de espera para grabar/detectar pulsos. 
#define PulsosMax 30 //Pulsos maximos en una secuencia
#define TiempoInicioGrabacion 10000 //Tiempo de espera maximo para empezar a grabar tras entrar en el modo grabacion
#define TiempoParpadeoLed 50 // Tiempo de parpadeo del led
#define TiempoPuerta 5000 // Tiempo que el motor mantiene la puerta abierta
#define CodigoMemoria 120 //Codigo guardado en la primera posicion de la EEPROM para comprobar que hay datos guardados

const int UmbralPiezo = 100;
#include <EEPROM.h>
#include <Stepper.h>
byte codigo[PulsosMax];

const int stepsPerRevolution = 360;

SoftwareSerial toESP(4, 5); //Rx, Tx con esto se inicia una comunicacion serial entre el arduino y modulo wifi ESP8266-01

Stepper myStepper(stepsPerRevolution, 8, 9, 10, 11);//se setean los pines que controlaran al motor a paso para abrir la puerta

void setup() {

  Serial.begin(9600);// serial para ver desde el pc
  toESP.begin(115200);//serial de comunicacion entre arduino y ESP8266-01

  myStepper.setSpeed(60);

  //PinMode
  pinMode(PinLedVerde, OUTPUT);
  pinMode(PinLedRojo, OUTPUT);
  pinMode(PinBoton, INPUT_PULLUP);

  //Cargar EEPROM
  LedON(PinLedVerde);
  LedON(PinLedRojo);
  CargarMemoria();
  LedOFF(PinLedVerde);
  LedOFF(PinLedRojo);


}

void loop() {

  //MODO GRABACIÓN DEL CODIGO
  if (LecturaBoton()) {

    //Modo grabacion
    LedON(PinLedVerde);
    unsigned long tmax = millis() + TiempoInicioGrabacion;
    //Espera a golpear para iniciar a grabar
    while (Golpe() == 0) {
      //Si el tiempo es mayor al de espera de inicio, salimos del modo grabacion y borramos la memoria
      if (millis() > tmax) {
        LedON(PinLedRojo);
        BorrarMemoria();
        BorrarCodigo();
        LedOFF(PinLedVerde);
        LedOFF(PinLedRojo);
        return;
      }
    }
    //Codigo guardado correctamente
    if (GuardarCodigo() == 1) {
      GuardarMemoria();
      Parpadeo(PinLedVerde, 5);
    }
    //Error al guardar el codigo
    else {
      //Serial.println("error");
      Parpadeo(PinLedRojo, 5);
    }
    LedOFF(PinLedVerde);
  }

  //MODO LECTURA DE GOLPES
  

  if (Golpe()) {//Si introdujo correctamente la clave de voz
    char inChar;//se vacia

    if (toESP.available()) {//intenta leer el serial que printea el modulo ESP8266-01
      inChar = toESP.read();
    }
    if (inChar == 'E') {//detecta un golpe
      if (LeerCodigo() == 1) {//Codigo correcto
        LedON(PinLedVerde);
        toESP.println('G');
        AbrirPuerta();
        LedOFF(PinLedVerde);
      }

      else {//Codigo incorrecto
        toESP.println('B');
        Parpadeo(PinLedRojo, 10);
      }
    }
  }

}

/////////////////////////////////////////


boolean Golpe() {
  static unsigned long tmin = 0;
  static boolean estado = 0;
  //Tiempo antirrebotes superado
  if (millis() > tmin) {
    //Serial.println(analogRead(PinPiezo));
    //Lectura analogica por encima del umbral
    if (analogRead(PinPiezo) > UmbralPiezo) {
      tmin = millis() + TiempoPulsoMin; //Tiempo antirrebotes
      LedON(PinLedRojo);
      estado = 1;
      return 1;
    }
    //Umbral no superado
    else {
      if (estado) {
        estado = 0;
        LedOFF(PinLedRojo);
      }
      return 0;
    }
  }
  else return 0;
}

boolean GuardarCodigo() {
  unsigned long t = millis();
  int addr = 0;
  //Ponemos el código a 0
  BorrarCodigo();
  //Mientras sigamos dando golpes
  while (millis() < t + TimeOut) {
    //Golpe detectado
    if (Golpe()) {
      //Guardamos el tiempo entre el pulso anterior y el actual
      codigo[addr] = (millis() - t) / 10;
      t = millis();
      addr++;
    }
  }
  //No se a guardado ningun golpe
  if (addr == 0) return 0;
  //Se ha guardado almenos un golpe
  else return 1;
}

boolean LeerCodigo() {
  unsigned long t = millis();
  int addr = 0;
  boolean ok = 1;
  //Mientras sigamos dando golpes
  while (millis() < t + TimeOut) {
    //Golpe detectado
    if (Golpe()) {
      unsigned long tiempo = millis() - t; //Tiempo entre el pulso anterior y el actual
      t = millis();
      int Tmin = tiempo - ErrorMax; //Margen de tiempo inferior
      int Tmax = tiempo + ErrorMax; //Margen de tiempo superior
      Tmin = constrain(Tmin, 0, TimeOut);
      //Tiempo de pulso correcto dentro de los margenes
      if (codigo[addr] * 10 > Tmin && codigo[addr] * 10 < Tmax) addr++;
      //Error
      else ok = 0;
    }
  }
  //El golpe ha sido el ultimo almacenado
  if (codigo[addr] == 0 && addr > 0) return ok;
  //Hay mas golpes almacenados
  else return 0;
}

boolean LecturaBoton() {
  //Serial.println(!digitalRead(PinBoton));
  return !digitalRead(PinBoton);
}

void Parpadeo(int led, int n) {
  for (int i = 0; i < n; i++) {
    digitalWrite(led, HIGH);
    delay(TiempoParpadeoLed);
    digitalWrite(led, LOW);
    delay(TiempoParpadeoLed);
  }
}

void LedON(int led) {
  digitalWrite(led, HIGH);
}

void LedOFF(int led) {
  digitalWrite(led, LOW);
}

void AbrirPuerta() {
  //delay(TiempoPuerta);
  //Serial.println("abriendo");
  myStepper.step(1800);
  delay(6000);

  //Serial.println("cerrando");
  myStepper.step(-1800);
  delay(1000);
}

void GuardarMemoria() {
  //Borrado de los datos anteriores
  BorrarMemoria();
  //Codigo de sincronizacion
  EEPROM.update(0, CodigoMemoria);
  int addr = 0;
  while (codigo[addr] != 0) {
    //Guardado del codigo en la memoria eeprom
    EEPROM.write(addr + 1, codigo[addr]);
    addr++;
  }
}

void CargarMemoria() {
  //Hay datos guardados?
  if (EEPROM.read(0) == CodigoMemoria) {
    int addr = 0;
    while (1) {
      //Cargamos el valor de la eeprom
      codigo[addr] = EEPROM.read(addr + 1);
      //Codigo cargado completamente
      if (codigo[addr] == 0) break;
      addr++;
    }
  }
  else BorrarMemoria();
  delay(1000);
}

void BorrarMemoria() {
  for (int i = 0; i < 1023; i++) EEPROM.update(i, 0);
}

void BorrarCodigo() {
  for (int i = 0; i < PulsosMax; i++) codigo[i] = 0;
}

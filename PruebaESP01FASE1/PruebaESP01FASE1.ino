#include <SoftwareSerial.h>

#include <Firebase.h>
#include <FirebaseArduino.h>
#include <FirebaseCloudMessaging.h>
#include <FirebaseError.h>
#include <FirebaseHttpClient.h>
#include <FirebaseObject.h>

#include <ESP8266WiFi.h>//Incluye la librería para controlar el módulo ESP8266
#include <FirebaseArduino.h>//Incluye la librería para utilizar Firebase



#define FIREBASE_HOST "base-de-datos-pst-grupo2.firebaseio.com"//Define las credenciales para la base de datos de Firebase, en este caso el Host
#define FIREBASE_AUTH "PqE9qy7A7ojPIubKdJETTyKUmJOHfI4dRV1SvyZL"//Define las credenciales para la base de datos de Firebase, en este caso el código secreto                  
#define WIFI_SSID "NETLIFE-RODRIGUEZ"//Define el nombre de la red WiFi                                         
#define WIFI_PASSWORD "Better123"//Define la contraseña de la red WiFi


//Se da el valor de el número de pasos por una vuelta
int n;//Variable para manejo de datos de las base de datos


void setup() {
  // put your setup code here, to run once:

  Serial.begin(115200);//Se incia la conexión Serial


  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);//Se inicia la conexión
  //Serial.print("Connecting to Wi-Fi");//Mensaje por pantalla
  while (WiFi.status() != WL_CONNECTED)//Se espera por que se conecte al WiFi
  {
    //Serial.print(".");
    delay(500);
  }
  //Serial.println();
  //Serial.print("Connected with IP: ");
  //Serial.println(WiFi.localIP());
  //Serial.print("Conectado actualmente a: ");
  //Serial.println(WIFI_SSID);
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);//Se empieza la conexión con el Firebase

}


void loop() {
  n = Firebase.getInt("Estado");
  if (Firebase.failed()) {
    //Serial.print("fallo en obtener el estado, reintentando...");
    //Serial.println(Firebase.error());
    return;
  }
  delay(1000);
  //Se toma el valor de Estado de la base de datos
  //Serial.print("El estado actual de la base de datos es: ");//Se imprime por conexión serial el Estado de la base de datos
  //Serial.println(n);//Se imprime el valor de la base de datos
  delay(500);
  //Serial.print("El estado actual del sensor de golpeo es: ");//Se imprime por conexión serial el Estado del sensor de Golpeo
  //Serial.println(digitalRead(5));
  delay(500);


  char inChar;

  if (n == 1) { //cuando el ususario ingresa su contraseña de voz correctamente
    Serial.println('E');



    delay(15000);

    if (Serial.available()) {
      inChar = Serial.read();
    }

    if (inChar == 'G') { //Se establece la condición para que la puerta se abra cuando el sensor de vibración esté activo
      //Serial.print("ABRIENDO");
      delay(6500);
      //Serial.print("CERRANDO");
      Firebase.setInt("Estado", 0);
      if (Firebase.failed()) {
        //Serial.print("fallo seteando el estado, reintentando...");
        //Serial.println(Firebase.error());
        return;

      }

      Firebase.setInt("Acceso", 1);
      if (Firebase.failed()) {
        //Serial.print("fallo seteando el estado, reintentando...");
        //Serial.println(Firebase.error());
        return;


      }
      delay(3000);//Se desactiva el control de la base de datos
    }

    if (inChar == 'B') {
      Firebase.setInt("Estado", 0);
      if (Firebase.failed()) {
        //Serial.print("fallo seteando el estado, reintentando...");
        //Serial.println(Firebase.error());
        return;
      }

      Firebase.setInt("Acceso", 0);
      if (Firebase.failed()) {
        //Serial.print("fallo seteando el estado, reintentando...");
        //Serial.println(Firebase.error());
        return;

        delay(3000);
      }

    }

    Firebase.setInt("Acceso", 2);
    if (Firebase.failed()) {
      //Serial.print("fallo seteando el estado, reintentando...");
      //Serial.println(Firebase.error());
      return;
    }
  }
  if (n == 0) { //Se espera que se de se tenga la autorización desde el dispositivo móvil
    //Serial.println("Esperando Autorización desde el Dispositivo Móvil...");
    delay(1000);
  }
}

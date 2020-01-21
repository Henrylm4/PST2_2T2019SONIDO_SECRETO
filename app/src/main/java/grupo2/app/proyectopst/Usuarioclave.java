package grupo2.app.proyectopst;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;



import grupo2.app.proyectopst.models.Persona;
import grupo2.app.proyectopst.models.Registro;

import static grupo2.app.proyectopst.Administracion_Accesos.ListP;
import static grupo2.app.proyectopst.Firebase.dbrefence;

public class Usuarioclave extends AppCompatActivity {//Clase para comparar la clave ingresada con la de los usuarios en la base de datos
    String clave;
    private static final int REQ_CODE_SPEECH_INPUT=1;//Constante para el uso del reconocimiento de voz
    private ImageButton Bvoz;//Botón para iniciar el reconocimiento de voz
    boolean bool = true;
    private static final int PERMISSION_SEND_SMS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarioclave);
        Bvoz = findViewById(R.id.Btn_voz);//Se busca el botón para el ingreso de la clave



        listarDatos();//

    }

    public void comparar(){//Función destinada para comparar la clave ingresada con la de los usuarios en la base de datos
        boolean bool = false;
        Persona pentrante;
        for(Persona p:ListP){//Se recorre la lista de todas las personas registradas
            System.out.println(p.getClave());
            if(p.getClave().equals(clave)){//si coinciden las claves se procede a enviar un Toast ademas de cambiar el valor de "Estado" a 1, donde el arduino lo detectará para proceder a abrir la puerta
                dbrefence.child("estado").setValue(true);
                Toast.makeText(this,"Acceso concedido",Toast.LENGTH_SHORT).show();
                bool=true;
                pentrante=p;
                Date fecha= new Date();
                Registro registro = new Registro(pentrante, fecha);//Se crea un registro para guardarlo en la base de datos
                registro.setId(UUID.randomUUID().toString());
                dbrefence.child("Registro").child(registro.getId()).setValue(registro);


            }
        }

        if (bool){

        }
        else{
            Toast.makeText(this,"Acceso denegado",Toast.LENGTH_SHORT).show();
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS);
            }
            else
            {
                //do send or read sms
            }
            String phone = "0996343536";//obtiene datos para enviar un sms en caso de que se haya ingresado una clave incorrecta
            String text = "Se ha registrado el ingreso de un usuario no válido";
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phone, null, text , null, null);
        }

    }
    private void listarDatos() {//obtiene los objetos personas guardadas en la base de datos
        dbrefence.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListP.clear();
                for(DataSnapshot objsnpashot : dataSnapshot.getChildren()){
                    Persona p = objsnpashot.getValue(Persona.class);
                    ListP.add(p);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void IniciarEntradaVoz(View view){//Función para activar el reconocimiento de voz
        Intent intentActionRecognizeSpeech = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Configura el Lenguaje (Español-México)
        intentActionRecognizeSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-MX");
        try {
            startActivityForResult(intentActionRecognizeSpeech,
                    REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Tú dispositivo no soporta el reconocimiento por voz",
                    Toast.LENGTH_SHORT).show();


        }
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT:
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> speech = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String strSpeech2Text = speech.get(0);
                        clave=strSpeech2Text;
                        comparar();

                    }
                    break;
                default:
                    break;
            }
        }

        public void CambiarEstado(View view){
            if(bool){
                dbrefence.child("estado").setValue(true);
                bool=false;

            }
            else{
                dbrefence.child("estado").setValue(false);
                bool=true;

            }
        }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch(requestCode)
        {
            case PERMISSION_SEND_SMS:
                if(grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //do send or read sms
                }
                break;
        }
    }


}

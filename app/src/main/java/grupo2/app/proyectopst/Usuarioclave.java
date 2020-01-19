package grupo2.app.proyectopst;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import grupo2.app.proyectopst.models.Persona;
import grupo2.app.proyectopst.models.Registro;

import static grupo2.app.proyectopst.Administracion_Accesos.ListP;
import static grupo2.app.proyectopst.FirebasePersistance.dbrefence;
import static grupo2.app.proyectopst.FirebasePersistance.firebasedb;

public class Usuarioclave extends AppCompatActivity {
    private EditText clave;
    private static final int REQ_CODE_SPEECH_INPUT=1;
    private ImageButton Bvoz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarioclave);
        Bvoz = findViewById(R.id.Bvoz);
        listarDatos();

    }

    public void comparar(View view){
        boolean bool = false;
        clave = findViewById(R.id.clave);
        Persona pentrante;
        for(Persona p:ListP){
            System.out.println(p.getClave());
            if(p.getClave().equals(clave.getText().toString())){
                Toast.makeText(this,"agregado",Toast.LENGTH_SHORT).show();
                bool=true;
                pentrante=p;
                Date fecha= new Date();
                Registro registro = new Registro(pentrante, fecha);
                registro.setId(UUID.randomUUID().toString());
                dbrefence.child("Registro").child(registro.getId()).setValue(registro);

            }
        }

        if (bool){

        }
        else{
            Toast.makeText(this,"denegado",Toast.LENGTH_SHORT).show();

        }

    }
    private void listarDatos() {
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

    public void IniciarEntradaVoz(View view){
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


        }}

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT:
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> speech = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String strSpeech2Text = speech.get(0);
                        clave.setText(strSpeech2Text);
                    }
                    break;
                default:
                    break;
            }
        }
}

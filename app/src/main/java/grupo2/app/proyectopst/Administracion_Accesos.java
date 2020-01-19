package grupo2.app.proyectopst;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import grupo2.app.proyectopst.models.Persona;

import static grupo2.app.proyectopst.Firebase.dbrefence;
import static grupo2.app.proyectopst.Firebase.firebasedb;

public class Administracion_Accesos extends AppCompatActivity {
    public static List<Persona> ListP = new ArrayList<Persona>(); //Lista de Personas donde van a hacer almacenadas todas las personas agregadas en la base de datos previamente
    ArrayAdapter<Persona> arrayAdapterPersona; //ArrayAdapter al momento de agregar personas al listview
    Persona personaSeleccionada; //Objeto persona para obtener atributos
    EditText nombres,apellidos,clave,cargo; //EditText usados en las pantallas donde se escribirá los datos a guardar de cada usuario
    ListView listaP; //Listview para mostrar a las personas en forma de lista
    private static final int REQ_CODE_SPEECH_INPUT=1; //Parámetro creado para la función de reconocimiento de voz



    @Override
    protected void onCreate(Bundle savedInstanceState) { //Se encuentran todos los editText y views creados, además de iniciar las funciones creadas, también se creó un onItemclistener para obtener los datos de cada persona seleccionada
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administracion__accesos);
        //inicializarfb();
        nombres= findViewById(R.id.Nombres);
        apellidos= findViewById(R.id.Apellidos);
        clave= findViewById(R.id.Clave);
        cargo= findViewById(R.id.Cargo);
        listaP = findViewById(R.id.Lista_usuarios);
        listarDatos();
        listaP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                personaSeleccionada= (Persona) parent.getItemAtPosition(position);
                nombres.setText(personaSeleccionada.getNombre());
                apellidos.setText(personaSeleccionada.getApellido());
                clave.setText(personaSeleccionada.getClave());
                cargo.setText(personaSeleccionada.getCargo());
            }
        });
    }

    private void listarDatos() {//Función que se encarga de cargar los datos de las personas en la base de datos y guardarlas en una lista llamada ListaP, además existe otra lista listaP con minúscula para representar al ListView donde se van a mostrar las personas cargadas
        dbrefence.child("Usuario").addValueEventListener(new ValueEventListener() {//También se crea el ArrayAdapter propio del Listview
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ListP.clear();
            for(DataSnapshot objsnpashot : dataSnapshot.getChildren()){
                Persona p = objsnpashot.getValue(Persona.class);
                ListP.add(p);
                arrayAdapterPersona = new ArrayAdapter<Persona>(Administracion_Accesos.this,android.R.layout.simple_list_item_1,ListP);
                listaP.setAdapter(arrayAdapterPersona);
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializarfb() {//Función desechada por la clase Firebase
        FirebaseApp.initializeApp(this);
        firebasedb= FirebaseDatabase.getInstance();
        //firebasedb.setPersistenceEnabled(true);
        dbrefence= firebasedb.getReference();
    }

    public void Agregar(View view){//Agrega los datos del EditText a la base de datos firebase
        String Nombres= nombres.getText().toString();
        String Apellidos= apellidos.getText().toString();
        String Clave= clave.getText().toString();
        String Cargo= cargo.getText().toString();
        if (Nombres.equals("")|Apellidos.equals("")|Clave.equals("")|Cargo.equals("")){
            validación();
        }
        else{
            Persona p = new Persona(Nombres,Apellidos,Clave,Cargo);
            p.setId(UUID.randomUUID().toString()); //se crea un ID random para guardar a las personas por IDs diferentes
            dbrefence.child("Usuario").child(p.getId()).setValue(p);//En el valor de usuario de la base de datos empieza a agregar a las personas
            Toast.makeText(this,"Agregado", Toast.LENGTH_SHORT).show();//muestra un Toast como señal de que se ha agregado un usuario
            limpiarCaja();
        }
    }

    public void Actualizar(View view){//Actualiza los datos de usuario previamente guardados
        Persona p = new Persona();
        p.setId(personaSeleccionada.getId());
        p.setApellido(apellidos.getText().toString());
        p.setCargo(cargo.getText().toString());
        p.setClave(clave.getText().toString());
        p.setNombre(nombres.getText().toString());
        dbrefence.child("Usuario").child(p.getId()).setValue(p);//Busca al usuario que seleccionamos y sobreescribe los nuevos valores
        Toast.makeText(this,"Actualizado", Toast.LENGTH_SHORT).show();
        limpiarCaja();
        }
    public void Eliminar(View view){//Elimina los usuarios seleccionado de la base de datos
        Persona p = new Persona();
        p.setId(personaSeleccionada.getId());
        dbrefence.child("Usuario").child(p.getId()).removeValue();
        Toast.makeText(this,"Eliminado", Toast.LENGTH_SHORT).show();
        limpiarCaja();
    }

    private void limpiarCaja() {//limpia los valores de los EditText
        nombres.setText("");
        apellidos.setText("");
        clave.setText("");
        cargo.setText("");
    }
    private void validación(){//Valida que todos los edittext se encuentren llenos
        String Nombres= nombres.getText().toString();
        String Apellidos= apellidos.getText().toString();
        String Clave= clave.getText().toString();
        String Cargo= cargo.getText().toString();
        if(Nombres.equals("")){
            nombres.setError("Texto Requerido");
        }
        else if(Apellidos.equals("")) {
            apellidos.setError("Texto Requerido");
        }
        else if(Clave.equals("")) {
            clave.setError("Texto Requerido");
        }
        else if(Cargo.equals("")) {
            cargo.setError("Texto Requerido");
        }
    }

    public void IniciarEntradaVoz(View view){//Función para grabar la clave dicha por voz
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
                    clave.setText(strSpeech2Text);//La clave queda guardada en el EditText para luego ser subida a la base de datos

                }
                break;
            default:
                break;
        }
    }
}

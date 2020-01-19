package grupo2.app.proyectopst;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import grupo2.app.proyectopst.models.Persona;

import static grupo2.app.proyectopst.FirebasePersistance.dbrefence;
import static grupo2.app.proyectopst.FirebasePersistance.firebasedb;

public class Administracion_Accesos extends AppCompatActivity {
    public static List<Persona> ListP = new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapterPersona;
    Persona personaSeleccionada;
    EditText nombres,apellidos,clave,cargo;
    ListView listaP;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    private void listarDatos() {
        dbrefence.child("Usuario").addValueEventListener(new ValueEventListener() {
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

    private void inicializarfb() {
        FirebaseApp.initializeApp(this);
        firebasedb= FirebaseDatabase.getInstance();
        //firebasedb.setPersistenceEnabled(true);
        dbrefence= firebasedb.getReference();
    }

    public void Agregar(View view){
        String Nombres= nombres.getText().toString();
        String Apellidos= apellidos.getText().toString();
        String Clave= clave.getText().toString();
        String Cargo= cargo.getText().toString();
        if (Nombres.equals("")|Apellidos.equals("")|Clave.equals("")|Cargo.equals("")){
            validación();
        }
        else{
            Persona p = new Persona(Nombres,Apellidos,Clave,Cargo);
            p.setId(UUID.randomUUID().toString());
            dbrefence.child("Usuario").child(p.getId()).setValue(p);
            Toast.makeText(this,"Agregado", Toast.LENGTH_SHORT).show();
            limpiarCaja();
        }
    }

    public void Actualizar(View view){
        Persona p = new Persona();
        p.setId(personaSeleccionada.getId());
        p.setApellido(apellidos.getText().toString());
        p.setCargo(cargo.getText().toString());
        p.setClave(clave.getText().toString());
        p.setNombre(nombres.getText().toString());
        dbrefence.child("Usuario").child(p.getId()).setValue(p);
        Toast.makeText(this,"Actualizado", Toast.LENGTH_SHORT).show();
        limpiarCaja();
        }
    public void Eliminar(View view){
        Persona p = new Persona();
        p.setId(personaSeleccionada.getId());
        dbrefence.child("Usuario").child(p.getId()).removeValue();
        Toast.makeText(this,"Eliminado", Toast.LENGTH_SHORT).show();
        limpiarCaja();
    }

    private void limpiarCaja() {
        nombres.setText("");
        apellidos.setText("");
        clave.setText("");
        cargo.setText("");
    }
    private void validación(){
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
}

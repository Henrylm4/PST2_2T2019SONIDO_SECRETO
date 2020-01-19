package grupo2.app.proyectopst;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import grupo2.app.proyectopst.models.Registro;

import static grupo2.app.proyectopst.Firebase.dbrefence;

public class Registro_Accesos extends AppCompatActivity {//Pantalla que muestra todos los registros de usuario
    ArrayAdapter<Registro> arrayAdapterRegistro;//Array Adapter destinado para el listview
    ListView listaR;//Listview visual
    public static List<Registro> ListR = new ArrayList<Registro>();//Lista de Todos los registros guardados en la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro__accesos);
        listaR = findViewById(R.id.Lista_Personas);
        listarDatos();
    }
    private void listarDatos() {//Se obtienen todos los datos de Registros guardados en la base de datos
        dbrefence.child("Registro").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListR.clear();
                for(DataSnapshot objsnpashot : dataSnapshot.getChildren()){
                    Registro r = objsnpashot.getValue(Registro.class);
                    ListR.add(r);
                    arrayAdapterRegistro = new ArrayAdapter<Registro>(Registro_Accesos.this,android.R.layout.simple_list_item_1,ListR);
                    listaR.setAdapter(arrayAdapterRegistro);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

package grupo2.app.proyectopst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Pantalla_Admin extends AppCompatActivity {//Pantalla donde el admin decide si ver los registros o la administración de accesos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla__admin);
    }
    public void AdministracionAccesos(View view) {
        Intent i = new Intent(this, Administracion_Accesos.class );
        startActivity(i);

    }
    public void RegistroAccesos(View view) {
        Intent i = new Intent(this, Registro_Accesos.class );
        startActivity(i);

    }
}

package grupo2.app.proyectopst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Pantalla_Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla__admin);
    }
    public void AdministracionAccesos(View view) {
        Intent i = new Intent(this, Administracion_Accesos.class );
        startActivity(i);

    }
}

package grupo2.app.proyectopst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {//Pantalla dedicada al ingreso de dos diferentes activities de admin y de usuario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Admin(View view) {
        Intent i = new Intent(this, Login.class );
        startActivity(i);

    }

    public void Usuario(View view) {
        Intent i = new Intent(this, Usuarioclave.class );
        startActivity(i);

    }


}

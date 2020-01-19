package grupo2.app.proyectopst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {//Pantalla dedicada a el inicio de sesión del administrador
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void ingresara(View view) {
        EditText et1 =  findViewById(R.id.user);
        EditText et2 =  findViewById(R.id.pass);
        try{
            if (et1.getText().toString().equals("123")){//Se define el usuario del admin
                if (et2.getText().toString().equals("123")){//Se define la contraseña del admin

                    Intent i = new Intent(this, Pantalla_Admin.class );
                    i.putExtra("usuario", et1.getText().toString());
                    startActivity(i);        }
                else{
                    Toast.makeText(getApplicationContext(),"Contraseña Incorrecta",Toast.LENGTH_SHORT).show();//Se crean Toast en caso de que sean credenciales incorrectas

                }

            }
            else{
                Toast.makeText(getApplicationContext(),"Usuario Incorrecto",Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}

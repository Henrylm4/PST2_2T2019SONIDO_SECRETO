package grupo2.app.proyectopst;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebasePersistance extends android.app.Application {
    public static FirebaseDatabase firebasedb;
    public static DatabaseReference dbrefence;

    public void onCreate() {

        super.onCreate();
        FirebaseApp.initializeApp(this);
        firebasedb= FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        dbrefence= firebasedb.getReference();
    }
}

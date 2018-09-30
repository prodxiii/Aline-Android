package coolgroup.com.aline;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Aline extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}

package net.mssc.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    String TAG = "RAID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //OBTENER UNA INSTANCIA DE FIREBASE MESSAGING Y OBTENER EL TOKEN REPRESENTATIVO DEL DISPOSITIVO
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            //CUANDO SE COMPLETE OBTENER EL TOKEN
            @Override
            public void onComplete(@NonNull Task<String> task) {
                //SI LA TAREA NO SE REALIZO CON EXITO ANUNCIAR FALLO
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                }

                //OBTENER UN NUEVO TOKEN DE FCM
                String token = task.getResult();

                //MOSTRAR EL TOKEN
                String msg = token;
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
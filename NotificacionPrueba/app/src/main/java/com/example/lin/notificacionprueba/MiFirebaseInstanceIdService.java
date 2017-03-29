package com.example.lin.notificacionprueba;

import android.bluetooth.le.ScanRecord;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by LIN on 25/03/2017.
 */

public class MiFirebaseInstanceIdService extends FirebaseInstanceIdService {
    public  static final String TAG="NOTICIAS";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String Token= FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"Token"+Token);
        Registrarse(Token);
    }

    private void Registrarse(String Token) {


    }


}

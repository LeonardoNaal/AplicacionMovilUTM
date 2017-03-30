package com.fsociety.linkutmbetty;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by LIN on 29/03/2017.
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
        String action="AgregarRegistrosFCM";
        //String Url="http://fsociety.somee.com/WebService.asmx/";
        //String Url="http://192.168.1.71:8091/WebService.asmx/";
        String Url="http://davisaac19-001-site1.atempurl.com/WebService.asmx/";
        String UrlWeb=Url+action+"?token="+Token;
        new JSONTask().execute(UrlWeb);
    }

    public class  JSONTask extends AsyncTask<String ,String, String> {
        @Override
        protected  String doInBackground(String... parametros){
            HttpURLConnection conexion=null;
            BufferedReader reader=null;
            try{
                URL url=new URL(parametros[0]);
                conexion=(HttpURLConnection)url.openConnection();
                conexion.connect();
                InputStream stream=conexion.getInputStream();
                reader=new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer=new StringBuffer();
                String Line="";
                while((Line=reader.readLine())!=null){
                    buffer.append(Line);
                }
                return buffer.toString();
            }
            catch (MalformedURLException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            finally {
                if(conexion!=null){
                    conexion.disconnect();
                }

                try{
                    if(reader!=null){
                        reader.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            return  null;
        }
        @Override
        protected  void onPostExecute(String resultado){
            super.onPostExecute(resultado);

            try{
                Log.e("salida",resultado);
            }
            catch (Throwable t){
                Log.e("Falla",t.toString());
            }
        }
    }
}

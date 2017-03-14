package com.fsociety.linkutmbetty;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RegistroActivity extends AppCompatActivity {
    Button btnGuardar;
    Button btnCancelar;
EditText txtNombre,txtMatricula,txtAPaterno,txtAMaterno,txtContraseña,txtContraseña2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Referencias y eventos de los botones
        txtMatricula=(EditText)findViewById(R.id.txtMatricula);
        txtNombre=(EditText)findViewById(R.id.txtNombre);
        txtAMaterno=(EditText)findViewById(R.id.txtApellidoMaterno);
        txtAPaterno=(EditText)findViewById(R.id.txtApellidoPaterno);
        txtContraseña=(EditText)findViewById(R.id.txtContraseña);
        txtContraseña2=(EditText)findViewById(R.id.txtConfirmPass);

        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Código para registrar al usuario
                String action="Registrarse";
                //String Url="http://fsociety.somee.com/WebService.asmx/";
                String Url="http://169.254.3.130:8091/WebService.asmx/";

                    String UrlWeb=Url+action+"?codUser="+txtMatricula.getText().toString()+"&nombre="+txtNombre.getText().toString()+"&apPaterno="+txtAPaterno.getText().toString()+"&apMaterno="+txtAMaterno.getText().toString()+"&contraseña="+txtContraseña.getText().toString()+"&TipoUser="+1;
                    new JSONTask().execute(UrlWeb);

                //    Toast.makeText(RegistroActivity.this,"Favor de verificar sus datos",Toast.LENGTH_SHORT).show();

            }
        });

        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Código que envía a MainActivity
                Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
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

                try{
                 if(Integer.parseInt(resultado)==1){
                 Toast.makeText(RegistroActivity.this,"Datos agregados correctamente",Toast.LENGTH_SHORT).show();
                 }
                 else {
                     Toast.makeText(RegistroActivity.this,"Error, Matrícula Existente",Toast.LENGTH_SHORT).show();
                 }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            catch (Throwable t){
                Log.e("Falla",t.toString());

            }
        }
    }
}

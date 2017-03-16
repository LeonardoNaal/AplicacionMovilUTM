package com.fsociety.linkutmbetty;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LogInActivity extends AppCompatActivity {
EditText edtUsuario,edtContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        edtUsuario=(EditText)findViewById(R.id.txtMatricula);
        edtContraseña=(EditText)findViewById(R.id.txtContraseña);
        Button btnIniciar = (Button) findViewById(R.id.btnIniciar);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=edtUsuario.getText().toString();
                String contraseña=edtContraseña.getText().toString();
                String action="Loguin";
                //String Url="http://fsociety.somee.com/WebService.asmx/";
                String Url="http://169.254.3.130:8091/WebService.asmx/";
                String UrlWeb=Url+action+"?user="+user+"&contraseña="+contraseña;
                new JSONTask().execute(UrlWeb);
            }
        });
        Button btnRegistro=(Button)findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogInActivity.this,RegistroActivity.class);
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
                if(Integer.parseInt(resultado)==1){
                    Toast.makeText(LogInActivity.this,"Bienvenido",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent (LogInActivity.this, UserMainActivity.class);
                    String Nombre=edtUsuario.getText().toString();
                    intent.putExtra("Matricula",Nombre);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LogInActivity.this,"Usuario o contraseña incorrecto",Toast.LENGTH_SHORT).show();
                }
                /*
                 Intent i=new Intent(MainActivity.this,Main2Activity.class);
                i.putExtra("ResultadoEnArray",resultado);
                startActivity(i);
                 */
            }
            catch (Throwable t){
                Log.e("Falla",t.toString());
            }
        }
    }
}

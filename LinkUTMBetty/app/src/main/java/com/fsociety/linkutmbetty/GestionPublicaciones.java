package com.fsociety.linkutmbetty;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class GestionPublicaciones extends AppCompatActivity {
    Button btnModificar;
    Button btnEliminar;
    EditText txtTitulo;
    EditText txtContenido;
    public int idPublicacion;
    public String codUser;
    ImageView img1;
    public Bitmap bmp1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_publicaciones);

        //La actividad GestionPublicaciones recibe los datos de la publicación que están en el ListView de UserMainActivity
        //Como título, descripción o ¿tipo?
        //La actividad incluye dos botones
        //Un botón para guardar los cambios que después de guardar regresa a UserMainActivity
        //Otro botón para eliminar que después de eliminar regresa a UserMainActivity
        img1=(ImageView)findViewById(R.id.imageView6);
        //Referencia al EditText Título
        txtTitulo = (EditText) findViewById(R.id.txtTitulo);
        //Referencia al EditTex Contenido
        txtContenido = (EditText) findViewById(R.id.txtContenido);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Bitmap image= (Bitmap)extras.get("imagen");
            String datotitulo = (String) extras.get("titulo");
            String datocontenido = (String) extras.get("contenido");
            idPublicacion = (int) extras.getInt("id");
            String fecha=(String) extras.get("fecha");
            codUser=(String) extras.get("codUser");
            bmp1=image;
            txtContenido.setText(datocontenido);
            txtTitulo.setText(datotitulo);
            img1.setImageBitmap(image);
        }

        //Referencia y evento al botón Guardar
        btnModificar = (Button) findViewById(R.id.btnModificar);
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Al finalizar este método debe regresar a UserMainActivity
                String action="BuscarPublicacionUsuario";
                String Url="http://fsociety.somee.com/WebService.asmx/";
                String UrlWeb=Url+action+"?idPublicacion="+idPublicacion+"&codUser="+codUser+"&tit="+txtTitulo.getText().toString()+"&contenido="+txtContenido.getText().toString();
                new JSONTask().execute(UrlWeb);
            }
        });

        //Referencia y evento al botón Eliminar
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action="EliminarPublicaciones";
                String Url="http://fsociety.somee.com/WebService.asmx/";
                String UrlWeb=Url+action+"?idPublicacion="+idPublicacion;
                new JSONTask().execute(UrlWeb);
                //Al finalizar este método debe regresar a UserMainActivity
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
            Toast.makeText(GestionPublicaciones.this, "Exito", Toast.LENGTH_SHORT).show();
        }
                else{
            Toast.makeText(GestionPublicaciones.this, "Error", Toast.LENGTH_SHORT).show();
        }

            }
            catch (Throwable t){
                Log.e("Falla",t.toString());

            }
        }
    }
}

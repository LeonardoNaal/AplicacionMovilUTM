package com.fsociety.linkutmbetty;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GestionPublicaciones extends AppCompatActivity {
    Button btnModificar;
    Spinner spn1;
    Button btnEliminar;
    EditText txtTitulo;
    EditText txtContenido;
    public int idPublicacion;
    public String codUser;
    ImageView img1;
    public Bitmap bmp1, image;
    //public String SERVER = "http://fsociety.somee.com/WebService.asmx/ModificarPublicacion?",     timestamp;
    //public String SERVER = "http://169.254.3.130:8091/WebService.asmx/ModificarPublicacion?",timestamp;
    public String SERVER = "http://davisaac19-001-site1.atempurl.com//WebService.asmx/ModificarPublicacion?",timestamp;
public String carrer,grup;
    public int TipoPub,idTipoSeleccionado=0,grad;
    String[] Tipos={"Seleccionar...","Publicidad","Aviso","Reporte","Otra"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_publicaciones);
        //La actividad GestionPublicaciones recibe los datos de la publicación que están en el ListView de UserMainActivity
        //Como título, descripción o ¿tipo?
        //La actividad incluye dos botones
        //Un botón para guardar los cambios que después de guardar regresa a UserMainActivity
        //Otro botón para eliminar que después de eliminar regresa a UserMainActivity
        spn1 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Tipos);
        spn1.setAdapter(adaptador);
        img1 = (ImageView) findViewById(R.id.imageView6);
        //Referencia al EditText Título
        txtTitulo = (EditText) findViewById(R.id.txtTitulo);
        //Referencia al EditTex Contenido
        txtContenido = (EditText) findViewById(R.id.txtContenido);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Bitmap image = (Bitmap) extras.get("imagen");
            String datotitulo = (String) extras.get("titulo");
            String datocontenido = (String) extras.get("contenido");
            idPublicacion = (int) extras.getInt("id");
            String fecha=(String) extras.get("fecha");
            codUser=(String) extras.get("codUser");
            TipoPub=(int)extras.getInt("IDTipo");
            grad=(int)extras.getInt("grado");
            grup=(String)extras.get("grupo");
            carrer=(String)extras.get("carrera");
            bmp1=image;
            txtContenido.setText(datocontenido);
            txtTitulo.setText(datotitulo);
            img1.setImageBitmap(image);
        }
        setTitle(txtTitulo.getText());
        spn1.setSelection(TipoPub);
        spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        idTipoSeleccionado = 1;
                        break;
                    case 2:
                        idTipoSeleccionado = 2;
                        break;
                    case 3:
                        idTipoSeleccionado = 3;
                        break;
                    case 4:
                        idTipoSeleccionado = 4;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Referencia y evento al botón Guardar
        btnModificar = (Button) findViewById(R.id.btnModificar);
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Al finalizar este método debe regresar a UserMainActivity
                if (((BitmapDrawable) img1.getDrawable()).getBitmap() == null) {
                    Resources resources = getResources();
                    Bitmap source = BitmapFactory.decodeResource(resources, R.drawable.estandar2);

                    int width = source.getWidth();
                    int height = source.getHeight();
                    int newWidth = 180;
                    int newHeight = 150;

                    // calculamos el escalado de la imagen destino
                    float scaleWidth = ((float) newWidth) / width;
                    float scaleHeight = ((float) newHeight) / height;
                    // para poder manipular la imagen
                    // debemos crear una matriz
                    Matrix matrix = new Matrix();
                    // resize the Bitmap
                    matrix.postScale(scaleWidth, scaleHeight);
                    // volvemos a crear la imagen con los nuevos valores
                    Bitmap resizedBitmap = Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);
                    img1.setImageBitmap(resizedBitmap);
                    image = resizedBitmap;
                } else {
                    image = ((BitmapDrawable) img1.getDrawable()).getBitmap();
                }
                //execute the async task and upload the image to server
                if (idTipoSeleccionado == 0) {
                    idTipoSeleccionado = TipoPub;
                }
                new Upload(image, txtTitulo.getText().toString(), txtContenido.getText().toString(), idTipoSeleccionado, codUser, idPublicacion).execute();
            }
        });

        //Referencia y evento al botón Eliminar
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = "EliminarPublicaciones";
                //String Url="http://fsociety.somee.com/WebService.asmx/";
                //String Url="http://169.254.3.130:8091/WebService.asmx/";
                String Url = "http://davisaac19-001-site1.atempurl.com/WebService.asmx/";
                String UrlWeb = Url + action + "?idPublicacion=" + idPublicacion;
                new JSONTask().execute(UrlWeb);
                //Al finalizar este método debe regresar a UserMainActivity
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(GestionPublicaciones.this, UserMainActivity.class);
        intent.putExtra("Matricula",codUser);
        intent.putExtra("grado",grad);
        intent.putExtra("grupo",grup);
        intent.putExtra("carrera",carrer);;
        startActivity(intent);
        finish();
    }
    public class JSONTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... parametros) {
            HttpURLConnection conexion = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(parametros[0]);
                conexion = (HttpURLConnection) url.openConnection();
                conexion.connect();
                InputStream stream = conexion.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String Line = "";
                while ((Line = reader.readLine()) != null) {
                    buffer.append(Line);
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conexion != null) {
                    conexion.disconnect();
                }

                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            try{
                Log.e("salida",resultado);
        if(Integer.parseInt(resultado)==1){
            Toast.makeText(GestionPublicaciones.this, "Datos eliminados correctamente", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(GestionPublicaciones.this,UserMainActivity.class);
            intent.putExtra("Matricula",codUser);
            intent.putExtra("grado",grad);
            intent.putExtra("grupo",grup);
            intent.putExtra("carrera",carrer);
            startActivity(intent);
        }
                else{
            Toast.makeText(GestionPublicaciones.this, "Error", Toast.LENGTH_SHORT).show();
        }

            } catch (Throwable t) {
                Log.e("Falla", t.toString());

            }
        }
    }

    private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            StringBuilder append = result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private class Upload extends AsyncTask<Void, Void, String> {
        private Bitmap imagen;
        private String titulo;
        private String contenido;
        private int tipoPub;
        private String CodUsuario;
        private int idPublicacion;

        public Upload(Bitmap imagen, String titulo, String contenido, int Tipo, String CodUsuario, int idPublicacion) {
            this.imagen = imagen;
            this.titulo = titulo;
            this.contenido = contenido;
            this.tipoPub = Tipo;
            this.CodUsuario = CodUsuario;
            this.idPublicacion = idPublicacion;
        }

        @Override
        protected String doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //compress the image to jpg format
            imagen.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            /*
            * encode image to base64 so that it can be picked by saveImage.php file
            * */
            String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            String idpub = String.valueOf(idPublicacion);
            String tip = String.valueOf(tipoPub);
            //generate hashMap to store encodedImage and the name
            HashMap<String, String> detail = new HashMap<>();
            detail.put("idPublicacion", idpub);
            detail.put("codUser", CodUsuario);
            detail.put("tit", titulo);
            detail.put("contenido", contenido);
            detail.put("imagen", encodeImage);
            detail.put("tipo", tip);

            try {
                //convert this HashMap to encodedUrl to send to php file
                String dataToSend = hashMapToUrl(detail);
                //make a Http request and send data to saveImage.php file
                String response = Request.post(SERVER, dataToSend);

                //return the response
                return response;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            //show image uploaded
            Toast.makeText(getApplicationContext(),"Datos modificados correctamente", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(GestionPublicaciones.this,UserMainActivity.class);
            intent.putExtra("Matricula",codUser);
            intent.putExtra("grado",grad);
            intent.putExtra("grupo",grup);
            intent.putExtra("carrera",carrer);
            startActivity(intent);
        }
    }
}

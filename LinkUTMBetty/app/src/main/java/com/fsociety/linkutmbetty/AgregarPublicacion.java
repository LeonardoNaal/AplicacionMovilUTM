package com.fsociety.linkutmbetty;

import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.bitmap;
import static android.R.attr.breadCrumbShortTitle;
import static android.provider.Telephony.Carriers.SERVER;

public class AgregarPublicacion extends AppCompatActivity {
    Button btnIniciar,btnAbrirGaleria;
    EditText txtTitulo,txtContenido;
    ImageView imageView;
    public int idTipoSeleccionado;
    public String SERVER = "http://fsociety.somee.com/WebService.asmx/agregarPublicacion?",
            timestamp;
    private static final String TAG = AgregarPublicacion.class.getSimpleName();
    Spinner spn1;
private static  final int RESULT_CODE=1;
    String[] Tipos={"Seleccionar...","Publicidad","Aviso","Reporte","Otra"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_publicacion);
        imageView =(ImageView)findViewById(R.id.imageView4);
        txtTitulo=(EditText)findViewById(R.id.txtTitulo);
        txtContenido=(EditText)findViewById(R.id.txtContenido);
        spn1=(Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Tipos);
        spn1.setAdapter(adaptador);
        spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             switch (position){
                 case 1:
                     idTipoSeleccionado=1;
                     break;
                 case 2:
                     idTipoSeleccionado=2;
                     break;
                 case 3:
                     idTipoSeleccionado=3;
                     break;
                 case 4:
                     idTipoSeleccionado=4;
                     break;
             }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Referencia al botón Iniciar
        btnIniciar = (Button) findViewById(R.id.btnIniciar);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Código para publicar los datos
                Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            //execute the async task and upload the image to server
                new Upload(image,txtTitulo.getText().toString(),txtContenido.getText().toString(),idTipoSeleccionado).execute();
            }
        });

        //referencia para abrir la galeria
        btnAbrirGaleria=(Button)findViewById(R.id.btnAbrir);
        btnAbrirGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos el Intent para llamar a la Camara
                Intent cameraIntent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //Creamos una carpeta en la memeria del terminal
                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Imagenes");
                imagesFolder.mkdirs();
                //añadimos el nombre de la imagen
                File image = new File(imagesFolder, "foto.jpg");
                Uri uriSavedImage = Uri.fromFile(image);
                //Le decimos al Intent que queremos grabar la imagen
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                //Lanzamos la aplicacion de la camara con retorno (forResult)
                startActivityForResult(cameraIntent, 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Comprovamos que la foto se a realizado
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Creamos un bitmap con la imagen recientemente
            //almacenada en la memoria
            try{
                Bitmap bMap = BitmapFactory.decodeFile(
                        Environment.getExternalStorageDirectory()+
                                "/Imagenes/"+"foto.jpg");
                //Añadimos el bitmap al imageView para
                //mostrarlo por pantalla

                imageView.setImageBitmap(bMap);

                //get the current timeStamp and strore that in the time Variable
                Long tsLong = System.currentTimeMillis() / 1000;
                timestamp = tsLong.toString();

                Toast.makeText(getApplicationContext(),timestamp,Toast.LENGTH_SHORT).show();


            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
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
    //async task to upload image

    private class Upload extends AsyncTask<Void,Void,String> {
        private Bitmap image;
        private String titulo;
        private String contenido;
        private int tipoPub;
        public Upload(Bitmap image,String titulo,String contenido,int Tipo){
            this.image = image;
            this.titulo = titulo;
            this.contenido=contenido;
            this.tipoPub=Tipo;
        }

        @Override
        protected String doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //compress the image to jpg format
            image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            /*
            * encode image to base64 so that it can be picked by saveImage.php file
            * */
            String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            String tip=String.valueOf(tipoPub);
            //generate hashMap to store encodedImage and the name
            HashMap<String,String> detail = new HashMap<>();
            detail.put("tit", titulo);
            detail.put("imagen", encodeImage);
            detail.put("contenido",contenido);
            detail.put("tipo",tip);

            try{
                //convert this HashMap to encodedUrl to send to php file
                String dataToSend = hashMapToUrl(detail);
                //make a Http request and send data to saveImage.php file
                String response = Request.post(SERVER,dataToSend);

                //return the response
                return response;

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            //show image uploaded
            Toast.makeText(getApplicationContext(),"Image Uploaded", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.fsociety.linkutmbetty;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ActividadPrueba extends AppCompatActivity {
    Button btnIniciar, btnAbrirGaleria, btnCancelar;
    ImageView imageView;
    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";
    private RelativeLayout mRlView;
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private String mPath;
    Bitmap bnp;
    EditText txtTitulo, txtContenido;
    public int idTipoSeleccionado;
    //public String SERVER = "http://fsociety.somee.com/WebService.asmx/agregarPublicacion?", timestamp;

    //public String SERVER = "http://192.168.1.71:8091/WebService.asmx/agregarPublicacion?", timestamp;
    public String SERVER = "http://davisaac19-001-site1.atempurl.com/WebService.asmx/agregarPub?", timestamp;
    private static final String TAG = ActividadPrueba.class.getSimpleName();
    Spinner spn1;
    String[] Tipos = {"Seleccionar...", "Publicidad", "Aviso", "Reporte", "Otra"};
    String matricula, grupo, carrera;
    int grado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_prueba);
        setTitle("Publicar");
        mRlView = (RelativeLayout) findViewById(R.id.rlative);

        imageView = (ImageView) findViewById(R.id.imageView5);
        txtTitulo = (EditText) findViewById(R.id.txtTitulo);
        txtContenido = (EditText) findViewById(R.id.txtContenido);
        spn1 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Tipos);

        spn1.setAdapter(adaptador);
        final Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {//ver si contiene datos

            matricula = (String) extras.get("Matricula");//Obtengo la matriculs
            grado = (int) extras.getInt("grado");
            grupo = (String) extras.getString("grupo");
            carrera = (String) extras.getString("carrera");


        }
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActividadPrueba.this, UserMainActivity.class);
                intent.putExtra("Matricula", matricula);
                intent.putExtra("grado", grado);
                intent.putExtra("grupo", grupo);
                intent.putExtra("carrera", carrera);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
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

        final String[] MalasPalabras = new String[]{"PUTO", "PENDEJO", "MARICÓN", "PINCHE", "CHINGADERA", "PENDEJETE", "PUTA", "CHINGADA", "PUÑETAS", "HIJO DE LA CHINGADA", "MIERDA", "MARRANO", "VERGA", "MAMES", "MAMADA", "MAMÓN", "MAMONA", "MADRAZO", "CABRÓN", "REPENDEJO", "PENDEJA", "PUTAZO", "VERGAZO"};
        //Referencia al botón Iniciar
        btnIniciar = (Button) findViewById(R.id.btnPublicar);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean comprobar = true;
                boolean ValTitulo = true, ValContenido = true;
                String strTitulo = txtTitulo.getText().toString().toUpperCase();
                String strContenido = txtContenido.getText().toString().toUpperCase();
                for (String d : MalasPalabras) {
                    if (strTitulo.contains(d)) {
                        comprobar = false;
                    }
                    if (strContenido.contains(d)) {
                        comprobar = false;
                    }
                }
                if (comprobar == false) {
                    Toast.makeText(ActividadPrueba.this, "Contenido inapropiado", Toast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(strTitulo)) {
                        txtTitulo.setError("Dato obligatorio");
                        ValTitulo = false;
                    }
                    if (TextUtils.isEmpty(strContenido)) {
                        txtContenido.setError("Dato obligatorio");
                        ValContenido = false;
                    }
                    if (ValContenido == true && ValTitulo == true) {
                        if (imageView.getDrawable() == null) {

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
                            imageView.setImageBitmap(resizedBitmap);
                            new Upload(resizedBitmap, txtTitulo.getText().toString(), txtContenido.getText().toString(), idTipoSeleccionado, matricula).execute();
                        } else {
                            //Código para publicar los datos
                            Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                            int width = image.getWidth();
                            int height = image.getHeight();
                            int newWidth = 150;
                            int newHeight = 120;

                            // calculamos el escalado de la imagen destino
                            float scaleWidth = ((float) newWidth) / width;
                            float scaleHeight = ((float) newHeight) / height;
                            // para poder manipular la imagen
                            // debemos crear una matriz
                            Matrix matrix = new Matrix();
                            // resize the Bitmap
                            matrix.postScale(scaleWidth, scaleHeight);
                            // volvemos a crear la imagen con los nuevos valores
                            Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height, matrix, true);
                            //execute the async task and upload the image to serve
                            new Upload(resizedBitmap, txtTitulo.getText().toString(), txtContenido.getText().toString(), idTipoSeleccionado, matricula).execute();
                        }
                    }
                }

            }
        });
        btnAbrirGaleria = (Button) findViewById(R.id.btnAbrir);
        btnAbrirGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });
    }

    private boolean mayRequestStoragePermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))) {
            Snackbar.make(mRlView, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            Toast.makeText(this, "This version is not android 6 or later " + Build.VERSION.SDK_INT, Toast.LENGTH_LONG).show();

        } else {

            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CAMERA);

            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);

                Toast.makeText(this, "Requesting permissions", Toast.LENGTH_LONG).show();

            } else if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "The permissions are already granted ", Toast.LENGTH_LONG).show();
                openCamera();
            }
        }
        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestsCode, String[] permissions, int[] grantResults) {
        if (REQUEST_CODE_ASK_PERMISSIONS == requestsCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "OK Permissions granted ! " + Build.VERSION.SDK_INT, Toast.LENGTH_LONG).show();
                openCamera();
            } else {
                Toast.makeText(this, "Permissions are not granted " + Build.VERSION.SDK_INT, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestsCode, permissions, grantResults);
        }
        if (requestsCode == MY_PERMISSIONS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ActividadPrueba.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                btnAbrirGaleria.setEnabled(true);
            }
        } else {
            showExplanation();
        }
    }

    private void showOptions() {
        final CharSequence[] option = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(ActividadPrueba.this);
        builder.setTitle("Elige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (option[which] == "Tomar foto") {
                    checkPermission();
                } else if (option[which] == "Elegir de galeria") {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                } else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void openCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PHOTO_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActividadPrueba.this, UserMainActivity.class);
        intent.putExtra("Matricula", matricula);
        intent.putExtra("grado", grado);
        intent.putExtra("grupo", grupo);
        intent.putExtra("carrera", carrera);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CODE:
                    Bundle ext = data.getExtras();
                    bnp = (Bitmap) ext.get("data");
                    imageView.setImageBitmap(bnp);
                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();
                    InputStream is;
                    try {
                        is = getContentResolver().openInputStream(path);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        Bitmap bit = BitmapFactory.decodeStream(bis);
                        imageView.setImageBitmap(bit);
                        //get the current timeStamp and strore that in the time Variable
                        Long tsLong = System.currentTimeMillis() / 1000;
                        timestamp = tsLong.toString();

                        Toast.makeText(getApplicationContext(), timestamp, Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActividadPrueba.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
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
    //async task to upload image

    private class Upload extends AsyncTask<Void, Void, String> {
        private Bitmap image;
        private String titulo;
        private String contenido;
        private int tipoPub;
        private String CodUsuario;
        private String car;
        private String grup;
        private int grad;

        public Upload(Bitmap image, String titulo, String contenido, int Tipo, String CodUsuario) {

                this.image = image;
                this.titulo = titulo;
                this.contenido = contenido;
                this.tipoPub = Tipo;
                this.CodUsuario = CodUsuario;
            }

        @Override
        protected String doInBackground (Void... params){
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                //compress the image to jpg format
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            /*
            * encode image to base64 so that it can be picked by saveImage.php file
            * */
                //exec InsertarPublicacion @titulo='nuevo',@contenido='hola',@image=null,@IDTipo=2,@video=null,@CodUsuario='aga00001',@carrera=null,@grado=null,@grupo=null
                String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                String tip = String.valueOf(tipoPub);
                //generate hashMap to store encodedImage and the name

                HashMap<String, String> detail = new HashMap<>();
                detail.put("codUser", CodUsuario);
                detail.put("tit", titulo);
                detail.put("contenido", contenido);
                detail.put("imagen", encodeImage);
                detail.put("tipo", tip);
                detail.put("carrera", "null");
            detail.put("grado","0");
            detail.put("grupo","0");
            detail.put("reporte","true");
                try{
                    String dataToSend = hashMapToUrl(detail);
                    String response = Request.post(SERVER, dataToSend);
                    return response;
                } catch (Exception e){
                    e.printStackTrace();
                    return  null;
                }
            }
        @Override
        protected void onPostExecute (String s){
            //show image uploaded

            super.onPostExecute(s);
            try {
                Log.e("salida", s);
                try {
                    Toast.makeText(getApplicationContext(), "Datos agregados correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActividadPrueba.this, UserMainActivity.class);
                    intent.putExtra("Matricula", matricula);
                    intent.putExtra("grado", grado);
                    intent.putExtra("grupo", grupo);
                    intent.putExtra("carrera", carrera);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Throwable t) {
                Log.e("Falla", t.toString());
            }
        }
        }
    }


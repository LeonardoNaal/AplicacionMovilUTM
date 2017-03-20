package com.fsociety.linkutmbetty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

public class AgregarComentario extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    TextView lblTitulo;
    String codUser;
    int codPublicacion;
    ListView lsvComents2;
    EditText edtContenido;
    public String SERVER = "http://davisaac19-001-site1.atempurl.com/WebService.asmx/AgregarComentarios?", timestamp;
    private SwipeRefreshLayout swipeLayout;
    Button btnAgregar;
    boolean validar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_comentario);
        setTitle("Escribe un comentario");
        lblTitulo = (TextView) findViewById(R.id.lblTitulo1);
        lsvComents2 = (ListView) findViewById(R.id.lsvComents2);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String datotitulo = (String) extras.get("titulo");
            codPublicacion = (int) extras.getInt("id");
            codUser = (String) extras.get("codUser");
            lblTitulo.setText(datotitulo);
        }
        //Se ajusta un botón de atrás al título de la actividad
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        edtContenido=(EditText)findViewById(R.id.txtComentario);
        btnAgregar=(Button)findViewById(R.id.btnComentar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contenido=edtContenido.getText().toString();
                if(TextUtils.isEmpty(contenido)){
                    edtContenido.setError("Dato obligatorio");
                   validar=false;
                }else{
                    validar=true;
                }
                if (validar==true)
                    {
                        new Upload(codUser,codPublicacion,edtContenido.getText().toString()).execute();
                    }
            }
        });


        //Indicamos que listener recogerá la retrollamada (callback), en este caso, será el metodo OnRefresh de esta clase.

        swipeLayout.setOnRefreshListener(this);
        //Podemos espeficar si queremos, un patron de colores diferente al patrón por defecto.
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        lsvComents2.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int filaSuperior = (
                        lsvComents2 == null//Si la lista esta vacía ó
                                || lsvComents2.getChildCount() == 0) ? 0 : lsvComents2.getChildAt(0).getTop();//Estamos en el elemento superior
                swipeLayout.setEnabled(filaSuperior >= 0);
            }
        });


        String action = "VerComentarios";
        //String Url="http://fsociety.somee.com/WebService.asmx/";
        //String Url="http://192.168.1.71:8091/WebService.asmx/";
        String Url = "http://davisaac19-001-site1.atempurl.com/WebService.asmx/";
        String UrlWeb = Url + action + "?CodPublicacion=" + codPublicacion;
        new JSONTask().execute(UrlWeb);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                //NavUtils.navigateUpFromSameTask(this);
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.putExtra("Matricula", codUser);
                NavUtils.navigateUpTo(AgregarComentario.this,intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        //Codigo para traer todas las publicaciones
        String action = "VerComentarios";
        //String Url="http://fsociety.somee.com/WebService.asmx/";
        String Url = "http://davisaac19-001-site1.atempurl.com/WebService.asmx/";
        //String Url="http://davisaac19-001-site1.atempurl.com//WebService.asmx/";
        String UrlWeb = Url + action + "?CodPublicacion=" + codPublicacion;
        new JSONTask().execute(UrlWeb);
        //Antes de ejecutarlo, indicamos al swipe layout que muestre la barra indeterminada de progreso.
        swipeLayout.setRefreshing(true);

        //Vamos a simular un refresco con un handle.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //Se supone que aqui hemos realizado las tareas necesarias de refresco, y que ya podemos ocultar la barra de progreso
                swipeLayout.setRefreshing(false);
            }
        }, 3000);
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
            try {
                Log.e("salida", resultado);
                ArrayList<Comentario> image = new ArrayList<Comentario>();
                // ArrayList list=new ArrayList();
                JSONArray ResultadoArray = null;
                try {
                    JSONObject Jasonobject = new JSONObject(resultado);
                    JSONArray Jarray = Jasonobject.getJSONArray("Table");
                    for (int i = 0; i <= Jarray.length(); i++) {
                        ;
                        JSONObject objeto = Jarray.getJSONObject(i);
                        //list.add(objeto.getString("Titulo"));
                        Comentario comentario = new Comentario();
                        comentario.setIdComentario(objeto.getInt("CodComentario"));
                        comentario.setApellido(objeto.getString("ApePat"));
                        comentario.setNombreUsuario(objeto.getString("Nombres"));
                        comentario.setComentario(objeto.getString("contenido"));
                        image.add(comentario);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AdaptadorComentarios obj = new AdaptadorComentarios(AgregarComentario.this, image);
                lsvComents2.setAdapter(obj);
            } catch (Throwable t) {
                Log.e("Falla", t.toString());
            }
        }
    }

    public class AdaptadorComentarios extends BaseAdapter {
        protected Activity act;
        protected ArrayList<Comentario> array;

        public AdaptadorComentarios(Activity ac, ArrayList<Comentario> arr) {
            this.act = ac;
            this.array = arr;
        }

        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public Object getItem(int position) {
            return array.get(position);
        }

        @Override
        public long getItemId(int position) {
            return array.get(position).getIdComentario();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi = inflater.inflate(R.layout.vista_comentarios, null);
            }
            Comentario comentario = array.get(position);
            TextView contenido = (TextView) vi.findViewById(R.id.lblComentario);
            contenido.setText(comentario.getComentario());

            TextView Nombre = (TextView) vi.findViewById(R.id.lblNombre);
            Nombre.setText(comentario.getNombreUsuario() + " " + comentario.getApellido());
            return vi;
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

    //async task to upload image
    private class Upload extends AsyncTask<Void, Void, String> {
        private String codUsuario;
        private int codPublicacion;
        private String Contenido;

        public Upload(String codUsuario, int Codpublicacion, String contenido) {
            this.codUsuario = codUsuario;
            this.codPublicacion = Codpublicacion;
            this.Contenido = contenido;

        }

        @Override
        protected String doInBackground(Void... params) {

            String cod = String.valueOf(codPublicacion);
            //generate hashMap to store encodedImage and the name
            HashMap<String, String> detail = new HashMap<>();
            detail.put("CodPublicacion", cod);
            detail.put("codUsuario", codUsuario);
            detail.put("contenido", Contenido);
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
            Toast.makeText(getApplicationContext(), "Datos agregados correctamente", Toast.LENGTH_SHORT).show();
        }
    }
}

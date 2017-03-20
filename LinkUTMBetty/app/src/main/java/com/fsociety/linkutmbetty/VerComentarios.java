package com.fsociety.linkutmbetty;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class VerComentarios extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ListView list;
    String codUser;
    int codPublicacion;
    private SwipeRefreshLayout swipeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_comentarios);
        setTitle("Comentarios");

        //Se ajusta un botón de atrás al título de la actividad
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        list=(ListView)findViewById(R.id.lsvComents);
        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);

        //Indicamos que listener recogerá la retrollamada (callback), en este caso, será el metodo OnRefresh de esta clase.

        swipeLayout.setOnRefreshListener(this);
        //Podemos espeficar si queremos, un patron de colores diferente al patrón por defecto.
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int filaSuperior = (
                        list == null//Si la lista esta vacía ó
                                || list.getChildCount() == 0) ? 0 : list.getChildAt(0).getTop();//Estamos en el elemento superior
                swipeLayout.setEnabled(filaSuperior >= 0);
            }
        });
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String datotitulo = (String) extras.get("titulo");
            codPublicacion = (int) extras.getInt("id");
            codUser=(String) extras.get("codUser");
        }
        String action="VerComentarios";
        //String Url="http://fsociety.somee.com/WebService.asmx/";
        String Url="http://davisaac19-001-site1.atempurl.com/WebService.asmx/";
        //String Url="http://davisaac19-001-site1.atempurl.com//WebService.asmx/";
        String UrlWeb=Url+action+"?CodPublicacion="+codPublicacion;
        new JSONTask().execute(UrlWeb);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                ArrayList<Comentario> image=new ArrayList<Comentario>();
                // ArrayList list=new ArrayList();
                JSONArray ResultadoArray=null;
                try{
                    JSONObject Jasonobject = new JSONObject(resultado);
                    JSONArray Jarray = Jasonobject.getJSONArray("Table");
                    for(int i=0;i<=Jarray.length();i++){
                        ;
                        JSONObject objeto=Jarray.getJSONObject(i);
                        //list.add(objeto.getString("Titulo"));
                        Comentario comentario=new Comentario();
                        comentario.setIdComentario(objeto.getInt("CodComentario"));
                        comentario.setApellido(objeto.getString("ApePat"));
                        comentario.setNombreUsuario(objeto.getString("Nombres"));
                        comentario.setComentario(objeto.getString("contenido"));
                        image.add(comentario);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                AdaptadorComentarios obj=new AdaptadorComentarios(VerComentarios.this,image);
                list.setAdapter(obj);
            }
            catch (Throwable t){
                Log.e("Falla",t.toString());
            }
        }
    }
    public class AdaptadorComentarios extends BaseAdapter {
        protected Activity act;
        protected ArrayList<Comentario> array;

        public AdaptadorComentarios(Activity ac,ArrayList<Comentario> arr) {
            this.act=ac;
            this.array=arr;
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
            View vi=convertView;

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi = inflater.inflate(R.layout.vista_comentarios, null);
            }
            Comentario comentario=array.get(position);
            TextView contenido=(TextView)vi.findViewById(R.id.lblComentario);
            contenido.setText(comentario.getComentario());

            TextView Nombre=(TextView)vi.findViewById(R.id.lblNombre);
            Nombre.setText(comentario.getNombreUsuario()+" "+comentario.getApellido());
            return vi;
        }
    }
    @Override
    public void onRefresh() {

    }
}

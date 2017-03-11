package com.fsociety.linkutmbetty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.ListView;

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


public class UserMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PublicacionesFragment.OnFragmentInteractionListener, ActividadesFragment.OnFragmentInteractionListener,SwipeRefreshLayout.OnRefreshListener {
    ListView listaUsuario;
    TextView txtMatricula;
    ArrayList<publicacion> image;
    public String dato;
    private SwipeRefreshLayout swipeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_main);
        setContentView(R.layout.activity_user_main);

        //Asignar matricula a textview
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//Obtenemos una referencia al viewgroup SwipeLayout
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        //Indicamos que listener recogerá la retrollamada (callback), en este caso, será el metodo OnRefresh de esta clase.

        swipeLayout.setOnRefreshListener(this);
        //Podemos espeficar si queremos, un patron de colores diferente al patrón por defecto.
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

//Referencia al botón redondo
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Este código será reemplazado por un intent para llevar a otra actividad (AgregarPublicación)
                Intent intent = new Intent(UserMainActivity.this, ActividadPrueba.class);
                intent.putExtra("Matricula",dato);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header =navigationView.getHeaderView(0);

        //LayoutInflater.from(this).inflate(R.layout.nav_header_user_main, null);
        txtMatricula=(TextView)header.findViewById(R.id.txtMat);
        Intent intent=getIntent();
        Bundle extras =intent.getExtras();
        if (extras != null) {//ver si contiene datos
            dato=(String)extras.get("Matricula");//Obtengo la matriculs
            txtMatricula.setText(dato);
        }
        String action="BuscarPublicacionUsuario";
        String Url="http://fsociety.somee.com/WebService.asmx/";
        String UrlWeb=Url+action+"?CodigoUsuario="+txtMatricula.getText().toString();
        new JSONTask().execute(UrlWeb);

        //Referencia al listView
        listaUsuario = (ListView) findViewById(R.id.lsvUsuarioPub);

        listaUsuario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                publicacion elegido = (publicacion) parent.getItemAtPosition(position);
                Intent intent = new Intent(UserMainActivity.this,GestionPublicaciones.class);
                String auxtitulo = elegido.getTitulo();
                String auxfecha = elegido.getFecha();
                Bitmap auxphoto = elegido.getPhoto();
                String contenido=elegido.getContenido();
                int auxid = elegido.getId();

                intent.putExtra("id",auxid);
                intent.putExtra("titulo",auxtitulo);
                intent.putExtra("fecha",auxfecha);
                intent.putExtra("imagen",auxphoto);
                intent.putExtra("contenido",contenido);
                intent.putExtra("codUser",dato);
                startActivity(intent);
            }
        });
        listaUsuario.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int filaSuperior = (
                        listaUsuario == null//Si la lista esta vacía ó
                                || listaUsuario.getChildCount() == 0) ? 0 : listaUsuario.getChildAt(0).getTop();//Estamos en el elemento superior
                swipeLayout.setEnabled(filaSuperior >= 0);
            }
        });
       //navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onRefresh() {
        //Aqui ejecutamos el codigo necesario para refrescar nuestra interfaz grafica.
        String action="BuscarPublicacionUsuario";
        String Url="http://fsociety.somee.com/WebService.asmx/";
        String UrlWeb=Url+action+"?CodigoUsuario="+txtMatricula.getText().toString();
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


                image=new ArrayList<publicacion>();
                // ArrayList list=new ArrayList();
                JSONArray ResultadoArray=null;
                try{
                    JSONObject Jasonobject = new JSONObject(resultado);
                    JSONArray Jarray = Jasonobject.getJSONArray("Table");
                    for(int i=0;i<=Jarray.length();i++){
                        ;
                        JSONObject objeto=Jarray.getJSONObject(i);
                        //list.add(objeto.getString("Titulo"));
                        publicacion pub=new publicacion(objeto.getInt("IDPublicacion"),objeto.getString("Titulo"));
                        pub.setData(objeto.getString("Image"));
                        pub.setContenido(objeto.getString("Contenido"));
                        pub.setFecha(objeto.getString("Fecha"));
                        image.add(pub);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                ImagenAdapter obj=new ImagenAdapter(UserMainActivity.this,image);
                listaUsuario.setAdapter(obj);

                // ArrayList list=new ArrayList();
            }
            catch (Throwable t){
                Log.e("Falla",t.toString());

            }
        }
    }
    public class ImagenAdapter extends BaseAdapter {
        protected Activity act;
        protected ArrayList<publicacion> array;

        public ImagenAdapter(Activity ac,ArrayList<publicacion> arr) {
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
            return array.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi=convertView;

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi = inflater.inflate(R.layout.vista_usuario_publicaciones, null);
            }

            publicacion pub = array.get(position);

            ImageView image = (ImageView) vi.findViewById(R.id.imgImagenPub);
            image.setImageBitmap(pub.getPhoto());

            TextView titulo = (TextView) vi.findViewById(R.id.lblTituloUsrPub);
            titulo.setText(pub.getTitulo());

            TextView fecha =(TextView)vi.findViewById(R.id.lblFechaPub);
            fecha.setText(pub.getFecha());

            TextView Contenido =(TextView)vi.findViewById(R.id.lblContenidoPub);
            Contenido.setText(pub.getContenido());

            return vi;
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(UserMainActivity.this, LogInActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    Boolean fragmentSelect = false;
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_camera) {
            fragment = new PublicacionesFragment();
            fragmentSelect = true;
        } else if (id == R.id.nav_gallery) {
            fragment = new ActividadesFragment();
            fragmentSelect = true;

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_user_main, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}

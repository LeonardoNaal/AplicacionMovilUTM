package com.fsociety.linkutmbetty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

import static com.fsociety.linkutmbetty.R.layout.activity_user_main;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UsuarioPubFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UsuarioPubFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsuarioPubFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Activity activity;
    ListView list;
    private OnFragmentInteractionListener mListener;

    public UsuarioPubFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsuarioPubFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsuarioPubFragment newInstance(String param1, String param2) {
        UsuarioPubFragment fragment = new UsuarioPubFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private SwipeRefreshLayout swipeLayout;
    TextView txtMatricula;
    ArrayList<publicacion> image;
    public String dato;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Bundle bdl = getArguments();
        try
        {
            dato = bdl.getString("id");
        }
        catch(final Exception e)
        {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        activity = getActivity();
        final View view = inflater.inflate(R.layout.fragment_usuario_pub, container, false);

        list = (ListView) view.findViewById(R.id.lsvUsuarioPub);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        String action="BuscarPublicacionUsuario";
        //String Url="http://fsociety.somee.com/WebService.asmx/";
        //String Url="http://169.254.3.130:8091/WebService.asmx/";
        String Url="http://davisaac19-001-site1.atempurl.com//WebService.asmx/";
        String UrlWeb=Url+action+"?CodigoUsuario="+dato;
        new JSONTask().execute(UrlWeb);
        //Referencia al listView

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                publicacion elegido = (publicacion) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(),GestionPublicaciones.class);
                String auxtitulo = elegido.getTitulo();
                String auxfecha = elegido.getFecha();
                Bitmap auxphoto = elegido.getPhoto();
                String contenido=elegido.getContenido();
                int TipoPub=elegido.getTipo();
                int auxid = elegido.getId();

                intent.putExtra("id",auxid);
                intent.putExtra("titulo",auxtitulo);
                intent.putExtra("fecha",auxfecha);
                intent.putExtra("imagen",auxphoto);
                intent.putExtra("contenido",contenido);
                intent.putExtra("codUser",dato);
                intent.putExtra("IDTipo",TipoPub);
                startActivity(intent);
            }
        });
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
        return view;
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
                        pub.setTipo(objeto.getInt("IDTipo"));
                        image.add(pub);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                ImagenAdapter obj=new ImagenAdapter(getActivity(),image);
                list.setAdapter(obj);

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        //Aqui ejecutamos el codigo necesario para refrescar nuestra interfaz grafica.
        String action="BuscarPublicacionUsuario";
        //String Url="http://fsociety.somee.com/WebService.asmx/";
        //String Url="http://169.254.3.130:8091/WebService.asmx/";
        String Url="http://davisaac19-001-site1.atempurl.com//WebService.asmx/";
        String UrlWeb=Url+action+"?CodigoUsuario="+dato;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

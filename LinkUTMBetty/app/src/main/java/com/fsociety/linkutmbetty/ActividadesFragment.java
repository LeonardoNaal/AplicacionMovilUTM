package com.fsociety.linkutmbetty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActividadesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActividadesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActividadesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SwipeRefreshLayout swipeLayout;
    ListView listaAct;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ActividadesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActividadesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActividadesFragment newInstance(String param1, String param2) {
        ActividadesFragment fragment = new ActividadesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onRefresh() {
        String action="TopTenActividades";
        String Url="http://fsociety.somee.com/WebService.asmx/";
        String UrlWeb=Url+action;
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
                JSONArray ResultadoArray=null;
                ArrayList<Actividades> Actividad=new ArrayList<Actividades>();
                try{
                    JSONObject Jasonobject = new JSONObject(resultado);
                    JSONArray Jarray = Jasonobject.getJSONArray("Table");
                    for(int i=0;i<=Jarray.length();i++){
                        JSONObject objeto=Jarray.getJSONObject(i);
                        //list.add(objeto.getString("Titulo"));
                        Actividades act=new Actividades();
                        act.setNombre(objeto.getString("Nombre"));
                        act.setId(objeto.getInt("IDActividad"));
                        act.setHFin(objeto.getString("HoraFin"));
                        act.setHInicio(objeto.getString("HoraInicio"));
                        act.setFFin(objeto.getString("FechaFin"));
                        act.setFInicio(objeto.getString("FechaIni"));
                        act.setContenido(objeto.getString("Descripcion"));
                        Actividad.add(act);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                ImagenAdapter obj=new ImagenAdapter(getActivity(),Actividad);
                listaAct.setAdapter(obj);

                // ArrayList list=new ArrayList();
            }
            catch (Throwable t){
                Log.e("Falla",t.toString());

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_actividades, container, false);

        //Referencia al listView que está en fragment_actividades
        listaAct = (ListView) view.findViewById(R.id.lsvListaAct);
        //Obtenemos una referencia al viewgroup SwipeLayout
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        //Indicamos que listener recogerá la retrollamada (callback), en este caso, será el metodo OnRefresh de esta clase.

        swipeLayout.setOnRefreshListener(this);
        //Podemos espeficar si queremos, un patron de colores diferente al patrón por defecto.
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        listaAct.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int filaSuperior = (
                        listaAct == null//Si la lista esta vacía ó
                                || listaAct.getChildCount() == 0) ? 0 : listaAct.getChildAt(0).getTop();//Estamos en el elemento superior
                swipeLayout.setEnabled(filaSuperior >= 0);
            }
        });
        String action="TopTenActividades";
        String Url="http://fsociety.somee.com/WebService.asmx/";
        String UrlWeb=Url+action;
        new JSONTask().execute(UrlWeb);
        return view;
    }
    public class ImagenAdapter extends BaseAdapter{

        protected ArrayList<Actividades> array;
        protected Fragment fragmentActivity;
        protected FragmentActivity fAct;
        public ImagenAdapter(FragmentActivity ac,ArrayList<Actividades> arr) {
            this.fAct=ac;
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
                LayoutInflater inflater = (LayoutInflater)fAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi = inflater.inflate(R.layout.vista_actividades, null);
            }
            Actividades pub=array.get(position);

            TextView txt1=(TextView)vi.findViewById(R.id.lblNombre);
            txt1.setText(pub.getNombre());

            TextView txt2=(TextView)vi.findViewById(R.id.lblHoraFin);
            txt2.setText(pub.getHFin());

            TextView txt3=(TextView)vi.findViewById(R.id.lblHoraInicio);
            txt3.setText(pub.getHInicio());

            TextView txt4=(TextView)vi.findViewById(R.id.lblContenido);
            txt4.setText(pub.getContenido());

            TextView txt5=(TextView)vi.findViewById(R.id.FechaFin);
            txt5.setText(pub.getFFin());

            TextView txt6=(TextView)vi.findViewById(R.id.FechaInicio);
            txt6.setText(pub.getFInicio());

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

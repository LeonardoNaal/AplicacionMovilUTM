package com.fsociety.linkutmbetty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicacionesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PublicacionesFragment extends Fragment {
    ListView listaPub;

    private OnFragmentInteractionListener mListener;

    public PublicacionesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_publicaciones, container, false);
        //Referencia al listView que está en fragmentPublicaciones
        listaPub = (ListView) view.findViewById(R.id.lsvListaPub);
        String action="TopTenPublicaciones";
        String Url="http://192.168.200.2:8091/WebService.asmx/";
        String UrlWeb=Url+action;
        new JSONTask().execute(UrlWeb);
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


                ArrayList<publicacion> image=new ArrayList<publicacion>();
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
                        pub.setData(objeto.getString("image"));
                        pub.setContenido(objeto.getString("Contenido"));
                        pub.setFecha(objeto.getString("Fecha"));
                        image.add(pub);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                ImagenAdapter obj=new ImagenAdapter(getActivity(),image);
                listaPub.setAdapter(obj);

                // ArrayList list=new ArrayList();
            }
            catch (Throwable t){
                Log.e("Falla",t.toString());

            }
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
                vi = inflater.inflate(R.layout.vista_publicaciones, null);
            }

            publicacion pub = array.get(position);

            ImageView image = (ImageView) vi.findViewById(R.id.imageView3);
            image.setImageBitmap(pub.getPhoto());

            TextView titulo = (TextView) vi.findViewById(R.id.lblTitulo);
            titulo.setText(pub.getTitulo());

            TextView fecha =(TextView)vi.findViewById(R.id.lblFecha);
            fecha.setText(pub.getFecha());

            TextView Contenido =(TextView)vi.findViewById(R.id.lblContenido);
            Contenido.setText(pub.getContenido());

            return vi;
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

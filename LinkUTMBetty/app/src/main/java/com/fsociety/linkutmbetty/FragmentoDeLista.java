package com.fsociety.linkutmbetty;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentoDeLista.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentoDeLista#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoDeLista extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String grado;
    String grupo;
    String carrera;
    ArrayList<HorarioBO> listaDatos = new ArrayList<HorarioBO>();
    ListView listview;
    Spinner spin;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentoDeLista() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoDeLista.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoDeLista newInstance(String param1, String param2) {
        FragmentoDeLista fragment = new FragmentoDeLista();
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
                ArrayList<HorarioBO> listaDatos = new ArrayList<HorarioBO>();
                JSONArray ResultadoArray = null;
                try{
                    JSONObject Jasonobject = new JSONObject(resultado);
                    JSONArray Jarray = Jasonobject.getJSONArray("Table");
                    for(int i=0;i<=Jarray.length();i++){
                        JSONObject objeto = Jarray.getJSONObject(i);
                        HorarioBO oHorario = new HorarioBO();
                        oHorario.setId(objeto.getInt("id"));
                        oHorario.setMateria(objeto.getString("materia"));
                        oHorario.setHora(objeto.getString("Hora"));
                        oHorario.setMaestro(objeto.getString("profesor"));
                        oHorario.setEdificio(objeto.getString("lugar"));
                        listaDatos.add(oHorario);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                ImagenAdapter obj = new ImagenAdapter(getActivity(),listaDatos);
                listview.setAdapter(obj);

                //ActividadesFragment.ImagenAdapter obj=new ActividadesFragment.ImagenAdapter(getActivity(),Actividad);
                //listaDatos.setAdapter(obj);
            }
            catch (Throwable t){
                Log.e("Error",t.toString());

            }
        }
    }

    public class ImagenAdapter extends BaseAdapter {
        protected Activity act;
        protected ArrayList<HorarioBO> array;

        public ImagenAdapter(Activity ac,ArrayList<HorarioBO> arr) {
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
                vi = inflater.inflate(R.layout.vistahorario, null);
            }

            HorarioBO entrada = array.get(position);

            TextView Materia = (TextView) vi.findViewById(R.id.txtMateria);
            Materia.setText(((HorarioBO) entrada).getMateria());

            TextView Hora = (TextView) vi.findViewById(R.id.txtHora);
            Hora.setText(((HorarioBO) entrada).getHora());

            TextView Maestro = (TextView) vi.findViewById(R.id.txtMaestro);
            Maestro.setText(((HorarioBO) entrada).getMaestro());

            TextView edificio = (TextView) vi.findViewById(R.id.txtEdificio);
            edificio.setText(((HorarioBO) entrada).getEdificio());

            return vi;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Bundle bdl = getArguments();
        try
        {
            grado=bdl.getString("grado");
            grupo=bdl.getString("grupo");
            //carrera=bdl.getString("carrera");
        }
        catch(final Exception e)
        {
            e.printStackTrace();
        }

        final View view = inflater.inflate(R.layout.fragment_fragmento_de_lista, container, false);
        spin = (Spinner) view.findViewById(R.id.spinnerDias);
        listview = (ListView)view.findViewById(R.id.ListaFragHorario);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter.add("Lunes");
        adapter.add("Martes");
        adapter.add("Miercoles");
        adapter.add("Jueves");
        adapter.add("Viernes");
        spin.setAdapter(adapter);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dia = sdf.format(d);
        Toast.makeText(getContext(), dia , Toast.LENGTH_SHORT).show();
        switch (dia)
        {
            case "lunes":
                spin.setSelection(0);
                break;
            case "martes":
                spin.setSelection(1);
                break;
            case "miércoles":
                spin.setSelection(2);
                break;
            case "jueves":
                spin.setSelection(3);
                break;
            case "viernes":
                spin.setSelection(4);
                break;
        }

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                String UrlWeb;
                switch (i) {
                    case 0:
                        UrlWeb = "http://davisaac19-001-site1.atempurl.com/WebService.asmx/Horarios?carrera=Sistemas%20inform%C3%A1ticos&grado=5&grupo=B&dia=Lunes";
                        UrlWeb = UrlWeb.replaceAll(" ","%20");
                        new JSONTask().execute(UrlWeb);
                        break;
                    case 1:
                        UrlWeb = "http://davisaac19-001-site1.atempurl.com/WebService.asmx/Horarios?carrera=Sistemas%20inform%C3%A1ticos&grado=5&grupo=B&dia=Martes";
                        UrlWeb = UrlWeb.replaceAll(" ","%20");
                        new JSONTask().execute(UrlWeb);
                        break;
                    case 2:
                        UrlWeb = "http://davisaac19-001-site1.atempurl.com/WebService.asmx/Horarios?carrera=Sistemas%20inform%C3%A1ticos&grado=5&grupo=B&dia=Miercoles";
                        UrlWeb = UrlWeb.replaceAll(" ","%20");
                        new JSONTask().execute(UrlWeb);
                        break;
                    case 3:
                        UrlWeb = "http://davisaac19-001-site1.atempurl.com/WebService.asmx/Horarios?carrera=Sistemas%20inform%C3%A1ticos&grado=5&grupo=B&dia=Jueves";
                        UrlWeb = UrlWeb.replaceAll(" ","%20");
                        new JSONTask().execute(UrlWeb);
                        break;
                    case 4:
                        UrlWeb = "http://davisaac19-001-site1.atempurl.com/WebService.asmx/Horarios?carrera=Sistemas%20inform%C3%A1ticos&grado=5&grupo=B&dia=Viernes";
                        UrlWeb = UrlWeb.replaceAll(" ","%20");
                        new JSONTask().execute(UrlWeb);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


        dia = dia.substring(0,1).toUpperCase() + dia.substring(1).toLowerCase();
        String Url="http://davisaac19-001-site1.atempurl.com/WebService.asmx/Horarios";
        carrera = "Sistemas%20inform%C3%A1ticos";
        //String UrlWeb = Url + "?carrera=" + carrera + "&grado=" + grado + "&grupo=" + grupo + "&dia=" + dia;
        String UrlWeb = "http://davisaac19-001-site1.atempurl.com/WebService.asmx/Horarios?carrera=Sistemas%20inform%C3%A1ticos&grado=5&grupo=B&dia=Miercoles";
        UrlWeb = UrlWeb.replaceAll(" ","%20");
        //String g = "http://davisaac19-001-site1.atempurl.com/WebService.asmx/Horarios?carrera=Sistemas%20inform%C3%A1ticos&grado=5&grupo=B&dia=Martes";
        new JSONTask().execute(UrlWeb);
        return view;
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

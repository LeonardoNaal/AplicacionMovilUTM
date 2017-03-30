package com.fsociety.linkutmbetty;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SitiosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SitiosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SitiosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<Point> listaPuntos = new ArrayList<Point>();
    int[][] matrizAdyacencia = new int[53][53];
    Spinner spinnerOrigen;
    Spinner spinnerDestino;
    Bitmap icon,imagenEdificio;
    Canvas tempCanvas;
    Paint paint = new Paint();
    TouchImageView imageView;
    TouchImageView imEdi;
    Bitmap tempBitmap;
    CheckBox checkBox;
    Button btnBuscar;
    TextView txtResultado,lblnomEd,lbldesEd;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SitiosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SitiosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SitiosFragment newInstance(String param1, String param2) {
        SitiosFragment fragment = new SitiosFragment();
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
    //Cambia la imagen y los labels que estan debajo del mapa;
    private void llenarDatosEdificio(int destino){
        Context context=getActivity().getApplicationContext();
        imEdi.setVisibility(View.VISIBLE);
        lblnomEd.setVisibility(View.VISIBLE);
        lbldesEd.setVisibility(View.VISIBLE);

        //region swichCambioInfoImagen

        switch (destino){
            case 0:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.edificioa);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Edificio A");
                lbldesEd.setText("Centro de información, biblioteca, aula interactiva TIC y aula magna");
                break;
            case 1:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.edificiob);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Edificio B");
                lbldesEd.setText("Rectoría y control escolar");
                break;
            case 2:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.edificioc);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Edificio C");
                lbldesEd.setText("Dirección TIC");
                break;
            case 3:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.edificioe);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Edificio E");
                lbldesEd.setText("Cafetería");
                break;
            case 4:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.edificiof);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Edificio F");
                lbldesEd.setText("Dirección Administración");
                break;
            case 5:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.edificiog);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Edificio G");
                lbldesEd.setText("Dirección industrial");
                break;
            case 6:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.edificioh);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Edificio H");
                lbldesEd.setText("Laboratorio pesado 2 (Industrial, turismo y gastronomía)");
                break;
            case 7:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.edificioi);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Edificio I");
                lbldesEd.setText("Laboratorio pesado 1 (Industrial)");
                break;
            case 8:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.edificioj);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Edificio J");
                lbldesEd.setText("Docencia administración");
                break;
            case 9:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.edificiok);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Edificio K");
                lbldesEd.setText("Vinculación");
                break;
            case 10:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.edificiom);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Edificio M");
                lbldesEd.setText("Docencia");
                break;
            case 11:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.edificion);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Edificio N");
                lbldesEd.setText("UNIDE, ECE, Laboratorios físico-químico y de 3D, Docencia");
                break;
            case 12:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.edificioq);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Edificio Q");
                lbldesEd.setText("Ágora");
                break;
            case 13:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.edificior);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Edificio R");
                lbldesEd.setText("CIDU, artes gráficas, CTA Y C-Pro");
                break;
            case 14:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.edificiot);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Edificio T");
                lbldesEd.setText("Dirección DIDE");
                break;
            case 15:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.entrada1);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Entrada principal");
                lbldesEd.setText("Entrada principal de la UTM");
                break;
            case 16:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.entrada2);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Entrada cafetería");
                lbldesEd.setText("Entrada secundaria de la UTM");
                break;
            case 17:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.entrada2);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Estacionamiento");
                lbldesEd.setText("Estacionamiento principal UTM");
                break;
            case 18:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.canchitas);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Canchitas");
                lbldesEd.setText("Tres canchas pequeñas de la UTM");
                break;
            case 19:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.cancha);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Cancha");
                lbldesEd.setText("Cancha de la UTM");
                break;
            case 20:
                imagenEdificio = BitmapFactory.decodeResource(context.getResources(), R.drawable.campo);
                imEdi.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
                lblnomEd.setText("Campo");
                lbldesEd.setText("Campo de la UTM");
                break;
        }
        //endregion
    }
    List<SpinnerItem> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        imagenEdificio = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.mapautm);
        final View view = inflater.inflate(R.layout.fragment_sitios, container, false);
        imEdi = (TouchImageView) view.findViewById(R.id.imgFotoDestino);
        lblnomEd = (TextView) view.findViewById(R.id.lblNombreDestino);
        lbldesEd = (TextView) view.findViewById(R.id.lblDescripcionDestino);
        imageView = (TouchImageView) view.findViewById(R.id.imageView);
        imageView.setImageDrawable(new BitmapDrawable(getResources(), imagenEdificio));
        imEdi.setVisibility(View.INVISIBLE);
        lblnomEd.setVisibility(View.INVISIBLE);
        lbldesEd.setVisibility(View.INVISIBLE);
        //Codigo para la localizacion de los sitios
        for(int i = 0;i<53;i++){
            for(int j = 0;j<53;j++){
                matrizAdyacencia[i][j]=-1;
            }
        }
        //region listaPuntos
        listaPuntos.add(new Point(250,570));
        listaPuntos.add(new Point(275,480));
        listaPuntos.add(new Point(250,390));
        listaPuntos.add(new Point(475,575));
        listaPuntos.add(new Point(460,465));
        listaPuntos.add(new Point(420,280));
        listaPuntos.add(new Point(335,210));
        listaPuntos.add(new Point(595,560));
        listaPuntos.add(new Point(600,465));
        listaPuntos.add(new Point(630,310));
        listaPuntos.add(new Point(570,185));
        listaPuntos.add(new Point(435,220));
        listaPuntos.add(new Point(690,530));
        listaPuntos.add(new Point(720,400));
        listaPuntos.add(new Point(725,130));
        listaPuntos.add(new Point(345,625));
        listaPuntos.add(new Point(490,625));
        listaPuntos.add(new Point(340,570));
        listaPuntos.add(new Point(530,560));
        listaPuntos.add(new Point(190,515));
        listaPuntos.add(new Point(275,515));
        listaPuntos.add(new Point(340,515));
        listaPuntos.add(new Point(530,515));
        listaPuntos.add(new Point(670,515));
        listaPuntos.add(new Point(340,470));
        listaPuntos.add(new Point(380,470));
        listaPuntos.add(new Point(530,465));
        listaPuntos.add(new Point(670,465));
        listaPuntos.add(new Point(795,490));
        listaPuntos.add(new Point(385,415));
        listaPuntos.add(new Point(440,380));
        listaPuntos.add(new Point(365,355));
        listaPuntos.add(new Point(520,390));
        listaPuntos.add(new Point(565,395));
        listaPuntos.add(new Point(630,395));
        listaPuntos.add(new Point(665,395));
        listaPuntos.add(new Point(330,375));
        listaPuntos.add(new Point(300,355));
        listaPuntos.add(new Point(360,320));
        listaPuntos.add(new Point(315,310));
        listaPuntos.add(new Point(275,250));
        listaPuntos.add(new Point(500,345));
        listaPuntos.add(new Point(510,310));
        listaPuntos.add(new Point(495,230));
        listaPuntos.add(new Point(470,200));
        listaPuntos.add(new Point(540,260));
        listaPuntos.add(new Point(560,355));
        listaPuntos.add(new Point(670,260));
        listaPuntos.add(new Point(740,270));
        listaPuntos.add(new Point(910,250));
        listaPuntos.add(new Point(670,185));
        listaPuntos.add(new Point(640,140));
        listaPuntos.add(new Point(670,310));
        //endregion
        //region listaDistancias
        matrizAdyacencia[0][17]=90;
        matrizAdyacencia[17][0]=90;
        matrizAdyacencia[1][20]=35;
        matrizAdyacencia[20][0]=35;
        matrizAdyacencia[1][24]=66;
        matrizAdyacencia[24][1]=66;
        matrizAdyacencia[2][37]=61;
        matrizAdyacencia[37][2]=61;
        matrizAdyacencia[3][17]=135;
        matrizAdyacencia[17][3]=135;
        matrizAdyacencia[3][18]=57;
        matrizAdyacencia[18][3]=57;
        matrizAdyacencia[4][25]=80;
        matrizAdyacencia[25][4]=80;
        matrizAdyacencia[4][26]=70;
        matrizAdyacencia[26][4]=70;
        matrizAdyacencia[5][38]=72;
        matrizAdyacencia[38][5]=72;
        matrizAdyacencia[5][43]=90;
        matrizAdyacencia[43][5]=90;
        matrizAdyacencia[6][40]=72;
        matrizAdyacencia[40][6]=72;
        matrizAdyacencia[7][18]=65;
        matrizAdyacencia[18][7]=65;
        matrizAdyacencia[8][26]=70;
        matrizAdyacencia[26][8]=70;
        matrizAdyacencia[8][27]=70;
        matrizAdyacencia[27][8]=70;
        matrizAdyacencia[9][34]=85;
        matrizAdyacencia[34][9]=85;
        matrizAdyacencia[9][46]=83;
        matrizAdyacencia[46][9]=83;
        matrizAdyacencia[9][52]=40;
        matrizAdyacencia[52][9]=40;
        matrizAdyacencia[10][43]=87;
        matrizAdyacencia[43][10]=87;
        matrizAdyacencia[10][51]=83;
        matrizAdyacencia[51][10]=83;
        matrizAdyacencia[11][44]=40;
        matrizAdyacencia[44][11]=40;
        matrizAdyacencia[12][23]=25;
        matrizAdyacencia[23][12]=25;
        matrizAdyacencia[13][35]=55;
        matrizAdyacencia[35][13]=55;
        matrizAdyacencia[14][49]=221;
        matrizAdyacencia[49][14]=221;
        matrizAdyacencia[14][51]=86;
        matrizAdyacencia[51][14]=86;
        matrizAdyacencia[15][17]=55;
        matrizAdyacencia[17][15]=55;
        matrizAdyacencia[16][18]=76;
        matrizAdyacencia[18][16]=76;
        matrizAdyacencia[17][21]=55;
        matrizAdyacencia[21][17]=55;
        matrizAdyacencia[18][22]=45;
        matrizAdyacencia[22][18]=45;
        matrizAdyacencia[19][20]=85;
        matrizAdyacencia[20][19]=85;
        matrizAdyacencia[20][21]=65;
        matrizAdyacencia[21][20]=65;
        matrizAdyacencia[21][22]=190;
        matrizAdyacencia[22][21]=190;
        matrizAdyacencia[22][23]=140;
        matrizAdyacencia[22][23]=140;
        matrizAdyacencia[23][27]=50;
        matrizAdyacencia[27][23]=50;
        matrizAdyacencia[23][28]=127;
        matrizAdyacencia[28][23]=127;
        matrizAdyacencia[24][25]=40;
        matrizAdyacencia[24][25]=40;
        matrizAdyacencia[24][36]=96;
        matrizAdyacencia[36][24]=96;
        matrizAdyacencia[25][29]=55;
        matrizAdyacencia[29][25]=55;
        matrizAdyacencia[26][33]=78;
        matrizAdyacencia[33][26]=78;
        matrizAdyacencia[27][35]=70;
        matrizAdyacencia[35][27]=70;
        matrizAdyacencia[28][48]=227;
        matrizAdyacencia[28][48]=227;
        matrizAdyacencia[29][30]=65;
        matrizAdyacencia[30][29]=65;
        matrizAdyacencia[30][31]=79;
        matrizAdyacencia[31][30]=79;
        matrizAdyacencia[30][32]=81;
        matrizAdyacencia[32][30]=81;
        matrizAdyacencia[30][41]=69;
        matrizAdyacencia[41][30]=69;
        matrizAdyacencia[31][36]=40;
        matrizAdyacencia[36][31]=40;
        matrizAdyacencia[31][38]=35;
        matrizAdyacencia[38][31]=35;
        matrizAdyacencia[32][33]=45;
        matrizAdyacencia[33][32]=45;
        matrizAdyacencia[32][46]=53;
        matrizAdyacencia[46][32]=53;
        matrizAdyacencia[33][34]=65;
        matrizAdyacencia[34][33]=65;
        matrizAdyacencia[34][35]=35;
        matrizAdyacencia[35][34]=35;
        matrizAdyacencia[35][52]=85;
        matrizAdyacencia[52][35]=85;
        matrizAdyacencia[36][37]=36;
        matrizAdyacencia[37][36]=36;
        matrizAdyacencia[37][39]=47;
        matrizAdyacencia[39][37]=47;
        matrizAdyacencia[38][39]=46;
        matrizAdyacencia[39][38]=46;
        matrizAdyacencia[39][40]=72;
        matrizAdyacencia[40][39]=72;
        matrizAdyacencia[41][42]=36;
        matrizAdyacencia[42][41]=36;
        matrizAdyacencia[42][43]=81;
        matrizAdyacencia[43][42]=81;
        matrizAdyacencia[42][46]=67;
        matrizAdyacencia[46][42]=67;
        matrizAdyacencia[43][44]=39;
        matrizAdyacencia[44][43]=39;
        matrizAdyacencia[43][45]=54;
        matrizAdyacencia[45][43]=54;
        matrizAdyacencia[45][47]=130;
        matrizAdyacencia[47][45]=130;
        matrizAdyacencia[45][50]=150;
        matrizAdyacencia[50][45]=150;
        matrizAdyacencia[47][48]=71;
        matrizAdyacencia[48][47]=71;
        matrizAdyacencia[47][49]=240;
        matrizAdyacencia[49][47]=240;
        matrizAdyacencia[47][50]=75;
        matrizAdyacencia[50][47]=75;
        matrizAdyacencia[47][52]=50;
        matrizAdyacencia[52][47]=50;
        matrizAdyacencia[48][49]=171;
        matrizAdyacencia[49][48]=171;
        matrizAdyacencia[48][52]=81;
        matrizAdyacencia[52][48]=81;
        matrizAdyacencia[49][50]=249;
        matrizAdyacencia[50][49]=249;
        matrizAdyacencia[49][52]=247;
        matrizAdyacencia[52][49]=247;
        matrizAdyacencia[50][51]=54;
        matrizAdyacencia[51][50]=54;
        //endregion
        //region spinners
        spinnerDestino = (Spinner)view.findViewById(R.id.spnDestino);
        spinnerOrigen = (Spinner) view.findViewById(R.id.spnOrigen);
        String[] array = (getResources().getStringArray(R.array.SitiosPrincipal));
        list = new ArrayList();
        //Lista principal
        list.add(new SpinnerItem(array[0],R.drawable.icon_a));
        list.add(new SpinnerItem(array[1],R.drawable.icon_b));
        list.add(new SpinnerItem(array[2],R.drawable.icon_c));
        list.add(new SpinnerItem(array[3],R.drawable.icon_e));
        list.add(new SpinnerItem(array[4],R.drawable.icon_f));
        list.add(new SpinnerItem(array[5],R.drawable.icon_g));
        list.add(new SpinnerItem(array[6],R.drawable.icon_h));
        list.add(new SpinnerItem(array[7],R.drawable.icon_i));
        list.add(new SpinnerItem(array[8],R.drawable.icon_j));
        list.add(new SpinnerItem(array[9],R.drawable.icon_k));
        list.add(new SpinnerItem(array[10],R.drawable.icon_m));
        list.add(new SpinnerItem(array[11],R.drawable.icon_n));
        list.add(new SpinnerItem(array[12],R.drawable.icon_q));
        list.add(new SpinnerItem(array[13],R.drawable.icon_r));
        list.add(new SpinnerItem(array[14],R.drawable.icon_t));
        list.add(new SpinnerItem(array[15],R.drawable.icon_o));
        list.add(new SpinnerItem(array[16],R.drawable.icon_o));
        list.add(new SpinnerItem(array[17],R.drawable.icon_o));
        list.add(new SpinnerItem(array[18],R.drawable.icon_o));
        list.add(new SpinnerItem(array[19],R.drawable.icon_o));
        list.add(new SpinnerItem(array[20],R.drawable.icon_o));
        //21-26
        array = (getResources().getStringArray(R.array.EdiA));
        for(String nom : array){
            list.add(new SpinnerItem(nom,R.drawable.icon_a));
        }
        //27-31
        array = (getResources().getStringArray(R.array.EdiB));
        for(String nom : array){
            list.add(new SpinnerItem(nom,R.drawable.icon_b));
        }
        //32-43
        array = (getResources().getStringArray(R.array.EdiC));
        for(String nom : array){
            list.add(new SpinnerItem(nom,R.drawable.icon_c));
        }//44-46
        array = (getResources().getStringArray(R.array.EdiE));
        for(String nom : array){
            list.add(new SpinnerItem(nom,R.drawable.icon_e));
        }
        //47-68
        array = (getResources().getStringArray(R.array.EdiF));
        for(String nom : array){
            list.add(new SpinnerItem(nom,R.drawable.icon_f));
        }
        //69-90
        array = (getResources().getStringArray(R.array.EdiG));
        for(String nom : array){
            list.add(new SpinnerItem(nom,R.drawable.icon_g));
        }
        //91-98
        array = (getResources().getStringArray(R.array.EdiH));
        for(String nom : array){
            list.add(new SpinnerItem(nom,R.drawable.icon_h));
        }
        //99-111
        array = (getResources().getStringArray(R.array.EdiI));
        for(String nom : array){
            list.add(new SpinnerItem(nom,R.drawable.icon_i));
        }
        //112-125
        array = (getResources().getStringArray(R.array.EdiJ));
        for(String nom : array){
            list.add(new SpinnerItem(nom,R.drawable.icon_j));
        }
        //126-135
        array = (getResources().getStringArray(R.array.EdiK));
        for(String nom : array){
            list.add(new SpinnerItem(nom,R.drawable.icon_k));
        }
        //136-154
        array = (getResources().getStringArray(R.array.EdiM));
        for(String nom : array){
            list.add(new SpinnerItem(nom,R.drawable.icon_m));
        }
        //155-175
        array = (getResources().getStringArray(R.array.EdiN));
        for(String nom : array){
            list.add(new SpinnerItem(nom,R.drawable.icon_n));
        }
        //176-184
        array = (getResources().getStringArray(R.array.EdiR));
        for(String nom : array){
            list.add(new SpinnerItem(nom,R.drawable.icon_r));
        }
        //177-202
        array = (getResources().getStringArray(R.array.EdiT));
        for(String nom : array){
            list.add(new SpinnerItem(nom,R.drawable.icon_t));
        }
        SpinnerAdapter spinnerAdapter = new FilterAdapter(getActivity(),list);

        spinnerDestino.setAdapter(spinnerAdapter);
        spinnerOrigen.setAdapter(spinnerAdapter);
        //endregion
        paint.setColor(Color.RED);
        paint.setStrokeWidth(15);
        //region checkbox
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    spinnerOrigen.setClickable(false);
                    spinnerOrigen.setSelection(15);
                }
                else {
                    spinnerOrigen.setClickable(true);
                }
            }
        });
        //endregion
        //region btnBuscar
        btnBuscar=(Button)view.findViewById(R.id.button2);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context=getActivity().getApplicationContext();
                txtResultado = (TextView) view.findViewById(R.id.txtResultado);
                txtResultado.setText("Esta es la mejor ruta!");
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.mapautm);
                tempBitmap = Bitmap.createBitmap(icon.getWidth(), icon.getHeight(), Bitmap.Config.RGB_565);
                tempCanvas = new Canvas(tempBitmap);
                tempCanvas.drawBitmap(icon, 0, 0, null);
                int destino;
                int origen;
                origen = (int)spinnerOrigen.getSelectedItemId();
                destino = (int)spinnerDestino.getSelectedItemId();

                //21-26 A
                if(destino >= 21 && destino <= 26){
                    destino = 0;
                }
                //27-31 B
                if(destino >= 27 && destino <= 31){
                    destino = 1;
                }
                //32-43 C
                if(destino >= 32 && destino <= 43){
                    destino = 2;
                }
                //44-46 E
                if(destino >= 44 && destino <= 46){
                    destino = 3;
                }
                //47-68 F
                if(destino >= 47 && destino <= 68){
                    destino = 4;
                }
                //69-80 G
                if(destino >= 69 && destino <= 80){
                    destino = 5;
                }
                //90-98
                if(destino >= 81 && destino <= 88){
                    destino = 6;
                }
                //99-111
                if(destino >= 89 && destino <= 101){
                    destino = 7;
                }
                //112-125
                if(destino >= 102 && destino <= 115){
                    destino = 8;
                }
                //126-135
                if(destino >= 116 && destino <= 125){
                    destino = 9;
                }
                //136-154
                if(destino >= 126 && destino <= 144){
                    destino = 10;
                }
                //155-175
                if(destino >= 145 && destino <= 165){
                    destino = 11;
                }
                //176-184
                if(destino >= 166 && destino <= 174){
                    destino = 13;
                }
                //176-202
                if(destino >= 175 && destino <= 192){
                    destino = 14;
                }

                llenarDatosEdificio(destino);

                if(origen == 17){origen = 19;}
                else if(origen == 18){origen = 48;}
                else if(origen == 19){origen = 28;}
                else if(origen == 20){origen = 49;}

                if(destino == 17){destino = 19;}
                else if(destino == 18){destino = 48;}
                else if(destino == 19){destino = 28;}
                else if(destino == 20){destino = 49;}

                if(origen == destino){
                    Toast.makeText(context,"El origen y el destino deben ser diferentes", Toast.LENGTH_SHORT).show();
                }
                else {
                    dijkstra asd = new dijkstra(matrizAdyacencia, origen, destino);
                    int x = 0;
                    imageView = (TouchImageView) view.findViewById(R.id.imageView);
                    Point trazo1 = new Point(0, 0), trazo2;
                    for (int i = 0; i < asd.caminosFinal.length(); i++) {
                        if (x == 0) {
                            trazo1 = listaPuntos.get((int) asd.caminosFinal.charAt(i) - 65);
                            x = 1;
                        } else {
                            trazo2 = listaPuntos.get((int) asd.caminosFinal.charAt(i) - 65);
                            int width = tempBitmap.getWidth() / 1000;
                            int height = tempBitmap.getHeight() / 655;
                            tempCanvas.drawLine(trazo1.x * width, trazo1.y * height, trazo2.x * width, trazo2.y * height, paint);
                            trazo1 = trazo2;
                        }
                    }
                    imageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
                }
                //Fin del código
            }
        });
        //endregion
        return  view;
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

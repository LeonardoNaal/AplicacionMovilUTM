package com.fsociety.linkutmbetty;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ReportarActivity extends AppCompatActivity {
    TextView lblNombreUser, lblApellidoUser, lblTitulo;
    RadioButton falsa, desnudos, ofensa, ofensa2, inadecuado, otra;
    Button btnReportar;
    EditText txtRazon;
    private RadioGroup radioGroup;
    int grado,codPublicacion;
    String eleccion,grupo,carrera,codUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar);

        //Referencias a objetos TextView de la vista
        lblNombreUser = (TextView) findViewById(R.id.lblNombreUser);
        lblApellidoUser = (TextView) findViewById(R.id.lblApellidoUser);
        lblTitulo = (TextView) findViewById(R.id.lblTitulo);

        //Referencia al EditText de la vista
        txtRazon = (EditText) findViewById(R.id.txtRazon);
        txtRazon.setInputType(InputType.TYPE_NULL);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String datotitulo = (String) extras.get("titulo");
            codPublicacion = (int) extras.getInt("id");
            codUser = (String) extras.get("codUser");
            lblTitulo.setText(datotitulo);
            grado=(int)extras.getInt("grado");
            grupo=(String)extras.getString("grupo");
            carrera=(String)extras.getString("carrera");
        }

        //Referencias a los RadioButon
        falsa = (RadioButton) findViewById(R.id.falsa);
        desnudos = (RadioButton) findViewById(R.id.desnudos);
        ofensa = (RadioButton) findViewById(R.id.ofensas);
        ofensa2 = (RadioButton) findViewById(R.id.ofensas2);
        inadecuado = (RadioButton) findViewById(R.id.inadecuado);
        otra = (RadioButton) findViewById(R.id.otra);
        radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // find which radio button is selected

                if(checkedId == R.id.otra) {
                    txtRazon.setEnabled(true);
                } else {
                    txtRazon.setText("");
                    txtRazon.setEnabled(false);
                }
                if(checkedId==R.id.inadecuado){
                    eleccion=inadecuado.getText().toString();
                }
                else  if(checkedId==R.id.falsa){
                eleccion=falsa.getText().toString();
                }
                else if(checkedId==R.id.desnudos){
                    eleccion=desnudos.getText().toString();
                }
                else if(checkedId==R.id.ofensas){
                    eleccion=ofensa.getText().toString();
                }
                else if(checkedId==R.id.ofensas2){
                    eleccion=ofensa2.getText().toString();
                }
            }
        });

        //Referencia al botón Reportar
        btnReportar = (Button) findViewById(R.id.btnReportar);
        btnReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String razon;
                if(txtRazon.getText().toString().equals("")){
                    razon=eleccion;
                }else{
                 razon=txtRazon.getText().toString();
                }
                //Acción para el botón Reportar
                String action = "AgregarReporte";
                //String Url="http://fsociety.somee.com/WebService.asmx/";
                //String Url="http://169.254.3.130:8091/WebService.asmx/";
                String Url = "http://davisaac19-001-site1.atempurl.com/WebService.asmx/";
                String UrlWeb = Url + action + "?CodPublicacion=" + codPublicacion + "&codUsuario=" + codUser+"&Razon="+razon;
                UrlWeb=UrlWeb.replaceAll(" ","%20");
                new JSONTask().execute(UrlWeb);
            }
        });
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
                if (Integer.parseInt(resultado) == 1) {
                    Toast.makeText(ReportarActivity.this, "Bienvenido ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReportarActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                    //Snackbar.make(LogInActivity.this, "Usuario o contraseña incorrectos", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                /*
                 Intent i=new Intent(MainActivity.this,Main2Activity.class);
                i.putExtra("ResultadoEnArray",resultado);
                startActivity(i);
                 */
            } catch (Throwable t) {
                Log.e("Falla", t.toString());
            }
        }
    }
}

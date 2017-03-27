package com.fsociety.linkutmbetty;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {
    EditText edtUsuario, edtContraseña;
    TextView lblCondiciones;
    boolean verificar = true;
    public String SERVER = "http://davisaac19-001-site1.atempurl.com/WebService.asmx/DatosAlumno?", timestamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setTitle("Iniciar sesión");
        edtUsuario = (EditText) findViewById(R.id.txtMatricula);
        edtUsuario.addTextChangedListener(new TextValidator(edtUsuario) {
            @Override
            public void validate(EditText editText, String text) {
                if (text.length() < 8) {
                    edtUsuario.setError("La matricula es muy corta");
                    verificar = false;
                } else {
                    verificar = true;
                }
            }
        });
        edtContraseña = (EditText) findViewById(R.id.txtContraseña);
        Button btnIniciar = (Button) findViewById(R.id.btnIniciar);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtUsuario.getText().toString();
                String contraseña = edtContraseña.getText().toString();
                if (verificar) {
                    String action = "Loguin";
                    //String Url="http://fsociety.somee.com/WebService.asmx/";
                    //String Url="http://169.254.3.130:8091/WebService.asmx/";
                    String Url = "http://davisaac19-001-site1.atempurl.com/WebService.asmx/";
                    String UrlWeb = Url + action + "?user=" + user + "&contra=" + contraseña;
                    new JSONTask().execute(UrlWeb);
                } else {
                    Snackbar.make(v, "Usuario o contraseña incorrectos", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        Button btnRegistro = (Button) findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        lblCondiciones = (TextView) findViewById(R.id.lblCondiciones);
        lblCondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo().show();
                //dialogo().dismiss();
            }
        });
    }

    public AlertDialog dialogo(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
        final LayoutInflater inflater = LogInActivity.this.getLayoutInflater();

        View v = inflater.inflate(R.layout.vista_condiciones, null);
        builder.setView(v);


        Button btnCerrar = (Button) v.findViewById(R.id.btnDismiss);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ad.dismiss();
            }
        });

        return builder.create();
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
                    String Nombre = edtUsuario.getText().toString();
                    new Upload(Nombre).execute();
                } else {
                    Toast.makeText(LogInActivity.this, "Usuario o contraseña incorrecto", Toast.LENGTH_SHORT).show();
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

        public Upload(String codUsuario) {
            this.codUsuario = codUsuario;
        }

        @Override
        protected String doInBackground(Void... params) {

            //generate hashMap to store encodedImage and the name
            HashMap<String, String> detail = new HashMap<>();
            detail.put("matricula", codUsuario);
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
            super.onPostExecute(s);
            try {
                Log.e("salida", s);
                JSONArray ResultadoArray = null;
                try {
                    JSONObject Jasonobject = new JSONObject(s);
                    JSONArray Jarray = Jasonobject.getJSONArray("Table");
                    JSONObject objeto = Jarray.getJSONObject(0);
                    Toast.makeText(LogInActivity.this, "Bienvenido "+objeto.getString("Nombres")+" "+objeto.getString("ApePat"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this, UserMainActivity.class);
                    intent.putExtra("carrera",objeto.getString("carrera"));
                    intent.putExtra("Matricula",objeto.getString("CodUsuario"));

                    if(objeto.getString("grado")!="null")
                    {
                        intent.putExtra("grado",objeto.getInt("grado"));
                    }
                    else
                    {
                        intent.putExtra("grado",0);
                    }

                    intent.putExtra("grupo",objeto.getString("grupo"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    LogInActivity.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Throwable t) {
                Log.e("Falla", t.toString());
            }
        }
    }
}

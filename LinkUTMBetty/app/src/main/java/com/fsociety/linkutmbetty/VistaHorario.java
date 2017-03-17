package com.fsociety.linkutmbetty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.EditText;
import android.widget.TextView;

public class VistaHorario extends AppCompatActivity {

    TextView materia,maestro,edificio,hora;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_horario);

        materia = (TextView) findViewById(R.id.txtMateria);
        maestro = (TextView) findViewById(R.id.txtMaestro);
        edificio = (TextView) findViewById(R.id.txtEdificio);
        hora = (TextView) findViewById(R.id.txtHora);
    }

    public String getMateria()
    {
        return materia.getText().toString();
    }
    public String getMaestro()
    {
        return maestro.getText().toString();
    }
    public String getEdificio()
    {
        return edificio.getText().toString();
    }
    public String getHora()
    {
        return hora.getText().toString();
    }
}

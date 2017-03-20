package com.fsociety.linkutmbetty;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class DetallesActividades extends AppCompatActivity {
    TextView lblTitulo, lblContenido, lblFechaIni, lblFinFecha, lblHoraIni, lblHoraFin;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_actividades);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lblContenido = (TextView) findViewById(R.id.detalles_contenido);
        lblFechaIni = (TextView) findViewById(R.id.fechaini);
        lblFinFecha = (TextView) findViewById(R.id.finfecha);
        lblHoraIni = (TextView) findViewById(R.id.horaini);
        lblHoraFin = (TextView) findViewById(R.id.horafin);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            String datoTitulo = (String) extras.get("nombre");
            String datoFechaIni = (String) extras.get("fechaini");
            String datoFechaFin = (String) extras.get("finfecha");
            String datoHoraIni = (String) extras.get("horaini");
            String datoHoraFin = (String) extras.get("horafin");
            String datoContenido = (String) extras.get("contenido");

            lblFechaIni.setText(datoFechaIni);
            lblFinFecha.setText(datoFechaFin);
            lblHoraIni.setText(datoHoraIni);
            lblHoraFin.setText(datoHoraFin);
            lblContenido.setText(datoContenido);
            //setTitle(datoTitulo);
        }
    }
}

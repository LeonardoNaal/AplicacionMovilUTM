package com.fsociety.linkutmbetty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ReportarActivity extends AppCompatActivity {
    TextView lblNombreUser, lblApellidoUser, lblTitulo;
    RadioButton falsa, desnudos, ofensa, ofensa2, inadecuado, otra;
    Button btnReportar;
    EditText txtRazon;

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

        //Referencias a los RadioButon
        falsa = (RadioButton) findViewById(R.id.falsa);
        desnudos = (RadioButton) findViewById(R.id.desnudos);
        ofensa = (RadioButton) findViewById(R.id.ofensas);
        ofensa2 = (RadioButton) findViewById(R.id.ofensas2);
        inadecuado = (RadioButton) findViewById(R.id.inadecuado);
        otra = (RadioButton) findViewById(R.id.otra);

        //Si selecciona la opción "Otra" se habilida el EditText para escribir la razón
        if (otra.isChecked()) {
            Toast.makeText(this, "Funciona", Toast.LENGTH_SHORT).show();
        }

        //Referencia al botón Reportar
        btnReportar = (Button) findViewById(R.id.btnReportar);
        btnReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Acción para el botón Reportar
                Toast.makeText(ReportarActivity.this, "Works fine", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

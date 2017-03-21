package com.fsociety.linkutmbetty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Horario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        crearLista();

        Button boton = (Button) findViewById(R.id.btnAgregar);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Horario.this, GestionHorario.class);
                intent.putExtra("Accion", "Guardar");
                startActivity(intent);
            }
        });
    }

    private void crearLista()
    {
        String[] dias = {"Lunes","Martes","Miercoles","Jueves","Viernes"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, dias);
        final ListView listView = (ListView) findViewById(R.id.lstvLista);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String  itemValue    = (String) listView.getItemAtPosition(i);
                Object item = (Object) listView.getItemAtPosition(i);
                Intent intent = new Intent(Horario.this, ListaHorario.class);
                intent.putExtra("Dia", itemValue);
                startActivity(intent);
            }
        });
    }

}

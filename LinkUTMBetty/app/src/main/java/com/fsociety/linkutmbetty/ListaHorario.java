package com.fsociety.linkutmbetty;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaHorario extends AppCompatActivity {

    ArrayList<HorarioBO> listaDatos = new ArrayList<HorarioBO>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_horario);

        Bundle extra = getIntent().getExtras();
        if (extra != null)
        {
            String dia = (String)extra.get("Dia").toString();
            Toast.makeText(getApplicationContext(), dia , Toast.LENGTH_SHORT).show();
        }

        final ListView listview = (ListView)findViewById(R.id.lstvHorario);
        Consulta((String)extra.get("Dia").toString());

        listview.setAdapter(new Lista_adaptador(this, R.layout.activity_vista_horario, listaDatos)
        {
            @Override
            public void onEntrada(Object entrada, View view)
            {
                if (entrada != null)
                {
                    TextView Materia = (TextView) view.findViewById(R.id.txtMateria);
                    Materia.setText(((HorarioBO) entrada).getMateria());

                    TextView Hora = (TextView) view.findViewById(R.id.txtHora);
                    Hora.setText(((HorarioBO) entrada).getHora());

                    TextView Maestro = (TextView) view.findViewById(R.id.txtMaestro);
                    Maestro.setText(((HorarioBO) entrada).getMaestro());

                    TextView edificio = (TextView) view.findViewById(R.id.txtEdificio);
                    edificio.setText(((HorarioBO) entrada).getEdificio());
                }
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String  itemValue    = (String) listview.getItemAtPosition(i);
                Object item = (Object) listview.getItemAtPosition(i);

                return true;
            }
        });

    }

    public void Consulta(String dia)
    {
        HorarioOpenHelper hdb = new HorarioOpenHelper(this,"DBHorario",null,1);
        SQLiteDatabase db = hdb.getReadableDatabase();

        try
        {
            String[] columna = {"MATERIA","HORA","PROFESOR","LABORATORIO"};
            String[] seleccion = {dia};

            Cursor fila = db.query("HORARIO",columna,"DIA=?",seleccion,null,null,"HORA");
            if(fila.moveToFirst())
            {
                do
                {
                    Toast.makeText(getApplicationContext(),fila.getString(1), Toast.LENGTH_SHORT).show();
                    listaDatos.add(new HorarioBO(fila.getString(0),fila.getString(1),fila.getString(2),fila.getString(3)));
                }while(fila.moveToNext());
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "No hay horarios" , Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}

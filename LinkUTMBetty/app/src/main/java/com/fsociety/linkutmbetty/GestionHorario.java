package com.fsociety.linkutmbetty;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class GestionHorario extends AppCompatActivity {

    private Spinner spin;
    private EditText materia,profesor,laboratorio,hora;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_horario);


        materia = (EditText) findViewById(R.id.edtxMateria);
        profesor = (EditText) findViewById(R.id.edtxProfesor);
        laboratorio = (EditText) findViewById(R.id.edtxLaboratorio);
        hora = (EditText) findViewById(R.id.edtHora);
        spin = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter.add("Lunes");
        adapter.add("Martes");
        adapter.add("Miercoles");
        adapter.add("Jueves");
        adapter.add("Viernes");
        spin.setAdapter(adapter);

        final Button boton = (Button) findViewById(R.id.btnBoton);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle extra = getIntent().getExtras();
                if (extra != null)
                {
                    String accion = (String)extra.get("Accion");
                    switch (accion)
                    {
                        case "Guardar":
                            Alta();
                            break;
                        case "Actualizar":
                            break;
                    }
                }
            }
        });

        final Button btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void Alta()
    {
        HorarioOpenHelper hdb = new HorarioOpenHelper(this,"DBHorario",null,1);
        SQLiteDatabase db = hdb.getWritableDatabase();

        String Usuario = "YO";
        String Dia = spin.getSelectedItem().toString();
        String Materia = materia.getText().toString();
        String Profesor = profesor.getText().toString();
        String Laboratorio = laboratorio.getText().toString();
        String Hora = hora.getText().toString();

        ContentValues datos = new ContentValues();
        datos.put("USUARIO",Usuario);
        datos.put("DIA",Dia);
        datos.put("MATERIA",Materia);
        datos.put("PROFESOR",Profesor);
        datos.put("LABORATORIO",Laboratorio);
        datos.put("HORA",Hora);
        db.insert("HORARIO", null, datos);

        Toast.makeText(this, "Datos guardados",Toast.LENGTH_SHORT).show();
        db.close();
    }

    public void Eliminar()
    {
        HorarioOpenHelper hdb = new HorarioOpenHelper(this,"DBUsuario",null,1);
        SQLiteDatabase db = hdb.getWritableDatabase();

        String hor = hora.getText().toString();

        int cant = db.delete("HORARIO", "HORA=" + hor, null);
        if (cant == 1)
        {
            Toast.makeText(this, "Datos borrados",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Datos inexistentes",Toast.LENGTH_SHORT).show();
        }
    }

}

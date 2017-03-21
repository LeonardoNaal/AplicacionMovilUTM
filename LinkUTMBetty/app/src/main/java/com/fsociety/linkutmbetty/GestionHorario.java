package com.fsociety.linkutmbetty;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class GestionHorario extends AppCompatActivity  implements DialogInterface.OnClickListener,TimePicker.OnTouchListener
{

    private Spinner spin;
    private EditText materia,profesor,laboratorio,hora;
    private int CalendarHour, CalendarMinute;
    String format;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_horario);

        materia = (EditText) findViewById(R.id.edtxMateria);
        profesor = (EditText) findViewById(R.id.edtxProfesor);
        laboratorio = (EditText) findViewById(R.id.edtxLaboratorio);
        hora = (EditText) findViewById(R.id.edtHora);
        spin = (Spinner) findViewById(R.id.spinner);


        Button btn = (Button) findViewById(R.id.btnSeleccionar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(GestionHorario.this ,new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        if (hourOfDay == 0)
                        {
                            hourOfDay += 12;
                            format = "AM";
                        }
                        else
                        {
                            if (hourOfDay == 12)
                            {
                                format = "PM";
                            } else
                            {
                                if (hourOfDay > 12)
                                {
                                    hourOfDay -= 12;
                                    format = "PM";
                                } else
                                {
                                    format = "AM";
                                }
                            }
                        }
                        hora.setText(hourOfDay + ":" + minute + format);
                    };
                },CalendarHour, CalendarMinute, false);
                timePickerDialog.show();
            }
        });


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
                            Actualizar();
                            break;
                    }
                }
            }
        });

        final Button btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Eliminar();
            }
        });

        CargarDatos();
    }

    public void CargarDatos()
    {
        Bundle extra = getIntent().getExtras();
        String accion = (String)extra.get("Accion").toString();

        if(accion.length() > 8)
        {
            switch((String)extra.get("Dia").toString())
            {
                case "Lunes":
                    spin.setSelection(0);
                    break;
                case "Martes":
                    spin.setSelection(1);
                    break;
                case "Miercoles":
                    spin.setSelection(2);
                    break;
                case "Jueves":
                    spin.setSelection(3);
                    break;
                case "Viernes":
                    spin.setSelection(4);
                    break;
            }
            materia.setText((String)extra.get("Materia").toString());
            profesor.setText((String)extra.get("Maestro").toString());
            laboratorio.setText((String)extra.get("Edificio").toString());
            hora.setText((String)extra.get("Hora").toString());
        }
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

        if(Materia.length() == 0 || Profesor.length() == 0 || Laboratorio.length() == 0 || Hora.length() == 0)
        {
            Toast.makeText(this, "LLene los campos",Toast.LENGTH_SHORT).show();
            db.close();
        }
        else
        {
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
            Fin();
        }
    }

    public void Eliminar()
    {
        HorarioOpenHelper hdb = new HorarioOpenHelper(this,"DBHorario",null,1);
        SQLiteDatabase db = hdb.getWritableDatabase();

        String hor = hora.getText().toString();

        db.execSQL("DELETE FROM HORARIO WHERE HORA=" + "'" + hor + "'");
        db.close();

        String Dia = spin.getSelectedItem().toString();
        String Materia = materia.getText().toString();
        String Profesor = profesor.getText().toString();
        String Laboratorio = laboratorio.getText().toString();
        String Hora = hora.getText().toString();
        if(Materia.length() == 0 || Profesor.length() == 0 || Laboratorio.length() == 0 || Hora.length() == 0)
        {
            Toast.makeText(this, "Datos inexistentes",Toast.LENGTH_SHORT).show();
            db.close();
        }
        else
        {
            Toast.makeText(this, "Datos borrados",Toast.LENGTH_SHORT).show();
        }
        Fin();
    }

    public void Actualizar()
    {
        HorarioOpenHelper hdb = new HorarioOpenHelper(this,"DBUsuario",null,1);
        SQLiteDatabase db = hdb.getWritableDatabase();

        String Usuario = "YO";
        String Dia = spin.getSelectedItem().toString();
        String Materia = materia.getText().toString();
        String Profesor = profesor.getText().toString();
        String Laboratorio = laboratorio.getText().toString();
        String Hora = hora.getText().toString();

        if(Materia.length() == 0 || Profesor.length() == 0 || Laboratorio.length() == 0 || Hora.length() == 0)
        {
            Toast.makeText(this, "LLene los campos",Toast.LENGTH_SHORT).show();
            db.close();
        }
        else
        {
            ContentValues datos = new ContentValues();
            datos.put("USUARIO",Usuario);
            datos.put("DIA",Dia);
            datos.put("MATERIA",Materia);
            datos.put("PROFESOR",Profesor);
            datos.put("LABORATORIO",Laboratorio);
            datos.put("HORA",Hora);
            //db.update("HORARIO", datos,"USUARIO=" + "'YO'",null);
            //db.execSQL("UPDATE HORARIO SET DIA='" + Dia + "',MATERIA='" + Materia + "',PROFESOR='" + Profesor + "',LABORATORIO='" + Laboratorio + "',HORA='" + Hora + "' WHERE USUARIO='YO'");
            Toast.makeText(this, "Datos actualizados",Toast.LENGTH_SHORT).show();
            db.close();
            Fin();
        }
    }
    public void Fin()
    {
        Intent intent = new Intent(GestionHorario.this,Horario.class);
        intent.putExtra("Dia","Ninguna");
        startActivity(intent);
    }
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}

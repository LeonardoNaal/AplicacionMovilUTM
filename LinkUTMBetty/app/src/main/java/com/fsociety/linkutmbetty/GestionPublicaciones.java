package com.fsociety.linkutmbetty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GestionPublicaciones extends AppCompatActivity {
    Button btnModificar;
    Button btnEliminar;
    EditText txtTitulo;
    EditText txtContenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_publicaciones);

        //La actividad GestionPublicaciones recibe los datos de la publicación que están en el ListView de UserMainActivity
        //Como título, descripción o ¿tipo?
        //La actividad incluye dos botones
        //Un botón para guardar los cambios que después de guardar regresa a UserMainActivity
        //Otro botón para eliminar que después de eliminar regresa a UserMainActivity

        //Referencia al EditText Título
        txtTitulo = (EditText) findViewById(R.id.txtTitulo);
        //Referencia al EditTex Contenido
        txtContenido = (EditText) findViewById(R.id.txtContenido);


        //Referencia y evento al botón Guardar
        btnModificar = (Button) findViewById(R.id.btnModificar);
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Al finalizar este método debe regresar a UserMainActivity
            }
        });

        //Referencia y evento al botón Eliminar
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Al finalizar este método debe regresar a UserMainActivity
            }
        });
    }
}

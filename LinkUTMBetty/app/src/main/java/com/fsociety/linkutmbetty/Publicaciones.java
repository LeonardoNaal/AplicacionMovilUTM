package com.fsociety.linkutmbetty;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by admin on 09/03/2017.
 */

public class Publicaciones {




    protected int id;
    protected String titulo;
    protected String fecha;
    protected String contenido;
    protected Bitmap photo;
    protected String data;

    public  Publicaciones(int id,String data,String tit,String Fecha,String Contenido){
        this.id=id;
        this.data=data;
        this.titulo=tit;
        this.contenido=contenido;
        try {
            byte[] byteData = Base64.decode(data, Base64.DEFAULT);
            this.photo = BitmapFactory.decodeByteArray( byteData, 0,
                    byteData.length);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getFecha() {
        return fecha;
    }

    public String getContenido() {
        return contenido;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public String getData() {
        return data;
    }

}

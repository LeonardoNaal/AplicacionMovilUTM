package com.fsociety.linkutmbetty;

/**
 * Created by admin on 06/03/2017.
 */

public class Actividades {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int id;
    public String Nombre;
    public String HInicio;
    public String HFin;

    public String getContenido() {
        return Contenido;
    }

    public void setContenido(String contenido) {
        Contenido = contenido;
    }

    public String Contenido;

    public String getFInicio() {
        return FInicio;
    }

    public void setFInicio(String FInicio) {
        this.FInicio = FInicio;
    }

    public String getFFin() {
        return FFin;
    }

    public void setFFin(String FFin) {
        this.FFin = FFin;
    }

    public String FInicio;
    public String FFin;

    public String getHInicio() {
        return HInicio;
    }

    public void setHInicio(String HInicio) {
        this.HInicio = HInicio;
    }

    public String getHFin() {
        return HFin;
    }

    public void setHFin(String HFin) {
        this.HFin = HFin;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

}

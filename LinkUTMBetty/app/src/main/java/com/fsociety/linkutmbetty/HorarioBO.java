package com.fsociety.linkutmbetty;

/**
 * Created by Emmanuel on 14/03/2017.
 */

public class HorarioBO {
    private String Dia;
    private String materia;
    private String hora;
    private String maestro;
    private String edificio;

    public HorarioBO (String materia, String hora, String maestro, String edificio) {
        this.edificio = edificio;
        this.materia = materia;
        this.hora = hora;
        this.maestro = maestro;
    }

    public String getDia() {
        return Dia;
    }

    public void setDia(String dia) {
        Dia = dia;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getHora() {
        return hora;
    }

    public String getMaestro() {
        return maestro;
    }

    public String getMateria() {
        return materia;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setMaestro(String maestro) {
        this.maestro = maestro;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }
}

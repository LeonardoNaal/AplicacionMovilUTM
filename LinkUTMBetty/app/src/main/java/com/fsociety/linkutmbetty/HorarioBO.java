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
    private String grado;
    private String grupo;
    private String carrera;
    private int id;

    public HorarioBO()
    {

    }

    public HorarioBO (String materia, String hora, String maestro, String edificio) {
        this.edificio = edificio;
        this.materia = materia;
        this.hora = hora;
        this.maestro = maestro;
    }

    public int getId(){return  id;}

    public void setId(int id){this.id = id;}

    public String getGrado(){return  grado;}

    public void setGrado(String Grado){this.grado = Grado;}

    public String getGrupo(){return  grupo;}

    public void setGrupo(String grupo){this.grupo = grupo;}

    public String getCarrera(){return  carrera;}

    public void setCarrera(String carrera){this.carrera = carrera;}

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

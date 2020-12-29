package gestion.informacion.appadivinalacancion.util.clasesBase;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.net.URL;
import java.util.Date;
import java.util.List;

import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Helper;
import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Struct;

public class Partida {
    private int id;
    private Jugador ganador;
    private Date fecha;
    private int rondas;
    private URL playlist;
    private List<Jugador> jugadores;
    private List<Cancion> canciones;

    //Este constructor es para crear
    public Partida(Date fecha, int rondas, URL playlist, BBDD_Helper helper) {
        this.fecha = fecha;
        this.rondas = rondas;
        this.playlist = playlist;
    }

    //Este constructor es para sacar los datos de la bd
    public Partida(int id, Jugador ganador, Date fecha, int rondas, URL playlist, List<Jugador> jugadores, List<Cancion> canciones) {
        this.id = id;
        this.ganador = ganador;
        this.fecha = fecha;
        this.rondas = rondas;
        this.playlist = playlist;
        this.jugadores = jugadores;
        this.canciones = canciones;
    }

    public int getId() {
        return id;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public void setGanador(Jugador ganador) {
        this.ganador = ganador;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getRondas() {
        return rondas;
    }

    public void setRondas(int rondas) {
        this.rondas = rondas;
    }

    public URL getPlaylist() {
        return playlist;
    }

    public void setPlaylist(URL playlist) {
        this.playlist = playlist;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public List<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }
}

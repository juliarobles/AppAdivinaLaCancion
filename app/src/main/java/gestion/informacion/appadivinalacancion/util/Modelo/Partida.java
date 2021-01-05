package gestion.informacion.appadivinalacancion.util.Modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Helper;
import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Struct;
import gestion.informacion.appadivinalacancion.util.Otros.AppException;
import gestion.informacion.appadivinalacancion.util.Otros.JugadorProvisional;
import gestion.informacion.appadivinalacancion.util.Otros.Tupla;

public class Partida {
    private int id;
    private Jugador ganador;
    private Date fecha;
    private int rondas;
    private Playlist playlist;
    private List<Jugador> jugadores;
    private List<Cancion> canciones;


//------------------------------------------------------------
    // Constructores
    //------------------------------------------------------------

    /**
     * Este constructor es para crear una nueva partida. La guarda en la bd.
     * @param fecha
     * @param rondas
     * @param playlist
     * @param helper
     * @throws Exception
     */
    public Partida(Date fecha, int rondas, Playlist playlist, BBDD_Helper helper) throws AppException {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BBDD_Struct.FECHA_PARTIDA, fecha.toString());
        values.put(BBDD_Struct.ID_PLAYLIST_PARTIDA, playlist.getId());
        values.put(BBDD_Struct.RONDAS_PARTIDA, rondas);

        long ex = db.insert(BBDD_Struct.TABLA_PARTIDA, null, values);
        if(ex == -1) {
            throw new AppException("Fallo al crear la partida");
        }
        this.id = Math.toIntExact(ex);
        this.fecha = fecha;
        this.rondas = rondas;
        this.playlist = playlist;
    }

    /**
     * Este constructor es para sacar una partida de la bd proporcionando un id
     * @param id
     * @param helper
     * @throws Exception
     */
    public Partida (int id, BBDD_Helper helper) throws AppException, MalformedURLException, ParseException {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] proy = {
                BBDD_Struct.ID_PARTIDA,
                BBDD_Struct.ID_PLAYLIST_PARTIDA,
                BBDD_Struct.FECHA_PARTIDA,
                BBDD_Struct.RONDAS_PARTIDA,
                BBDD_Struct.GANADOR_PARTIDA
        };
        String selection = BBDD_Struct.ID_PARTIDA + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor c = db.query(BBDD_Struct.TABLA_PARTIDA, proy, selection, selectionArgs, null, null, null);
        if(c.moveToFirst()){
            copiarPartida(new Partida(c, helper));
            this.jugadores = Jugador.getJugadoresPartida(this.id, helper);
            this.canciones = Cancion.getCancionesPartida(this.id, helper);
            c.close();
        } else {
            c.close();
            throw new AppException("Partida con id " + id + " no encontrada");
        }
    }

    private void copiarPartida(Partida p){
        this.id = p.id;
        this.fecha = p.fecha;
        this.ganador = p.ganador;
        this.playlist = p.playlist;
        this.rondas = p.rondas;
        this.canciones = p.canciones;
        this.jugadores = p.jugadores;
    }


    /**
     * Este constructor es solo para las consultas
     * @param c - Objeto cursor que devuelven las consultas
     * @throws MalformedURLException
     * @throws ParseException
     */
    private Partida(Cursor c, BBDD_Helper helper) throws AppException, MalformedURLException, ParseException {
        try{
            this.ganador = new Jugador(c.getInt(c.getColumnIndexOrThrow(BBDD_Struct.GANADOR_PARTIDA)), helper);
        } catch (Exception e){
            this.ganador = null;
        }
        this.id = c.getInt(c.getColumnIndexOrThrow(BBDD_Struct.ID_PARTIDA));
        this.fecha = BBDD_Struct.formatoFecha.parse(c.getString(c.getColumnIndex(BBDD_Struct.FECHA_PARTIDA)));
        this.rondas = c.getInt(c.getColumnIndexOrThrow(BBDD_Struct.RONDAS_PARTIDA));
        this.playlist = new Playlist(c.getInt(c.getColumnIndexOrThrow(BBDD_Struct.ID_PLAYLIST_PARTIDA)), helper);
        this.jugadores = null;
        this.canciones = null;
    }

    //------------------------------------------------------------
    // Consultas
    //------------------------------------------------------------

    /**
     * Esta consulta devuelve todas las partidas ordenadas por fecha de más reciente a más antiguas.
     * Son partidas simples porque no saca ni los jugadores ni las canciones.
     * @param helper Objeto BBDD_Helper
     * @return Lista de objetos Partida. Si no hay ninguna partida devolverá una lista vacia.
     * @throws MalformedURLException (Playlist)
     * @throws ParseException (Fecha)
     */
    public static List<Partida> todasPartidasSimples(BBDD_Helper helper) throws Exception {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + BBDD_Struct.TABLA_PARTIDA + " ORDER BY " + BBDD_Struct.FECHA_PARTIDA + " DESC", null);

        List<Partida> list = new ArrayList<>();
        if (c.moveToFirst()){
            while (!c.isAfterLast()){
                list.add(new Partida(c, helper));
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    //CONSULTA DE PLAYLIST MAS USADAS TOP 5
    public static List<Tupla> playlistMasUsadas(BBDD_Helper helper) throws MalformedURLException {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT pl." + BBDD_Struct.URL_PLAYLIST  + ", pl." + BBDD_Struct.NOMBRE_PLAYLIST + ", pl."
                + BBDD_Struct.PROPIETARIO_PLAYLIST+ ", pl." + BBDD_Struct.IMAGEN_PLAYLIST + ", COUNT(*)"
                + " FROM " + BBDD_Struct.TABLA_PARTIDA + " p, " + BBDD_Struct.TABLA_PLAYLIST + " pl"
                + " WHERE p." + BBDD_Struct.ID_PLAYLIST_PARTIDA + " = pl." + BBDD_Struct.ID_PLAYLIST
                + " GROUP BY "+ BBDD_Struct.ID_PLAYLIST_PARTIDA
                + " ORDER BY COUNT(*) DESC"
                + " LIMIT 5", null);

        List<Tupla> list = new ArrayList<>();
        if (c.moveToFirst()){
            while (!c.isAfterLast()){
                list.add(new Tupla(
                        c.getString(c.getColumnIndexOrThrow(BBDD_Struct.NOMBRE_PLAYLIST)),
                        c.getString(c.getColumnIndexOrThrow(BBDD_Struct.URL_PLAYLIST)),
                        c.getString(c.getColumnIndexOrThrow(BBDD_Struct.IMAGEN_PLAYLIST)),
                        c.getString(c.getColumnIndexOrThrow(BBDD_Struct.PROPIETARIO_PLAYLIST))
                ));
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    //CONSULTA DE PLAYLIST MAS RECIENTES TOP 5
    public static List<Tupla> playlistRecientes(BBDD_Helper helper) throws MalformedURLException {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT pl." + BBDD_Struct.URL_PLAYLIST  + ", pl." + BBDD_Struct.NOMBRE_PLAYLIST + ", pl."
                + BBDD_Struct.PROPIETARIO_PLAYLIST+ ", pl." + BBDD_Struct.IMAGEN_PLAYLIST
                + " FROM " + BBDD_Struct.TABLA_PARTIDA + " p, " + BBDD_Struct.TABLA_PLAYLIST + " pl"
                + " WHERE p." + BBDD_Struct.ID_PLAYLIST_PARTIDA + " = pl." + BBDD_Struct.ID_PLAYLIST
                + " ORDER BY "+ BBDD_Struct.FECHA_PARTIDA +" DESC"
                + " LIMIT 5", null);

        List<Tupla> list = new ArrayList<>();
        if (c.moveToFirst()){
            while (!c.isAfterLast()){
                list.add(new Tupla(
                        c.getString(c.getColumnIndexOrThrow(BBDD_Struct.NOMBRE_PLAYLIST)),
                        c.getString(c.getColumnIndexOrThrow(BBDD_Struct.URL_PLAYLIST)),
                        c.getString(c.getColumnIndexOrThrow(BBDD_Struct.IMAGEN_PLAYLIST)),
                        c.getString(c.getColumnIndexOrThrow(BBDD_Struct.PROPIETARIO_PLAYLIST))
                ));
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    //------------------------------------------------------------
    // Borrar
    //------------------------------------------------------------

    /**
     * Método para eliminar una partida de la base de datos.
     * Se eliminarán todos los jugadores asociados y la relación de la partida con las canciones.+
     * El objeto se quedará con los atributos a null o -1.
     * @param helper
     * @throws Exception
     */
    public void eliminarPartida(BBDD_Helper helper) throws Exception {
        SQLiteDatabase db = helper.getWritableDatabase();

        if(this.jugadores == null){
            this.jugadores = Jugador.getJugadoresPartida(this.id, helper);
        }

        for(Jugador j : this.jugadores){
            j.eliminarJugador(helper);
        }

        eliminarPartidaCancion(db);

        int e = db.delete(
                BBDD_Struct.TABLA_PARTIDA,
                BBDD_Struct.ID_PARTIDA + " = ?",
                new String[] {String.valueOf(this.id)}
        );

        if(e < 1){
            throw new Exception("Error al eliminar");
        }

        this.id = -1;
        this.canciones = null;
        this.jugadores = null;
        this.rondas = -1;
        this.playlist = null;
        this.ganador = null;
        this.fecha = null;
    }

    private void eliminarPartidaCancion(SQLiteDatabase db) throws Exception {
        int e = db.delete(
                BBDD_Struct.TABLA_CANCIONPARTIDA,
                BBDD_Struct.ID_PARTIDA_CANCIONPARTIDA + " = ?",
                new String[] {String.valueOf(this.id)}
        );
        this.canciones = null;
    }

    //------------------------------------------------------------
    // Getters y setters
    //------------------------------------------------------------

    public int getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public int getRondas() {
        return rondas;
    }

    public void setRondas(int rondas){
        this.rondas = rondas;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public void setGanador(Jugador ganador, BBDD_Helper helper) throws Exception {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BBDD_Struct.GANADOR_PARTIDA, ganador.getId());
        actualizar(db, values);

        this.ganador = ganador;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    /**
     * Este método permite establecer la lista de jugadores provisionales como jugadores definitivos de la partida
     * @param jugadores
     * @param helper
     * @throws Exception
     */
    public void setJugadores(List<JugadorProvisional> jugadores, BBDD_Helper helper) throws Exception {
        List<Jugador> list = new ArrayList<>();
        for (JugadorProvisional jugador : jugadores){
            list.add(new Jugador(jugador.getNombre(), this, String.valueOf(jugador.getColor()), helper));
        }
        this.jugadores = list;
    }

    public List<Cancion> getCanciones() {
        return canciones;
    }

    /* No hace falta porque al crear la cancion ya se asigna a la partida
    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }*/
    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }

    private void actualizar(SQLiteDatabase db, ContentValues values) throws Exception {
        int count = db.update(
                BBDD_Struct.TABLA_PARTIDA,
                values,
                BBDD_Struct.ID_PARTIDA + " = ?",
                new String[]{String.valueOf(this.id)});

        if (count == 0){
            throw new Exception("Error al actualizar");
        }
    }
}

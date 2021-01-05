package gestion.informacion.appadivinalacancion.util.Modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.net.MalformedURLException;
import java.net.URL;

import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Helper;
import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Struct;
import gestion.informacion.appadivinalacancion.util.Otros.AppException;

public class Playlist {
    private int id;
    private URL url;
    private URL imagen;
    private String propietario;
    private String nombre;

    /**
     * Este constructor es para crear una nueva playlist. La guarda en la bd.
     * @param url
     * @param imagen
     * @param propietario
     * @param nombre
     * @param helper
     */
    public Playlist(URL url, URL imagen, String propietario, String nombre, BBDD_Helper helper) throws AppException {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + BBDD_Struct.TABLA_PLAYLIST + " WHERE " + BBDD_Struct.URL_PLAYLIST + " = ?",
                new String[]{url.toString()});
        if(c.getCount() < 1){
            //Creo la playlist
            ContentValues values = new ContentValues();
            values.put(BBDD_Struct.NOMBRE_PLAYLIST, nombre);
            values.put(BBDD_Struct.URL_PLAYLIST, url.toString());
            values.put(BBDD_Struct.PROPIETARIO_PLAYLIST, propietario);
            values.put(BBDD_Struct.IMAGEN_PLAYLIST, imagen.toString());

            long ex = db.insert(BBDD_Struct.TABLA_PLAYLIST, null, values);
            if(ex == -1) {
                throw new AppException("Fallo al crear la playlist");
            }
            this.id = Math.toIntExact(ex);
        } else {
            //Posible actualizacion?
            if (c.moveToFirst()){
                this.id = Math.toIntExact(c.getInt(c.getColumnIndexOrThrow(BBDD_Struct.ID_PLAYLIST)));
            }
        }
        this.url = url;
        this.imagen = imagen;
        this.propietario = propietario;
        this.nombre = nombre;
    }

    /**
     * Este constructor es para sacar los datos de la bd
     * @param id
     * @param helper
     * @throws Exception
     */
    public Playlist(int id, BBDD_Helper helper) throws AppException, MalformedURLException {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] proy = {
                BBDD_Struct.ID_PLAYLIST,
                BBDD_Struct.NOMBRE_PLAYLIST,
                BBDD_Struct.URL_PLAYLIST,
                BBDD_Struct.IMAGEN_PLAYLIST,
                BBDD_Struct.PROPIETARIO_PLAYLIST
        };
        String selection = BBDD_Struct.ID_PLAYLIST + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor c = db.query(BBDD_Struct.TABLA_PLAYLIST, proy, selection, selectionArgs, null, null, null);
        if(c.moveToFirst()){
            copiarPlaylist(new Playlist(c, helper));
            c.close();
        } else {
            c.close();
            throw new AppException("Playlist con id " + this.id + " no encontrado");
        }
    }

    /**
     * Este constructor es solo para las consultas
     * @param c
     * @param helper
     * @throws MalformedURLException
     */
    public Playlist(Cursor c, BBDD_Helper helper) throws MalformedURLException {
        this.id = c.getInt(c.getColumnIndexOrThrow(BBDD_Struct.ID_PLAYLIST));
        this.propietario = c.getString(c.getColumnIndexOrThrow(BBDD_Struct.PROPIETARIO_PLAYLIST));
        this.url = new URL(c.getString(c.getColumnIndexOrThrow(BBDD_Struct.URL_PLAYLIST)));
        this.nombre = c.getString(c.getColumnIndexOrThrow(BBDD_Struct.NOMBRE_PLAYLIST));
        this.imagen = new URL(c.getString(c.getColumnIndexOrThrow(BBDD_Struct.IMAGEN_PLAYLIST)));
    }

    private void copiarPlaylist(Playlist p){
        this.id = p.id;
        this.url = p.url;
        this.imagen = p.imagen;
        this.propietario = p.propietario;
        this.nombre = p.nombre;
    }

    //------------------------------------------------------------
    // Getters
    //------------------------------------------------------------

    public int getId() {
        return id;
    }

    public URL getUrl() {
        return url;
    }

    public URL getImagen() {
        return imagen;
    }

    public String getPropietario() {
        return propietario;
    }

    public String getNombre() {
        return nombre;
    }
}

package gestion.informacion.appadivinalacancion.util.Modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Helper;
import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Struct;
import gestion.informacion.appadivinalacancion.util.Otros.AppException;

public class Cancion {

    private int id;
    private String nombre;
    private URL url;
    private String autor;
    private URL imagen;

    //------------------------------------------------------------
    // Constructores
    //------------------------------------------------------------

    /**
     * Este constructor es para crear una nueva cancion. La guarda en la bd.
     * @param nombre
     * @param url
     * @param autor
     * @param imagen
     * @param helper
     * @throws Exception
     */
    public Cancion(String nombre, URL url, String autor, URL imagen, BBDD_Helper helper) throws AppException {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + BBDD_Struct.TABLA_CANCION + " WHERE " + BBDD_Struct.URL_CANCION + " = ?",
                new String[]{url.toString()});
        if(c.getCount() < 1){
            //Creo la canción
            ContentValues values = new ContentValues();
            values.put(BBDD_Struct.NOMBRE_CANCION, nombre);
            values.put(BBDD_Struct.URL_CANCION, url.toString());
            values.put(BBDD_Struct.AUTOR_CANCION, autor);
            values.put(BBDD_Struct.IMAGEN_CANCION, imagen.toString());

            long ex = db.insert(BBDD_Struct.TABLA_CANCION, null, values);
            if(ex == -1) {
                throw new AppException("Fallo al crear la cancion");
            }
            this.id = Math.toIntExact(ex);
        } else {
            //Posible actualizacion?
            if (c.moveToFirst()){
                this.id = Math.toIntExact(c.getInt(c.getColumnIndexOrThrow(BBDD_Struct.ID_CANCION)));
            }
        }
        c.close();
        this.nombre = nombre;
        this.url = url;
        this.autor = autor;
        this.imagen = imagen;
    }

    //Este constructor es para sacar los datos de la bd
    //Por ahora no parece necesario así que sin hacer
    /*
    public Cancion(int id, BBDD_Helper helper) {
    }
    */

    /**
     * Este constructor es solo para las consultas
     * @param c
     * @throws MalformedURLException
     */
    private Cancion(Cursor c) throws MalformedURLException {
        this.id = c.getInt(c.getColumnIndexOrThrow(BBDD_Struct.ID_CANCION));
        this.autor = c.getString(c.getColumnIndexOrThrow(BBDD_Struct.AUTOR_CANCION));
        this.url = new URL(c.getString(c.getColumnIndexOrThrow(BBDD_Struct.URL_CANCION)));
        this.nombre = c.getString(c.getColumnIndexOrThrow(BBDD_Struct.NOMBRE_CANCION));
        this.imagen = new URL(c.getString(c.getColumnIndexOrThrow(BBDD_Struct.IMAGEN_CANCION)));
    }

    //------------------------------------------------------------
    // Consultas
    //------------------------------------------------------------

    /**
     * Devuelve todas las canciones que pertenecen a una partida con el idPartida proporcionado
     * @param idPartida
     * @param helper
     * @return
     * @throws MalformedURLException
     */
    public static List<Cancion> getCancionesPartida(int idPartida, BBDD_Helper helper) throws MalformedURLException {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT c FROM " + BBDD_Struct.TABLA_CANCION + " c, " + BBDD_Struct.TABLA_CANCIONPARTIDA 
                + " cp WHERE c." + BBDD_Struct.ID_CANCION + " = cp." + BBDD_Struct.ID_CANCION_CANCIONPARTIDA 
                + " AND cp." + BBDD_Struct.ID_PARTIDA_CANCIONPARTIDA + " = ?", new String[] {String.valueOf(idPartida)});

        List<Cancion> list = new ArrayList<>();
        if (c.moveToFirst()){
            while (!c.isAfterLast()){
                list.add(new Cancion(c));
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
     * Método para eliminar una canción de la base de datos.
     * Se eliminará la relación de la canción con cualquier partida.
     * El objeto se quedará con los atributos a null o -1.
     * @param helper
     * @throws Exception
     */
    /*Privado porque en principio no lo necesitamos*/
    private void eliminarCancion(BBDD_Helper helper) throws AppException {
        SQLiteDatabase db = helper.getWritableDatabase();

        eliminarPartidaCancion(db);
        int e = db.delete(
                BBDD_Struct.TABLA_CANCION,
                BBDD_Struct.ID_CANCION + " = ?",
                new String[] {String.valueOf(this.id)}
                );

        if(e < 1){
            throw new AppException("Error al eliminar");
        }

        this.id = -1;
        this.nombre = null;
        this.autor = null;
        this.url = null;
        this.imagen = null;
    }

    private void eliminarPartidaCancion(SQLiteDatabase db) throws AppException {
        int e = db.delete(
                BBDD_Struct.TABLA_CANCIONPARTIDA,
                BBDD_Struct.ID_CANCION_CANCIONPARTIDA + " = ?",
                new String[] {String.valueOf(this.id)}
        );
    }

    //------------------------------------------------------------
    // Getters
    //------------------------------------------------------------

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public URL getUrl() {
        return url;
    }

    public String getAutor() {
        return autor;
    }

    public URL getImagen() {
        return imagen;
    }
}

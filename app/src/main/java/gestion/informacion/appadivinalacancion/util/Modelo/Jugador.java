package gestion.informacion.appadivinalacancion.util.Modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Helper;
import gestion.informacion.appadivinalacancion.util.BBDD.BBDD_Struct;
import gestion.informacion.appadivinalacancion.util.Otros.AppException;


public class Jugador {

    private int id;
    private String nombre;
    private int puntos;
    private int acertadas;
    private Partida partida;
    private String color;

    //------------------------------------------------------------
    // Constructores
    //------------------------------------------------------------

    //Constructor unicamente para spinner
    public Jugador(String nombre, String color){
        this.nombre = nombre;
        this.color = color;
        this.puntos = -2;
    }

    /**
     * Este constructor es para crear un nuevo jugador. Lo guarda en la bd.
     * @param nombre
     * @param partida
     * @param color
     * @param helper
     * @throws Exception
     */
    public Jugador(String nombre, Partida partida, String color, BBDD_Helper helper) throws AppException {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BBDD_Struct.NOMBRE_JUGADOR, nombre);
        values.put(BBDD_Struct.PUNTOS_JUGADOR, 0);
        values.put(BBDD_Struct.ACERTADAS_JUGADOR, 0);
        values.put(BBDD_Struct.ID_PARTIDA_JUGADOR, partida.getId());
        values.put(BBDD_Struct.COLOR_JUGADOR, color);

        long ex = db.insert(BBDD_Struct.TABLA_JUGADOR, null, values);
        if(ex == -1){
            throw new AppException("Error creating player");
        }
        this.id = Math.toIntExact(ex);
        this.nombre = nombre;
        this.puntos = 0;
        this.acertadas = 0;
        this.partida = partida;
        this.color = color;
    }

    /**
     * Este constructor es para sacar un jugador de la bd proporcionando un id.
     * Con este método se devolvera un jugador con idPartida nulo.
     * @param id
     * @param helper
     * @throws Exception
     */
    public Jugador(int id, BBDD_Helper helper) throws AppException {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] proy = {
                BBDD_Struct.ID_JUGADOR,
                BBDD_Struct.NOMBRE_JUGADOR,
                BBDD_Struct.ACERTADAS_JUGADOR,
                BBDD_Struct.COLOR_JUGADOR,
                BBDD_Struct.PUNTOS_JUGADOR,
                BBDD_Struct.ID_PARTIDA_JUGADOR
        };
        String selection = BBDD_Struct.ID_JUGADOR + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor c = db.query(BBDD_Struct.TABLA_JUGADOR, proy, selection, selectionArgs, null, null, null);
        if(c.moveToFirst()){
            copiarJugador(new Jugador(c, helper));
            c.close();
        } else {
            c.close();
            throw new AppException("Jugador con id " + id + " no encontrado");
        }
    }

    private void copiarJugador(Jugador jugador) {
        this.id = jugador.id;
        this.partida = jugador.partida;
        this.acertadas = jugador.acertadas;
        this.color = jugador.color;
        this.nombre = jugador.nombre;
        this.puntos = jugador.puntos;
    }

    /**
     * Este constructor es solo para las consultas
     * @param c
     * @param helper
     */
    protected Jugador(Cursor c, BBDD_Helper helper) {
        this.id = c.getInt(c.getColumnIndexOrThrow(BBDD_Struct.ID_JUGADOR));
        this.nombre = c.getString(c.getColumnIndexOrThrow(BBDD_Struct.NOMBRE_JUGADOR));
        this.puntos = c.getInt(c.getColumnIndexOrThrow(BBDD_Struct.PUNTOS_JUGADOR));
        this.acertadas = c.getInt(c.getColumnIndexOrThrow(BBDD_Struct.ACERTADAS_JUGADOR));
        /*
        try {
            this.partida = new Partida(c.getInt(c.getColumnIndexOrThrow(BBDD_Struct.ID_PARTIDA_JUGADOR)), helper);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        this.partida = null; //Si no lo pongo null se queda en un bucle infinito
        this.color = c.getString(c.getColumnIndexOrThrow(BBDD_Struct.COLOR_JUGADOR));
    }


    //------------------------------------------------------------
    // Consultas
    //------------------------------------------------------------

    /**
     * Devuelve todas los jugadores que pertenecen a la partida con el idPartida proporcionado
     * Estos jugadores tendrán el idPartida nulo.
     * @param idPartida
     * @param helper
     * @return
     */
    public static List<Jugador> getJugadoresPartida(int idPartida, BBDD_Helper helper) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + BBDD_Struct.TABLA_JUGADOR + " WHERE "
                + BBDD_Struct.ID_PARTIDA_JUGADOR + " = ?", new String[] {String.valueOf(idPartida)});

        List<Jugador> list = new ArrayList<>();
        if (c.moveToFirst()){
            while (!c.isAfterLast()){
                list.add(new Jugador(c, helper));
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
     * Método para eliminar un jugador de la base de datos.
     * El objeto se quedará con los atributos a null o -1.
     * @param helper
     * @throws Exception
     */
    public void eliminarJugador(BBDD_Helper helper) throws AppException {
        SQLiteDatabase db = helper.getWritableDatabase();

        int e = db.delete(
                BBDD_Struct.TABLA_JUGADOR,
                BBDD_Struct.ID_JUGADOR + " = ?",
                new String[] {String.valueOf(this.id)}
        );

        if(e < 1){
            throw new AppException("Error al eliminar");
        }

        this.id = -1;
        this.puntos = -1;
        this.nombre = null;
        this.color = null;
        this.acertadas = -1;
        this.partida = null;
    }

    //------------------------------------------------------------
    // Getters y setters
    //------------------------------------------------------------

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getColor() {
        return color;
    }

    public Partida getPartida() {
        return partida;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos, BBDD_Helper helper) throws AppException {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BBDD_Struct.PUNTOS_JUGADOR, puntos);
        actualizar(db, values);

        this.puntos = puntos;
    }

    public int getAcertadas() {
        return acertadas;
    }

    public void setAcertadas(int acertadas, BBDD_Helper helper) throws AppException {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BBDD_Struct.ACERTADAS_JUGADOR, acertadas);
        actualizar(db, values);

        this.acertadas = acertadas;
    }

    private void actualizar(SQLiteDatabase db, ContentValues values) throws AppException {
        int count = db.update(
                BBDD_Struct.TABLA_JUGADOR,
                values,
                BBDD_Struct.ID_JUGADOR + " = ?",
                new String[]{String.valueOf(this.id)});

        if (count == 0){
            throw new AppException("Error al actualizar");
        }
    }
}

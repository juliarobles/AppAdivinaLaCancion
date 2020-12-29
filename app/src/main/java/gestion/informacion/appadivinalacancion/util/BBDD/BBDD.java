package gestion.informacion.appadivinalacancion.util.BBDD;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import gestion.informacion.appadivinalacancion.util.clasesBase.*;

public class BBDD {
    /*
    createPartida
    addJugadores
    addCanciones
     */

    public void createPartida(Partida partida, BBDD_Helper helper) throws Exception {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BBDD_Struct.FECHA_PARTIDA, partida.getFecha().toString());
        values.put(BBDD_Struct.PLAYLIST_PARTIDA, partida.getPlaylist().toString());
        values.put(BBDD_Struct.RONDAS_PARTIDA, partida.getRondas());

        long ex = db.insert(BBDD_Struct.TABLA_PARTIDA, null, values);
        if(ex == -1){
            throw new Exception("Fallo al crear la partida");
        }
    }

    public void addJugadores(Partida partida, BBDD_Helper helper) throws Exception {
        if(partida.getJugadores() == null || partida.getJugadores().size() == 0){
            throw new Exception("Partida sin jugadores");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        for(Jugador jugador : partida.getJugadores()){
            createJugador(jugador, db);
        }
    }

    private void createJugador(Jugador jugador, SQLiteDatabase db) throws Exception {
        ContentValues values = new ContentValues();
        values.put(BBDD_Struct.NOMBRE_JUGADOR, jugador.getNombre());
        values.put(BBDD_Struct.PUNTOS_JUGADOR, jugador.getPuntos());
        values.put(BBDD_Struct.ACERTADAS_JUGADOR, jugador.getAcertadas());
        values.put(BBDD_Struct.ID_PARTIDA_JUGADOR, jugador.getPartida().getId());
        values.put(BBDD_Struct.COLOR_JUGADOR, jugador.getColor());

        long ex = db.insert(BBDD_Struct.TABLA_JUGADOR, null, values);
        if(ex == -1){
            throw new Exception("Fallo al crear al jugador");
        }
    }

}

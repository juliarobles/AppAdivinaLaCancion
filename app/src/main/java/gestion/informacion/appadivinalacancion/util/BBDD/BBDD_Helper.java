package gestion.informacion.appadivinalacancion.util.BBDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BBDD_Helper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1 ;
    public static final String DATABASE_NAME = "DDBB.db";

    public BBDD_Helper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BBDD_Struct.SQL_CREATE_PARTIDA);
        db.execSQL(BBDD_Struct.SQL_CREATE_JUGADOR);
        db.execSQL(BBDD_Struct.SQL_CREATE_CANCION);
        db.execSQL(BBDD_Struct.SQL_CREATE_CANCIONPARTIDA);
        db.execSQL(BBDD_Struct.SQL_ADDGANADOR_PARTIDA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BBDD_Struct.SQL_CREATE_PLAYLIST);
        db.execSQL(BBDD_Struct.SQL_DELETE_PARTIDA);
        db.execSQL(BBDD_Struct.SQL_DELETE_JUGADOR);
        db.execSQL(BBDD_Struct.SQL_DELETE_CANCION);
        db.execSQL(BBDD_Struct.SQL_DELETE_CANCIONPARTIDA);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

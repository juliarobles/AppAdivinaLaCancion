package gestion.informacion.appadivinalacancion.util.BBDD;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class BBDD_Struct {

    public static final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");;

    //-------------------------------------------------------------------------
    // CREACION DE TABLAS
    //-------------------------------------------------------------------------

    //Crear tabla playlist
    public static final String TABLA_PLAYLIST = "PLAYLIST";
    public static final String ID_PLAYLIST = "ID";
    public static final String URL_PLAYLIST = "URL";
    public static final String NOMBRE_PLAYLIST = "NOMBRE";
    public static final String IMAGEN_PLAYLIST = "IMAGEN";
    public static final String PROPIETARIO_PLAYLIST = "PROPIETARIO";

    protected static final String SQL_CREATE_PLAYLIST = "CREATE TABLE " + TABLA_PLAYLIST + " ("
            + ID_PLAYLIST + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NOMBRE_PLAYLIST + " TEXT NOT NULL,"
            + URL_PLAYLIST + " TEXT NOT NULL UNIQUE,"
            + IMAGEN_PLAYLIST + " TEXT NOT NULL,"
            + PROPIETARIO_PLAYLIST + " TEXT NOT NULL)";

    //Crear tabla partida
    public static final String TABLA_PARTIDA = "PARTIDA";
    public static final String ID_PARTIDA = "ID";
    public static final String ID_PLAYLIST_PARTIDA = "PLAYLIST";
    public static final String GANADOR_PARTIDA = "GANADOR";
    public static final String FECHA_PARTIDA = "FECHA";
    public static final String RONDAS_PARTIDA = "RONDAS";

    protected static final String SQL_CREATE_PARTIDA = "CREATE TABLE " + TABLA_PARTIDA + " ("
            + ID_PARTIDA + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ID_PLAYLIST_PARTIDA + " INTEGER NOT NULL,"
            + FECHA_PARTIDA + " DATE,"
            + RONDAS_PARTIDA + " INTEGER NOT NULL,"
            + "FOREIGN KEY(" + ID_PLAYLIST_PARTIDA + ") REFERENCES " + TABLA_PLAYLIST + "(" + ID_PLAYLIST + ")"
            + ")";

    //Crear tabla jugador
    public static final String TABLA_JUGADOR = "JUGADOR";
    public static final String ID_JUGADOR = "ID";
    public static final String NOMBRE_JUGADOR = "NOMBRE";
    public static final String PUNTOS_JUGADOR = "PUNTOS";
    public static final String ACERTADAS_JUGADOR = "ACERTADAS";
    public static final String ID_PARTIDA_JUGADOR = "ID_PARTIDA";
    public static final String COLOR_JUGADOR = "COLOR";

    protected static final String SQL_CREATE_JUGADOR = "CREATE TABLE " + TABLA_JUGADOR + " ("
            + ID_JUGADOR + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NOMBRE_JUGADOR + " TEXT NOT NULL,"
            + PUNTOS_JUGADOR + " INTEGER NOT NULL,"
            + ACERTADAS_JUGADOR + " INTEGER NOT NULL,"
            + ID_PARTIDA_JUGADOR + " INTEGER NOT NULL,"
            + COLOR_JUGADOR + " TEXT NOT NULL,"
            + "FOREIGN KEY(" + ID_PARTIDA_JUGADOR + ") REFERENCES " + TABLA_PARTIDA + "(" + ID_PARTIDA + ")"
            + ")";

    //Crear tabla cancion
    public static final String TABLA_CANCION = "CANCION";
    public static final String ID_CANCION = "ID";
    public static final String NOMBRE_CANCION = "NOMBRE";
    public static final String URL_CANCION = "URL";
    public static final String AUTOR_CANCION = "AUTOR";
    public static final String IMAGEN_CANCION = "IMAGEN";

    protected static final String SQL_CREATE_CANCION = "CREATE TABLE " + TABLA_CANCION + " ("
            + ID_CANCION + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NOMBRE_CANCION + " TEXT NOT NULL,"
            + URL_CANCION + " TEXT NOT NULL UNIQUE,"
            + IMAGEN_CANCION + " TEXT NOT NULL,"
            + AUTOR_CANCION + " TEXT NOT NULL)";

    //Crear tabla cancionPartida
    public static final String TABLA_CANCIONPARTIDA = "CANCIONPARTIDA";
    public static final String ID_CANCION_CANCIONPARTIDA = "ID_CANCION";
    public static final String ID_PARTIDA_CANCIONPARTIDA = "ID_PARTIDA";

    protected static final String SQL_CREATE_CANCIONPARTIDA = "CREATE TABLE " + TABLA_CANCIONPARTIDA + " ("
            + ID_CANCION_CANCIONPARTIDA + " INTEGER,"
            + ID_PARTIDA_CANCIONPARTIDA + " INTEGER,"
            + "FOREIGN KEY(" + ID_PARTIDA_CANCIONPARTIDA + ") REFERENCES " + TABLA_PARTIDA + "(" + ID_PARTIDA + "),"
            + "FOREIGN KEY(" + ID_CANCION_CANCIONPARTIDA + ") REFERENCES " + TABLA_CANCION + "(" + ID_CANCION + "),"
            + "PRIMARY KEY(" + ID_CANCION_CANCIONPARTIDA + ", " + ID_PARTIDA_CANCIONPARTIDA + ")"
            + ")";

    //Añadir ganador a partida, que tiene constraint jugador (antes no se podia porque la tabla jugador no existia aún)
    protected static final String SQL_ADDGANADOR_PARTIDA = "ALTER TABLE " + TABLA_PARTIDA
            + " ADD COLUMN " + GANADOR_PARTIDA + " INTEGER REFERENCES " + TABLA_CANCION + "(" + ID_CANCION + ")";

    //-------------------------------------------------------------------------
    // BORRAR TABLAS
    //-------------------------------------------------------------------------

    protected static final String SQL_DELETE_PARTIDA =
            "DROP TABLE IF EXISTS " + TABLA_PARTIDA;

    protected static final String SQL_DELETE_JUGADOR =
            "DROP TABLE IF EXISTS " + TABLA_JUGADOR;

    protected static final String SQL_DELETE_CANCION =
            "DROP TABLE IF EXISTS " + TABLA_CANCION;

    protected static final String SQL_DELETE_CANCIONPARTIDA =
            "DROP TABLE IF EXISTS " + TABLA_CANCIONPARTIDA;

    protected static final String SQL_DELETE_PLAYLIST =
            "DROP TABLE IF EXISTS " + TABLA_PLAYLIST;
}

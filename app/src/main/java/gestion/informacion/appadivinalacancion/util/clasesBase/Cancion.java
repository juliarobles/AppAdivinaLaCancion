package gestion.informacion.appadivinalacancion.util.clasesBase;

import java.net.URL;

public class Cancion {

    private int id;
    private String nombre;
    private URL cancion;
    private String autor;

    //Este constructor es para crear
    public Cancion(String nombre, URL cancion, String autor) {
        this.nombre = nombre;
        this.cancion = cancion;
        this.autor = autor;
    }

    //Este constructor es para sacar los datos de la bd
    public Cancion(int id, String nombre, URL cancion, String autor) {
        this.id = id;
        this.nombre = nombre;
        this.cancion = cancion;
        this.autor = autor;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public URL getCancion() {
        return cancion;
    }

    public void setCancion(URL cancion) {
        this.cancion = cancion;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}

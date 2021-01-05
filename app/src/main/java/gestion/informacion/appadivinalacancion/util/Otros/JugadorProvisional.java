package gestion.informacion.appadivinalacancion.util.Otros;

import gestion.informacion.appadivinalacancion.util.Modelo.Partida;

public class JugadorProvisional {

    // Esta clase sirve unicamente para no ir modificando la base de datos mientras se van creando los jugadores, eliminando, etc.
    // Así cuando ya tengamos decididos los jugadores se crearan de verdad en la base de datos.

    private String nombre;
    private int color;

    public JugadorProvisional(String nombre, int color) {
        this.nombre = nombre;
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

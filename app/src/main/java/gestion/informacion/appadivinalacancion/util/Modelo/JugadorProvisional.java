package gestion.informacion.appadivinalacancion.util.Modelo;

public class JugadorProvisional {

    // Esta clase sirve unicamente para no ir modificando la base de datos mientras se van creando los jugadores, eliminando, etc.
    // Así cuando ya tengamos decididos los jugadores se crearan de verdad en la base de datos.

    private String nombre;
    private Partida partida;
    private String color;

    public JugadorProvisional(String nombre, Partida partida, String color) {
        this.nombre = nombre;
        this.partida = partida;
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

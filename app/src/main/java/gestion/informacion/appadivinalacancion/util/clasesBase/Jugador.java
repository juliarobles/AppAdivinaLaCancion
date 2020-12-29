package gestion.informacion.appadivinalacancion.util.clasesBase;

public class Jugador {

    private int id;
    private String nombre;
    private int puntos;
    private int acertadas;
    private Partida partida;
    private String color;

    //Este constructor es para crear
    public Jugador(String nombre, Partida partida, String color) {
        this.nombre = nombre;
        this.puntos = 0;
        this.acertadas = 0;
        this.partida = partida;
        this.color = color;
    }

    //Este constructor es para sacar los datos de la bd
    public Jugador(int id, String nombre, int puntos, int acertadas, Partida partida, String color) {
        this.id = id;
        this.nombre = nombre;
        this.puntos = puntos;
        this.acertadas = acertadas;
        this.partida = partida;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getAcertadas() {
        return acertadas;
    }

    public void setAcertadas(int acertadas) {
        this.acertadas = acertadas;
    }

    public Partida getPartida() {
        return partida;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

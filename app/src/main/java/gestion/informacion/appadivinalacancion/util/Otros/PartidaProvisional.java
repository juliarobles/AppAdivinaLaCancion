package gestion.informacion.appadivinalacancion.util.Otros;

import java.net.URL;
import java.util.Date;

public class PartidaProvisional {
    private Date fecha;
    private int rondas;
    private URL playlist;

    public PartidaProvisional(Date fecha, int rondas, URL playlist) {
        this.fecha = fecha;
        this.rondas = rondas;
        this.playlist = playlist;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getRondas() {
        return rondas;
    }

    public void setRondas(int rondas) {
        this.rondas = rondas;
    }

    public URL getPlaylist() {
        return playlist;
    }

    public void setPlaylist(URL playlist) {
        this.playlist = playlist;
    }
}

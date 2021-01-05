package gestion.informacion.appadivinalacancion.util.Otros;

import java.net.URL;

public class Tupla {
    private String x1; //titulo
    private String x2; //url
    private String imagen;
    private String prop;

    public Tupla(String x1, String x2, String url, String prop) {
        this.x1 = x1;
        this.x2 = x2;
        this.imagen = url;
        this.prop = prop;
    }

    public String getX1() {
        return x1;
    }

    public void setX1(String x1) {
        this.x1 = x1;
    }

    public String getX2() {
        return x2;
    }

    public void setX2(String x2) {
        this.x2 = x2;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @Override
    public String toString() {
        return x1;
    }
}

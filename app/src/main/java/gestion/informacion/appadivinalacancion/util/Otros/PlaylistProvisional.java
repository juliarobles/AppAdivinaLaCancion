package gestion.informacion.appadivinalacancion.util.Otros;

public class PlaylistProvisional {
    private String titulo; //titulo
    private String urlPl; //url
    private String imagen;
    private String prop;

    public PlaylistProvisional(String titulo, String urlPl, String url, String prop) {
        this.titulo = titulo;
        this.urlPl = urlPl;
        this.imagen = url;
        this.prop = prop;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrlPl() {
        return urlPl;
    }

    public void setUrlPl(String urlPl) {
        this.urlPl = urlPl;
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
        return titulo;
    }
}

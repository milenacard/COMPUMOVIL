package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Daniel on 06/05/2017.
 */

public class Libro implements Serializable{

    private String nombre;
    private String foto;
    private String autor;
    private float estado;
    private String genero;
    private String idioma;
    private String paginas;
    private String propietario;
    private String sinopsis;


    public Libro() {
    }

    public Libro(String nombre, String foto, String autor, float estado, String genero, String idioma, String paginas, String propietario, String sinopsis, ArrayList<String> interesados) {
        this.nombre = nombre;
        this.foto = foto;
        this.autor = autor;
        this.estado = estado;
        this.genero = genero;
        this.idioma = idioma;
        this.paginas = paginas;
        this.propietario = propietario;
        this.sinopsis = sinopsis;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public float getEstado() {
        return estado;
    }

    public void setEstado(float estado) {
        this.estado = estado;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getPaginas() {
        return paginas;
    }

    public void setPaginas(String paginas) {
        this.paginas = paginas;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

}

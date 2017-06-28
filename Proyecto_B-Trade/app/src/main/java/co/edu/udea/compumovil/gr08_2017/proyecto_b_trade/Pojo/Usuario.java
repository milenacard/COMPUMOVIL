package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo;

import java.io.Serializable;

/**
 * Created by Milena Cárdenas on 09-may-17.
 */

public class Usuario implements Serializable{

    private String nombre;
    private String foto;
    private String email;
    private String password;

    public Usuario() {

    }

    public Usuario(String nombre, String foto, String email, String password) {
        this.nombre = nombre;
        this.foto = foto;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

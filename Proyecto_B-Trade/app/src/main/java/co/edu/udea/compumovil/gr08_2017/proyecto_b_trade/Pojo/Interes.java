package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo;

import java.io.Serializable;

/**
 * Created by Daniel on 10/05/2017.
 */

public class Interes implements Serializable{

    private String email;
    private String libro;

    public Interes(String email, String libro) {
        this.email = email;
        this.libro = libro;
    }

    public Interes() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }
}

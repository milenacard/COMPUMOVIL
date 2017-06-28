package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo;

/**
 * Created by Daniel on 02/06/2017.
 */

public class Token {

    private String token;
    private String email;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Token(String token, String email) {

        this.token = token;
        this.email = email;
    }

    public Token() {

    }
}

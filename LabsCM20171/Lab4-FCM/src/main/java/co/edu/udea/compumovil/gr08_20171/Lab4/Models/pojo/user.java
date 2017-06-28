package co.edu.udea.compumovil.gr08_20171.Lab4.Models.pojo;

/**
 * Created by Milena Cardenas on 02-mar-17.
 */

public class user {
    private String username;
    private String password;
    private String nombre;
    private String foto;
    private String email;
    private String edad;


    public user() {
    }

    public user(String username, String password, String nombre, String edad, String foto, String email) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.edad = edad;
        this.foto = foto;
        this.email = email;
    }

    public String getPhoto() {
        return foto;
    }

    public void setPhoto(String photo) {
        this.foto = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return nombre;
    }

    public void setName(String name) {
        this.nombre = name;
    }

    public String getAge() {
        return edad;
    }

    public void setAge(String age) {
        this.edad = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}

package co.edu.udea.compumovil.gr08_20171.Lab2.Activity.Models.pojo;

/**
 * Created by Milena Cardenas on 02-mar-17.
 */

public class user {
    private String username;
    private String password;
    private String name;
    private String age;
    private int photo;
    private String email;

    public user() {
    }

    public user(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

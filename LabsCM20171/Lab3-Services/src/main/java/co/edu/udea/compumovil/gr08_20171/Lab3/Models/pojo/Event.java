package co.edu.udea.compumovil.gr08_20171.Lab3.Models.pojo;

import java.io.Serializable;

/**
 * Created by Milena Cárdenas on 02-mar-17.
 */

public class Event  implements Serializable {
    private int id;
    private String foto;
    private String nombre;
    private String descripcion;
    private float puntuacion;
    private String responsable;
    private String fecha;
    private double latitud;
    private double longitud;
    private String informacionGeneral;

    public Event() {
    }

    public Event(int id, String photo, String name, String description, float score, String responsible, String date, double latitude, double longitude, String generalInformation) {
        this.id = id;
        this.foto = photo;
        this.nombre = name;
        this.descripcion = description;
        this.puntuacion = score;
        this.responsable = responsible;
        this.fecha = date;
        this.latitud = latitude;
        this.longitud = longitude;
        this.informacionGeneral = generalInformation;
    }

    public void setPhoto(String photo) {
        this.foto = photo;
    }

    public String getPhoto() {
        return foto;
    }

    public float getScore() {
        return puntuacion;
    }

    public void setScore(float score) {
        this.puntuacion = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return nombre;
    }

    public void setName(String name) {
        this.nombre = name;
    }

    public String getDescription() {
        return descripcion;
    }

    public void setDescription(String description) {
        this.descripcion = description;
    }


    public String getResponsible() {
        return responsable;
    }

    public void setResponsible(String responsible) {
        this.responsable = responsible;
    }

    public String getDate() {
        return fecha;
    }

    public void setDate(String date) {
        this.fecha = date;
    }

    public double getLatitude() {
        return latitud;
    }

    public void setLatitude(double latitude) {
        this.latitud = latitude;
    }

    public double getLongitude() {
        return longitud;
    }

    public void setLongitude(double longitude) {
        this.longitud = longitude;
    }

    public String getGeneralInformation() {
        return informacionGeneral;
    }

    public void setGeneralInformation(String generalInformation) {
        this.informacionGeneral = generalInformation;
    }
}

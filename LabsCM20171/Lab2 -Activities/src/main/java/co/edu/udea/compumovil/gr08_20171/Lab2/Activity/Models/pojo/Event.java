package co.edu.udea.compumovil.gr08_20171.Lab2.Activity.Models.pojo;

import java.io.Serializable;

/**
 * Created by Milena Cárdenas on 02-mar-17.
 */

public class Event  implements Serializable {
    private int id;
    private int photo;
    private String name;
    private String description;
    private float score;
    private String responsible;
    private String date;
    private double latitude;
    private double longitude;
    private String generalInformation;

    public Event() {
    }

    public Event(int id, int photo, String name, String description, float score, String responsible, String date, double latitude, double longitude, String generalInformation) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.description = description;
        this.score = score;
        this.responsible = responsible;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.generalInformation = generalInformation;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public int getPhoto() {
        return photo;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getGeneralInformation() {
        return generalInformation;
    }

    public void setGeneralInformation(String generalInformation) {
        this.generalInformation = generalInformation;
    }
}

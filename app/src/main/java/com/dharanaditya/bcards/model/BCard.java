package com.dharanaditya.bcards.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BCard {

    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("headline")
    @Expose
    private String headline;
    @SerializedName("pictureUrl")
    @Expose
    private String pictureUrl;
    @SerializedName("publicProfileUrl")
    @Expose
    private String publicProfileUrl;
    @SerializedName("id")
    @Expose
    private String id;

    public BCard() {
    }

    public BCard(String firstName, String lastName, String emailAddress, String headline, String pictureUrl, String publicProfileUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.headline = headline;
        this.pictureUrl = pictureUrl;
        this.publicProfileUrl = publicProfileUrl;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPublicProfileUrl() {
        return publicProfileUrl;
    }

    public void setPublicProfileUrl(String publicProfileUrl) {
        this.publicProfileUrl = publicProfileUrl;
    }

    @Override
    public String toString() {
        return "BCard{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", headline='" + headline + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", publicProfileUrl='" + publicProfileUrl + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
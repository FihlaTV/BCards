package com.dharanaditya.bcards;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dharan1011 on 26/8/17.
 */

public class BCard implements Parcelable {

    public final static Parcelable.Creator<BCard> CREATOR = new Creator<BCard>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BCard createFromParcel(Parcel in) {
            BCard instance = new BCard();
            instance.firstName = ((String) in.readValue((String.class.getClassLoader())));
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.lastName = ((String) in.readValue((String.class.getClassLoader())));
            instance.pictureUrl = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public BCard[] newArray(int size) {
            return (new BCard[size]);
        }

    };
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("pictureUrl")
    @Expose
    private String pictureUrl;

    /**
     * No args constructor for use in serialization
     */
    public BCard() {
    }

    /**
     * @param id
     * @param lastName
     * @param pictureUrl
     * @param firstName
     */
    public BCard(String firstName, String id, String lastName, String pictureUrl) {
        super();
        this.firstName = firstName;
        this.id = id;
        this.lastName = lastName;
        this.pictureUrl = pictureUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(firstName);
        dest.writeValue(id);
        dest.writeValue(lastName);
        dest.writeValue(pictureUrl);
    }

    public int describeContents() {
        return 0;
    }

}
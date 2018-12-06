package io.github.lily_wittle.phlogging;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.location.Location;

@Entity (tableName = "PhlogEntry")
public class DataRoomEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "time")
    private long time;
    @ColumnInfo(name = "text")
    private String text;
    @ColumnInfo(name = "photo")
    private String photo;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "orientation")
    private float orientation;

    public DataRoomEntity() {}

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public String getPhoto() {
        return photo;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getOrientation() {
        return orientation;
    }

    public void setId(int newId) {
        id = newId;
    }

    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public void setTime(long newTime) {
        time = newTime;
    }

    public void setText(String newText) {
        text = newText;
    }

    public void setPhoto(String newPhoto) {
        photo = newPhoto;
    }

    public void setLatitude(double newLatitude) {
        latitude = newLatitude;
    }

    public void setLongitude(double newLongitude) {
        longitude = newLongitude;
    }

    public void setOrientation(float newOrientation) {
        orientation = newOrientation;
    }

}

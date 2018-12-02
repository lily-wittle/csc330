package io.github.lily_wittle.phlogging;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
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
    @ColumnInfo(name = "imageId")
    private long imageId;
    @ColumnInfo(name = "location")
    private Location location;
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

    public long getImageId() {
        return imageId;
    }

    public Location getLocation() {
        return location;
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

    public void setImageId(long newImageId) {
        imageId = newImageId;
    }

    public void setLocation(Location newLocation) {
        location = newLocation;
    }

    public void setOrientation(float newOrientation) {
        orientation = newOrientation;
    }

}

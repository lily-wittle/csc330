package io.github.lily_wittle.talkingpicturelist;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "ImageAndDescription")
public class DataRoomEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "imageId")
    private long imageId;
    @ColumnInfo(name = "description")
    private String description;

    public DataRoomEntity() {
    }

    public int getId() {
        return(id);
    }

    public long getImageId() {
        return(imageId);
    }

    public String getDescription() {
        return(description);
    }

    public void setId(int newId) {
        id = newId;
    }

    public void setImageId(long newImageId) {
        imageId = newImageId;
    }

    public void setDescription(String newDescription) {
        description = newDescription;
    }

}

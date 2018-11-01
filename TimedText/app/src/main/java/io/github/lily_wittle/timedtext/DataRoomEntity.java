package io.github.lily_wittle.timedtext;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "SavedText")
public class DataRoomEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "text")
    private String text;
    @ColumnInfo(name = "time")
    private long seconds;

    public DataRoomEntity() {
    }

    public int getId() {
        return(id);
    }

    public String getText() {
        return(text);
    }

    public long getTime() {
        return(seconds);
    }

    public void setId(int newId) {
        id = newId;
    }

    public void setText(String newText) {
        text = newText;
    }

    public void setTime(long newSeconds) {
        seconds = newSeconds;
    }

}

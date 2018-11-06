package io.github.lily_wittle.timedtext;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "SavedText")
public class DataRoomEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "note")
    private String note;
    @ColumnInfo(name = "time")
    private long time;

    public DataRoomEntity() {
    }

    public int getId() {
        return(id);
    }

    public String getNote() {
        return(note);
    }

    public long getTime() {
        return(time);
    }

    public void setId(int newId) {
        id = newId;
    }

    public void setNote(String newNote) {
        note = newNote;
    }

    public void setTime(long newTime) {
        time = newTime;
    }

}

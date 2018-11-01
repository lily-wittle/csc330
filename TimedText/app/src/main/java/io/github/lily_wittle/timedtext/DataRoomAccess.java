package io.github.lily_wittle.timedtext;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface DataRoomAccess {

    @Query("SELECT * FROM SavedText ORDER BY time ASC")
    List<DataRoomEntity> fetchAllNotes();

    @Insert
    void addNote(DataRoomEntity newNote);

}

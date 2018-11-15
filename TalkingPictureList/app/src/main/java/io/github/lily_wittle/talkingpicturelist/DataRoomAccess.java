package io.github.lily_wittle.talkingpicturelist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DataRoomAccess {

    @Query("SELECT * FROM ImageAndDescription")
    List<DataRoomEntity> fetchAll();

    @Insert
    void addEntry(DataRoomEntity newEntry);

    @Update
    void updateDescription(DataRoomEntity newEntry);

}

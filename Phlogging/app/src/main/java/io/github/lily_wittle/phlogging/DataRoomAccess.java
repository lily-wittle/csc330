package io.github.lily_wittle.phlogging;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DataRoomAccess {
    @Query("SELECT * FROM PhlogEntry ORDER BY time DESC")
    List<DataRoomEntity> fetchAll();

    @Insert
    void addPhlog(DataRoomEntity newPhlog);

    @Delete
    void deletePhlog(DataRoomEntity phlogToDelete);
}

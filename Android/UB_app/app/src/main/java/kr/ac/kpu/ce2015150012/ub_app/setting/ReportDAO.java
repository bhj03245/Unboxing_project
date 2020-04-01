package kr.ac.kpu.ce2015150012.ub_app.setting;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

import java.util.List;


@Dao
public interface ReportDAO {
   // @Query("SELECT * FROM report")
   // LiveData<List<ReportVO>> getAll();

    @Query("SELECT phone FROM report WHERE id=1")
    public List<ReportVO> loadPhone();

    @Query("SELECT contents FROM report WHERE id=1")
    public List<ReportVO> loadContents();

    @Insert
    void insert(ReportVO reportVO);

    @Update
    void update(ReportVO reportVO);

    @Delete
    void delete(ReportVO reportVO);

    @Query("DELETE FROM report")
    void deleteAll();

}

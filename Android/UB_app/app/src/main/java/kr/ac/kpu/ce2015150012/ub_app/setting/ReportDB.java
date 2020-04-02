package kr.ac.kpu.ce2015150012.ub_app.setting;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ReportVO.class}, version = 1)
public abstract class ReportDB extends RoomDatabase {
    private static ReportDB INSTANCE;
    public abstract ReportDAO reportDAO();


    public static ReportDB getAppDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, ReportDB.class, "report").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}


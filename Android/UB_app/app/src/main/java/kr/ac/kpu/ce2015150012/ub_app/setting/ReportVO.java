package kr.ac.kpu.ce2015150012.ub_app.setting;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "report")
public class ReportVO {
    @PrimaryKey(autoGenerate = true)
    protected int id;
    @ColumnInfo(name="phone")
    protected String phone;
    @ColumnInfo(name="contents")
    protected String contents;

    public ReportVO(String phone, String contents){
        this.phone = phone; this.contents = contents;
    }

    public int getId(){
        return id;
    }
    public void setId(){
        this.id = id;
    }
    public String getPhone(){
        return phone;
    }
    public void setPhone(){
        this.phone = phone;
    }

    public String getContents(){
        return contents;
    }
    public void setContents(){
        this.contents = contents;
    }
}

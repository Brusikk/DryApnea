package cz.apneaman.dryapnea.db.tables;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import cz.apneaman.dryapnea.db.AppDatabase;

@Table(database = AppDatabase.class)
public class Training extends BaseModel{

    @PrimaryKey(autoincrement = true)
    private int id;

    @Column
    private String name;

    /*uložení miliseconds*/
    @Column
    private Long timestamp;

    @Column
    private String type;

    /*Prázdnej konstruktor kvůli DB flow knihovně*/
    public Training() {
    }

    public Training(String name, String type) {
        this.name = name;
        this.timestamp = System.currentTimeMillis();
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

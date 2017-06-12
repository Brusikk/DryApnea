package cz.apneaman.dryapnea.db.tables;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import cz.apneaman.dryapnea.db.AppDatabase;

@Deprecated //Bude společné nastavení pro všechny tréningy
@Table(database = AppDatabase.class)
public class Settings extends BaseModel{

    @PrimaryKey
    private int id; //shoduje se s TrainingID !!!!!!

    @Column
    private int volume;

    @Column
    private boolean vibrate;

    @Column
    private int toStart; //inSecs

    @Column
    private int afterStart; //inSecs

    public Settings() {
    }

    public Settings(int id) {
        this.id = id;
        this.volume = 5; //out of 10
        this.vibrate = true;
        this.toStart = 60; //seconds
        this.afterStart = 10; //seconds
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public int getToStart() {
        return toStart;
    }

    public void setToStart(int toStart) {
        this.toStart = toStart;
    }

    public int getAfterStart() {
        return afterStart;
    }

    public void setAfterStart(int afterStart) {
        this.afterStart = afterStart;
    }
}

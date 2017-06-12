package cz.apneaman.dryapnea.db.tables;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import cz.apneaman.dryapnea.db.AppDatabase;

@Table(database = AppDatabase.class)
public class Counter extends BaseModel{

    @PrimaryKey(autoincrement = true)
    private int id;

    @Column
    private int cycleId; //foreign key

    @Column
    private Long holdTime; //

    @Column
    private Long timeStamp;

    public Counter() {
    }

    public Counter(Cycle cycle) {
        this.cycleId = cycle.getId();
        this.holdTime = cycle.getHoldTime();
        this.timeStamp = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCycleId() {
        return cycleId;
    }

    public void setCycleId(int cycleId) {
        this.cycleId = cycleId;
    }

    public Long getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(Long holdTime) {
        this.holdTime = holdTime;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

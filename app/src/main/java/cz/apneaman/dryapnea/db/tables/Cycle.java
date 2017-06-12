package cz.apneaman.dryapnea.db.tables;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import cz.apneaman.dryapnea.db.AppDatabase;

@Table(database = AppDatabase.class)
public class Cycle extends BaseModel{

    @PrimaryKey(autoincrement = true)
    private int id;

    @Column
    private int trainingId; //foreign key

    @Column
    private Long breathTime;

    @Column
    private Long holdTime;

    @Column
    private Long timestamp;

    public Cycle() {
    }

    public Cycle(Long breathTime, Long holdTime, Training training) {
        this.trainingId = training.getId();
        this.breathTime = breathTime;
        this.holdTime = holdTime;
        this.timestamp = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    public Long getBreathTime() {
        return breathTime;
    }

    public void setBreathTime(Long breathTime) {
        this.breathTime = breathTime;
    }

    public Long getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(Long holdTime) {
        this.holdTime = holdTime;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

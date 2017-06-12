package cz.apneaman.dryapnea.db.dao;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import cz.apneaman.dryapnea.db.tables.Cycle;
import cz.apneaman.dryapnea.db.tables.Cycle_Table;
import cz.apneaman.dryapnea.db.tables.Training;

public class CycleDao {

    public static void createOrUpdate(Cycle cycle){
        cycle.save();
    }

    public static void deleteCycle(Cycle cycle){
        cycle.delete();
    }

    public static List<Cycle> selectCyclesByTraining(Training training){
        return SQLite.select().from(Cycle.class).where(Cycle_Table.trainingId.eq(training.getId())).queryList();
    }

    public static void deleteCycles(Training training) {
        SQLite.delete().from(Cycle.class).where(Cycle_Table.trainingId.eq(training.getId())).execute();
    }
}

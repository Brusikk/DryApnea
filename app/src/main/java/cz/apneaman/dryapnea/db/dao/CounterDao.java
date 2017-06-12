package cz.apneaman.dryapnea.db.dao;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import cz.apneaman.dryapnea.db.tables.Counter;
import cz.apneaman.dryapnea.db.tables.Counter_Table;
import cz.apneaman.dryapnea.db.tables.Cycle;
import cz.apneaman.dryapnea.db.tables.Cycle_Table;
import cz.apneaman.dryapnea.db.tables.Training;
import cz.apneaman.dryapnea.db.tables.Training_Table;

public class CounterDao {

    public static void createCounter(Counter counter) {
        counter.save();
    }

    public static List<Counter> selectCountersByTraining(Training training) {
        return  SQLite.select().from(Counter.class)
                .innerJoin(Cycle.class).on(Cycle_Table.id.withTable().eq(Counter_Table.cycleId.withTable()))
                .where(Cycle_Table.trainingId.eq(training.getId())).queryList();
    }
}

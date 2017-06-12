package cz.apneaman.dryapnea.db.dao;

import android.database.Cursor;

import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import cz.apneaman.dryapnea.db.tables.Settings;
import cz.apneaman.dryapnea.db.tables.Training;
import cz.apneaman.dryapnea.db.tables.Training_Table;

public class TrainingDao {

    /* ORM - dbFlow */
  /* Volání metod bez instance - práce s db */

    /* Seznam tréninků vytažený z db */
    public static List<Training> selectAllTrainings(){
        return SQLite.select().from(Training.class).queryList();
    }

    /* Uložení nového tréninku do db */
    public static void create(Training training){
        training.save();
        SettingsDao.createOrUpdate(new Settings(training.getId()));
    }

    /* Uložení tréninku do db */
    public static void update(Training training){
        training.save();
    }

    /* Smazání tréninku z db */
    public static void delete(Training training) {
        training.delete();
        CycleDao.deleteCycles(training);
        SettingsDao.deleteSettings(training);
    }

    /* Výběr konkrétního tréninku z db */
    public static Training selectTrainingById(int trainingId) {
        return SQLite.select().from(Training.class).where(Training_Table.id.eq(trainingId)).querySingle();
    }

    /* Výběr jména tréninku */
    public static String getTrainingName(int trainingId) {
        return selectTrainingById(trainingId).getName();
    }
}

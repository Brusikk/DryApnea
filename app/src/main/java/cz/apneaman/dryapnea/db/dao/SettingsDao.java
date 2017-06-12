package cz.apneaman.dryapnea.db.dao;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import cz.apneaman.dryapnea.db.tables.Settings;
import cz.apneaman.dryapnea.db.tables.Settings_Table;
import cz.apneaman.dryapnea.db.tables.Training;

public class SettingsDao {

    public static Settings selectSettingByTrainingId(int trainingId) {
        return SQLite.select().from(Settings.class).where(Settings_Table.id.eq(trainingId)).querySingle();
    }

    public static void createOrUpdate(Settings settings) {
        settings.save();
    }

    public static void deleteSettings(Training training) {
        SQLite.delete().from(Settings.class).where(Settings_Table.id.eq(training.getId())).execute();
    }
}

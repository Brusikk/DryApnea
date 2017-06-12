package cz.apneaman.dryapnea.db;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "dryapnea";
    public static final int VERSION = 4;

}

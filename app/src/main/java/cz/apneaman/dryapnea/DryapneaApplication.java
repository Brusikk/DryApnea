package cz.apneaman.dryapnea;

import android.app.Application;
import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/* Spouštěcí třída*/
public class DryapneaApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        FlowManager.init(new FlowConfig.Builder(this).openDatabasesOnInit(true).build());
    }

    public static Context getContext(){
        return context;
    }
}

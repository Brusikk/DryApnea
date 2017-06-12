package cz.apneaman.dryapnea.preferences;

import android.content.Context;

import cz.apneaman.dryapnea.DryapneaApplication;

/* Globální předvolby nastavení tréninků*/
public class PrefManager {

    private static final String PREFS = "preferences";
    
    private static final String IS_VIBRATION = "IS_VIBRATION";
    private static final String VOLUME = "VOLUME";

    public static final String TO_START_2_MINUTES = "TO_START_2_MINUTES";
    public static final String TO_START_1_MINUTE = "TO_START_1_MINUTE";
    public static final String TO_START_30_SECSONDS = "TO_START_30_SECSONDS";
    public static final String TO_START_10_SECSONDS = "TO_START_10_SECSONDS";
    public static final String TO_START_5_SECSONDS = "TO_START_5_SECSONDS";
    public static final String TO_START_0_SECSONDS = "TO_START_0_SECSONDS";

    public static final String AFTER_START_2_MINUTES = "AFTER_START_2_MINUTES";
    public static final String AFTER_START_1_MINUTE = "AFTER_START_1_MINUTE";
    public static final String AFTER_START_30_SECSONDS = "AFTER_START_30_SECSONDS";
    public static final String AFTER_START_10_SECSONDS = "AFTER_START_10_SECSONDS";
    public static final String AFTER_START_5_SECSONDS = "AFTER_START_5_SECSONDS";
    public static final String AFTER_START_0_SECSONDS = "AFTER_START_0_SECSONDS";

    /* Defaultní nastavení*/
    public static int getVolume() {
        return DryapneaApplication.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(VOLUME, 5);
    }

    public static void setVolume(int volume) {
        DryapneaApplication.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putInt(VOLUME, volume).apply();
    }

    public static boolean isVibration() {
        return DryapneaApplication.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(IS_VIBRATION, false);
    }

    public static void setVibration(boolean isVibration) {
        DryapneaApplication.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putBoolean(IS_VIBRATION, isVibration).apply();
    }

    public static boolean isToStart(String TO_START) {
        return DryapneaApplication.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(TO_START, false);
    }

    public static void setToStart(String TO_START, boolean bool) {
        DryapneaApplication.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putBoolean(TO_START, bool).apply();
    }

    public static boolean isAfterStart(String AFTER_START) {
        return DryapneaApplication.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(AFTER_START, false);
    }

    public static void setAfterStart(String AFTER_START, boolean bool) {
        DryapneaApplication.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putBoolean(AFTER_START, bool).apply();
    }
}

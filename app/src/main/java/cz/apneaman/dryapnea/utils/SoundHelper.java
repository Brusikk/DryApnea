package cz.apneaman.dryapnea.utils;

import android.content.Context;
import android.media.MediaPlayer;

import cz.apneaman.dryapnea.R;
import cz.apneaman.dryapnea.activities.TrainingActivity;
import cz.apneaman.dryapnea.db.tables.Training;
import cz.apneaman.dryapnea.preferences.PrefManager;

public class SoundHelper {

    public static String AFTER_START = "AFTER_START";
    public static String TO_START = "TO_START";

    /* Pípání podle nastavení */
    public static void shouldBeep(Context context, long millisUntilFinished, String type, TrainingActivity trainingActivity, boolean iAmOk) {
        int seconds = (int) millisUntilFinished / 1000;
        /*Pokud neni zaškrtlý, tak is AfterStart frátí false*/
        if (type.equals(AFTER_START)) {
            if (seconds == 120 && PrefManager.isAfterStart(PrefManager.AFTER_START_2_MINUTES)) {
                beep(context, seconds);
            }
            if (seconds == 60 && PrefManager.isAfterStart(PrefManager.AFTER_START_1_MINUTE)) {
                beep(context, seconds);
            }
            if (seconds == 30 && PrefManager.isAfterStart(PrefManager.AFTER_START_30_SECSONDS)) {
                beep(context, seconds);
            }
            /* Dej mi signál */
            if (seconds == 20) {
                beep(context, seconds);
                trainingActivity.setupIamOkayMessage();
            }
            /* Jsem při vědomí */
            if (iAmOk) {
                beep(context, -1);
                trainingActivity.setIAmOk(false);
            }

            if (seconds == 10 && PrefManager.isAfterStart(PrefManager.AFTER_START_10_SECSONDS)) {
                beep(context, seconds);
            }
            if (seconds == 5 && PrefManager.isAfterStart(PrefManager.AFTER_START_5_SECSONDS)) {
                beep(context, seconds);
            }
            /* Dýchej */
            if (seconds == 0 && PrefManager.isAfterStart(PrefManager.AFTER_START_0_SECSONDS)) {
                beep(context, -2);
            }
        } else { //TO_START
            if (seconds == 120 && PrefManager.isToStart(PrefManager.TO_START_2_MINUTES)) {
                beep(context, seconds);
            }
            if (seconds == 60 && PrefManager.isToStart(PrefManager.TO_START_1_MINUTE)) {
                beep(context, seconds);
            }
            if (seconds == 30 && PrefManager.isToStart(PrefManager.TO_START_30_SECSONDS)) {
                beep(context, seconds);
            }
            if (seconds == 10 && PrefManager.isToStart(PrefManager.TO_START_10_SECSONDS)) {
                beep(context, seconds);
            }
            if (seconds == 5 && PrefManager.isToStart(PrefManager.TO_START_5_SECSONDS)) {
                beep(context, seconds);
            }
            if (seconds == 0 && PrefManager.isToStart(PrefManager.TO_START_0_SECSONDS)) {
                beep(context, seconds);
            }
        }
    }

    /* Zvuky */
    private static void beep(Context context, int seconds) {
        final MediaPlayer mp;
        if (seconds == -1) {
            mp = MediaPlayer.create(context, R.raw.i_see_it);
        } else if (seconds == -2) {
            mp = MediaPlayer.create(context, R.raw.breathe);
        } else if (seconds == 0) {
            mp = MediaPlayer.create(context, R.raw.start);
        } else if (seconds == 5) {
            mp = MediaPlayer.create(context, R.raw.five_seconds);
        } else if (seconds == 10) {
            mp = MediaPlayer.create(context, R.raw.ten_seconds);
        } else if (seconds == 20) {
            mp = MediaPlayer.create(context, R.raw.give_me_signal);
        } else if (seconds == 30) {
            mp = MediaPlayer.create(context, R.raw.thirty_seconds);
        } else if (seconds == 60) {
            mp = MediaPlayer.create(context, R.raw.one_minute);
        } else {
            mp = MediaPlayer.create(context, R.raw.two_minutes);
        }


        if (!mp.isPlaying()) {
            mp.setVolume(PrefManager.getVolume(), PrefManager.getVolume());
            mp.start();
        }
    }

    /* Blackout alarm */
    public static void alarmBeep(Context context) {
        final MediaPlayer mp = MediaPlayer.create(context, R.raw.notice);
        if (!mp.isPlaying()) {
            mp.setVolume(PrefManager.getVolume(), PrefManager.getVolume());
            mp.start();
        }
    }

    /* Konečné zvuky */
    public static void endBeep(Context context, Boolean rip) {
        final MediaPlayer mp;

        if (rip) {
            mp = MediaPlayer.create(context, R.raw.rip);
        } else {
            mp = MediaPlayer.create(context, R.raw.training_complete);
        }

        if (!mp.isPlaying()) {
            mp.setVolume(PrefManager.getVolume(), PrefManager.getVolume());
            mp.start();
        }
    }


}

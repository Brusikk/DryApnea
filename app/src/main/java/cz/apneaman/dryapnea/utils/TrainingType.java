package cz.apneaman.dryapnea.utils;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TrainingType {

    public final String trainingType;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({O2,CO2,ONE_BREATH})
    public @interface TrainingTypeDef {}

    public static final String O2 = "O2";
    public static final String CO2 = "CO2";
    public static final String ONE_BREATH = "ONE_BREATH";

    public TrainingType(@TrainingTypeDef String trainingType) {
        this.trainingType = trainingType;
    }

    @TrainingTypeDef
    public String getTrainingType() {
        return trainingType;
    }
}

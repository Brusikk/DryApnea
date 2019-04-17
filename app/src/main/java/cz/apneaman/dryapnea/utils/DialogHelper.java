package cz.apneaman.dryapnea.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import cz.apneaman.dryapnea.activities.ApneaWalkingActivity;
import cz.apneaman.dryapnea.activities.DetailActivity;
import cz.apneaman.dryapnea.activities.StaticApneaActivity;
import cz.apneaman.dryapnea.db.dao.CycleDao;
import cz.apneaman.dryapnea.db.dao.TrainingDao;
import cz.apneaman.dryapnea.db.tables.Cycle;
import cz.apneaman.dryapnea.db.tables.Training;

public class DialogHelper {

    /* Dialog - vytvoření tréninku */
    public static void createTrainingDialog(final Activity activity) {
        String trainingType = Constants.STATIC_APNEA;
        if (activity instanceof ApneaWalkingActivity) {
            trainingType = Constants.APNEA_WALKING;
        }

        /* Dialog s tlačítkama */
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Zadejte název tréninku");

        /* Input s názvem*/
        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        builder.setView(input);

        /* Ukládací button */
        String finalTrainingType = trainingType;
        builder.setPositiveButton("Uložit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!input.getText().toString().isEmpty()) {
                    TrainingDao.create(new Training(input.getText().toString(), finalTrainingType));
                    refreshList(activity);
                    dialog.cancel();
                }
            }
        });

        /* Cancel button */
        builder.setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /* Dialog - editace tréninku */
    public static void editTrainingDialog(final Activity activity, final Training training) {
        if (activity instanceof StaticApneaActivity) {
            training.setType(Constants.STATIC_APNEA);
        } else {
            training.setType(Constants.APNEA_WALKING);
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Zadejte název tréninku");

        /* Input s názvem */
        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        input.setText(training.getName());
        builder.setView(input);

        /* Ukládací button */
        builder.setPositiveButton("Přejmenovat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        /* Delete button*/
        builder.setNeutralButton("Smazat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        /* Cancel button */
        builder.setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        /* Zobrazí dialog s údajema z db*/
        final AlertDialog dialog = builder.create();
        dialog.show();

        /* Změna názvu tréninku*/
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!input.getText().toString().isEmpty()) {
                    training.setName(input.getText().toString());
                    TrainingDao.update(training);
                    refreshList(activity);
                    dialog.dismiss();
                }
            }
        });

        /* Smazání tréninku */
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainingDao.delete(training);
                refreshList(activity);
                dialog.dismiss();
            }
        });

        /* Zavření dialogu*/
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private static void refreshList(Activity activity) {
        if (activity instanceof StaticApneaActivity) {
            ((StaticApneaActivity) activity).refreshList();
        } else {
            ((ApneaWalkingActivity) activity).refreshList();
        }
    }


    /* Dialog - přidání série*/
    public static void createCycleDialog(final DetailActivity activity, final Training training) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Zadejte hodnoty série");

        final EditText inputBreath = new EditText(activity);
        final EditText inputActionValue = new EditText(activity);
        inputBreath.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputBreath.setHint("Dýchání (sec)");
        inputActionValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (training.getType().equals(Constants.STATIC_APNEA)) {
            inputActionValue.setHint("Zádrž dechu (sec)");
        } else {
            inputActionValue.setHint("Počet kroků");
        }

        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(linearParams);
        linearLayout.addView(inputBreath);
        linearLayout.addView(inputActionValue);

        builder.setView(linearLayout);

        /* Ukládací button*/
        builder.setPositiveButton("Uložit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        /* Cancel button*/
        builder.setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog dialog = builder.create();

        dialog.show();

        /* Uložení série */
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputBreath.getText().toString().trim().isEmpty() && !inputActionValue.getText().toString().trim().isEmpty()) {
                    Cycle cycle = new Cycle(Long.valueOf(inputBreath.getText().toString().trim()), Long.valueOf(inputActionValue.getText().toString().trim()), training);
                    CycleDao.createOrUpdate(cycle);
                    activity.refreshList();
                    dialog.cancel();
                }
            }
        });

        /* Zavření dialogu */
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /* Dialog - editace série*/
    public static void editCycleDialog(final DetailActivity detailActivity, final Cycle cycle, Training training) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(detailActivity);
        builder.setTitle("Upravte hodnoty série");

        final EditText inputBreath = new EditText(detailActivity);
        final EditText inputActionValue = new EditText(detailActivity);
        inputBreath.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputBreath.setHint("Dýchání (původně " + cycle.getBreathTime() + " sec)");
        inputActionValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (training.getType().equals(Constants.STATIC_APNEA)) {
            inputActionValue.setHint("Zádrž dechu (původně " + cycle.getHoldTime() + " sec)");
        } else {
            inputActionValue.setHint("Počet kroků (původně " + cycle.getHoldTime()+")");
        }

        LinearLayout linearLayout = new LinearLayout(detailActivity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(linearParams);
        linearLayout.addView(inputBreath);
        linearLayout.addView(inputActionValue);

        builder.setView(linearLayout);

        builder.setPositiveButton("Upravit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setNeutralButton("Smazat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog dialog = builder.create();

        dialog.show();

        /* Uložení série*/
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!inputBreath.getText().toString().trim().isEmpty() && !inputActionValue.getText().toString().trim().isEmpty()) {
                        cycle.setBreathTime(Long.valueOf(inputBreath.getText().toString().trim()));
                        cycle.setHoldTime(Long.valueOf(inputActionValue.getText().toString().trim()));
                        CycleDao.createOrUpdate(cycle);
                        detailActivity.refreshList();
                        dialog.cancel();
                    }
                } catch (NumberFormatException e) {

                }
            }
        });

        /* Cancel button */
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        /* Delete button */
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CycleDao.deleteCycle(cycle);
                detailActivity.refreshList();
                dialog.dismiss();
            }
        });
    }
}

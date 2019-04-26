package cz.apneaman.dryapnea.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

public class HelpDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Postup měření tepu")
                .setMessage("1. Umísti prst na foťák" +"\n"+ "2. Stiskni: START MĚŘENÍ" +"\n"+ "3. Nehýbej s prstem" +"\n"+ "4. Zobrazí se srdeční tep")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}

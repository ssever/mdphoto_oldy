package labs.com.mdfoto.ui.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import labs.com.mdfoto.R;

/**
 * Created by oktay on 28.07.2016.
 */
public class LogoutDialog {
    private static AlertDialog dialog;

    public static void showDialog(Context context, final Runnable ok) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.dialog_title));
        builder.setMessage(context.getString(R.string.dialog_message));

        String positiveText = context.getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic

                        ok.run();
                        dialog.dismiss();
                    }
                });

        String negativeText = context.getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic

                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        // display dialog
        dialog.show();
    }
}

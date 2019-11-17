package labs.com.mdfoto.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import labs.com.mdfoto.MainActivity;
import labs.com.mdfoto.R;
import labs.com.mdfoto.models.UserManager;
import labs.com.mdfoto.ui.activities.AddDoctorActivity;
import labs.com.mdfoto.ui.activities.SettingsActivity;


public class RegisterDialog extends Dialog{

    public RegisterDialog(final Context context , final String ansver) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_dialog);

        TextView dialog_text = (TextView)findViewById(R.id.text_dialog);
        ImageView image =(ImageView)findViewById(R.id.a);
        Button okey =(Button)findViewById(R.id.btn_dialog);

        switch (ansver) {
            case "successful":
                dialog_text.setText("Record is successful");
                image.setImageResource(R.drawable.dialogitem2);
                break;
            case "failed":
                dialog_text.setText("Record is failed");
                image.setImageResource(R.drawable.dialogitem);
                break;

            case "empty":
                dialog_text.setText("Please fill in the blanks");
                image.setImageResource(R.drawable.dialogitem);
                break;
        }

        okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(ansver.equals("successful")){
                Intent redirect = new Intent(context, SettingsActivity.class);
                context.startActivity(redirect);
                }
            }
        });

    }

}

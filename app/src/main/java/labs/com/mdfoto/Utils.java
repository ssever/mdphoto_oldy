package labs.com.mdfoto;

import android.graphics.Color;
import android.widget.ImageButton;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by oktay on 11.08.2016.
 */
public class Utils {

    public static void toWhite(ImageButton button){

        int color = Color.parseColor("#FFFFFF"); //The color u want
        button.setColorFilter(color);
    }

    public static void mCreateAndSaveFile(String mJsonResponse,String path) {
        try {

            FileWriter file = new FileWriter(path,false);
            file.write(mJsonResponse);
            file.flush();
            file.close();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}

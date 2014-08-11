package upv.welcomeincoming.app.infoFragments;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import upv.welcomeincoming.app.R;
import util.TouchImageView;

public class Activity_FullScreen_Image extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_activity_full_screen_image);
        TouchImageView imgDisplay;
        imgDisplay = (TouchImageView) findViewById(R.id.imgDisplay);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.planometro, options);
        imgDisplay.setImageBitmap(bitmap);
    }

}
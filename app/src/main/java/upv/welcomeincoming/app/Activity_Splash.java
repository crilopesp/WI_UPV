package upv.welcomeincoming.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

public class Activity_Splash extends Activity {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 1500;

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        setContentView(R.layout.splash_screen);
        final ImageView imageView = (ImageView) this.findViewById(R.id.imageSplash);

        final int[] imageArray = {R.drawable.screen_splash1, R.drawable.screen_splash, R.drawable.screen_splash3, R.drawable.screen_splash4, R.drawable.screen_splash5};

        int i = 0;
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                imageView.setImageResource(imageArray[i]);
                i++;
                if (i > imageArray.length - 1) {
                    Intent mainIntent = new Intent().setClass(
                            Activity_Splash.this, Activity_Home.class);
                    startActivity(mainIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
                if (i < imageArray.length)
                    handler.postDelayed(this, 300);  //for interval...
            }
        };
        handler.postDelayed(runnable, 50); //for initial delay..
    }
}

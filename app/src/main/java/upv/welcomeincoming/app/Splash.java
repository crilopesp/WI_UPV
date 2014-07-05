package upv.welcomeincoming.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends Activity {

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

        final int []imageArray={R.drawable.splash_screen1,R.drawable.splash_screen,R.drawable.splash_screen3,R.drawable.splash_screen4,R.drawable.splash_screen5};

        int i = 0;
        Runnable runnable = new Runnable() {
            int i=0;
            public void run() {
                imageView.setImageResource(imageArray[i]);
                i++;
                if(i>imageArray.length-1)
                {
                    Intent mainIntent = new Intent().setClass(
                            Splash.this, Home.class);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    startActivity(mainIntent);
                    try {
                        this.finalize();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                if(i<imageArray.length)
                handler.postDelayed(this, 300);  //for interval...
            }
        };
        handler.postDelayed(runnable, 50); //for initial delay..
    }
}

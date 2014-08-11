package upv.welcomeincoming.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import util.LoadingTask;

public class Activity_Splash extends Activity implements LoadingTask.LoadingTaskFinishedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show the splash screen
        setContentView(R.layout.splash_screen);
        // Find the progress bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        new LoadingTask(progressBar, this, getResources(), getApplicationContext()).execute("NORMAL"); // Pass in whatever you need a url is just an example we don't use it in this tutorial
    }

    // This is the callback for when your async task has finished
    @Override
    public void onTaskFinished() {
        completeSplash();
    }


    private void completeSplash() {
        startApp();
        finish(); // Don't forget to finish this Splash Activity so the user can't return to it!
    }

    private void startApp() {
        Intent intent = new Intent(this, Activity_Home.class);
        startActivity(intent);
    }
}

package upv.welcomeincoming.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import upv.welcomeincoming.app.GCM.MessageSender;
import util.DBHandler_Horarios;
import util.InternetConnectionChecker;
import util.Preferencias;
import util.ProgressDialog_Custom;

public class Activity_Home extends ActionBarActivity implements Fragment_Calendar.CalendarListener, OnShowcaseEventListener {

    public static Handler mUiHandler = null;
    private String[] opcionesMenu;
    private DrawerLayout drawerLayout;
    private LinearLayout drawerList;
    private LinearLayout panelDrawer;
    private int fragmentActual;
    private String tituloApp;
    private String asignatura;
    ActionBarDrawerToggle drawerToggle;
    FragmentTransaction tx;
    DBHandler_Horarios helper;
    ProgressDialog_Custom progress;
    SQLiteDatabase db;
    ShowcaseView sv;
    InternetConnectionChecker icc;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String SENDER_ID = "1006259852133";
    static final String TAG = "welcomeincomingapp";
    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();

    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress = new ProgressDialog_Custom(this, getString(R.string.loading));
        helper = new DBHandler_Horarios(this);
        icc = new InternetConnectionChecker();
        db = helper.getWritableDatabase();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        opcionesMenu = new String[]{getString(R.string.menu_option1), getString(R.string.menu_option2), getString(R.string.menu_option3), getString(R.string.menu_option4), getString(R.string.menu_option5), getString(R.string.menu_option6), getString(R.string.menu_option7)};

        setContentView(R.layout.activity_home);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (LinearLayout) findViewById(R.id.home_drawer);
        panelDrawer = (LinearLayout) findViewById(R.id.panel_drawer);
        getWindow().setUiOptions(ActivityInfo.UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW);
        tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.contenedor_fragment, new Fragment_Home(db));
        tx.commit();
        fragmentActual = 0;
        // Check device for Play Services APK. If check succeeds, proceed with
        //  upv.welcomeincoming.app.GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(getApplicationContext());

            if (regid.isEmpty()) {
                Log.e("error", "Sin reg ID");
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
        inicializarElementos();

        ActionViewTarget target = new ActionViewTarget(this, ActionViewTarget.Type.HOME);

        sv = new ShowcaseView.Builder(this)
                .setTarget(target)
                .setContentTitle(R.string.tutorial_drawer_title)
                .setContentText(R.string.tutorial_drawer_message)
                .setStyle(R.style.ShowCaseTheme)
                .hideOnTouchOutside()
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mostrarFragment(1);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    private void inicializarElementos() {
        tituloApp = getTitle().toString();
        /*  ITEMS DRAWER */

        //Home
        View itemHome = generateItem(getString(R.string.menu_option1), R.drawable.icon_inicio);
        itemHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFragment(0);
            }
        });
        drawerList.addView(itemHome);
        //addDividier();

        //Calendar
        View itemFind = generateItem(getString(R.string.menu_option2), R.drawable.ic_action_collections_go_to_today);
        itemFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (icc.checkInternetConnection(getApplicationContext())) {
                    drawerLayout.closeDrawer(panelDrawer);
                    getActionBar().setTitle(opcionesMenu[1]);
                    if (Preferencias.getDNI(getApplicationContext()).equals("") || Preferencias.getPIN(getApplicationContext()).equals("")) {
                        Intent login = new Intent(getApplicationContext(), Activity_login.class);
                        startActivityForResult(login, 1);
                    } else {
                        mostrarFragment(1);
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Activity_no_connection.class);
                    intent.putExtra("case", getString(R.string.noInetCalendar));
                    startActivity(intent);
                    mostrarFragment(1);
                }
            }
        });
        drawerList.addView(itemFind);
        //addDividier();

        //Info
        View itemInfo = generateItem(getString(R.string.menu_option3), R.drawable.ic_action_about);
        itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFragment(2);
            }
        });
        drawerList.addView(itemInfo);
        //addDividier();

        //Traduccion
        View itemTraduce = generateItem(getString(R.string.menu_option4), R.drawable.ic_action_device_access_mic);
        itemTraduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (icc.checkInternetConnection(getApplicationContext())) {
                    mostrarFragment(3);
                } else {
                    Intent intent = new Intent(getApplicationContext(), Activity_no_connection.class);
                    intent.putExtra("case", getString(R.string.noInetBlock));
                    startActivity(intent);
                }
            }
        });
        drawerList.addView(itemTraduce);
        //addDividier();

        //Locate
        View itemLocate = generateItem(getString(R.string.menu_option5), R.drawable.ic_action_location_map);
        itemLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (icc.checkInternetConnection(getApplicationContext())) {
                    mostrarFragment(4);
                } else {
                    Intent intent = new Intent(getApplicationContext(), Activity_no_connection.class);
                    intent.putExtra("case", getString(R.string.noInetMaps));
                    startActivity(intent);
                    mostrarFragment(4);
                }
            }
        });
        drawerList.addView(itemLocate);
        //addDividier();
        //Forum
        View itemForum = generateItem(getString(R.string.menu_option7), R.drawable.ic_action_social_group);
        itemForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (icc.checkInternetConnection(getApplicationContext())) {
                    mostrarFragment(6);
                } else {
                    Intent intent = new Intent(getApplicationContext(), Activity_no_connection.class);
                    intent.putExtra("case", getString(R.string.noInetBlock));
                    startActivity(intent);
                }
            }
        });
        drawerList.addView(itemForum);
        //addDividier();

        //Option
        View itemOption = generateItem(getString(R.string.menu_option6), R.drawable.ic_action_action_settings);
        itemOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFragment(5);
            }
        });
        drawerList.addView(itemOption);
        //addDividier();



        /*   DrawerToggle  */

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {
                //getSupportActionBar().setTitle(tituloSeccion);
                // ActivityCompat.invalidateOptionsMenu(MainActivity.this);
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(tituloApp);
                sv.hide();
                // ActivityCompat.invalidateOptionsMenu(MainActivity.this); //invoca a onPrepareOptionsMenu()
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true); //Permite mostrar el icono del drawer
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

    }


    private View generateItem(String texto, int idIcon) {
        View item = this.getLayoutInflater().inflate(R.layout.item_drawer, null);
        TextView tv = (TextView) item.findViewById(R.id.tv_item_listdrawer);
        ImageView iv = (ImageView) item.findViewById(R.id.iv_item_listdrawer);
        tv.setText(texto);
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/futura_font.ttf"));
        iv.setBackgroundResource(idIcon);
        return item;
    }

    private void addDividier() {
        View v = this.getLayoutInflater().inflate(R.layout.dividier_itemdrawer, null);
        drawerList.addView(v);
    }

    private void mostrarFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Fragment_Home(db);
                break;
            case 1:
                fragment = new Fragment_Calendar(db);
                break;
            case 2:
                fragment = new Fragment_Info();
                break;
            case 3:
                fragment = new Fragment_Traduccion();
                break;
            case 4:
                fragment = new Fragment_Localizacion(db);
                break;
            case 5:
                fragment = new Fragment_Opciones();
                break;
            case 6:
                sendRegistrationIdToBackend();
                break;
        }


        if (fragment != null) {

            FragmentManager fgm = getSupportFragmentManager();
            fgm.beginTransaction().replace(R.id.contenedor_fragment, fragment).addToBackStack("tag").commit();
            getActionBar().setTitle(opcionesMenu[position]);
            drawerLayout.closeDrawer(panelDrawer);
            fragmentActual = position;
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            //
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (fragmentActual == 0) super.onBackPressed();
        else mostrarFragment(0);

    }

    @Override
    public void CalendarListenerError(String string) {

        Log.w(((Object) this).getClass().getName(), "Llamada a -> CalendarListenerError -> " + string);

        DialogFragment dialogFragment = this.getDialog(string);

        getSupportFragmentManager().popBackStack();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.contenedor_fragment, dialogFragment)
                .commit();
    }

    private DialogFragment getDialog(String string) {

        final String message = string;

        return new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Atención");
                builder.setMessage(message);

                builder.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                return builder.create();
            }
        };
    }

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {

    }

    /**
     * Gets the current registration ID for application on upv.welcomeincoming.app.GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private String getRegistrationId(Context context) {
        String registrationId = Preferencias.getGCMID(this);
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        /*int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }*/
        return registrationId;
    }

    /**
     * Registers the application with upv.welcomeincoming.app.GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    Log.e("regID", regid);
                    // You should send the registration ID to your server over HTTP,
                    // so it can use upv.welcomeincoming.app.GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(getApplicationContext(), regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.

                    Log.e("error", msg);
                }
                return msg;
            }
        }.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use upv.welcomeincoming.app.GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        if (!regid.isEmpty()) {
            MessageSender messageSender = new MessageSender();
            Bundle dataBundle = new Bundle();
            dataBundle.putString("action", "ECHO");
            dataBundle.putString("Mensaje", Preferencias.getUsername(this));
            messageSender.sendMessage(dataBundle, gcm);
            Log.e("gcm", "Succeded!");
        } else {
            Log.e("gcm", "no registration id");
        }
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        Log.i(TAG, "Saving regId on app version ");
        Preferencias.setGCMID(context, regId);
    }

    // You need to do the Play Services APK check here too.
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
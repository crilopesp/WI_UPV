package upv.welcomeincoming.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
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

import calendarupv.Calendario;
import util.DBHandler_Horarios;
import util.Preferencias;

public class Activity_Home extends ActionBarActivity implements Fragment_Diary.DiaryListener, Dialog_Login.EditDialogLoginListener, Fragment_Calendar.CalendarListener {

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
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DBHandler_Horarios(this);
        db = helper.getWritableDatabase();
        setContentView(R.layout.activity_home);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        opcionesMenu = new String[]{getString(R.string.menu_option1), getString(R.string.menu_option2), getString(R.string.menu_option3), getString(R.string.menu_option4), getString(R.string.menu_option5), getString(R.string.menu_option6), getString(R.string.menu_option7)};
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (LinearLayout) findViewById(R.id.home_drawer);
        panelDrawer = (LinearLayout) findViewById(R.id.panel_drawer);

        tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.contenedor_fragment, new Fragment_Home(db));
        tx.commit();
        fragmentActual = 0;
        inicializarElementos();
        //Aqui borrar preferencias para probar el log in

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
        addDividier();

        //Find
        View itemFind = generateItem(getString(R.string.menu_option2), R.drawable.ic_action_collections_go_to_today);
        itemFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(panelDrawer);
                getSupportActionBar().setTitle(opcionesMenu[1]);
                if (Preferencias.getDNI(getApplicationContext()).equals("") || Preferencias.getPIN(getApplicationContext()).equals("")) {

                    Dialog_Login fragmentCalendarLogin = new Dialog_Login();
                    FragmentManager fm = getSupportFragmentManager();
                    fragmentCalendarLogin.show(fm, "Login");

                }
                mostrarFragment(1);
            }
        });
        drawerList.addView(itemFind);
        addDividier();

        //Info
        View itemInfo = generateItem(getString(R.string.menu_option3), R.drawable.ic_action_about);
        itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFragment(2);
            }
        });
        drawerList.addView(itemInfo);
        addDividier();

        //Traduccion
        View itemTraduce = generateItem(getString(R.string.menu_option4), R.drawable.ic_action_device_access_mic);
        itemTraduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFragment(3);
            }
        });
        drawerList.addView(itemTraduce);
        addDividier();

        //Locate
        View itemLocate = generateItem(getString(R.string.menu_option5), R.drawable.ic_action_location_map);
        itemLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFragment(4);
            }
        });
        drawerList.addView(itemLocate);
        addDividier();
        //Forum
        View itemForum = generateItem(getString(R.string.menu_option7), R.drawable.ic_action_social_group);
        itemForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFragment(6);
            }
        });
        drawerList.addView(itemForum);
        addDividier();

        //Option
        View itemOption = generateItem(getString(R.string.menu_option6), R.drawable.ic_action_action_settings);
        itemOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFragment(5);
            }
        });
        drawerList.addView(itemOption);
        addDividier();



        /*   DrawerToggle  */

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {
                //getSupportActionBar().setTitle(tituloSeccion);
                // ActivityCompat.invalidateOptionsMenu(MainActivity.this);
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(tituloApp);
                // ActivityCompat.invalidateOptionsMenu(MainActivity.this); //invoca a onPrepareOptionsMenu()
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true); //Permite mostrar el icono del drawer
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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
                fragment = new Fragment_Diary(db);
                break;
            case 2:
                fragment = new Fragment_Info();
                break;
            case 3:
                fragment = new Fragment_Traduccion();
                break;
            case 4:
                Intent i = new Intent(this, Activity_Localizacion.class);
                startActivity(i);
                break;
            case 5:
                fragment = new Fragment_Opciones();
                break;
        }


        if (fragment != null) {

            FragmentManager fgm = getSupportFragmentManager();
            fgm.beginTransaction().replace(R.id.contenedor_fragment, fragment).commit();
            //setItemChecked
            getSupportActionBar().setTitle(opcionesMenu[position]);
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
    public void DiaryListenerOnClick(Calendario calendario) {

        //usamos el fragment simple
        boolean existe = calendario.existeEnDB(db);
        Log.e("db existe", existe + "");
        Fragment_Calendar fragmentCalendar = new Fragment_Calendar(calendario, existe, db);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedor_fragment, fragmentCalendar);
        transaction.addToBackStack(null);
        transaction.commit();
        drawerLayout.closeDrawer(panelDrawer);
        //fragmentCalendar.updateView(diaryJSON);
    }

    @Override
    public void DiaryListenerError(String string) {

        Log.w(((Object) this).getClass().getName(), "Llamada a -> DiaryListenerError -> " + string);

        DialogFragment dialogFragment = this.getDialog(string);

        getSupportFragmentManager().popBackStack();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.contenedor_fragment, dialogFragment)
                .commit();
        drawerLayout.closeDrawer(panelDrawer);

    }

    @Override
    public void EditDialogLoginListener(int button) {
        if (DialogInterface.BUTTON_POSITIVE == button) {

            Log.d(((Object) this).getClass().getName(), "Aceptar pulsado...");

            Fragment_Diary fragmentCalendarDiaryList = new Fragment_Diary(db);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contenedor_fragment, fragmentCalendarDiaryList)
                    .commit();

        } else if (DialogInterface.BUTTON_NEGATIVE == button) {

            Log.d(((Object) this).getClass().getName(), "Cancelar pulsado...");
            this.finish();

        } else {

            Log.w(((Object) this).getClass().getName(), "Algo esta jodido...");
        }
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
}
package upv.welcomeincoming.app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import util.Parser_XML_edificios;
import util.Parser_XML_emt;
import util.Parser_XML_interes;
import util.Parser_XML_metro;
import util.Parser_XML_valenbisi;
import util.ProgressDialog_Custom;

public class Fragment_RealidadAumentada extends Fragment {
    private SQLiteDatabase db;
    private ProgressDialog_Custom pg;

    public Fragment_RealidadAumentada(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_localizacion, container, false);
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinnerEdificios);
        Button btn_enc = (Button) view.findViewById(R.id.btn_enc);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.edificios, R.layout.item_lista_spinner_filtros);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(adapterView.getContext(), "Has seleccionado el edificio " + adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Acvitity_VistaRealidadAumentada.class);
                String edificio = spinner.getSelectedItem().toString().split(" ")[1];
                intent.putExtra("poi", edificio);
                startActivity(intent);
            }
        });


        Button btn_upvmap = (Button) view.findViewById(R.id.btn_upvmap);
        btn_upvmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg = new ProgressDialog_Custom(getActivity(), getString(R.string.loading));
                pg.show();
                if (edificiosestaVacia()) {
                    try {
                        new Parser_XML_edificios().parsearEdificios(getResources().openRawResource(R.raw.edificios), db);
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(getActivity(), Activity_Localizacion_UPV.class);
                startActivity(intent);
                pg.dismiss();
            }
        });
        Button btn_valenbisi = (Button) view.findViewById(R.id.btn_valenbisi);
        btn_valenbisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg = new ProgressDialog_Custom(getActivity(), getString(R.string.loading));
                pg.show();
                if (valenbisiestaVacia()) {
                    try {
                        new Parser_XML_valenbisi().parsear(getResources().openRawResource(R.raw.valenbisi), db);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(getActivity(), Activity_Localizacion_Valenbisi.class);
                startActivity(intent);
                pg.dismiss();
            }
        });
        Button btn_metro = (Button) view.findViewById(R.id.btn_metro);
        btn_metro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg = new ProgressDialog_Custom(getActivity(), getString(R.string.loading));
                pg.show();
                if (metroestaVacia()) {
                    try {
                        new Parser_XML_metro().parsear(getResources().openRawResource(R.raw.metro), db);
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(getActivity(), Acvitity_VistaRealidadAumentada.class);
                startActivity(intent);
                pg.dismiss();
            }
        });
        Button btn_emt = (Button) view.findViewById(R.id.btn_emt);
        btn_emt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg = new ProgressDialog_Custom(getActivity(), getString(R.string.loading));
                pg.show();
                if (emtestaVacia()) {
                    try {
                        new Parser_XML_emt().parsear(getResources().openRawResource(R.raw.emt), db);
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(getActivity(), Activity_Localizacion_EMT.class);
                startActivity(intent);
                pg.dismiss();
            }
        });
        Button btn_interes = (Button) view.findViewById(R.id.btn_interes);
        btn_interes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg = new ProgressDialog_Custom(getActivity(), getString(R.string.loading));
                pg.show();
                if (interesestaVacia()) {
                    try {
                        new Parser_XML_interes().parsear(getResources().openRawResource(R.raw.sitiosinteres), db);
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(getActivity(), Activity_Localizacion_Interes.class);
                startActivity(intent);
                pg.dismiss();
            }
        });
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean valenbisiestaVacia() {
        String sql = "SELECT * FROM Valenbisi";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private boolean edificiosestaVacia() {
        String sql = "SELECT * FROM Edificio";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private boolean emtestaVacia() {
        String sql = "SELECT * FROM EMT";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private boolean metroestaVacia() {
        String sql = "SELECT * FROM Metro";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private boolean interesestaVacia() {
        String sql = "SELECT * FROM Interes";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
}

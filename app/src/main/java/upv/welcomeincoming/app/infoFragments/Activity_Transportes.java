package upv.welcomeincoming.app.infoFragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import java.io.InputStream;
import java.util.List;

import upv.welcomeincoming.app.R;
import util.Parser_XML;
import util.Transporte;


/**
 * Created by Marcos on 30/04/14.
 */
public class Activity_Transportes extends FragmentActivity {
    ViewPager pager = null;
    PagerTabStrip tabStrip;
    FragmentPagerAdapter_Info pagerAdapter;
    List<Transporte> listaTransportes;
    String[] infos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info_transportes);

        InputStream fichero = this.getResources().openRawResource(R.raw.transportes);
        Parser_XML parseador = new Parser_XML("transportes");
        listaTransportes = parseador.parseando(fichero);
        listaTransportes.remove(0);
        Log.e("lista Transportes", listaTransportes.toString());

        this.pager = (ViewPager) findViewById(R.id.pager);
        this.tabStrip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);
        pager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setAlpha(normalizedposition);
            }
        });
        pagerAdapter = new FragmentPagerAdapter_Info(getSupportFragmentManager(), this);
        pagerAdapter.addFragment(new Fragment_Metro(listaTransportes.get(2)));
        pagerAdapter.addFragment(new Fragment_EMT(listaTransportes.get(3)));
        pagerAdapter.addFragment(new Fragment_Avion(listaTransportes.get(0)));
        pagerAdapter.addFragment(new Fragment_Tren(listaTransportes.get(4)));
        pagerAdapter.addFragment(new Fragment_Taxi(listaTransportes.get(1)));
        pager.setAdapter(pagerAdapter);
    }
}
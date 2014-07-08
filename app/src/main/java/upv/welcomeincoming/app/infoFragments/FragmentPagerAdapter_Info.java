package upv.welcomeincoming.app.infoFragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import upv.welcomeincoming.app.R;

/**
 * Created by Marcos on 30/04/14.
 */
public class FragmentPagerAdapter_Info extends android.support.v4.app.FragmentPagerAdapter {
    // List of fragments which are going to set in the view pager widget
    List<Fragment> fragments;
    final String[] titulos;

    public FragmentPagerAdapter_Info(FragmentManager fm, Context contexto) {
        super(fm);
        this.fragments = new ArrayList<Fragment>();
        titulos = new String[]{
                contexto.getString(R.string.valencia),
                contexto.getString(R.string.transporte),
                contexto.getString(R.string.escuelas),
                contexto.getString(R.string.asignaturas)
        };
    }

    public void addFragment(Fragment fragment) {
        this.fragments.add(fragment);
    }

    @Override
    public Fragment getItem(int arg0) {
        return this.fragments.get(arg0);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titulos[position];
    }

}

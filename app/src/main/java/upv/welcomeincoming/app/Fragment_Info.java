package upv.welcomeincoming.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import upv.welcomeincoming.app.infoFragments.FragmentPagerAdapter_Info;
import upv.welcomeincoming.app.infoFragments.Fragment_Asignaturas;
import upv.welcomeincoming.app.infoFragments.Fragment_Escuelas;
import upv.welcomeincoming.app.infoFragments.Fragment_Transportes;
import upv.welcomeincoming.app.infoFragments.Fragment_Upv;


public class Fragment_Info extends Fragment {

    ViewPager pager = null;
    PagerTabStrip tabStrip;
    FragmentPagerAdapter_Info pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        this.pager = (ViewPager) view.findViewById(R.id.pager);
        this.tabStrip = (PagerTabStrip) view.findViewById(R.id.pager_tab_strip);
        pager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setAlpha(normalizedposition);
            }
        });
        pagerAdapter = new FragmentPagerAdapter_Info(getActivity().getSupportFragmentManager(), getActivity().getApplicationContext());
        pagerAdapter.addFragment(new Fragment_Upv());
        pagerAdapter.addFragment(new Fragment_Transportes());
        pagerAdapter.addFragment(new Fragment_Escuelas());
        pagerAdapter.addFragment(new Fragment_Asignaturas());
        pager.setAdapter(pagerAdapter);

        return view;
    }


}
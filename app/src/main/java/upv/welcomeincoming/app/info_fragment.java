package upv.welcomeincoming.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import upv.welcomeincoming.app.infoFragments.info_Asignaturas_fragment;
import upv.welcomeincoming.app.infoFragments.info_Escuelas_fragment;
import upv.welcomeincoming.app.infoFragments.info_Valencia_fragment;
import upv.welcomeincoming.app.infoFragments.info_transportes_fragment;
import upv.welcomeincoming.app.infoFragments.MyFragmentPagerAdapter;


public class info_fragment extends Fragment {

    ViewPager pager = null;
    PagerTabStrip tabStrip;
    MyFragmentPagerAdapter pagerAdapter;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup  container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container,false);
        this.pager = (ViewPager) view.findViewById(R.id.pager);
        this.tabStrip = (PagerTabStrip) view.findViewById(R.id.pager_tab_strip);
        pager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setAlpha(normalizedposition);
            }
        });
        pagerAdapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(),getActivity().getApplicationContext());
        pagerAdapter.addFragment(new info_Valencia_fragment());
        pagerAdapter.addFragment(new info_transportes_fragment());
        pagerAdapter.addFragment(new info_Escuelas_fragment());
        pagerAdapter.addFragment(new info_Asignaturas_fragment());
        pager.setAdapter(pagerAdapter);

        return view;
    }




}
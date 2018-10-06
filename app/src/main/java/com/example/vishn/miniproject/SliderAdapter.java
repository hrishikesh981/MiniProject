package com.example.vishn.miniproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context)
    {
        this.context=context;
    }

    //arrays....

    public int slideimages[]=
            {
                    R.drawable.pill_icon,
                    R.drawable.monitor_icon,                   //array for our slide images
                    R.drawable.global_icon

            };


    public String slideheadings[]=
            {
                    "MEDICO",
                    "SEARCH",
                    "LOCATE"
            };

    public String slidedesc[]=
            {
                    "Your one stop to finding medicines online",
                    "Search for medicines and check available stock",
                    "Enter your location and find pharmacies closest to you"

            };




    @Override
    public int getCount() {
        return slideheadings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==(RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide,container,false);


        ImageView slideimageview=(ImageView) view.findViewById(R.id.slideimage);
        TextView slideheadingview=(TextView) view.findViewById(R.id.slideheading);
        TextView slidedescview=(TextView) view.findViewById(R.id.slidedesc);


        slideimageview.setImageResource(slideimages[position]);
        slideheadingview.setText(slideheadings[position]);
        slidedescview.setText(slidedesc[position]);


        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object)
    {
        container.removeView((RelativeLayout)object);


    }
}

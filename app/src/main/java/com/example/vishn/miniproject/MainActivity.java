package com.example.vishn.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mdot;

    private SliderAdapter sliderAdapter;

    private TextView[] mdots;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSlideViewPager= findViewById(R.id.slide_viewpager);
        mdot= findViewById(R.id.dots);

        sliderAdapter=new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);


        init_main();
    }


    public void init_main()
    {
        Button btn= findViewById(R.id.slide_butt);

        Button btn1= findViewById(R.id.butt);

        btn1.setOnClickListener(v -> {
            Intent main_register=new Intent(MainActivity.this,RegistrationActivity.class);
            startActivity(main_register);


        });
        btn.setOnClickListener(v -> {
            Intent main_login=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(main_login);
        });
    }

    public void addDotsIndicator(int position)
    {
        mdots=new TextView[3];
        mdot.removeAllViews();
        for(int i=0;i<mdots.length;i++)
        {
             mdots[i]=new TextView(this);
             mdots[i].setText(Html.fromHtml("&#8226;"));
             mdots[i].setTextSize(35);
             mdots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

             mdot.addView(mdots[i]);
        }

        if(mdots.length>0)
        {
            mdots[position].setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }





    ViewPager.OnPageChangeListener viewListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

}

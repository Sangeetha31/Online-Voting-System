package com.example.onlinevoting;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

import org.jetbrains.annotations.NotNull;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    public SliderAdapter(Context context){
        this.context=context;
    }

    public int[] slide_images ={
            R.drawable.voting,
            R.drawable.otp,
            R.drawable.recaptcha
    };

    public  String[] slide_headings = {
            "Easy to use",
            "OTP Verification",
            "Captcha"
        };

    public String[] slide_descs = {
            "An android application which is easy to vote by just a click of a button",
            "This application will generate the One Time Password for registered users. It is a speedy and easy idea to do the verification of the voters",
            "Captcha verification is a common web technique used to help ensure that your respondents are real humans "
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == (RelativeLayout) object;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);
        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);
        return  view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((RelativeLayout)object);
    }

}

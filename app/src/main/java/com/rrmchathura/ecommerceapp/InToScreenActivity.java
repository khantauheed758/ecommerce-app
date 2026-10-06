package com.rrmchathura.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

//import com.getkeepsafe.taptargetview.TapTargetView;
//import com.getkeepsafe.taptargetview.TapTarget;
import com.rrmchathura.ecommerceapp.databinding.ActivityInToScreenBinding;

public class InToScreenActivity extends AppCompatActivity {

    ActivityInToScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInToScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        TapTargetView.showFor(this, TapTarget.forView(binding.getStartedBtn,"")
        .outerCircleColor(R.color.sky_blue)
        .outerCircleAlpha(0.16f)
        .targetCircleColor(R.color.white)
        .titleTextSize(10)
        .targetCircleColor(R.color.white)
        .descriptionTextSize(10)
        .descriptionTextColor(R.color.black)
        .textColor(R.color.black)
        .textTypeface(Typeface.SANS_SERIF)
        .dimColor(R.color.black)
        .drawShadow(true)
        .cancelable(true)
        .tintTarget(true)
        .transparentTarget(true)
        .targetRadius(60),
                new TapTargetView.Listener(){

                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        Intent i = new Intent(InToScreenActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });

        binding.getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InToScreenActivity.this,LoginActivity.class));
                finish();
            }
        });


    }
}
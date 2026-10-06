package com.rrmchathura.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrmchathura.ecommerceapp.Seller.SellerHomeActivity;
import com.rrmchathura.ecommerceapp.User.UserHomeActivity;
import com.rrmchathura.ecommerceapp.admin.AdminHomeActivity;
import com.rrmchathura.ecommerceapp.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;

    FirebaseAuth mAuth;
    FirebaseDatabase database;

    SharedPreferences onBordingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        String text = "Fly Shopping";
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan fcsBlack = new ForegroundColorSpan(getResources().getColor(R.color.black));
        ForegroundColorSpan fcsBlue= new ForegroundColorSpan(getResources().getColor(R.color.sky_blue));

        ss.setSpan(fcsBlack,1,3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(fcsBlue,4,12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.welcome1.setText(ss);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        YoYo.with(Techniques.ZoomIn)
                .duration(3000)
                .repeat(0)
                .playOn(binding.welcome);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIsFirstTime();
              //  checkUser();
            }
        }, 3000);
    }

    private void checkIsFirstTime() {
        onBordingScreen = getSharedPreferences("onBordingScreen",MODE_PRIVATE);
        boolean isFirstTime = onBordingScreen.getBoolean("firstTime",true);

        if (isFirstTime){

            SharedPreferences.Editor editor = onBordingScreen.edit();
            editor.putBoolean("firstTime",false);
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), InToScreenActivity.class);
            startActivity(intent);
            finish();


        }
        else {

            checkUser();
        }
    }

    private void checkUser() {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {

            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        } else {
            DatabaseReference reference = database.getReference("Users");
            reference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String userType = "" + snapshot.child("userType").getValue();
                    String approval = "" + snapshot.child("Is_approval").getValue();


                    if (userType.equals("user")) {

                        startActivity(new Intent(SplashActivity.this, UserHomeActivity.class));
                        finish();

                    } else if (userType.equals("seller")) {

                        if (approval.equals("approved")) {

                            startActivity(new Intent(SplashActivity.this, SellerHomeActivity.class));
                            finish();

                        }
                        else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                    else {
                        startActivity(new Intent(SplashActivity.this, AdminHomeActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
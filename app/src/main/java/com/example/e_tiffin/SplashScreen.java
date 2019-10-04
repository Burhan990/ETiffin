package com.example.e_tiffin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TextView welcome_txt=findViewById(R.id.textSlogan);

        ImageView welcome_img=findViewById(R.id.splashimage);

        //lottieAnimationView=(LottieAnimationView)findViewById(R.id.animation);

        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/fonts.TTF");

        welcome_txt.setTypeface(typeface);



        Animation animation= AnimationUtils.loadAnimation(this,R.anim.mytransition);

        welcome_txt.startAnimation(animation);
        welcome_img.startAnimation(animation);

        final Intent intent=new Intent(SplashScreen.this,MainActivity.class);

        Thread timer=new Thread() {
            public void run() {
                try{
                    sleep(5000);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // lottieAnimationView.setVisibility(View.GONE);
                            finish();

                        }
                    });

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();
                }

            }
        };
        timer.start();
    }
}

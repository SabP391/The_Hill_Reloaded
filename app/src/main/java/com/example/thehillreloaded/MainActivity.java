package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RotateAnimation animazioneSole = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animazioneSole.setInterpolator(new LinearInterpolator());
        animazioneSole.setRepeatCount(Animation.INFINITE);
        animazioneSole.setDuration(7000);

// Start animating the image
        final ImageView soleRotante = (ImageView) findViewById(R.id.sole_rotante_sp);
        soleRotante.startAnimation(animazioneSole);

    }
}
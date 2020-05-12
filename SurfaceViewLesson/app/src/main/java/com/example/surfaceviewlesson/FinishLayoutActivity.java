package com.example.surfaceviewlesson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FinishLayoutActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_layout);
        Button resetBtn = findViewById(R.id.reset_button);

        View.OnClickListener oResetBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishLayoutActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        resetBtn.setOnClickListener(oResetBtn);
    }
}

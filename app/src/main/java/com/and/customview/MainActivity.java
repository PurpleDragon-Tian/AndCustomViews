package com.and.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.and.customview.activity.label.InputableLabelActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_activity);

    }

    public void InputableLabel(View view) {
        startActivity(new Intent(MainActivity.this, InputableLabelActivity.class));
    }
}

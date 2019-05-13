package com.and.customview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.and.customview.activity.label.InputableLabelActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvInputableLabel)
    TextView tvInputableLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_activity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tvInputableLabel )
    void tvInputableLabel() {
        startActivity(new Intent(MainActivity.this, InputableLabelActivity.class));
    }

}

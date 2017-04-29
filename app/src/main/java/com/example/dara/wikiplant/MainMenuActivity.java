package com.example.dara.wikiplant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainMenuActivity extends AppCompatActivity {

    @BindView(R.id.button_list)
    Button buttonList;
    @BindView(R.id.button_scan)
    Button buttonScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button_list, R.id.button_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_list:
                Intent intent = new Intent(MainMenuActivity.this, ListOfPlantsActivity.class);
                startActivity(intent);


                break;
            case R.id.button_scan:

                break;
        }
    }
}

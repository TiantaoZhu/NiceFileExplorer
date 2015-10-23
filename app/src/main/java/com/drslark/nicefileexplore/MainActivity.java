package com.drslark.nicefileexplore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends TitleControlBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.category_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PicCategoryActivity.class);
                startActivity(intent);
            }
        });
    }

}

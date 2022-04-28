package by.bsuir.dshparko.mobilki_lab2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().hide();
        @SuppressLint("ResourceType") TabLayout toolbar;
        toolbar = findViewById(R.id.toolbar);
        toolbar.selectTab(toolbar.getTabAt(2),true);
        toolbar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:

                        Intent intent2 = new Intent(SearchActivity.this, HomeActivity.class);
                        startActivity(intent2);
                        break;
                    case 1:

                        Intent intent1 = new Intent(SearchActivity.this, UserActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:

                        Intent intent3 = new Intent(SearchActivity.this, SearchActivity.class);
                        startActivity(intent3);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}

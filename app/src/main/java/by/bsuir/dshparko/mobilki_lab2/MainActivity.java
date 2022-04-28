package by.bsuir.dshparko.mobilki_lab2;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent2 = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent2);

    }


}

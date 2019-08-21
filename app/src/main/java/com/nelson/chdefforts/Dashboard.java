package com.nelson.chdefforts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.nelson.chdefforts.MainMenu.MapsActivity;

public class Dashboard extends AppCompatActivity {
    ImageButton vehicle, maps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        vehicle = findViewById(R.id.btnVehicle);
        maps = findViewById(R.id.btnMaps);

        vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Dashboard.this, MainActivity.class);
                startActivity(i);
            }
        });

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Dashboard.this, MapsActivity.class);
                startActivity(i);
            }
        });

    }
}

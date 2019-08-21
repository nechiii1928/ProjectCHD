package com.nelson.chdefforts;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nelson.chdefforts.MainMenu.MapsActivity;
import com.nelson.chdefforts.Users.LoginActivity;

public class MainActivity extends AppCompatActivity {
    EditText txtvehicleno,txtstartread , txtendread, txtamount;
    Button btnaddvehicle, v;
    DatabaseReference databasevehicle;
    FirebaseDatabase mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databasevehicle = FirebaseDatabase.getInstance().getReference("");
        txtvehicleno =(EditText) findViewById(R.id.etvehicleno);
        txtstartread= (EditText) findViewById(R.id.etstartreading);
        txtendread=(EditText) findViewById(R.id.etendreding);
        txtamount= (EditText) findViewById(R.id.etamount);
        btnaddvehicle =(Button) findViewById(R.id.btnsubmitvehicle);
        v =(Button) findViewById(R.id.btngoto);
        btnaddvehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addvehicle();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

    }
    public void addvehicle(){
        String vehicleno = txtvehicleno.getText().toString().trim();
        String startread = txtstartread.getText().toString().trim();
        String endread= txtendread.getText().toString().trim();
        String amount = txtamount.getText().toString().trim();

        if(!TextUtils.isEmpty(vehicleno)){
            String id =  databasevehicle.push().getKey();
            Vehicle vehicles = new Vehicle(id,vehicleno,startread,endread,amount);
            databasevehicle.child(id).setValue(vehicles);
            Toast.makeText(this, "Vehicle Data Added", Toast.LENGTH_LONG).show();

        }else{
            txtvehicleno.setError("Please Enter Vehicle Number");

        }
    }
    public static class Utils {
        private static FirebaseDatabase mDatabase;
        public static FirebaseDatabase getDatabase() {
            if (mDatabase == null) {
                mDatabase = FirebaseDatabase.getInstance();
                mDatabase.setPersistenceEnabled(true);
            }
            return mDatabase;
        }

    }

}

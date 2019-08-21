package com.nelson.chdefforts.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nelson.chdefforts.MainActivity;
import com.nelson.chdefforts.R;

public class AdminRegistration extends AppCompatActivity {
    EditText nname, npassword, nconfirmpwd, nmobile, nemail;
    Button nregister;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration);

        nname = findViewById(R.id.etNname);
        npassword = findViewById(R.id.etNpassword);
        nconfirmpwd = findViewById(R.id.etNconfirmpss);
        nmobile = findViewById(R.id.etNmobile);
        nemail = findViewById(R.id.etNemail);
        nregister = findViewById(R.id.btnNregister);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        nregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = nname.getText().toString().trim();
                String password = npassword.getText().toString().trim();
                String mobilenum = nmobile.getText().toString().trim();
                String confirmpwd = nconfirmpwd.getText().toString().trim();
                String email = nemail.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(AdminRegistration.this, "Please enter your Fucking name...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length()<6){
                    Toast.makeText(AdminRegistration.this, "Fucking 8 characters please", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(password.equals(confirmpwd)){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(AdminRegistration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        Toast.makeText(AdminRegistration.this, "Fucked UP", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AdminRegistration.this, "You are fucking dumass..", Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });

                }
            }
        });

    }
}

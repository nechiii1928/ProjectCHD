package com.nelson.chdefforts.Users;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nelson.chdefforts.Dashboard;
import com.nelson.chdefforts.MainMenu.MapsActivity;
import com.nelson.chdefforts.R;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private TextView txtNewac;
    EditText userEmail, userPassword;

    private Button mLoginButton;
    private ProgressDialog mProgressbar;
    private DatabaseReference mDatabaseUsers;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private String number;

    private GoogleApiClient mGoogleApiClient;

    CallbackManager callbackManager;

    private ImageView googleImg, fbImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);


        bindViews();

        onClicks();

    }



    private void bindViews() {
        mAuth = FirebaseAuth.getInstance();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);

        mProgressbar = new ProgressDialog(this);
        userEmail = findViewById(R.id.etUemail);
        userPassword = findViewById(R.id.etUpass);
        fbImage = findViewById(R.id.fblogin);
        googleImg = findViewById(R.id.glogin);
        mLoginButton = findViewById(R.id.btnLogin);
        txtNewac = findViewById(R.id.tvReg);

    }


    private void onClicks() {
        txtNewac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MapsActivity.class));
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });

        fbInit();

        googleInit();

        googleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpGoogle();
            }
        });
    }

    private void googleInit(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    private void signUpGoogle(){
        try {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } catch (Exception e) {
        }

    }

    private void fbInit() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("facebook:token", AccessToken.getCurrentAccessToken().getToken());
                AccessToken.getCurrentAccessToken().getToken();
                signInWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
        fbImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "user_location", "user_birthday", "public_profile", "user_friends"));
            }
        });
    }

    private void signInWithFacebook(AccessToken token) {
        Log.d(TAG, "signInWithFacebook:" + token.getToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        final String tokenString = token.getToken();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
//
//                        startActivity(new Intent(SignUp09.this, Dashboard.class));
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Sorry for inconvenience,Please try again..",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //signInFacebook(tokenString);
                            Toast.makeText(LoginActivity.this, "Welcome..!", Toast.LENGTH_SHORT).show();
                            checkUserExist();
                        }
                    }


                });
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        mProgressbar.setMessage("Checking LOGIN.....");
        mProgressbar.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            mAuth = FirebaseAuth.getInstance();
                            Toast.makeText(LoginActivity.this, "Welcome..!", Toast.LENGTH_SHORT).show();
                            checkUserExist();
                            mProgressbar.dismiss();
                            //***updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            mProgressbar.dismiss();
                            //****updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void checkLogin() {
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "uname:" + email + " passwd:" + password, Toast.LENGTH_LONG).show();

            mProgressbar.setMessage("Checking LOGIN.....");
            mProgressbar.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mProgressbar.dismiss();
                        checkUserExist();
                    } else {
                        mProgressbar.dismiss();
                        Toast.makeText(LoginActivity.this, "TASK FAILED", Toast.LENGTH_LONG).show();
                        Log.w("LoginThing---", "signInWithEmail:failed", task.getException());
                    }
                }
            });
        }
    }

    private void checkUserExist() {
        mProgressbar.setMessage("loading..");
        mProgressbar.show();
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)) {
                    Intent mainIntent = new Intent(LoginActivity.this, Dashboard.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    mProgressbar.dismiss();
                } else {
                    Intent setupIntent = new Intent(LoginActivity.this, Dashboard.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(setupIntent);
                    mProgressbar.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

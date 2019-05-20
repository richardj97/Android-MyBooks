package uk.ac.lincoln.a16629926student.booksapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // Activity controls (Buttons and Edit texts)
    private EditText editEmail, editPassword;
    private Button btnSignup, btnLogin;
    private SignInButton btnLoginGoogle;

    // Firebase and Google
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 9001;

    // Image and TAG
    private ImageView imageLogo;
    private String TAG = "SignIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(SignIn.this, Dashboard.class));
            finish();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(SignIn.this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        imageLogo = (ImageView)findViewById(R.id.logoLv);
        btnLoginGoogle = (SignInButton)findViewById(R.id.sign_in_button);
        btnLoginGoogle.setScopes(gso.getScopeArray());
        imageLogo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        setGoogleButtonText(btnLoginGoogle, "SIGN IN WITH GOOGLE               ");

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, uk.ac.lincoln.a16629926student.booksapi.SignUp.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editEmail.getText().toString();
                final String password = editPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        editPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(SignIn.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    if (auth.getCurrentUser().isEmailVerified()){
                                        FirebaseUser user = auth.getCurrentUser();
                                        ClearLocalStorage("userData");
                                        AddToLocalStorage("userData", "DisplayName", user.getDisplayName());
                                        AddToLocalStorage("userData", "Email", user.getEmail());
                                        AddToLocalStorage("userData", "UserId", user.getUid());

                                        if (user.getPhotoUrl() != null)
                                            AddToLocalStorage("userData", "Avatar", user.getPhotoUrl().toString());
                                        Toast.makeText(SignIn.this,
                                                "Welcome, " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignIn.this, Dashboard.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(SignIn.this, "Email not verified", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                authWithGoogle(account);
            }
        }
    }

    private void signInWithGoogle(){
        progressBar.setVisibility(View.VISIBLE);
        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signIntent,RC_SIGN_IN);
    }

    private void ClearLocalStorage(String keyName){
        try {
            SharedPreferences storage = getSharedPreferences(keyName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = storage.edit();
            editor.clear();
            editor.commit();
        }
        catch (Exception ex){
            Log.e(TAG, "Error clearing local storage");
        }
    }

    public void AddToLocalStorage(String keyName, String key, String data){
        try {
            SharedPreferences storage = getSharedPreferences(keyName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = storage.edit();
            editor.putString(key, data);
            editor.commit();
        }
        catch (Exception ex){
            Log.e(TAG, "Error saving to local storage");
        }
    }

    private void authWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);

        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    ClearLocalStorage("userData");
                    AddToLocalStorage("userData", "DisplayName", user.getDisplayName());
                    AddToLocalStorage("userData", "Email", user.getEmail());
                    AddToLocalStorage("userData", "Avatar", user.getPhotoUrl().toString());
                    AddToLocalStorage("userData", "UserId", user.getUid());
                    Toast.makeText(SignIn.this,
                            "Welcome, " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Auth Error", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    protected void setGoogleButtonText(SignInButton signInButton, String buttonText) {
        // Just a method to edit the google button text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, connectionResult.getErrorMessage());
    }
}

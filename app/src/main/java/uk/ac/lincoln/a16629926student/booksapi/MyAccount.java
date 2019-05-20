package uk.ac.lincoln.a16629926student.booksapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class MyAccount extends AppCompatActivity {

    private ImageView avatarIV;
    private TextView emailTV, displayNameTV, uIDTV;
    private Button signOutBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        auth = FirebaseAuth.getInstance();

        avatarIV = (ImageView)findViewById(R.id.avatarIV);
        emailTV = (TextView)findViewById(R.id.emailTV);
        displayNameTV = (TextView)findViewById(R.id.displayNameTV);
        uIDTV = (TextView)findViewById(R.id.uIDTV);
        signOutBtn = (Button)findViewById(R.id.signOutBtn);

        SharedPreferences preferences = getSharedPreferences("userData", MODE_PRIVATE);
        String avatarUrl = preferences.getString("Avatar", "N/A");
        String email = preferences.getString("Email", "N/A");
        String displayName = preferences.getString("DisplayName", "N/A");
        String uid = preferences.getString("UserId", "N/A");

        if (avatarUrl != "N/A"){
            Glide.with(this).load(avatarUrl).into(avatarIV);
        }

        if (email != "N/A"){
            emailTV.setText(email);
        }

        if (displayName != "N/A"){
            displayNameTV.setText(displayName);
        }

        if (uid != "N/A"){
            uIDTV.setText("UID: " + uid);
        }

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }
    private void signOut(){
        auth.signOut();
        startActivity(new Intent(getApplicationContext(), SignIn.class));
        finish();
    }
}

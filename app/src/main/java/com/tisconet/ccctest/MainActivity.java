package com.tisconet.ccctest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText inputAmount;
    private Button btnEnter,btnLogout;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        inputAmount = (EditText) findViewById(R.id.edtInput);
        btnEnter = (Button) findViewById(R.id.btn_send);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = inputAmount.getText().toString();

                if(TextUtils.isEmpty(amount)){
                    Toast.makeText(getApplicationContext(),"Enter your amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
               startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }

        });
    }

}

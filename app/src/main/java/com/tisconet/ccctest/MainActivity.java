package com.tisconet.ccctest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText inputAmount;
    private Button btnEnter,btnLogout;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        //Add to Activity
        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");

        inputAmount = (EditText) findViewById(R.id.edtInput);
        btnEnter = (Button) findViewById(R.id.btn_send);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = inputAmount.getText().toString();

                if (TextUtils.isEmpty(amount)) {
                    Toast.makeText(getApplicationContext(), "Enter your amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                getToken();
                Toast.makeText(MainActivity.this, "Subscribed to Topic: Push Notifications", Toast.LENGTH_SHORT).show();
                return;

            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }

        });
    }

    private void getToken() {

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            Log.d(TAG, idToken);
                            // Send token to your backend via HTTPS
                            makeHttpConnection(idToken);
                            // ...
                        } else {
                            // Handle error -> task.getException();
                            Log.d(TAG, task.getException().toString());
                        }
                    }
                });
    }

    private void makeHttpConnection(String token) {
        new MyDownloadTask().execute(token);
    }

    class MyDownloadTask extends AsyncTask<String, Void, Void> {


        protected void onPreExecute() {
            //display progress dialog.

        }

        protected Void doInBackground(String... params) {
            String token = params[0];
            Log.d(TAG, token);
            URL url;
            HttpURLConnection urlConnection = null;
            try {
//            ttttest-a3bd3
                url = new URL("https://us-central1-ttttest-a3bd3.cloudfunctions.net/pushNotification");

                urlConnection = (HttpURLConnection) url
                        .openConnection();
                urlConnection.setRequestProperty("Authorization", token);
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, String.valueOf(responseCode));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            // dismiss progress dialog and update ui
            Log.d(TAG, "onPostExecute");
        }
    }

}

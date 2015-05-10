package com.staggarlee.ribbit;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class LoginActivity extends ActionBarActivity {

    protected TextView mSignUpTextView;
    protected EditText mUserName;
    protected EditText mPassword;

    protected Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserName = (EditText) findViewById(R.id.usernameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mSignUpTextView = (TextView) findViewById(R.id.signUpText);
        mLoginButton = (Button) findViewById(R.id.loginButton);

        // create a new user link
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        // logging in with an account already created
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = mUserName.getText().toString().trim();
                String password = mPassword.getText().toString().trim();


                if(userName.isEmpty() || password.isEmpty()) {
                    // could have made a toast, but the user might miss it
                    // Toast.makeText(SignUpActivity.this, "Missing a field", Toast.LENGTH_LONG).show();

                    //creating an alert dialog instead (look at the factory model!)
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    // created the factory, now we tell it the details of the object
                    builder.setTitle(getString(R.string.login_error_title))
                            .setMessage(getString(R.string.login_error_message))
                            .setPositiveButton(android.R.string.ok, null);
                    // then we ask the factory to build us one
                    AlertDialog dialog = builder.create();
                    // then we use our object
                    dialog.show();



                } else {
                    // create user here
                    setSupportProgressBarIndeterminateVisibility(true);
                    ParseUser.logInInBackground(userName, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            setSupportProgressBarIndeterminateVisibility(false);
                            if(e == null) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);

                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle(getString(R.string.login_error_title))
                                        .setMessage(e.getMessage())
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                        }
                    });

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

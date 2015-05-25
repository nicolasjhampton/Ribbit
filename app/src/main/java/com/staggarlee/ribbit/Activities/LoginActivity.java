package com.staggarlee.ribbit.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
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
import com.staggarlee.ribbit.Constants.RibbitApplication;
import com.staggarlee.ribbit.R;


public class LoginActivity extends ActionBarActivity {

    protected TextView mSignUpTextView;
    protected EditText mUserName;
    protected EditText mPassword;

    protected Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

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
                    setProgressBarIndeterminateVisibility(true);
                    ParseUser.logInInBackground(userName, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            setProgressBarIndeterminateVisibility(false);
                            if(e == null) {
                                RibbitApplication.updateParseInstallation(parseUser);
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

}

package com.staggarlee.ribbit.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.staggarlee.ribbit.R;


public class SignUpActivity extends ActionBarActivity {

    protected EditText mUserName;
    protected EditText mPassword;
    protected EditText mEmail;
    protected Button mSignUpButton;
    protected Button mCancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mUserName = (EditText) findViewById(R.id.usernameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mEmail = (EditText) findViewById(R.id.emailField);
        mSignUpButton = (Button) findViewById(R.id.signUpButton);
        mCancelButton = (Button) findViewById(R.id.cancelButton);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // NavUtils.navigateUpFromSameTask(SignUpActivity.this);
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });



        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = mUserName.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String email = mEmail.getText().toString().trim();

                if(userName.isEmpty() || password.isEmpty()|| email.isEmpty()) {
                    // could have made a toast, but the user might miss it
                    // Toast.makeText(SignUpActivity.this, "Missing a field", Toast.LENGTH_LONG).show();

                    //creating an alert dialog instead (look at the factory model!)
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    // created the factory, now we tell it the details of the object
                    builder.setTitle(getString(R.string.signup_error_title))
                            .setMessage(getString(R.string.signup_error_message))
                            .setPositiveButton(android.R.string.ok, null);
                    // then we ask the factory to build us one
                    AlertDialog dialog = builder.create();
                    // then we use our object
                    dialog.show();



                } else {
                    // create user here

                    ParseUser user = new ParseUser();
                    user.setUsername(userName);
                    user.setEmail(email);
                    user.setPassword(password);
                    setSupportProgressBarIndeterminateVisibility(true);
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            setSupportProgressBarIndeterminateVisibility(false);
                            if(e == null) {
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setTitle(getString(R.string.signup_error_title))
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

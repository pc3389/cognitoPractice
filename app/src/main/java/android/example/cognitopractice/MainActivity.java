package android.example.cognitopractice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProvider;
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProviderClient;
import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText emailEditText;
        EditText passwordEditText;
        EditText confirmationEditText;
        Button signUpButton;
        Button signOutButton;
        Button signInButton;
        Button checkButton;
        Button credButton;

        emailEditText = findViewById(R.id.editTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextPassword);
        confirmationEditText = findViewById(R.id.editTextConfirmation);
        signUpButton = findViewById(R.id.button2);
        signOutButton = findViewById(R.id.signoutbutton);
        signInButton = findViewById(R.id.signinbutton);
        checkButton = findViewById(R.id.checkbutton);
        credButton = findViewById(R.id.credButton);

        Amplify.Auth.fetchAuthSession(
                result -> Log.i("AmplifyQuickstart", result.toString()),
                error -> Log.e("AmplifyQuickstart", error.toString())
        );

















        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signUp(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), emailEditText.getText().toString()).build(),
                        result -> Log.i("AuthQuickstart", "Result: " + result.toString()),
                        error -> Log.e("AuthQuickstart", "Sign up failed", error)
                );
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.confirmSignUp(
                        emailEditText.getText().toString(),
                        confirmationEditText.getText().toString(),
                        result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
                        error -> Log.e("AuthQuickstart", error.toString())
                );
            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Amplify.Auth.getCurrentUser() == null) {
                    Amplify.Auth.signIn(
                            emailEditText.getText().toString(),
                            passwordEditText.getText().toString(),
                            result -> {
                                Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded  " + Amplify.Auth.getCurrentUser() : "Sign in not complete");
                            },
                            error -> Log.e("AuthQuickstart", error.toString())
                    );
                } else {
                    Log.i("AuthQuickstart", "Another user is logged in");
                }

            }
        });


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signOut(
                        () -> Log.i("AuthQuickstart", "Signed out successfully"),
                        error -> Log.e("AuthQuickstart", error.toString())
                );
            }
        });

        credButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Amplify.Auth.fetchAuthSession(
                        result -> {
                            AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                            switch(cognitoAuthSession.getIdentityId().getType()) {
                                case SUCCESS:
                                    Log.i("AuthQuickstart", "IdentityId: " + cognitoAuthSession.getIdentityId().getValue() + "   " + Amplify.Auth.getCurrentUser());
                                    break;
                                case FAILURE:
                                    Log.i("AuthQuickstart", "IdentityId not present because: " + cognitoAuthSession.getIdentityId().getError().toString());
                            }
                        },
                        error -> Log.e("AuthQuickstart", error.toString())
                );
            }
        });
    }

}
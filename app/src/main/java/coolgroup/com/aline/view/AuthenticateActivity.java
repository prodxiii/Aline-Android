package coolgroup.com.aline.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.firebase.database.DatabaseReference;
import com.rengwuxian.materialedittext.MaterialEditText;

import coolgroup.com.aline.Controller;
import coolgroup.com.aline.Maps.Homepage;
import coolgroup.com.aline.R;
import coolgroup.com.aline.model.User;
import dmax.dialog.SpotsDialog;

public class AuthenticateActivity extends AppCompatActivity {


    // Declare a users database
    DatabaseReference users;
    // Declare Button and Intro View
    private Button btnSignIn, btnRegister;
    private RelativeLayout authLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        // Why even bother with the compartmentalisation if we're just going to cut
        // through it like this?
        users = Controller.getInstance().getServerCommunicator().getmDatabase().getReference("Users");

        // Initialize Views
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        authLayout = (RelativeLayout) findViewById(R.id.authLayout);

        // Override button onClick methods to show Register Dialog
        btnRegister.setOnClickListener(v -> showRegisterDialog());

        // Override button onClick methods to show Login Dialog
        btnSignIn.setOnClickListener(v -> showLogInDialog());
    }

    // Create Login Dialog which is of type AlertDialog
    private void showLogInDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        // Set the Title and Message for the dialog
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please use email to sign in");

        // Instantiates a Login layout XML file into the corresponding AuthenticateActivity view object
        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login, null);

        // Create custom TextView for email and password input
        final MaterialEditText edtEmail = (MaterialEditText) login_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = (MaterialEditText) login_layout.findViewById(R.id.edtPassword);

        dialog.setView(login_layout);

        // Set Sign In Button
        dialog.setPositiveButton("SIGN IN", (dialog1, which) -> {

            dialog1.dismiss();

            // Disable Sign In button after it starts processing
            btnSignIn.setEnabled(false);

            // Validate email and password
            if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                Snackbar.make(authLayout, "Please enter email address", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                Snackbar.make(authLayout, "Please enter password", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            // Android AlertDialog with moving spots progress indicator packed as android library.
            android.app.AlertDialog waitingDialog = new SpotsDialog(AuthenticateActivity.this);
            waitingDialog.show();

            // Try to Login with correctly validated email and password
            // auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()) TODO: Delete me!
            Controller.getInstance().getServerCommunicator().logInUserEmail(edtEmail.getText().toString(), edtPassword.getText().toString())
                    .addOnSuccessListener(authResult -> {

                        // Remove the loading dialog
                        waitingDialog.dismiss();

                        // Welcome to Aline
                        proceedToHomepage();

                    })
                    .addOnFailureListener(e -> {

                        // Remove the loading dialog
                        waitingDialog.dismiss();

                        // Incorrect email or password
                        Snackbar.make(authLayout, e.getMessage(), Snackbar.LENGTH_LONG)
                                .show();

                        // Make SignIn button active again
                        btnSignIn.setEnabled(true);

                    });
        });

        // Set the Cancel button
        dialog.setNegativeButton("CANCEL", (dialog12, which) -> dialog12.dismiss());

        // Show the Login dialog when ShowLogInDialog method is called
        dialog.show();
    }

    // Create Register Dialog which is of type AlertDialog
    private void showRegisterDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        // Set the Title and Message for the dialog
        dialog.setTitle("REGISTER");
        dialog.setMessage("Please use email to register");

        // Instantiates a Register layout XML file into the corresponding AuthenticateActivity view object
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register, null);

        // Create custom TextView for email, name, phone and password
        final MaterialEditText edtName, edtEmail, edtPhone, edtPassword;
        edtName = (MaterialEditText) register_layout.findViewById(R.id.edtName);
        edtEmail = (MaterialEditText) register_layout.findViewById(R.id.edtEmail);
        edtPhone = (MaterialEditText) register_layout.findViewById(R.id.edtPhone);
        edtPassword = (MaterialEditText) register_layout.findViewById(R.id.edtPassword);

        edtPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        dialog.setView(register_layout);

        // Set Register button
        dialog.setPositiveButton("REGISTER", (dialog12, which) -> {

            dialog12.dismiss();

            // Disable Sign In button after it starts processing
            btnRegister.setEnabled(false);

            // Validate email, password, name and phone
            if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                Snackbar.make(authLayout, "Please enter email address", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                Snackbar.make(authLayout, "Please enter phone number", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            if (TextUtils.isEmpty(edtName.getText().toString())) {
                Snackbar.make(authLayout, "Please enter name", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                Snackbar.make(authLayout, "Please enter password", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            // If password is shorter than six characters
            if (edtPassword.getText().toString().length() < 6) {
                Snackbar.make(authLayout, "Password too short!", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            // Android AlertDialog with moving spots progress indicator packed as android library.
            android.app.AlertDialog waitingDialog = new SpotsDialog(AuthenticateActivity.this);
            waitingDialog.show();

            // Create an authentication in the Firebase Authentication
            Controller.getInstance().getServerCommunicator().signUpUser(edtEmail.getText().toString(), edtPassword.getText().toString(), "", "")
                    .addOnSuccessListener(authResult -> {

                        // Create an user in the Firebase Realtime Database
                        Controller.getInstance().getServerCommunicator().
                                createUserChild(edtName.getText().toString(), edtEmail.getText().toString(), edtPhone.getText().toString())
                                .addOnSuccessListener(aVoid -> {

                                    // Remove the loading dialog
                                    waitingDialog.dismiss();


                                    //WELCOME TO ALINE
                                    proceedToHomepage();

                                })
                                .addOnFailureListener(e -> {
                                    // Remove the loading dialog
                                    waitingDialog.dismiss();

                                    // Incorrect email or password
                                    Snackbar.make(authLayout, e.getMessage(), Snackbar.LENGTH_LONG)
                                            .show();

                                    // Make SignIn button active again
                                    btnSignIn.setEnabled(true);
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Remove the loading dialog
                        waitingDialog.dismiss();

                        Snackbar.make(authLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    });
        });

        dialog.setNegativeButton("CANCEL", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }


    private void proceedToHomepage() {

        Snackbar.make(authLayout, "Welcome to ALINE!", Snackbar.LENGTH_SHORT)
                .show();

        // Create current user
        //TODO: somehow make the current user.
        String uid = Controller.getInstance().getServerCommunicator().getCurrentUID();

        User mainUser = new User(null, null, null, null, uid);
        Controller.getInstance().setMainUser(mainUser);

        // Create the SinchCommunicator
        Log.d("AuthActivity", "Creating iVOIPCommunicator");
        Controller.getInstance().createiVOIPCommunicator(getApplicationContext());

        Intent homeIntent = new Intent(AuthenticateActivity.this, Homepage.class);

        // If email and password is authenticated open the welcome layout
        startActivity(homeIntent);

        // Validation to stop user from going to the authenticate activity again
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // And close the Login layout
        finish();
    }


}


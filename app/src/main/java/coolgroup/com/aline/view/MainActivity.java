package coolgroup.com.aline.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.rengwuxian.materialedittext.MaterialEditText;

import coolgroup.com.aline.Controller;
import coolgroup.com.aline.model.User;
import coolgroup.com.aline.R;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    /*
     * MainActivity is the entrance to the app. It is where we come when the app is first opened.
     * After that, for doing anything, we palm it off to another activity. We want very little
     * functionality in this activity.
     */


    // Declare Button and Intro View
    Button btnSignIn, btnRegister;
    RelativeLayout rootLayout;

    // Declare a users database
    DatabaseReference users;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        //TODO: get this activity to follow this pattern.
//        // If (user logged in) {
//            // goto map activity or something.
//        // else {
//            // ask user to choose between login or signup activities.
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        users = Controller.getInstance().serverCommunicator.getFirebaseDatabase().getReference("Users");

        // Initialize Views
        btnRegister = findViewById(R.id.btnRegister);
        btnSignIn = findViewById(R.id.btnSignIn);
        rootLayout = findViewById(R.id.rootLayout);

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

        // Instantiates a Login layout XML file into the corresponding MainActivity view object
        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login, null);

        // Create custom TextView for email and password input
        final MaterialEditText edtEmail = login_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = login_layout.findViewById(R.id.edtPassword);

        dialog.setView(login_layout);

        // Set Sign In Button
        dialog.setPositiveButton("SIGN IN", (dialog1, which) -> {

            dialog1.dismiss();

            // Disable Sign In button after it starts processing
            btnSignIn.setEnabled(false);


            // Validate email and password
            if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            // If password is shorter than six characters
            if (edtPassword.getText().toString().length() < 6) {
                Snackbar.make(rootLayout, "Password too short!", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            // Android AlertDialog with moving spots progress indicator packed as android library.
            android.app.AlertDialog waitingDialog = new SpotsDialog(MainActivity.this);
            waitingDialog.show();

            // Try to Login with correctly validated email and password
            // auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()) TODO: Delete me!
            Controller.getInstance().serverCommunicator.logInUserEmail(
                    edtEmail.getText().toString(),
                    edtPassword.getText().toString(),
                    mainUser -> {
                        Controller.getInstance().setMainUser(mainUser);

                        // Remove the loading dialog
                        waitingDialog.dismiss();

                        // If email and password is authenticated open the welcome layout
                        startActivity(new Intent(MainActivity.this, Homepage.class));

                        // And close the Login layout
                        finish();
                    });
//                    .addOnFailureListener(e -> {
//
//                        // Remove the loading dialog
//                        waitingDialog.dismiss();
//
//                        // Incorrect email or password
//                        Snackbar.make(rootLayout, "Failed " + e.getMessage(), Snackbar.LENGTH_LONG)
//                                .show();
//
//                        // Make SignIn button active again
//                        btnSignIn.setEnabled(true);
//
//                    });
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

        // Instantiates a Register layout XML file into the corresponding MainActivity view object
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register, null);

        // Create custom TextView for email, name, phone and password
        final MaterialEditText edtName = register_layout.findViewById(R.id.edtName);
        final MaterialEditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPhone = register_layout.findViewById(R.id.edtPhone);
        final MaterialEditText edtPassword = register_layout.findViewById(R.id.edtPassword);

        dialog.setView(register_layout);

        // Set Register button
        dialog.setPositiveButton("REGISTER", (dialog12, which) -> {

            dialog12.dismiss();

            // Validate email, password, name and phone
            if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                Snackbar.make(rootLayout, "Please enter phone number", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            if (TextUtils.isEmpty(edtName.getText().toString())) {
                Snackbar.make(rootLayout, "Please enter name", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            // If password is shorter than six characters
            if (edtPassword.getText().toString().length() < 6) {
                Snackbar.make(rootLayout, "Password too short!", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            // Create a new user with the input details
            // auth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()) TODO: Delete me!
            Controller.getInstance().serverCommunicator.signUpUser(
                    edtEmail.getText().toString(),
                    edtPassword.getText().toString(),
                    "",
                    "",
                    authResult -> {
                        // Save user to database
                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String email = edtEmail.getText().toString();
                        String name = edtName.getText().toString();
                        String phone = edtPhone.getText().toString();

                        User user = new User(id, email, name, phone);

//                        System.out.println(user.getName());
//                        System.out.println(user.getEmail());
//                        System.out.println(user.getPassword());
//                        System.out.println(user.getPhone());

                        // Use UID as the unique key
                        users.child(id)
                                .setValue(user)
                                .addOnSuccessListener(aVoid -> {
                                    //WELCOME TO ALINE
                                    Snackbar.make(rootLayout, "Welcome to ALINE!", Snackbar.LENGTH_SHORT)
                                            .show();
                                })
                                .addOnFailureListener(e -> Snackbar.make(rootLayout, "Failed" + e.getMessage(), Snackbar.LENGTH_LONG)
                                        .show());

                    });
//                    .addOnFailureListener(e -> Snackbar.make(rootLayout, "Failed" + e.getMessage(), Snackbar.LENGTH_SHORT)
//                            .show());
        });
        // Set the Cancel button
        dialog.setNegativeButton("CANCEL", (dialog1, which) -> dialog1.dismiss());

        dialog.show();

    }
}


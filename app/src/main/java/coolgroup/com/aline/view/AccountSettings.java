package coolgroup.com.aline.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import coolgroup.com.aline.R;
import coolgroup.com.aline.view.Update.Status;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettings extends AppCompatActivity {

    //**************FIREBASE******************//
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    //**************FIREBASE******************//

    // Account Settings Layout
    private CircleImageView mAccountImage;
    private TextView mAccountName;
    private TextView mAccountStatus;
    private TextView mAccountPhone;
    private TextView mAccountEmail;

    private Button mChangeImage;
    private Button mUpdateStatus;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAccountImage = findViewById(R.id.account_image);
        mAccountName = findViewById(R.id.account_name);
        mAccountStatus = findViewById(R.id.account_status);
        mAccountEmail = findViewById(R.id.account_email);
        mAccountPhone = findViewById(R.id.account_phone);

        mChangeImage = findViewById(R.id.btn_change_image);
        mUpdateStatus = findViewById(R.id.btn_update_status);

        mToolbar = findViewById(R.id.profile_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account");

        //**************FIREBASE******************//
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        //***************************************//

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String thumbnail = dataSnapshot.child("thumbnail").getValue().toString();

                mAccountName.setText(name);
                mAccountStatus.setText(status);
                mAccountEmail.setText(email);
                mAccountPhone.setText(phone);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statusIntent = new Intent(AccountSettings.this, Status.class);
                startActivity(statusIntent);
            }
        });

    }
}

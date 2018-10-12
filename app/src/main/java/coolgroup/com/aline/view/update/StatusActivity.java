package coolgroup.com.aline.view.update;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import coolgroup.com.aline.R;
import coolgroup.com.aline.view.options.AccountSettingsActivity;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private MaterialEditText mStatus;
    private Button mSave;

    //****************Firebase***************//
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;
    //**************************************//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        //**********************Firebase********************//
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUID = mCurrentUser.getUid();

        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("AllUsersActivity").child(currentUID);
        //**************************************************//

        mToolbar = (Toolbar) findViewById(R.id.status_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Update Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String statusValue = getIntent().getStringExtra("statusValue");
        mStatus = (MaterialEditText) findViewById(R.id.input_status);
        mSave = (Button) findViewById(R.id.btn_save_status);

        mStatus.setText(statusValue);

        mSave.setOnClickListener(v -> {

            String status = mStatus.getText().toString();
            mStatusDatabase.child("status").setValue(status)
                    .addOnSuccessListener(aVoid -> {
                        backToParentActivity();
                    })
                    .addOnFailureListener(e -> Toast.makeText(StatusActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
        });

    }

    // Send user back to parent page
    private void backToParentActivity() {
        Intent parentIntent = new Intent(StatusActivity.this, AccountSettingsActivity.class);
        startActivity(parentIntent);
        finish();
    }
}

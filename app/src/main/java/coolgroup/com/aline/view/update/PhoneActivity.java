package coolgroup.com.aline.view.update;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

import coolgroup.com.aline.R;
import coolgroup.com.aline.view.options.AccountSettingsActivity;

public class PhoneActivity extends AppCompatActivity {

    private MaterialEditText mPhone;

    //****************Firebase***************//
    private DatabaseReference mPhoneDatabase;
    //**************************************//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        //**********************Firebase********************//
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mCurrentUser != null;
        String currentUID = mCurrentUser.getUid();

        mPhoneDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUID);
        //**************************************************//

        Toolbar mToolbar = (Toolbar) findViewById(R.id.phone_appbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Update Phone Number");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String phoneValue = getIntent().getStringExtra("phoneValue");
        mPhone = (MaterialEditText) findViewById(R.id.input_phone);
        Button mSave = (Button) findViewById(R.id.btn_save_phone);

        mPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        mPhone.setText(phoneValue);

        mSave.setOnClickListener(v -> {

            String phone = mPhone.getText().toString();
            mPhoneDatabase.child("phone").setValue(phone)
                    .addOnSuccessListener(aVoid -> backToParentActivity())
                    .addOnFailureListener(e -> Toast.makeText(PhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
        });

    }

    // Send user back to parent page
    private void backToParentActivity() {
        Intent parentIntent = new Intent(PhoneActivity.this, AccountSettingsActivity.class);
        startActivity(parentIntent);
        finish();
    }
}

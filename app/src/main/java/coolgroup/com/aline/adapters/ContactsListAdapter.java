package coolgroup.com.aline.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import coolgroup.com.aline.Controller;
import coolgroup.com.aline.R;

public class ContactsListAdapter extends FirebaseListAdapter<String> {

    private static final DatabaseReference CONTACTS_REF =
            FirebaseDatabase.getInstance().getReference("Contacts");
    private static final DatabaseReference USERS_REF =
            FirebaseDatabase.getInstance().getReference("Users");

    public ContactsListAdapter(Activity activity) {
        super(activity,
              String.class,
              android.R.layout.simple_list_item_1,
              CONTACTS_REF.child(Controller.getInstance().getMainUser().id)
        );
    }

    @Override
    protected void populateView(View v, String model, int position) {
        TextView textView = v.findViewById(android.R.id.text1);

        // set the list item TextView to the name of the contact
        USERS_REF.child(model).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {  // name corresponding to contact user ID found
                    textView.setText(dataSnapshot.getValue(String.class));
                    textView.setTextColor(
                            ContextCompat.getColor(v.getContext(), R.color.contactTextColor));

                } else {  // name corresponding to contact user ID not found
                    textView.setText(R.string.name_no_record);
                    textView.setTextColor(
                            ContextCompat.getColor(v.getContext(), R.color.contactErrorTextColor));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

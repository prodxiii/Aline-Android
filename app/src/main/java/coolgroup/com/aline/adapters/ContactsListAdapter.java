package coolgroup.com.aline.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import coolgroup.com.aline.Controller;

public class ContactsListAdapter extends FirebaseListAdapter<String> {

    private static final DatabaseReference CONTACTS_REF =
            FirebaseDatabase.getInstance().getReference("Contacts");

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
        textView.setText(model);
    }

}

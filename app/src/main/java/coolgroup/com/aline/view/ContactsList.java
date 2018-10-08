package coolgroup.com.aline.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import coolgroup.com.aline.Controller;
import coolgroup.com.aline.R;
import coolgroup.com.aline.adapters.ContactsArrayAdapter;
import coolgroup.com.aline.adapters.ContactsListAdapter;

public class ContactsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "TODO: Add a new contact", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Context context = this;
        ListView lv = findViewById(R.id.lvContacts);
//        lv.setAdapter(new ContactsListAdapter(this));
        String mainUserId = Controller.getInstance().getMainUser().id;
        Controller.getInstance().serverCommunicator.getContactsList(mainUserId, new OnSuccessListener<List<String>>() {
            @Override
            public void onSuccess(List<String> contacts) {
                lv.setAdapter(new ContactsArrayAdapter(context, android.R.layout.simple_list_item_1, contacts));
            }
        });
    }

}

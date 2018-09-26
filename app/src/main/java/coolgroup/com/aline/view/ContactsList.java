package coolgroup.com.aline.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import coolgroup.com.aline.Controller;
import coolgroup.com.aline.R;
import coolgroup.com.aline.adapters.ContactsListAdapter;
import coolgroup.com.aline.model.User;
import coolgroup.com.aline.viewmodels.ContactsListViewModel;

public class ContactsList extends AppCompatActivity {

    private ContactsListViewModel viewModel;
    private ContactsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        User mainUser = Controller.getInstance().getMainUser();
        String mainUserId;
        if (mainUser == null)
            mainUserId = "ERROR";
        else
            mainUserId = mainUser.id;

        List<String> contactIds = Controller.getInstance().serverCommunicator.getContactsList(mainUserId);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Main User: " + mainUserId + " Contacts: " + contactIds.size(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        viewModel = ViewModelProviders.of(this).get(ContactsListViewModel.class);
//        viewModel.getContacts().observe(this, user -> updateContactsListUI());

        adapter = new ContactsListAdapter(this, android.R.layout.simple_list_item_1, contactIds);

        ListView lv = findViewById(R.id.lvContacts);
        lv.setAdapter(adapter);
    }

    private void updateContactsListUI() {
        //TODO: Fill me out
    }
}

package coolgroup.com.aline.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

public class ContactsListAdapter extends ArrayAdapter<String> {

    public ContactsListAdapter(@NonNull Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

}

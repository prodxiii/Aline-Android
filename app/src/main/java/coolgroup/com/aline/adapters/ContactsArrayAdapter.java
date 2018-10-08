package coolgroup.com.aline.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

public class ContactsArrayAdapter extends ArrayAdapter<String> {

    public ContactsArrayAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

}

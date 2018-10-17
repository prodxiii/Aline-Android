package coolgroup.com.aline.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import coolgroup.com.aline.fragments.chat.Chats;
import coolgroup.com.aline.fragments.chat.Contacts;
import coolgroup.com.aline.fragments.chat.Requests;

public class SectionsPagerAdapter extends FragmentPagerAdapter {


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Chats chatsFragment = new Chats();
                return chatsFragment;

            case 1:
                Contacts contactsFragment = new Contacts();
                return contactsFragment;

            case 2:
                Requests requestsFragment = new Requests();
                return requestsFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "CHATS";

            case 1:
                return "CONTACTS";

            case 2:
                return "REQUESTS";

            default:
                return null;
        }

    }

}

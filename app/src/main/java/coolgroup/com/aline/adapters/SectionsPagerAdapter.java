package coolgroup.com.aline.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import coolgroup.com.aline.fragments.chat.Chats;
import coolgroup.com.aline.fragments.chat.Contacts;
import coolgroup.com.aline.fragments.chat.Requests;

/**
 * Created by AkshayeJH on 11/06/17.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter{


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                Requests requestsFragment = new Requests();
                return requestsFragment;

            case 1:
                Chats chatsFragment = new Chats();
                return  chatsFragment;

            case 2:
                Contacts contactsFragment = new Contacts();
                return contactsFragment;

            default:
                return  null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){

        switch (position) {
            case 0:
                return "REQUESTS";

            case 1:
                return "CHATS";

            case 2:
                return "CONTACTS";

            default:
                return null;
        }

    }

}

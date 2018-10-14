package coolgroup.com.aline.model;

import android.os.Looper;
import android.support.test.InstrumentationRegistry;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class FirebaseCommunicatorTest {

    private DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");
    private DatabaseReference contacts = FirebaseDatabase.getInstance().getReference("Contacts");
    private FirebaseCommunicator communicator = new FirebaseCommunicator();

    @BeforeClass
    public static void setUpClass() {
        Looper.prepare();
    }

    @Before
    public void setUp() {
        // configure db with dummy test users
        users.child("TEST_1").child("name").setValue("Test 1");
        users.child("TEST_1").child("email").setValue("1@test.com");
        users.child("TEST_1").child("phone").setValue("0400000001");

        users.child("TEST_2").child("name").setValue("Test 2");
        users.child("TEST_2").child("email").setValue("2@test.com");
        users.child("TEST_2").child("phone").setValue("0400000002");

        users.child("TEST_3").child("name").setValue("Test 3");
        users.child("TEST_3").child("email").setValue("3@test.com");
        users.child("TEST_3").child("phone").setValue("0400000003");

        users.child("TEST_4").child("name").setValue("Test 4");
        users.child("TEST_4").child("email").setValue("4@test.com");
        users.child("TEST_4").child("phone").setValue("0400000004");

        // add dummy contacts
        contacts.child("TEST_1").push().setValue("TEST_2");
        contacts.child("TEST_1").push().setValue("TEST_3");
    }

    @After
    public void tearDown() {
        // remove dummy test users from db
        users.child("TEST_1").removeValue();
        users.child("TEST_2").removeValue();
        users.child("TEST_3").removeValue();

        // remove dummy contacts
        contacts.child("TEST_1").removeValue();
    }

    @Test
    public void getBasicUserInfo() {
        communicator.getBasicUserInfo("TEST_1", user -> {
            assertEquals("TEST_1", user.id);
            assertEquals("1@test.com", user.email);
            assertEquals("Test 1", user.name);
            assertEquals("0400000001", user.phone);
        });
    }

    @Test
    public void getBasicUserInfoTest2() {
        communicator.getBasicUserInfo("INVALID_USER", user -> {
            assertEquals(null, user);
        });
    }

    @Test
    public void getContactsList() {
        communicator.getContactsList("TEST_1", contacts -> {
            assertEquals(2, contacts.size());
            assertTrue(contacts.contains("TEST_2"));
            assertTrue(contacts.contains("TEST_3"));
        });
    }

    @Test
    public void getContactsUserList() {
        communicator.getContactsUserList("TEST_1", users -> assertEquals(2, users.size()));
    }

    @Test
    public void getContactsUserListTest2() {
        communicator.getContactsUserList("INVALID_USER", users -> assertEquals(0, users.size()));
    }

    @Test
    public void addContact() {
        communicator.addContact("USER_1", "USER_4");
        communicator.getContactsList("USER_1", contacts -> assertEquals(3, contacts.size()));
    }

    @Test
    public void removeContact() {
        communicator.removeContact("USER_1", "USER_2");
        communicator.getContactsList("USER_1", contacts -> assertEquals(1, contacts.size()));
    }

    @Test
    public void removeContactTest2() {
        communicator.removeContact("USER_1", "USER_2");
        communicator.removeContact("USER_1", "USER_3");
        communicator.getContactsList("USER_1", contacts -> assertEquals(0, contacts.size()));
    }

    @Test
    public void removeContactTest3() {
        communicator.removeContact("USER_1", "NON_CONTACT");
        communicator.getContactsList("USER_1", contacts -> assertEquals(2, contacts.size()));
    }

    @Test
    public void logInUserEmail() {
        communicator.logInUserEmail("AUTH_TEST", "AUTH_PASSWORD", user -> {
            assertEquals("AUTH_NAME", user.name);
            assertEquals("AUTH_TEST", user.id);
            assertEquals("auth@test.com", user.email);
            assertEquals("0400000000", user.phone);
        });
    }

}
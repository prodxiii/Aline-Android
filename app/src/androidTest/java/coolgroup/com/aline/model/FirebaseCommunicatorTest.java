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

import static junit.framework.TestCase.assertEquals;

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
            assertEquals(user.id, "TEST_1");
            assertEquals(user.email, "1@test.com");
            assertEquals(user.name, "Test 1");
            assertEquals(user.phone, "0400000001");
        });
    }

    @Test
    public void getContactsList() {
    }

    @Test
    public void getContactsUserList() {
    }

    @Test
    public void addContact() {
    }

    @Test
    public void removeContact() {
    }
}
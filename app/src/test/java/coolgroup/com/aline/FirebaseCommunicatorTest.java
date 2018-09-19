package coolgroup.com.aline;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

import coolgroup.com.aline.Model.FirebaseCommunicator;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseDatabase.class, FirebaseAuth.class}) // this allows static classes to be mocked.
public class FirebaseCommunicatorTest {

    private DatabaseReference mockedDatabaseReference;
    private FirebaseAuth mockedFirebaseAuth;

    private FirebaseCommunicator testFirebaseCommunicator;

    @Before
    public void SetUp() throws Exception {
        // This code demonstrating how to mock Firebase (incl. use of PowerMockito) courtesy of
        // https://stackoverflow.com/questions/43225804/junit-testing-in-android-studio-with-firebase
        mockedDatabaseReference = Mockito.mock(DatabaseReference.class);
        mockedFirebaseAuth = Mockito.mock(FirebaseAuth.class);

        FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);

        // Mock static values for Firebase
        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockedFirebaseAuth);
        // These references may seem useless, but mockStatic will hold onto the references for them.

        // Finally, create an instance of the thing we're testing.

        testFirebaseCommunicator = new FirebaseCommunicator();
    }

    @After
    public void TearDown() throws Exception {
        mockedDatabaseReference = null;
        mockedFirebaseAuth = null;

        testFirebaseCommunicator = null;
    }

    // ======== SIGNUP TESTS ========

    // testing signUpUser(String email, String password, String name, String phone);

    @Test
    public void testValidRegistration(){

        String validEmail = "oldperson@needsnavigational.help";
        String validPassword = "oldpersonpassword";
        String name = "Julius Caesar";
        String validPhoneNumber = "0444444444";

        Task<AuthResult> taskToReturn = new Task<AuthResult>() {
            @Override
            public boolean isComplete() {
                return true;
            }

            @Override
            public boolean isSuccessful() {
                return true;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public AuthResult getResult() {
                return null;
            }

            @Override
            public <X extends Throwable> AuthResult getResult(@NonNull Class<X> aClass) throws X {
                return null;
            }

            @Nullable
            @Override
            public Exception getException() {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
                return null;
            }
        };
        Task<AuthResult> taskFromFirebaseCommunicator;

        // specify the particulars for the FirebaseAuth for this test
        Mockito.when(mockedFirebaseAuth.createUserWithEmailAndPassword(validEmail, validPassword)).thenReturn(taskToReturn);

        // Perform the test
        taskFromFirebaseCommunicator = testFirebaseCommunicator.signUpUser(validEmail, validPassword, name, validPhoneNumber);

        // now check the test ran successfully
        PowerMockito.verifyStatic();

        Mockito.verify(mockedFirebaseAuth.createUserWithEmailAndPassword(validEmail, validPassword));

        assertEquals(taskToReturn, taskFromFirebaseCommunicator);


    }

    // ======== LOGIN TESTS ========

    // testing logInUserEmail(String email, String password);

    // This test will try to login with a valid email and password.
    @Test
    public void testValidEmailLogin() {
        String validEmail = "oldperson@needsnavigational.help";
        String validPassword = "oldpersonpassword";

        Task<AuthResult> taskToReturn = new Task<AuthResult>() {
            @Override
            public boolean isComplete() {
                return true;
            }

            @Override
            public boolean isSuccessful() {
                return true;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public AuthResult getResult() {
                return null;
            }

            @Override
            public <X extends Throwable> AuthResult getResult(@NonNull Class<X> aClass) throws X {
                return null;
            }

            @Nullable
            @Override
            public Exception getException() {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
                return null;
            }
        };
        Task<AuthResult> taskFromFirebaseCommunicator;

        // specify the particulars for the FirebaseAuth for this test
        Mockito.when(mockedFirebaseAuth.signInWithEmailAndPassword(validEmail,
                validPassword)).thenReturn(taskToReturn);

        // Perform the test
        taskFromFirebaseCommunicator = testFirebaseCommunicator.logInUserEmail(validEmail, validPassword);

        // now check the test ran successfully
        PowerMockito.verifyStatic();

        Mockito.verify(mockedFirebaseAuth.signInWithEmailAndPassword(validEmail, validPassword));

        assertEquals(taskToReturn, taskFromFirebaseCommunicator);

    }

    // testing logInUserPhone(String phone, String password);

    // This test will try to login with a valid phone number and password.
    @Test
    public void testValidPhoneLogin() {
        String validPassword = "oldpersonpassword";
        String validPhoneNumber = "0444444444";

        Task<AuthResult> taskToReturn = new Task<AuthResult>() {
            @Override
            public boolean isComplete() {
                return true;
            }

            @Override
            public boolean isSuccessful() {
                return true;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public AuthResult getResult() {
                return null;
            }

            @Override
            public <X extends Throwable> AuthResult getResult(@NonNull Class<X> aClass) throws X {
                return null;
            }

            @Nullable
            @Override
            public Exception getException() {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
                return null;
            }
        };
        Task<AuthResult> taskFromFirebaseCommunicator;

        taskFromFirebaseCommunicator = testFirebaseCommunicator.logInUserPhone(validPhoneNumber, validPassword);

        assertEquals(taskToReturn, taskFromFirebaseCommunicator);
    }

    // ======== CONTACTS TESTS ========

    // testing getUserId(String email, String name, String phone);

    @Test
    public void testValidRequestForUserID(){

    }

    // testing getBasicUserInfo(String userId);

    @Test
    public void testGetUserDetailsFromValidUserID(){

    }

    // testing addNewContact(String userId, String contactUserId);

    @Test
    public void TestAddNewValidContact(){

    }

    // Testing removeContact(String userId, String contactUserId);

    @Test
    public void TestRemoveValidContact(){

    }

    @Test
    public void TestRemoveAbsentContact(){

    }


}

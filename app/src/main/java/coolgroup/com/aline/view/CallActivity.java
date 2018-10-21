package coolgroup.com.aline.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import coolgroup.com.aline.Controller;
import coolgroup.com.aline.model.User;
import coolgroup.com.aline.model.iVOIPCommunicator;

public class CallActivity extends AppCompatActivity {

    public static final String otherUserID = "otherUser";
    private static final int REQUEST_PERMISSION_RECORD_AUDIO = 55;
    iVOIPCommunicator voipCommunicator;
    private User otherUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        voipCommunicator = Controller.getInstance().getiVOIPCommunicator();
        if (voipCommunicator == null) {
            Log.d("CallActivity", "voipCommunicator is null! oh no!");
        } else {
            Log.d("CallActivity", "voipCommunicator is not null B)");
        }

        otherUser = (User) getIntent().getSerializableExtra(otherUserID);
        Log.d("CallActivity", "user ID: " + otherUser.getuID());
        if (otherUser == null) {
            Log.e("CallActivity", "otherUser is null! oh no!");
        } else {
            Log.d("CallActivity", "otherUser is not null.");
        }

        startCallWithPermissionsCheck();

    }

    private void startCallWithPermissionsCheck() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted;

            // request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_PERMISSION_RECORD_AUDIO);
        } else {
            // We've already got permission.
            voipCommunicator.startCall(otherUser);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted! Make the call. B|
                    voipCommunicator.startCall(otherUser);
                }  // permission denied. I guess we just go back to the map?

            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}

package coolgroup.com.aline.Model;

import android.telecom.Call;

import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;

public class SinchCommunicator implements iVOIPCommunicator {

    private SinchClient mSinchClient;

    public SinchCommunicator(String currentUserID){

        mSinchClient = Sinch.getSinchClientBuilder()
                .build();
    }

    @Override
    public Call startCall(User user) {
        return null;
    }
}

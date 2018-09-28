package coolgroup.com.aline.Model;

import android.content.Context;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;


import java.util.List;

public class SinchCommunicator implements iVOIPCommunicator {

    private SinchClient mSinchClient;
    private Call currentCall = null;

    // TODO: fill out these strings in a meaningful way
    private String mApplicationKey;
    private String mApplicationSecret;
    private String mEnvironmentHost;

    //TODO: allow other classes to subscribe to certain events - hanging up, etc.

    public SinchCommunicator(Context context, User currentUser){

        //TODO: use userID instead of user's phone number as a unique identifier.
        mSinchClient = Sinch.getSinchClientBuilder()
                .context(context)
                .userId(currentUser.getPhone()) // <-- here
                .applicationKey(mApplicationKey)
                .applicationSecret(mApplicationSecret)
                .environmentHost(mEnvironmentHost)
                .build();

        mSinchClient.setSupportCalling(true);
        mSinchClient.startListeningOnActiveConnection();

        mSinchClient.start();

        mSinchClient.getCallClient().addCallClientListener(new CallClientListener() {
            @Override
            public void onIncomingCall(CallClient callClient, Call incomingCall) {
                currentCall = incomingCall;
                currentCall.answer();

                currentCall.addCallListener( new MCallListener() );
            }
        });
    }

    @Override
    public void startCall(User user) {
        hangUpCall();

        currentCall = mSinchClient.getCallClient().callUser(user.getPhone());

        currentCall.addCallListener( new MCallListener() );
    }

    @Override
    public void hangUpCall(){
        if (currentCall == null) {
            return;
        }
        else {
            currentCall.hangup();
            currentCall = null;
        }
    }

    private class MCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            currentCall = null;
        }
        @Override
        public void onCallEstablished(Call establishedCall) {

        }
        @Override
        public void onCallProgressing(Call progressingCall) {
            //call is ringing
        }
        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            //don't worry about this right now
        }
    }
}

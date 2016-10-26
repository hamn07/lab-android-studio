package tk.pichannel.viewer.auth;

import android.accounts.Account;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import tk.pichannel.viewer.R;

public class AuthenticatorService extends Service {

    // Instance field that stores the authenticator( object
    private Authenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mAuthenticator.getIBinder();
    }
}

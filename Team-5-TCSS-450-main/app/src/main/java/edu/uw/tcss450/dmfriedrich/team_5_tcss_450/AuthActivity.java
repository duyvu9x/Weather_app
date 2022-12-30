package edu.uw.tcss450.dmfriedrich.team_5_tcss_450;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.model.PushyTokenViewModel;
import me.pushy.sdk.Pushy;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        //If it is not already running, start the Pushy listening service
        Pushy.listen(this);
        initiatePushyTokenRequest();




    }

    private void initiatePushyTokenRequest() {
        new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken();
    }
}

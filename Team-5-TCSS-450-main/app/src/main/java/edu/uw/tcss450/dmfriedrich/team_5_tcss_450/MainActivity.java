package edu.uw.tcss450.dmfriedrich.team_5_tcss_450;

import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.databinding.ActivityMainBinding;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.model.UserInfoViewModel;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.location.MapViewModel;

public class MainActivity extends AppCompatActivity {

    //For location
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // A constant int for the permissions request code. Must be a 16 bit number
    private static final int MY_PERMISSIONS_LOCATIONS = 8414;

    private LocationRequest mLocationRequest;

    //Use a FusedLocationProviderClient to request the location
    private FusedLocationProviderClient mFusedLocationClient;

    // Will use this call back to decide what to do when a location change is detected
    private LocationCallback mLocationCallback;

    //The ViewModel that will store the current location
    private MapViewModel mLocationModel;

    //End of location


    Button notify;

    AppBarConfiguration mAppBarConfiguration;

    private ActivityMainBinding binding;

    private UserInfoViewModel userInfo;



    private EditText title;
    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());

        userInfo = new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(args.getEmail(), args.getJwt())
        ).get(UserInfoViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_room_list, R.id.navigation_weather)

                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_LOCATIONS);
        } else {
            //The user has already allowed the use of Locations. Get the current location.
            requestLocation();
        }
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    Log.d("LOCATION UPDATE!", location.toString());
                    if (mLocationModel == null) {
                        mLocationModel = new ViewModelProvider(MainActivity.this)
                                .get(MapViewModel.class);
                    }
                    mLocationModel.setLocation(location);
                }
            };
        };


        createLocationRequest();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // locations-related task you need to do.
                    requestLocation();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("PERMISSION DENIED", "Nothing to see or do here.");

                    //Shut down the app. In production release, you would let the user
                    //know why the app is shutting down...maybe ask for permission again?
                    finishAndRemoveTask();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d("REQUEST LOCATION", "User did NOT allow permission to request location!");
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.d("LOCATION", location.toString());
                                if (mLocationModel == null) {
                                    mLocationModel = new ViewModelProvider(MainActivity.this)
                                            .get(MapViewModel.class);
                                }
                                mLocationModel.setLocation(location);
                            } else {
                                Log.d("LOCATION", "No Location retrieved. Hopefully this is" +
                                        " resolved in a later step");
                            }
                        }
                    });
        }
    }
    /**
     * Requests location updates from the FusedLocationApi.
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    /**
     * Create and configure a Location Request used when retrieving location updates
     */
    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drop_down, menu);
        return true;
    }


/*
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);


            // Create an explicit intent for an Activity in your app
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("My notification")
                    .setContentText("Hello World!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);



            notificationManager.notify(importance, builder.build());
        }
    } */


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sign_out) {
            //TODO SIGN OUT
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();
        //End the app completely
        finishAndRemoveTask();
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    public UserInfoViewModel getUserInfo() {
        return userInfo;
    }
}
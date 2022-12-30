package edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.location;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

public class MapViewModel extends AndroidViewModel {
    private MutableLiveData<Location> mLocation;
    private String zip;

    public MapViewModel(@NonNull Application application) {
        super(application);
        mLocation = new MediatorLiveData<>();
    }

    public void addLocationObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super Location> observer) {
        mLocation.observe(owner, observer);
    }
    public void setLocation(final Location location) {
        if (!location.equals(mLocation.getValue())) {
            mLocation.setValue(location);
        }
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Location getCurrentLocation() {
        return new Location(mLocation.getValue());
    }
}

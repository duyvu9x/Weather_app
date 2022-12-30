package edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.weather;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
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

public class WeatherViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse24;
    private MutableLiveData<JSONObject> mResponseCurrent;
    private MutableLiveData<JSONObject> mResponseDaily;


    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mResponse24 = new MutableLiveData<>();
        mResponse24.setValue(new JSONObject());
        mResponseCurrent = new MutableLiveData<>();
        mResponseCurrent.setValue(new JSONObject());
        mResponseDaily = new MutableLiveData<>();
        mResponseDaily.setValue(new JSONObject());
    }
    public void addResponseObserver24(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse24.observe(owner, observer);
    }
    public void addResponseObserverCurrent(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponseCurrent.observe(owner, observer);
    }
    public void addResponseObserverDaily(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponseDaily.observe(owner, observer);
    }
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse24.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
                mResponseCurrent.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
                mResponseDaily.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                JSONObject response = new JSONObject();
                response.put("code", error.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
                mResponse24.setValue(response);
                mResponseCurrent.setValue(response);
                mResponseDaily.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    public void connect(final double lat, final double longi,final String zip) {
        // String url = "https://cfb3-tcss450-labs-2021sp.herokuapp.com/auth";
        String url = "https://rmonto6-tcss450-project-auth.herokuapp.com/weather";

        JSONObject body = new JSONObject();
        try {
            body.put("lat", lat);
            body.put("long", longi);
            body.put("zip", zip);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponseDaily::setValue,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }
    public void connect2(final double lat, final double longi,final String zip) {
        // String url = "https://cfb3-tcss450-labs-2021sp.herokuapp.com/auth";
        String url = "https://rmonto6-tcss450-project-auth.herokuapp.com/weather_hourly";


        JSONObject body = new JSONObject();
        try {
            body.put("lat", lat);
            body.put("long", longi);
            body.put("zip", zip);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse24::setValue,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);

    }

    public void connect3(final double lat, final double longi,final String zip) {
        // String url = "https://cfb3-tcss450-labs-2021sp.herokuapp.com/auth";
        String url = "https://rmonto6-tcss450-project-auth.herokuapp.com/weather_current";

        JSONObject body = new JSONObject();
        try {
            body.put("lat", lat);
            body.put("long", longi);
            body.put("zip", zip);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponseCurrent::setValue,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }


}

package edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.account;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.R;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.io.RequestQueueSingleton;

public class AccountViewModel extends AndroidViewModel {

    private Map<String, MutableLiveData<List<AccountProfile>>> mContacts;
    private MutableLiveData<JSONObject> mResponse;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        mContacts = new HashMap<>();
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    public void addContactObserver(String email,
                                   @NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super List<AccountProfile>> observer) {
        getOrCreateMapEntry(email).observe(owner, observer);
    }

    private MutableLiveData<List<AccountProfile>> getOrCreateMapEntry(String email) {
        if(!mContacts.containsKey(email)) {
            mContacts.put(email, new MutableLiveData<>(new ArrayList<>()));
        }
        return mContacts.get(email);
    }

    public List<AccountProfile> getContactListByEmail(final String email) {
        return getOrCreateMapEntry(email).getValue();
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    public void sendContact(final String username_A, final String jwt, final String username_B) {
        String url = getApplication().getResources().getString(R.string.base_url_service) +
                "contacts";

        JSONObject body = new JSONObject();
        try {
            body.put("username_A", username_A);
            body.put("username_B", username_B);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body, //push token found in the JSONObject body
                mResponse::setValue, // we get a response but do nothing with it
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    public void getContacts(final String username, final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url_service) +
                "contacts";

        JSONObject body = new JSONObject();
        try {
            body.put("username_A", username);
            //body.put("username_B", "test2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                body, //no body for this get request
                this::handleSuccess,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);

        //code here will run
    }


    // Contact here should be the current account email used as the key to find their list.
    // Email in the account profile is the email of the person the user is connected to.
    // Type is Accepted, Requested, or No Request as denied should remove a request.
    private void handleSuccess(final JSONObject response) {
        List<AccountProfile> list;
        if (!response.has("contacts")) {
            throw new IllegalStateException("Unexpected response in AccountViewModel: " + response);
        }
        try {
            list = getContactListByEmail(response.getString("nickname"));
            JSONArray contacts = response.getJSONArray("rows");
            for(int i = 0; i < contacts.length(); i++) {
                JSONObject contact = contacts.getJSONObject(i);
                AccountProfile cProfile = new AccountProfile(
                        contact.getString("nickname"),
                        contact.getInt("type")
                );
                if (!list.contains(cProfile)) {
                    // don't add a duplicate
                    list.add(0, cProfile);
                } else {
                    // this shouldn't happen but could with the asynchronous
                    // nature of the application
                    Log.wtf("Chat message already received",
                            "Or duplicate id:" + cProfile.getEmail());
                }

            }
            //inform observers of the change (setValue)
            getOrCreateMapEntry(response.getString("contact")).setValue(list);
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ChatViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
    }

    public void addContact(final String email, final AccountProfile contact) {
        List<AccountProfile> list = getContactListByEmail(email);
        list.add(contact);
        getOrCreateMapEntry(email).setValue(list);
    }

}

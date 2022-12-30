package edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.account;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AccountProfile implements Serializable {

    private String email;
    private String nickname;
    private int status;

    public AccountProfile(String nickname, int status) {
        this.email = email;
        this.nickname = nickname;
        this.status = status;
    }

    public static AccountProfile createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject cnt = new JSONObject(cmAsJson);
        return new AccountProfile(
                cnt.getString("nickname"),
                cnt.getInt("status"));
    }

    public String getEmail() {
        return email;
    }
    public String getNickname() {
        return nickname;
    }
    public int getStatus() {
        return status;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof AccountProfile) {
            result = email.equals(((AccountProfile) other).email);
        }
        return result;
    }
}

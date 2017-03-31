package ru.skafcats.hackathon2017.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.skafcats.hackathon2017.R;

/**
 * Created by vasidmi on 31/03/2017.
 */

public class LoginFragment extends android.app.Fragment {

    public static final String TAG = "login";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_layout, container, false);
    }
}

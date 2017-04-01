package ru.skafcats.hackathon2017.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.skafcats.hackathon2017.R;

/**
 * Created by vasidmi on 01/04/2017.
 */


public class PinFragment extends Fragment {

    public static final String TAG = "pin";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pin_enter, container, false);
    }
}

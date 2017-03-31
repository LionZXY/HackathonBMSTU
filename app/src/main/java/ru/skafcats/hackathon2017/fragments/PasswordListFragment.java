package ru.skafcats.hackathon2017.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.skafcats.hackathon2017.R;

/**
 * Created by vasidmi on 31/03/2017.
 */

public class PasswordListFragment extends Fragment {

    public static final String TAG = "items";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.items_list_fragment, container, false);
    }
}

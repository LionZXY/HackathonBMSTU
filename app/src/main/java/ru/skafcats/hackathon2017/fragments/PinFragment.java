package ru.skafcats.hackathon2017.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ru.skafcats.crypto.CryptoApi;
import ru.skafcats.hackathon2017.R;

/**
 * Created by Nikita Kulikov on 01.04.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class PinFragment extends Fragment {
    public static final String TAG = "pin";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_pin, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        final EditText pinEdit = (EditText) getView().findViewById(R.id.pin_edit);
        pinEdit.setTransformationMethod(new PasswordTransformationMethod());

        int[] ids = {R.id.num0, R.id.num1, R.id.num2, R.id.num3, R.id.num4, R.id.num5, R.id.num6, R.id.num7, R.id.num8, R.id.num9};
        AppCompatButton[] buttons = new AppCompatButton[10];
        for (int i = 0; i < 10; i++) {
            final int num = i;
            buttons[i] = (AppCompatButton) getView().findViewById(ids[i]);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pinEdit.append(String.valueOf(num));
                }
            });
        }

        AppCompatImageButton pinDelete = (AppCompatImageButton) getView().findViewById(R.id.pin_delete);
        pinDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinEdit.setText(pinEdit.getText().subSequence(0, pinEdit.getText().length() - 1));
            }
        });

        AppCompatImageButton pinComplete = (AppCompatImageButton) getView().findViewById(R.id.complete_pin);
        pinComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getFragmentManager().beginTransaction().replace(R.id.main_fragment, new LoginFragment(), LoginFragment.TAG).commit();
                    CryptoApi.setMasterPassword(getActivity(), pinEdit.getText().toString());
                } catch (Exception e) {
                }
            }
        });
    }
}

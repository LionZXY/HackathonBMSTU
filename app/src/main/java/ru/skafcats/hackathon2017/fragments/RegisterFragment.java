package ru.skafcats.hackathon2017.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import ru.skafcats.crypto.CryptoApi;
import ru.skafcats.crypto.interfaces.OnLogin;
import ru.skafcats.hackathon2017.R;

/**
 * Created by Nikita Kulikov on 01.04.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class RegisterFragment extends Fragment {

    public static final String TAG = "register";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_layout, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getView().findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = (EditText) getView().findViewById(R.id.input_email);
                EditText password = (EditText) getView().findViewById(R.id.input_password);
                EditText repeat = (EditText) getView().findViewById(R.id.input_password_repeate);
                if (repeat.getText().equals(password))
                    register(email.getText().toString(), password.getText().toString());
                else
                    Toast.makeText(v.getContext(), "Пароли должны совпадать", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void register(String email, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Авторизация...");
        progressDialog.show();
        CryptoApi.getInstance(getContext(), null).register(email, password, new OnLogin() {
            @Override
            public void onResponse(boolean isAuth, String message) {
                if (!isAuth) {
                    progressDialog.setMessage(message);
                    progressDialog.setButton(1, "Попробывать еще раз", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Успешно", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    //TODO open main fragment
                }
            }
        });
    }
}

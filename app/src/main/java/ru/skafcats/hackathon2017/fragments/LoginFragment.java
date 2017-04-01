package ru.skafcats.hackathon2017.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.skafcats.crypto.CryptoApi;
import ru.skafcats.crypto.interfaces.OnLogin;
import ru.skafcats.hackathon2017.R;

/**
 * Created by Nikita Kulikov on 01.04.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class LoginFragment extends Fragment {

    public static final String TAG = "login";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_layout, container, false);
    }

    public void login(String email, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Авторизация...");
        progressDialog.show();
        CryptoApi.getInstance(getContext(), null).login(email, password, new OnLogin() {
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
                    //TODO open menu
                }
            }
        });
    }
}

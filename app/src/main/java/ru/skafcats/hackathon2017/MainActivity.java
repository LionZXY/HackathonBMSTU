package ru.skafcats.hackathon2017;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import ru.skafcats.crypto.CryptoApi;
import ru.skafcats.crypto.enums.Constants;
import ru.skafcats.crypto.interfaces.ITaskAnswerListener;
import ru.skafcats.crypto.services.MultiResultReciever;

public class MainActivity extends AppCompatActivity implements ITaskAnswerListener{
    String password = "1234";
    Toolbar mToolbar;
    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CryptoApi cryptoApi = CryptoApi.getInstance(this, "1234");
        //cryptoApi.register("nikita@kulikof.ru",password, null);
        //cryptoApi.addInfo(new SecureInfo("test2"));
//        cryptoApi.login("nikita@kulikof.ru", password, null);
//        cryptoApi.sync();
    }

    @Override
    public void onAnswer(Bundle data) {
        int code = data.getInt(Constants.KEY_RESULT_CODE, MultiResultReciever.CODE_RESULT_ERROR_TASK);
        Log.i("TEST", code + ' ' + data.getString(Constants.KEY_RESPONSE));
    }
}

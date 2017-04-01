package ru.skafcats.hackathon2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import ru.skafcats.crypto.CryptoApi;
import ru.skafcats.crypto.enums.Constants;
import ru.skafcats.crypto.interfaces.ITaskAnswerListener;
import ru.skafcats.crypto.services.MultiResultReciever;
import ru.skafcats.hackathon2017.face_unlock.FaceUnlockActivity;
import ru.skafcats.hackathon2017.pin_unlock.PinActivity;

public class MainActivity extends AppCompatActivity implements ITaskAnswerListener{
    String password = "1234";
    Toolbar mToolbar;
    FragmentManager mFragmentManager;


    private long pinCode;
    private long photoCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent pinIntent = new Intent(this, PinActivity.class);
        startActivityForResult(pinIntent, 1);

        Intent photoIntent = new Intent(this, FaceUnlockActivity.class);
        startActivityForResult(photoIntent, 2);
    }

    @Override
    public void onAnswer(Bundle data) {
        int code = data.getInt(Constants.KEY_RESULT_CODE, MultiResultReciever.CODE_RESULT_ERROR_TASK);
        Log.i("TEST", code + ' ' + data.getString(Constants.KEY_RESPONSE));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                pinCode = data.getLongExtra("code", 0);
                Log.i("Hack2017", "pinCode: " + pinCode);
                break;

            case 2:
                photoCode = data.getLongExtra("code", 0);
                Log.i("Hack2017", "photoCode: " + photoCode);

//                CryptoApi cryptoApi = CryptoApi.getInstance(this, "" + getCode());
                // TODO
                //cryptoApi.register("nikita@kulikof.ru",password, null);
                //cryptoApi.addInfo(new SecureInfo("test2"));
//                cryptoApi.login("nikita@kulikof.ru", password, null);
//                cryptoApi.sync();

                Log.i("Hack2017", "code: " + getCode());

                Intent intent = new Intent(this, SecureInfoActivity.class);
                startActivity(intent);

                break;
        }
    }

    private long getCode() {
        return photoCode << 16 + pinCode;
    }
}

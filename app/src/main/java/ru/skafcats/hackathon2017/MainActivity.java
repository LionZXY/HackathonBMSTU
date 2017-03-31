package ru.skafcats.hackathon2017;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import ru.skafcats.hackathon2017.models.InfoAboutSecureInfo;
import ru.skafcats.hackathon2017.models.SecureInfo;
import ru.skafcats.hackathon2017.storage.MySecureSharedPreference;

public class MainActivity extends AppCompatActivity {
    String password = "1234";
    Toolbar mToolbar;
    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.main_fragment, new PasswordListFragment(), PasswordListFragment.TAG).commit();*/

        SecureInfo secureInfo = new SecureInfo("login");
        secureInfo.addField("some", "some");
        InfoAboutSecureInfo infoAboutSecureInfo = new InfoAboutSecureInfo(secureInfo);
        new MySecureSharedPreference(this, password, secureInfo).setSecureInfo(secureInfo);
        secureInfo = new MySecureSharedPreference(this, password, infoAboutSecureInfo).getSecureInfo();
        Log.i("TEST", secureInfo.getName());
        Log.i("TEST", secureInfo.getByKey("some"));
    }
}

package ru.skafcats.hackathon2017;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ru.skafcats.hackathon2017.fragments.PasswordListFragment;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.main_fragment, new PasswordListFragment(), PasswordListFragment.TAG).commit();
    }
}

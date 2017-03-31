package ru.skafcats.hackathon2017;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ru.skafcats.hackathon2017.fragments.PasswordListFragment;
import ru.skafcats.hackathon2017.navigation.NavigationDrawer;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    android.app.FragmentManager mFragmentManager;
    NavigationDrawer mNavigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mNavigationDrawer = new NavigationDrawer(this, mToolbar);
        mFragmentManager = getFragmentManager();
        if (mFragmentManager.findFragmentById(R.id.main_fragment) == null) {
            mFragmentManager.beginTransaction().add(R.id.main_fragment, new PasswordListFragment(), PasswordListFragment.TAG).commit();
        }
    }
}

package ru.skafcats.hackathon2017;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.skafcats.hackathon2017.fragments.PinFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager mFragmentManager = getFragmentManager();
        if (mFragmentManager.findFragmentById(R.id.main_fragment) == null) {
            mFragmentManager.beginTransaction().add(R.id.main_fragment, new PinFragment(), PinFragment.TAG).commit();
        }

    }
}

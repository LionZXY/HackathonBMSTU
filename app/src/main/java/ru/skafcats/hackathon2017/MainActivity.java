package ru.skafcats.hackathon2017;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import io.realm.RealmResults;
import ru.skafcats.crypto.CryptoApi;
import ru.skafcats.crypto.models.InfoAboutSecureInfo;
import ru.skafcats.hackathon2017.adapters.ItemsAdapter;
import ru.skafcats.hackathon2017.fragments.PinFragment;
import ru.skafcats.hackathon2017.interfaces.ICodeReturner;

public class MainActivity extends AppCompatActivity implements ICodeReturner {
    private CryptoApi api = null;
    private RecyclerView mRecyclerView;
    private ItemsAdapter mItemsAdapter;
    private RealmResults<InfoAboutSecureInfo> data;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!CryptoApi.getInstance(this, null).checkPassword()) {
            FragmentManager mFragmentManager = getFragmentManager();
            if (mFragmentManager.findFragmentById(R.id.main_fragment) == null) {
                mFragmentManager.beginTransaction().add(R.id.main_fragment, new PinFragment(), PinFragment.TAG).commit();
            }
        } else {
            FragmentManager mFragmentManager = getFragmentManager();
            if (mFragmentManager.findFragmentById(R.id.main_fragment) == null) {
                mFragmentManager.beginTransaction().add(R.id.main_fragment, new PinFragment().subscribeToPin(this), PinFragment.TAG).commit();
            }
        }

    }

    @Override
    public void onCodeReturn(String master) {
        api = CryptoApi.getInstance(this, master);
        View view = View.inflate(this, R.layout.items_list_fragment, null);
        ViewGroup parent = (ViewGroup) findViewById(R.id.main_fragment);
        parent.removeAllViews();
        parent.addView(view);

        data = api.getInfo();

        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mItemsAdapter = new ItemsAdapter(view.getContext(), data);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.items_list);
        mFab.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mItemsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


    }
}

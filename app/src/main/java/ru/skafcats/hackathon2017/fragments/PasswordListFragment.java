package ru.skafcats.hackathon2017.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import ru.skafcats.hackathon2017.R;
import ru.skafcats.hackathon2017.adapters.ItemsAdapter;
import ru.skafcats.hackathon2017.models.ItemModel;

/**
 * Created by vasidmi on 31/03/2017.
 */

public class PasswordListFragment extends Fragment {

    public static final String TAG = "items";
    RecyclerView mRecyclerView;
    ItemsAdapter mItemsAdapter;
    ArrayList<ItemModel> data;
    FloatingActionButton mFab;
    Context mContext;
    private int last_id = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.items_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = view.getContext();
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        data = new ArrayList<ItemModel>();
        for (int i = 0; i < 100; i++) {
            data.add(i, new ItemModel("Vkontakte", "Durov", "V"));
        }
        mItemsAdapter = new ItemsAdapter(view.getContext(), data);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.items_list);
        mFab.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mItemsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.main_fragment, new PinFragment(), PinFragment.TAG);
        mFragmentTransaction.commit();
    }
}

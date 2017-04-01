package ru.skafcats.hackathon2017.navigation;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import ru.skafcats.hackathon2017.R;
import ru.skafcats.hackathon2017.fragments.LoginFragment;
import ru.skafcats.hackathon2017.fragments.PasswordListFragment;
import ru.skafcats.hackathon2017.fragments.PinFragment;

/**
 * Created by vasidmi on 31/03/2017.
 */

public class NavigationDrawer {


    Toolbar mToolbar;
    Drawer mDrawer;
    PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(0).withName("Все данные");
    PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(1).withName("Логин");
    PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(2).withName("PIN");

    android.app.FragmentManager mFragmentManager;

    public NavigationDrawer(final Activity activity, final Toolbar toolbar) {

        mToolbar = toolbar;
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .build();

        mDrawer = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        item2
//                        new DividerDrawerItem()
//                        item3
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case 0:
                                mFragmentManager = activity.getFragmentManager();
                                if (mFragmentManager.findFragmentByTag(PasswordListFragment.TAG) == null) {
                                    mFragmentManager.beginTransaction().replace(R.id.main_fragment, new PasswordListFragment(), PasswordListFragment.TAG).commit();
                                    toolbar.setTitle("Все данные");
                                }
                                break;
                            case 1:
                                mFragmentManager = activity.getFragmentManager();
                                if (mFragmentManager.findFragmentByTag(LoginFragment.TAG) == null) {
                                    mFragmentManager.beginTransaction().replace(R.id.main_fragment, new LoginFragment(), LoginFragment.TAG).commit();
                                    toolbar.setTitle("Вход");
                                }
                                break;
                            case 2:
                                mFragmentManager = activity.getFragmentManager();
                                if (mFragmentManager.findFragmentByTag(PinFragment.TAG) == null) {
                                    mFragmentManager.beginTransaction().replace(R.id.main_fragment, new PinFragment(), PinFragment.TAG).commit();
                                    toolbar.setTitle("Введите PIN");
                                }
                                break;
                        }
                        mDrawer.closeDrawer();
                        return true;
                    }
                })
                .build();
    }
}
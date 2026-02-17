package lk.jiat.eshop.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import lk.jiat.eshop.R;
import lk.jiat.eshop.fragment.CartFragment;
import lk.jiat.eshop.fragment.CategoryFragment;
import lk.jiat.eshop.fragment.HomeFragment;
import lk.jiat.eshop.fragment.LoginFragment;
import lk.jiat.eshop.fragment.LogoutFragment;
import lk.jiat.eshop.fragment.MessageFragment;
import lk.jiat.eshop.fragment.OrdersFragment;
import lk.jiat.eshop.fragment.ProfileFragment;
import lk.jiat.eshop.fragment.SettingsFragment;
import lk.jiat.eshop.fragment.WishListFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {

    DrawerLayout drawerLayout;
    MaterialToolbar toolbar;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.side_navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnItemSelectedListener(this);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            navigationView.getMenu().findItem(R.id.side_nav_home);
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        navigationView.setCheckedItem(-1);

        if (itemId == R.id.side_nav_home || itemId == R.id.bottom_nav_home) {
            loadFragment(new HomeFragment());
//            navigationView.setCheckedItem(R.id.side_nav_home);
            navigationView.getMenu().findItem(R.id.side_nav_home).setChecked(true);
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
        } else if (itemId == R.id.side_nav_profile || itemId == R.id.bottom_nav_profile) {
            loadFragment(new ProfileFragment());
//            navigationView.setCheckedItem(R.id.side_nav_profile);
            navigationView.getMenu().findItem(R.id.side_nav_profile).setChecked(true);
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_profile).setChecked(true);
        } else if (itemId == R.id.side_nav_orders) {
            loadFragment(new OrdersFragment());
//            navigationView.setCheckedItem(R.id.side_nav_orders);
            navigationView.getMenu().findItem(R.id.side_nav_orders).setChecked(true);
        } else if (itemId == R.id.side_nav_wishlist) {
            loadFragment(new WishListFragment());
//            navigationView.setCheckedItem(R.id.side_nav_wishlist);
            navigationView.getMenu().findItem(R.id.side_nav_wishlist).setChecked(true);
        } else if (itemId == R.id.side_nav_cart || itemId == R.id.bottom_nav_cart) {
            loadFragment(new CartFragment());
//            navigationView.setCheckedItem(R.id.side_nav_cart);
            navigationView.getMenu().findItem(R.id.side_nav_cart);
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_cart).setChecked(true);
        } else if (itemId == R.id.side_nav_message) {
            loadFragment(new MessageFragment());
//            navigationView.setCheckedItem(R.id.side_nav_message);
            navigationView.getMenu().findItem(R.id.side_nav_message).setChecked(true);
        } else if (itemId == R.id.side_nav_settings) {
            loadFragment(new SettingsFragment());
//            navigationView.setCheckedItem(R.id.side_nav_settings);
            navigationView.getMenu().findItem(R.id.side_nav_settings).setChecked(true);
        } else if (itemId == R.id.bottom_nav_category) {
            loadFragment(new CategoryFragment());
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_category).setChecked(true);
        } else if (itemId == R.id.side_nav_login) {
            loadFragment(new LoginFragment());
//            navigationView.setCheckedItem(R.id.side_nav_login);
            navigationView.getMenu().findItem(R.id.side_nav_login).setChecked(true);
        } else if (itemId == R.id.side_nav_logout) {
            loadFragment(new LogoutFragment());
//            navigationView.setCheckedItem(R.id.side_nav_logout);
            navigationView.getMenu().findItem(R.id.side_nav_logout).setChecked(true);
        }

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        return false;
    }

    public void loadFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.fragmentContainer, fragment);
//        transaction.commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit(); //oneLine code instead of 4 lines
    }

}
package lk.jiat.eshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import lk.jiat.eshop.R;
import lk.jiat.eshop.databinding.ActivityMainBinding;
import lk.jiat.eshop.databinding.SideNavHeaderBinding;
import lk.jiat.eshop.fragment.CartFragment;
import lk.jiat.eshop.fragment.CategoryFragment;
import lk.jiat.eshop.fragment.HomeFragment;
import lk.jiat.eshop.fragment.MessageFragment;
import lk.jiat.eshop.fragment.OrdersFragment;
import lk.jiat.eshop.fragment.ProfileFragment;
import lk.jiat.eshop.fragment.SettingsFragment;
import lk.jiat.eshop.fragment.WishListFragment;
import lk.jiat.eshop.model.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {

    private ActivityMainBinding binding;
    private SideNavHeaderBinding sideNavHeaderBinding;
    DrawerLayout drawerLayout;
    MaterialToolbar toolbar;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        View headerView = binding.sideNavigationView.getHeaderView(0);
        sideNavHeaderBinding = SideNavHeaderBinding.bind(headerView);

        drawerLayout = binding.drawerLayout;
        toolbar = binding.toolbar;
        navigationView = binding.sideNavigationView;
        bottomNavigationView = binding.bottomNavigationView;

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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //check load user details
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            firebaseFirestore.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);
                            sideNavHeaderBinding.sideNavUsername.setText(user.getName());
                            sideNavHeaderBinding.sideNavUserEmail.setText(user.getEmail());
                            Glide.with(MainActivity.this)
                                    .load(user.getProfilePicUrl())
                                    .circleCrop()
                                    .into(sideNavHeaderBinding.sideNavProfilePic);
                        }
                    }).addOnFailureListener(e -> {
                        Log.e("Firestore", "Error details: " + e.getMessage());
                    });

            navigationView.getMenu().findItem(R.id.side_nav_login).setVisible(false);

            navigationView.getMenu().findItem(R.id.side_nav_profile).setVisible(true);
            navigationView.getMenu().findItem(R.id.side_nav_orders).setVisible(true);
            navigationView.getMenu().findItem(R.id.side_nav_wishlist).setVisible(true);
            navigationView.getMenu().findItem(R.id.side_nav_cart).setVisible(true);
            navigationView.getMenu().findItem(R.id.side_nav_message).setVisible(true);
            navigationView.getMenu().findItem(R.id.side_nav_orders).setVisible(true);
            navigationView.getMenu().findItem(R.id.side_nav_logout).setVisible(true);

        } else {
            Log.e("Firestore", "Document is null");
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        navigationView.setCheckedItem(-1);

        Menu navMenu = navigationView.getMenu();
        Menu bottomNav = bottomNavigationView.getMenu();

        for (int i = 0; i < navMenu.size(); i++) {
            navMenu.getItem(i).setChecked(false);
        }

        for (int i = 0; i < bottomNav.size(); i++) {
            bottomNav.getItem(i).setChecked(false);
        }

        if (itemId == R.id.side_nav_home || itemId == R.id.bottom_nav_home) {
            loadFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.side_nav_home);
//            navigationView.getMenu().findItem(R.id.side_nav_home).setChecked(true);
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
        } else if (itemId == R.id.side_nav_profile || itemId == R.id.bottom_nav_profile) {
            if (firebaseAuth.getCurrentUser() == null) {
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
            }
            loadFragment(new ProfileFragment());
            navigationView.setCheckedItem(R.id.side_nav_profile);
//            navigationView.getMenu().findItem(R.id.side_nav_profile).setChecked(true);
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_profile).setChecked(true);
        } else if (itemId == R.id.side_nav_orders) {
            loadFragment(new OrdersFragment());
            navigationView.setCheckedItem(R.id.side_nav_orders);
//            navigationView.getMenu().findItem(R.id.side_nav_orders).setChecked(true);
        } else if (itemId == R.id.side_nav_wishlist) {
            loadFragment(new WishListFragment());
            navigationView.setCheckedItem(R.id.side_nav_wishlist);
//            navigationView.getMenu().findItem(R.id.side_nav_wishlist).setChecked(true);
        } else if (itemId == R.id.side_nav_cart || itemId == R.id.bottom_nav_cart) {
            if (firebaseAuth.getCurrentUser() == null) {
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
            }
            loadFragment(new CartFragment());
            navigationView.setCheckedItem(R.id.side_nav_cart);
//            navigationView.getMenu().findItem(R.id.side_nav_cart).setChecked(true);
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_cart).setChecked(true);
        } else if (itemId == R.id.side_nav_message) {
            loadFragment(new MessageFragment());
            navigationView.setCheckedItem(R.id.side_nav_message);
//            navigationView.getMenu().findItem(R.id.side_nav_message).setChecked(true);
        } else if (itemId == R.id.side_nav_settings) {
            loadFragment(new SettingsFragment());
            navigationView.setCheckedItem(R.id.side_nav_settings);
//            navigationView.getMenu().findItem(R.id.side_nav_settings).setChecked(true);
        } else if (itemId == R.id.bottom_nav_category) {
            loadFragment(new CategoryFragment());
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_category).setChecked(true);
        } else if (itemId == R.id.side_nav_login) {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
        } else if (itemId == R.id.side_nav_logout) {
            firebaseAuth.signOut();
            loadFragment(new HomeFragment());
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.side_nav_menu);

            navigationView.removeHeaderView(sideNavHeaderBinding.getRoot());
            navigationView.inflateHeaderView(R.layout.side_nav_header);
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
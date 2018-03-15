package com.davidgh.mults.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.claudiodegio.msv.MaterialSearchView;
import com.claudiodegio.msv.OnSearchViewListener;
import com.davidgh.mults.R;
import com.davidgh.mults.adapters.ViewPagerAdapter;
import com.davidgh.mults.fragments.MainDashboardFragment;
import com.davidgh.mults.fragments.MainHomeFragment;
import com.davidgh.mults.fragments.MainNotificationFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // TODO   : 03/05/2018
    // TODO 2 : IMPORTANT, Add Comment Layout, and the functionality
    // TODO 3 : IMPORTANT, Add Settings fragment in ProfileActivity

    // TODO   : 03/06/2018
    // TODO 5 : IMPORTANT, Add 1 Data into database;

    // Firebase
    private FirebaseAuth mAuth;

    // Android
    private DrawerLayout mDrawer;
    private NavigationView mNav;
    private MaterialSearchView mSearchView;


    // Android Layout
    private TextView profileEmail, profileName;
    private CircleImageView profileImg;
    private LinearLayout profileLayout;
    private ViewPager mViewPagerMain;
    private BottomNavigationView mBottomNavigation;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase
        mAuth = FirebaseAuth.getInstance();

        Toolbar mToolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        mViewPagerMain = (ViewPager) findViewById(R.id.view_pager_main);

        // Drawer Layout Functionality
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();


        mBottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        mViewPagerMain.setCurrentItem(0);
                        break;
                    case R.id.navigation_dashboard:
                        mViewPagerMain.setCurrentItem(1);
                        break;
                    case R.id.navigation_notifications:
                        mViewPagerMain.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });

        mViewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (null != menuItem)
                    menuItem.setChecked(false);
                else
                    mBottomNavigation.getMenu().getItem(0).setChecked(false);
                mBottomNavigation.getMenu().getItem(position).setChecked(true);
                menuItem = mBottomNavigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager();

        mNav = (NavigationView) findViewById(R.id.nav_view);
        mNav.setNavigationItemSelectedListener(this);
        LinearLayout header = (LinearLayout) mNav.getHeaderView(0);

        profileLayout = (LinearLayout) header.findViewById(R.id.ll_profile);
        profileImg = (CircleImageView) header.findViewById(R.id.profile_img);
        profileName = (TextView) header.findViewById(R.id.profile_name);
        profileEmail = (TextView) header.findViewById(R.id.profile_mail);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                // Animation
                Pair [] pairs = new Pair[4];
                pairs[0] = new Pair<View, String>(profileLayout, "llTransition");
                pairs[1] = new Pair<View, String>(profileImg, "imageTransition");
                pairs[2] = new Pair<View, String>(profileName, "nameTransition");
                pairs[3] = new Pair<View, String>(profileEmail, "emailTransition");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                startActivity(profileIntent, options.toBundle());
            }
        });


    // Search View Here

        mSearchView = (MaterialSearchView) findViewById(R.id.sv);
        mSearchView.setOnSearchViewListener(new OnSearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public void onQueryTextChange(String s) {

            }
        }); // this class implements OnSearchViewListener
    }

    private void setupViewPager() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new MainHomeFragment());
        adapter.addFragment(new MainDashboardFragment());
        adapter.addFragment(new MainNotificationFragment());

        mViewPagerMain.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Firebase
        if (null == currentUser) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                mDatabase.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        profileName.setText(dataSnapshot.child("username").getValue().toString());
                        profileEmail.setText(dataSnapshot.child("email").getValue().toString());
                        Picasso.with(getApplicationContext()).load(dataSnapshot.child("image").getValue().toString()).into(profileImg);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        } // End If
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        mSearchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_watchlist) {
            Intent watchlistIntent = new Intent(getApplicationContext(), WatchlistActivity.class);
            startActivity(watchlistIntent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout){
            mAuth.signOut();
            goToStart();
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToStart() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}

package com.davidgh.mults.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.claudiodegio.msv.MaterialSearchView;
import com.claudiodegio.msv.OnSearchViewListener;
import com.davidgh.mults.R;
import com.davidgh.mults.adapters.RecyclerViewDataAdapter;
import com.davidgh.mults.helpers.CommonSettings;
import com.davidgh.mults.helpers.NetworkUtils;
import com.davidgh.mults.models.Mult;
import com.davidgh.mults.models.SectionMultsModel;
import com.davidgh.mults.models.SingleMultModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Firebase
    private FirebaseAuth mAuth;

    // Android
    private DrawerLayout mDrawer;
    private NavigationView mNav;
    private MaterialSearchView mSearchView;
    private ArrayList<SectionMultsModel> allSampleData;

    // Adapter
    private RecyclerViewDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase
        mAuth = FirebaseAuth.getInstance();

        Toolbar mToolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        // Drawer Layout Functionality
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNav = (NavigationView) findViewById(R.id.nav_view);
        mNav.setNavigationItemSelectedListener(this);
        LinearLayout header = (LinearLayout) mNav.getHeaderView(0);

        final LinearLayout profileLayout = (LinearLayout) header.findViewById(R.id.ll_profile);
        final CircleImageView profileImg = (CircleImageView) header.findViewById(R.id.profile_img);
        final TextView profileName = (TextView) header.findViewById(R.id.profile_name);
        final TextView profileEmail = (TextView) header.findViewById(R.id.profile_mail);

        if (null != mAuth.getCurrentUser()) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
            mDatabase.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    profileName.setText(dataSnapshot.child("username").getValue().toString());
                    profileEmail.setText(dataSnapshot.child("email").getValue().toString());
                    Picasso.with(MainActivity.this).load(dataSnapshot.child("image").getValue().toString()).into(profileImg);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } // end if

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

        allSampleData = new ArrayList<>();

        createDummyData();

        RecyclerView rvMain = (RecyclerView) findViewById(R.id.rv_main);
        rvMain.setHasFixedSize(true);
        adapter = new RecyclerViewDataAdapter(allSampleData, this);
        rvMain.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvMain.setAdapter(adapter);
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
        }
    }

    private void createDummyData(){
        if (NetworkUtils.isNetworkAvailable(this)){
            new GetMults().execute(CommonSettings.API_ALL_MULTS);
        } else{
            // TODO : handel internet
        }
        // {now, popular, comming soon}
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
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

    private class GetMults extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String stream = null;
            String urlString = urls[0];

            NetworkUtils networkUtils = new NetworkUtils();

            stream = networkUtils.getHttpData(urlString);

            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Gson gson = new Gson();
            Type type = new TypeToken<Mult>(){}.getType();

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("mults");

                for (int i = 0; i < 3; i++) {
                    SectionMultsModel mults = new SectionMultsModel();
                    mults.setHeader(CommonSettings.getMultHeader(i));
                    ArrayList<SingleMultModel> singleMultModels = new ArrayList<>();

                    for (int j = i * 17; j < (i+1) * 17; j++) {

                        JSONObject multObject = jsonArray.getJSONObject(j);
                        Mult m = gson.fromJson(String.valueOf(multObject), type);

                        singleMultModels.add(new SingleMultModel(m.getName(), m.getUrl(), j)); // TODO make rating float, and add second string
                    }

                    mults.setAllMultsInSection(singleMultModels);
                    allSampleData.add(mults);
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

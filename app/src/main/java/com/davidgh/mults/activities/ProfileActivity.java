package com.davidgh.mults.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.davidgh.mults.R;
import com.davidgh.mults.adapters.ViewPagerAdapter;
import com.davidgh.mults.fragments.ProfileLikeFragment;
import com.davidgh.mults.fragments.ProfileSettingsFragment;
import com.davidgh.mults.fragments.ProfileWatchlistFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    public static final int GALLERY_PICK = 1;

    // Firebase
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private StorageReference mStorage;

    // Android Layout
    private TextView mName, mEmail;
    private CircleImageView mProfileImage;
    private BottomNavigationView mNav;

    // Fragments;
    private ViewPager mViewPager;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Android Layout
        mName = (TextView) findViewById(R.id.user_name);
        mEmail = (TextView) findViewById(R.id.user_email);
        mProfileImage = (CircleImageView) findViewById(R.id.user_image);
        mNav = (BottomNavigationView) findViewById(R.id.profile_navigation);

        // Fragments
        mViewPager = (ViewPager) findViewById(R.id.profile_pager);
        mNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile_like:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.profile_watching:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.profile_settings:
                        mViewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (null != menuItem)
                    menuItem.setChecked(false);
                else
                    mNav.getMenu().getItem(0).setChecked(false);
                mNav.getMenu().getItem(position).setChecked(true);
                menuItem = mNav.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Firebase
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mStorage = FirebaseStorage.getInstance().getReference().child("profile_images");

        mDatabase.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mName.setText(dataSnapshot.child("username").getValue().toString());
                mEmail.setText(dataSnapshot.child("email").getValue().toString());
                Picasso.with(ProfileActivity.this).load(dataSnapshot.child("image").getValue().toString()).into(mProfileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setupViewPager();

        // Change user Image
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), GALLERY_PICK);

            }
        });
    }

    private void setupViewPager() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new ProfileLikeFragment());
        adapter.addFragment(new ProfileWatchlistFragment());
        adapter.addFragment(new ProfileSettingsFragment());

        mViewPager.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();


            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);

        } else {
            // TODO: handel this situation
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();


                StorageReference filePath = mStorage.child(mUser.getUid()  + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){

                            mDatabase.child(mUser.getUid()).child("image").setValue(task.getResult().getDownloadUrl().toString()); // TODO; Add On Complete Listener
                            Toast.makeText(ProfileActivity.this, "Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                            
                        } else {
                            Toast.makeText(ProfileActivity.this, "Could Not Upload Image!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } else {
                //Exception error = result.getError();
            }

        } // End if
    }

}

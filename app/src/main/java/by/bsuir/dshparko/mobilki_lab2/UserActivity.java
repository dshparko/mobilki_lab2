package by.bsuir.dshparko.mobilki_lab2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

public class UserActivity extends AppCompatActivity  implements LocationListener {


    TextView name, email;
    ImageView userPhoto;
    LocationManager mLocationManager;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Profile");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            setContentView(R.layout.activity_user);
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();
            name = findViewById(R.id.userName);
            userPhoto = findViewById(R.id.userPhoto);
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Glide.with(getApplicationContext())
                  .load(personPhoto)
                    .into(userPhoto);

            email = findViewById(R.id.post);
            name.setText(personName);
            email.setText(personEmail);


            supportMapFragment =(SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.maps);


            client = LocationServices.getFusedLocationProviderClient(this);
            if(ActivityCompat.checkSelfPermission(UserActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }else{
                ActivityCompat.requestPermissions(UserActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            }

        } else {
            Intent intent1 = new Intent(this, SignInActivity.class);
            startActivity(intent1);
        }

        @SuppressLint("ResourceType") TabLayout toolbar;
        toolbar = findViewById(R.id.toolbar);
        toolbar.selectTab(toolbar.getTabAt(1),true);
        toolbar = findViewById(R.id.toolbar);

        toolbar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:

                        Intent intent2 = new Intent(UserActivity.this, HomeActivity.class);
                        startActivity(intent2);
                        break;
                    case 1:

                        Intent intent1 = new Intent(UserActivity.this, UserActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:

                        Intent intent3 = new Intent(UserActivity.this, SearchActivity.class);
                        startActivity(intent3);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

       if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    public void getCurrentLocation() {
        @SuppressLint("MissingPermission")
        Task<Location> task = client.getLastLocation(); //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(task+"!!!!!!!!!!!!!!!!!!!");

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {System.out.println(location+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                if (location != null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions options = new MarkerOptions().position(latLng).title("I'm right here!");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                            googleMap.addMarker(options).showInfoWindow();


                        }
                    });
                }
            }


        });
    }

    public void signOut(View view){
        Intent intent1 = new Intent(this, SignOutActivity.class);
        startActivity(intent1);
    }

    public void showInformationAboutCreator(View view) {

        String info = "lab2\nCreator: Darya Shparko, 951005 ";

        final AlertDialog aboutDialog = new AlertDialog.Builder(
                UserActivity.this).setMessage(info)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).create();

        aboutDialog.show();

        ((TextView) aboutDialog.findViewById(android.R.id.message))
                .setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}

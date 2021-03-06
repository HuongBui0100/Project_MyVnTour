package com.example.project_myvntour.ActivityMaintain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.project_myvntour.Adapter.AdapterLoaiKhachSanj;
import com.example.project_myvntour.Adapter.AdapterNearFromYou;
import com.example.project_myvntour.Database.SelectAll;
import com.example.project_myvntour.Mode.KhachSan;
import com.example.project_myvntour.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class NearFromYouActivity2 extends AppCompatActivity implements AdapterNearFromYou.Listernaer , OnMapReadyCallback {
    private List<KhachSan> list;
    private RecyclerView recyclerView;
    private AdapterNearFromYou adapterNearFromYou;
    private MaterialToolbar toolBar2;
    public static final int code = 100;
    private MarkerOptions markerOptions;
    LatLng currentUserLocation, searchPointLocation;
    Marker currentUser, searchPoint;
    private GoogleMap mMap;
    private double a , b ,tong22 ,tong44;
    private List<KhachSan> listKhachSan2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_from_you2);
        recyclerView = findViewById(R.id.recyclerview);


        toolBar2 = (MaterialToolbar) findViewById(R.id.tool_bar2);
        setSupportActionBar(toolBar2);
        toolBar2.setNavigationIcon(R.drawable.ic_baseline_close_24);
        toolBar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("G???n t??i nh???t");

        SelectAll selectAll = new SelectAll(this);
        list= selectAll.getListKhachSanByHotel(AdapterLoaiKhachSanj.index);
        getListGanNhat(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterNearFromYou = new AdapterNearFromYou(this,this);
        adapterNearFromYou.setData(listKhachSan2);
        recyclerView.setAdapter(adapterNearFromYou);
    }

    @Override
    public void onClick(View v, int position) {
        KhachSan khach = listKhachSan2.get(position);
        Intent intent = new Intent(NearFromYouActivity2.this , InFoKhachSanActivity.class);
        intent.putExtra("khachsan" , khach);
        startActivity(intent);

    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions( new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, code);
            }
            return;
        }
        mMap.setMyLocationEnabled(true);
        getCurrentLocation();// h??m l???y v??? tr?? ch??? m??nh ??ang ?????ng

        //t???o s??? ki???n click v??o button

    }
    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this.getBaseContext(), "H??y c???p quy???n truy c???p GPS cho ???ng d???ng", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(15)
                    .bearing(0)
                    .tilt(40)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // v??? maker
            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            currentUserLocation = myLocation;
            markerOptions = new MarkerOptions()
                    .position(myLocation)
                    .title("Your position at here")
                    .snippet("Hello MyVnTour Company")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            currentUser = mMap.addMarker(markerOptions);
            currentUser.setTag(false);

        } else {
            Toast.makeText(this.getBaseContext(), "Kh??ng l???y ???????c th??ng tin ?????nh v???, h??y b???t GPS v?? b???m n??t ?????nh v??? tr??n b???n ?????", Toast.LENGTH_LONG).show();
        }
    }
    public List<KhachSan> getListGanNhat(List<KhachSan> list){
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getActivity().getBaseContext(), "H??y c???p quy???n truy c???p GPS cho ???ng d???ng", Toast.LENGTH_SHORT).show();
//            return;
//        }
        listKhachSan2 = new ArrayList<>();
        LocationManager locationManager = (LocationManager)this. getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if(location != null){
            for (KhachSan khach: list
            ) {
                a = location.getLatitude();
                b = location.getLongitude();
                tong22 = ((khach.getKinhdo() - a)*(khach.getKinhdo() - a)) + ((khach.getVido() - b)*(khach.getVido() - b));
                tong44 = Math.sqrt(tong22);
                if(tong44 <= 1000){
                    listKhachSan2.add(khach);
                }
            }
        }
        return  listKhachSan2;
    }
}
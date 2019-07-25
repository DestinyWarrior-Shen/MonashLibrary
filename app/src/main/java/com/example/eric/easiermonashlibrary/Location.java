package com.example.eric.easiermonashlibrary;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

/**
 * Created by Eric on 15/5/17.
 * A fragment which display the available library details information, and using google map
 * to display the location information.
 *
 * The coding in this class is partly reference to a YouTube tutorial: "Android Studio: Add Map to Fragment"
 * https://www.youtube.com/watch?v=0dToEEuPL9Y
 */
public class Location extends Fragment implements OnMapReadyCallback
{
    private TextView libraryName,campus,openingHours,contectNumber;
    private String location;

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;

    private static final LatLng HARGRAVE_ANDREW_LIBRARY
            = new LatLng(-37.909856,145.132136);
    private static final LatLng SIR_LOUIS_MATHESON_LIBRARY
            = new LatLng(-37.912931,145.134271);
    private static final LatLng CAULFIELD_LIBRARY
            = new LatLng(-37.877189,145.045342);

    /**
     * Register all UI widgets and create the view of this fragment.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_location, container, false);
        libraryName = (TextView) mView.findViewById(R.id.library_name);
        campus = (TextView) mView.findViewById(R.id.campus);
        openingHours = (TextView) mView.findViewById(R.id.openingHour);
        contectNumber = (TextView) mView.findViewById(R.id.contactNumber);

        libraryName.setText(location);

        if (location.equals("Caulfield Library"))
        {
            campus.setText("Caulfield Campus");
        }
        else if (location.equals("Hargrave Andrew Library") ||
                (location.equals("Sir Louis Matheson Library")))
        {
            campus.setText("Clayton Campus");
        }

        if (location.equals("Caulfield Library"))
        {
            openingHours.setText("8AM - 8PM");
            contectNumber.setText("03 9905 5054");
        }
        else if (location.equals("Hargrave Andrew Library"))
        {
            openingHours.setText("8AM - 12PM");
            contectNumber.setText("03 9921 3546");
        }
        else if (location.equals("Sir Louis Matheson Library"))
        {
            openingHours.setText("8AM - 6PM");
            contectNumber.setText("03 9954 1357");
        }

        return mView;
    }

    /**
     * Register the mapView, create the mapView and load the map asynchronously
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.mapview);
        if (mMapView != null)
        {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    /**
     * When after loading the map, this method setting the map type, zoomable, permission, marker,
     * camera position etc. According to the location, display the position on map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            mGoogleMap.setMyLocationEnabled(true);
        }
        else
        {
            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},12345);
            // Show rationale and request permission.
        }
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);


        if (location.equals("Caulfield Library"))
        {
            googleMap.addMarker(new MarkerOptions().position(CAULFIELD_LIBRARY).title("Caufield Library").snippet("Caulfield Campus, building A, Caulfield East VIC 3145"));
            CameraPosition caulfield = CameraPosition.builder().target(CAULFIELD_LIBRARY).zoom(17).bearing(0).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(caulfield));
        }
        else if (location.equals("Hargrave Andrew Library"))
        {
            googleMap.addMarker(new MarkerOptions().position(HARGRAVE_ANDREW_LIBRARY).title("Hargrave-Andrew Library").snippet("Clayton Campus, 13 College Walk, Clayton VIC 3800"));
            CameraPosition Hargrave = CameraPosition.builder().target(HARGRAVE_ANDREW_LIBRARY).zoom(17).bearing(0).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Hargrave));
        }
        else if (location.equals("Sir Louis Matheson Library"))
        {
            googleMap.addMarker(new MarkerOptions().position(SIR_LOUIS_MATHESON_LIBRARY).title("Sir Louis Matheson Library").snippet("Clayton Campus, 40 Exhibition Walk,Clayton VIC 3800"));
            CameraPosition Sir = CameraPosition.builder().target(SIR_LOUIS_MATHESON_LIBRARY).zoom(17).bearing(0).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Sir));
        }
    }

    /**
     * Request the permission and grant it.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch(requestCode)
        {
            case 12345:
            {
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    try
                    {
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                    catch (SecurityException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Please grant the permission", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * This method used for transfer the available location String from "PickUp_Request" fragment
     * to this fragment.
     */
    public void setItemToLocation(String location)
    {
        this.location = location;
    }
}

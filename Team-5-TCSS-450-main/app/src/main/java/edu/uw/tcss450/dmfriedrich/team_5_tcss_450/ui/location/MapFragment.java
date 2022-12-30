package edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.location;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.R;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.databinding.FragmentMapBinding;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.weather.WeatherFragmentDirections;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener  {
    private MapViewModel mMapModel;
    private FragmentMapBinding binding;
    private GoogleMap mMap;
    private Marker marker = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapModel = new ViewModelProvider(getActivity())
                .get(MapViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater);
        return binding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //add this fragment as the OnMapReadyCallback -> See onMapReady()
        mapFragment.getMapAsync(this);

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] splitSpace = binding.searchTextBox.getText().toString().split(" ");
                String[] splitComma = binding.searchTextBox.getText().toString().split(",");
                if(splitSpace.length == 2) {
                    if((splitSpace[0].matches("-\\d|-\\d\\d") && splitSpace[1].matches("-\\d|-\\d\\d"))||(splitSpace[0].matches("\\d|\\d\\d") && splitSpace[1].matches("\\d|\\d\\d"))||(splitSpace[0].matches("\\d|\\d\\d") && splitSpace[1].matches("-\\d|-\\d\\d"))||(splitSpace[0].matches("-\\d|-\\d\\d") && splitSpace[1].matches("\\d|\\d\\d"))) {
                        Location location = new Location(LocationManager.GPS_PROVIDER);
                        location.setLatitude(Integer.parseInt(splitSpace[0]));
                        location.setLongitude(Integer.parseInt(splitSpace[1]));
                        mMapModel.setZip(null);
                        Navigation.findNavController(getView()).navigate(
                                MapFragmentDirections.actionMapFargmentToNavigationWeather()
                        );
                        mMapModel.setLocation(location);
                    }
                }
                if (splitComma.length == 2){
                    if((splitComma[0].matches("-\\d|-\\d\\d") && splitComma[1].matches("-\\d|-\\d\\d"))||(splitComma[0].matches("\\d|\\d\\d") && splitComma[1].matches("\\d|\\d\\d"))||(splitComma[0].matches("\\d|\\d\\d") && splitComma[1].matches("-\\d|-\\d\\d"))||(splitComma[0].matches("-\\d|-\\d\\d") && splitComma[1].matches("\\d|\\d\\d"))) {
                        Location location = new Location(LocationManager.GPS_PROVIDER);
                        location.setLatitude(Integer.parseInt(splitComma[0]));
                        location.setLongitude(Integer.parseInt(splitComma[1]));
                        mMapModel.setZip(null);
                        Navigation.findNavController(getView()).navigate(
                                MapFragmentDirections.actionMapFargmentToNavigationWeather()
                        );
                        mMapModel.setLocation(location);

                    }
                }
                if (splitComma.length == 1&&splitSpace.length == 1){
                    if(splitComma[0].matches("\\d\\d\\d\\d\\d")) {
                        mMapModel.setZip(splitComma[0]);
                        Navigation.findNavController(getView()).navigate(
                                MapFragmentDirections.actionMapFargmentToNavigationWeather()
                        );
                    }
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MapViewModel model = new ViewModelProvider(getActivity())
                .get(MapViewModel.class);
        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            if(location != null) {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setMyLocationEnabled(true);

                final LatLng c = new LatLng(location.getLatitude(), location.getLongitude());
                //Zoom levels are from 2.0f (zoomed out) to 21.f (zoomed in)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 15.0f));
            }
        });
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Log.d("LAT/LONG", latLng.toString());
        if(marker != null) {
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("New Marker"));
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        latLng, mMap.getCameraPosition().zoom));
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        mMapModel.setZip(null);
        mMapModel.setLocation(location);
        Log.d("LOCATION UPDATE!", location.toString());

    }
}
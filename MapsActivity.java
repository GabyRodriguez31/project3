
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

        // Mover cámara
        LatLng lugar = new LatLng(44, 20);
        mapa.moveCamera(CameraUpdateFactory.newLatLng(lugar));
        mapa.moveCamera(CameraUpdateFactory.zoomTo(1));

        // Localizar el dispositivo en el mapa
        activarLocalizacion();

        // Poner un mardador
        Marker marcador = mapa.addMarker(new MarkerOptions()
                .position(lugar)
                .title("Mi lugar favorito")
                .snippet("El mejor de todos los lugares")
                .alpha(0.7f));

        mapa.setOnMapClickListener(this);
        mapa.setOnMarkerClickListener(this);

        geocode();
    }

    public void activarLocalizacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] persimos = { Manifest.permission.ACCESS_FINE_LOCATION };
            ActivityCompat.requestPermissions(this, persimos, 123);
            return;
        }
        mapa.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            activarLocalizacion();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Marker marcador = mapa.addMarker(new MarkerOptions().position(latLng));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.remove();
        return false;
    }

    public void geocode() {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> direcciones = geocoder.getFromLocationName("London", 1);
            if (direcciones.size() != 0) {
                Address direccion = direcciones.get(0);
                LatLng sitio = new LatLng(direccion.getLatitude(), direccion.getLongitude());
                mapa.addMarker(new MarkerOptions().position(sitio).title(direccion.getLocality()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

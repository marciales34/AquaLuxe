package com.snt.aqualuxe.Clientes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.R;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Polyline;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UbicacionClientes extends BarraDeNavegacion implements OnMapReadyCallback {

    private static final int CODIGO_PERMISO_UBICACION = 100;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Polyline currentPolyline; // Variable para mantener la línea actual
    private com.google.android.gms.maps.model.Marker currentMarker; // Variable para el marcador actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_ubicacion_clientes, findViewById(R.id.frameLayout));

        // Configuración del mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        solicitarPermisosUbicacion();

        Button btnUbicacion1 = findViewById(R.id.btn_ubicacion1);
        Button btnUbicacion2 = findViewById(R.id.btn_ubicacion2);

        LatLng punto1 = new LatLng(4.754465, -74.104234); // Punto 1
        LatLng punto2 = new LatLng(4.626889, -74.162223); // Punto 2

        btnUbicacion1.setOnClickListener(view -> {
            moverMapaAPunto(punto1);
            dibujarRuta(punto1);
        });

        btnUbicacion2.setOnClickListener(view -> {
            moverMapaAPunto(punto2);
            dibujarRuta(punto2);
        });
    }

    private void dibujarRuta(LatLng destino) {
        if (mMap != null && fusedLocationClient != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng origen = new LatLng(location.getLatitude(), location.getLongitude());
                    String url = obtenerURL(origen, destino);
                    new DescargarDatosRuta().execute(url);
                }
            });
        }
    }

    private String obtenerURL(LatLng origen, LatLng destino) {
        String strOrigen = "origin=" + origen.latitude + "," + origen.longitude;
        String strDestino = "destination=" + destino.latitude + "," + destino.longitude;
        String parametros = strOrigen + "&" + strDestino + "&sensor=false&mode=driving&key=AIzaSyCosTs9UHRx9srV6SIqqCFTRTproZibXQo";
        return "https://maps.googleapis.com/maps/api/directions/json?" + parametros;
    }

    private class DescargarDatosRuta extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String respuesta = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
                InputStream inputStream = conexion.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String linea;
                while ((linea = reader.readLine()) != null) {
                    builder.append(linea);
                }
                respuesta = builder.toString();
                reader.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            try {
                JSONObject jsonObject = new JSONObject(resultado);
                List<List<HashMap<String, String>>> rutas = new AnalizarJSONRuta().parse(jsonObject);
                // Eliminar la línea anterior si existe
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }
                // Dibujar la nueva línea
                for (List<HashMap<String, String>> ruta : rutas) {
                    ArrayList<LatLng> puntos = new ArrayList<>();
                    for (HashMap<String, String> punto : ruta) {
                        double lat = Double.parseDouble(punto.get("lat"));
                        double lng = Double.parseDouble(punto.get("lng"));
                        puntos.add(new LatLng(lat, lng));
                    }
                    PolylineOptions opcionesLinea = new PolylineOptions().addAll(puntos).width(10).color(ContextCompat.getColor(UbicacionClientes.this, R.color.blue));
                    currentPolyline = mMap.addPolyline(opcionesLinea); // Guardar la nueva línea
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AnalizarJSONRuta {
        public List<List<HashMap<String, String>>> parse(JSONObject jsonObject) {
            List<List<HashMap<String, String>>> rutas = new ArrayList<>();
            try {
                JSONArray rutasJSON = jsonObject.getJSONArray("routes");
                for (int i = 0; i < rutasJSON.length(); i++) {
                    JSONArray legs = rutasJSON.getJSONObject(i).getJSONArray("legs");
                    List<HashMap<String, String>> ruta = new ArrayList<>();
                    for (int j = 0; j < legs.length(); j++) {
                        JSONArray pasos = legs.getJSONObject(j).getJSONArray("steps");
                        for (int k = 0; k < pasos.length(); k++) {
                            String polyline = pasos.getJSONObject(k).getJSONObject("polyline").getString("points");
                            List<LatLng> lista = decodificarPolyline(polyline);
                            for (LatLng punto : lista) {
                                HashMap<String, String> latLng = new HashMap<>();
                                latLng.put("lat", Double.toString(punto.latitude));
                                latLng.put("lng", Double.toString(punto.longitude));
                                ruta.add(latLng);
                            }
                        }
                    }
                    rutas.add(ruta);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rutas;
        }

        private List<LatLng> decodificarPolyline(String encoded) {
            List<LatLng> polyline = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, resultado = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    resultado |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((resultado & 1) != 0 ? ~(resultado >> 1) : (resultado >> 1));
                lat += dlat;

                shift = 0;
                resultado = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    resultado |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((resultado & 1) != 0 ? ~(resultado >> 1) : (resultado >> 1));
                lng += dlng;

                polyline.add(new LatLng(lat / 1E5, lng / 1E5));
            }
            return polyline;
        }
    }

    private void moverMapaAPunto(LatLng punto) {
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punto, 15));
            // Eliminar el marcador anterior si existe
            if (currentMarker != null) {
                currentMarker.remove();
            }
            // Agregar el nuevo marcador
            currentMarker = mMap.addMarker(new MarkerOptions().position(punto).title("Ubicación"));
        }
    }




    private void solicitarPermisosUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            iniciarLocalizacion();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            mostrarDialogoExplicativo();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, CODIGO_PERMISO_UBICACION);
        }
    }

    private void mostrarDialogoExplicativo() {
        new AlertDialog.Builder(this)
                .setTitle("Permisos necesarios")
                .setMessage("Esta aplicación requiere acceder a tu ubicación para mostrar el mapa. ¿Deseas permitir el acceso?")
                .setPositiveButton("Sí", (dialog, which) -> ActivityCompat.requestPermissions(
                        UbicacionClientes.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        CODIGO_PERMISO_UBICACION
                ))
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                    Toast.makeText(this, "La aplicación necesita permisos de ubicación para funcionar", Toast.LENGTH_LONG).show();
                })
                .create()
                .show();
    }

    private void iniciarLocalizacion() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setInterval(30000) // Intervalo entre actualizaciones
                .setFastestInterval(10000); // Tiempo mínimo entre actualizaciones rápidas

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult.getLastLocation() != null) {
                    double latitude = locationResult.getLastLocation().getLatitude();
                    double longitude = locationResult.getLastLocation().getLongitude();
                    centrarMapaEnUbicacion(latitude, longitude);
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
        }
    }

    private void centrarMapaEnUbicacion(double latitude, double longitude) {
        if (mMap != null) {
            LatLng ubicacionActual = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 15));
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            solicitarPermisosUbicacion(); // Solicitar permisos si no están habilitados
        }
    }

    private void reubicarUbicacion() {
        // Llamamos al método de localización para centrar el mapa en la ubicación actual
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            centrarMapaEnUbicacion(latitude, longitude);
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODIGO_PERMISO_UBICACION) {
            if (grantResults.length > 0 && allPermissionsGranted(grantResults)) {
                iniciarLocalizacion();
            } else {
                Toast.makeText(this, "Es necesario habilitar los permisos para usar la aplicación", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean allPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}

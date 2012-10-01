package org.k2htm.tnc;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class TrafficMapActivity extends MapActivity implements LocationListener {
	private MapController mapController;
	private MapView mapView;
	private LocationManager locationManager;
	private GeoPoint currentPoint;
	private Location currentLocation = null;
	private TrafficOverlay currPos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_map);
		// mapview setting
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(false);
		mapController = mapView.getController();
		mapController.setZoom(15);
		getLastLocation();
		animateToCurrentLocation();
		drawCurrPositionOverlay();
	}

	public void getLastLocation() {
		String provider = getBestProvider();
		currentLocation = locationManager.getLastKnownLocation(provider);
		if (currentLocation != null) {
			setCurrentLocation(currentLocation);
			((TextView) findViewById(R.id.providerText)).setText("Provider :"
					+ getBestProvider());
		} else {
			Toast.makeText(this, "Location not yet acquired", Toast.LENGTH_LONG)
					.show();
		}
	}

	public void animateToCurrentLocation() {
		if (currentPoint != null) {
			mapController.animateTo(currentPoint);
		}

	}

	public String getBestProvider() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = locationManager.getBestProvider(criteria, true);
		return bestProvider;
	}

	public void setCurrentLocation(Location location) {
		int currLatitude = (int) (location.getLatitude() * 1E6);
		int currLongitude = (int) (location.getLongitude() * 1E6);
		currentPoint = new GeoPoint(currLatitude, currLongitude);
		currentLocation = new Location("");
		currentLocation.setLatitude(currentPoint.getLatitudeE6() / 1E6);
		currentLocation.setLongitude(currentPoint.getLongitudeE6() / 1E6);
		((TextView) findViewById(R.id.latitudeText)).setText("Latitude : "
				+ String.format("%.6f", (location.getLatitude())));
		((TextView) findViewById(R.id.longitudeText)).setText("Longitude : "
				+ String.format("%.6f", (location.getLongitude())));
		((TextView) findViewById(R.id.accuracyText)).setText("Accuracy : "
				+ String.valueOf(location.getAccuracy()) + " m");
	}

	public void centerToCurrentLocation(View view) {
		animateToCurrentLocation();
	}

	public void drawCurrPositionOverlay() {
		List<Overlay> overlays = mapView.getOverlays();
		overlays.remove(currPos);
		Drawable marker = getResources().getDrawable(R.drawable.me);
		currPos = new TrafficOverlay(marker, mapView);
		if (currentPoint != null) {
			OverlayItem overlayitem = new OverlayItem(currentPoint, "Me",
					"Here I am!");
			currPos.addOverlay(overlayitem);
			overlays.add(currPos);
			currPos.setCurrentLocation(currentLocation);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_traffic_map, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		setCurrentLocation(location);
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		locationManager
				.requestLocationUpdates(getBestProvider(), 1000, 1, this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locationManager.removeUpdates(this);
	}

}

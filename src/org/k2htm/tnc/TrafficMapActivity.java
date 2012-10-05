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
		drawMalls();
	}

	public void getLastLocation() {
		String provider = getBestProvider();
		currentLocation = locationManager.getLastKnownLocation(provider);
		/*
		 * The next 4 lines are used to hardcode our location If you wish to get
		 * your current location remember to comment or remove them
		 */
		currentPoint = new GeoPoint(29647929, -82352486);
		currentLocation = new Location("");
		currentLocation.setLatitude(currentPoint.getLatitudeE6() / 1e6);
		currentLocation.setLongitude(currentPoint.getLongitudeE6() / 1e6);
		if (currentLocation != null) {
			setCurrentLocation(currentLocation);
		} else {
			Toast.makeText(this, "Location not yet acquired", Toast.LENGTH_LONG)
					.show();
		}
		((TextView) findViewById(R.id.providerText)).setText("Provider :"
				+ getBestProvider());
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
		/*
		 * int currLatitude = (int) (location.getLatitude()*1E6); int
		 * currLongitude = (int) (location.getLongitude()*1E6); currentPoint =
		 * new GeoPoint(currLatitude,currLongitude);
		 */
		/*
		 * ======================================================================
		 * ================== /*The Above Code displays your correct current
		 * location, but for the sake of the demo I will be hard coding your
		 * current location to the University of Florida, to get your real
		 * current location, comment or delete the line of code below and
		 * uncomment the code above.
		 */

		currentPoint = new GeoPoint(29647929, -82352486);
		currentLocation = new Location("");
		currentLocation.setLatitude(currentPoint.getLatitudeE6() / 1e6);
		currentLocation.setLongitude(currentPoint.getLongitudeE6() / 1e6);

		((TextView) findViewById(R.id.latitudeText)).setText("Latitude : "
				+ String.valueOf((int) (currentLocation.getLatitude() * 1E6)));
		((TextView) findViewById(R.id.longitudeText)).setText("Longitude : "
				+ String.valueOf((int) (currentLocation.getLongitude() * 1E6)));
		((TextView) findViewById(R.id.accuracyText)).setText("Accuracy : "
				+ String.valueOf(location.getAccuracy()) + " m");
		drawCurrPositionOverlay();
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

	
	public void drawMalls(){
	    Drawable marker = getResources().getDrawable(R.drawable.incidents);
	    TrafficOverlay mallsPos = new TrafficOverlay(marker,mapView);
	        GeoPoint[] mallCoords = new GeoPoint[6];
	        //Load Some Random Coordinates in Miami, FL
	        mallCoords[0] = new GeoPoint(29656582,-82411151);//The Oaks Mall
	        mallCoords[1] = new GeoPoint(29649831,-82376347);//Creekside mall
	        mallCoords[2] = new GeoPoint(29674146,-8238905);//Millhopper Shopping Center
	        mallCoords[3] = new GeoPoint(29675078,-82322617);//Northside Shopping Center
	        mallCoords[4] = new GeoPoint(29677017,-82339761);//Gainesville Mall
	        mallCoords[5] = new GeoPoint(29663835,-82325599);//Gainesville Shopping Center
	        List<Overlay> overlays = mapView.getOverlays();
	    OverlayItem overlayItem = new OverlayItem(mallCoords[0], "The Oaks Mall", "6419 W Newberry Rd, Gainesville, FL 32605");
	    mallsPos.addOverlay(overlayItem);
	    overlayItem = new OverlayItem(mallCoords[1], "Creekside Mall", "3501 Southwest 2nd Avenue, Gainesville, FL");
	    mallsPos.addOverlay(overlayItem);
	    overlayItem = new OverlayItem(mallCoords[2], "Millhopper Shopping Center", "NW 43rd St & NW 16th Blvd. Gainesville, FL");
	    mallsPos.addOverlay(overlayItem);
	    overlayItem = new OverlayItem(mallCoords[3], "Northside Shopping Center", "Gainesville, FL");
	    mallsPos.addOverlay(overlayItem);
	    overlayItem = new OverlayItem(mallCoords[4], "Gainesville Mall", "2624 Northwest 13th Street Gainesville, FL 32609-2834");
	    mallsPos.addOverlay(overlayItem);
	    overlayItem = new OverlayItem(mallCoords[5], "Gainesville Shopping Center", "1344 N Main St Gainesville, Florida 32601");
	    mallsPos.addOverlay(overlayItem);
	    overlays.add(mallsPos);
	    mallsPos.setCurrentLocation(currentLocation);
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

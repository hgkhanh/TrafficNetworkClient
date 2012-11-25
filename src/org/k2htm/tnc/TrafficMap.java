package org.k2htm.tnc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class TrafficMap extends MapActivity implements LocationListener {
	private static MapController mapController;
	private MapView mapView;
	private LocationManager locationManager;
	private static GeoPoint currentPoint;
	private Location currentLocation = null;
	private TrafficOverlay currPosOverlay;
	private Button btnReport;
	private TextView tvProvider;
	private static TextView tvType;
	private TrafficNetworkClient mApplication;
	private static TextView tvDes;
	private static LinearLayout llDetail;
	public static final int REQUEST_CODE = 100;
	public static final String TAG = "Traffic Map";
	public static final String LONG = "longitude";
	public static final String LAT = "latitude";
	public static final String INCIDENT_TYPE = "title";
	public static final String INCIDENT_DESCRIPTION = "des";
	public static final int TRAFFIC_JAM_CODE = 0;
	public static final String TRAFFIC_JAM_STRING = "Traffic Jam";
	public static final int ACCIDENT_CODE = 1;
	public static final String ACCIDENT_STRING = "Accident";
	public static final int BLOCKED_CODE = 2;
	public static final String BLOCKED_STRING = "Blocked";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_map);
		// app obj
		mApplication = (TrafficNetworkClient) getApplication();
		Toast.makeText(
				TrafficMap.this,
				getText(R.string.login_user_toast) + " : "
						+ mApplication.getUser(), Toast.LENGTH_SHORT).show();
		// find view
		btnReport = (Button) findViewById(R.id.btnReport);
		tvProvider = ((TextView) findViewById(R.id.providerText));
		mapView = (MapView) findViewById(R.id.mapView);
		llDetail = (LinearLayout) findViewById(R.id.llDetail);
		tvDes = (TextView) findViewById(R.id.tvDescription);
		tvType = (TextView) findViewById(R.id.tvIncType);
		// turn on zoom controller if device not support multitouch
		if (getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)) {
			// do multitouch
			mapView.setBuiltInZoomControls(false);
		} else {
			// do magnifying glass
			mapView.setBuiltInZoomControls(true);
		}
		// mapview setting
		mapView.setSatellite(false);
		mapController = mapView.getController();
		mapController.setZoom(15);
		getLastLocation();
		animateToCurrentLocation();
		drawCurrPositionOverlay();

		// set button listener
		btnReport.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle oBundle = new Bundle();
				oBundle.putInt(LAT, currentPoint.getLatitudeE6());
				oBundle.putInt(LONG, currentPoint.getLongitudeE6());
				Intent oIntent = new Intent(TrafficMap.this, ReportMap.class);
				oIntent.putExtras(oBundle);
				startActivityForResult(oIntent, REQUEST_CODE);
			}
		});

	}

	public void centerToCurrentLocation(View view) {
		animateToCurrentLocation();
	}

	public static void showDetail(GeoPoint point, String type,
			String description) {
		llDetail.setVisibility(View.VISIBLE);
		tvType.setText(type);
		tvDes.setText(description);
		if (point != null) {
			mapController.animateTo(point);
		}
	}

	public void hideDetail(View view) {
		llDetail.setVisibility(View.GONE);
	}

	public void getLastLocation() {
		String provider = getBestProvider();
		currentLocation = locationManager.getLastKnownLocation(provider);
		if (currentLocation == null) {

			currentLocation = TrafficOverlay.convertGpToLoc(new GeoPoint(
					21027555, 105849538));
			Toast.makeText(
					this,
					"Location not yet acquired.Set to default location : (21.027555;105.849538)",
					Toast.LENGTH_LONG).show();
		}
		setCurrentLocation(currentLocation);
		tvProvider.setText("Provider :" + getBestProvider());
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

	// public void setCurrentLocation() {
	// currentPoint = new GeoPoint(21027555, 105849538);
	//
	// ((TextView) findViewById(R.id.latitudeText)).setText("Latitude : "
	// + String.valueOf((int) (currentPoint.getLatitudeE6())));
	// ((TextView) findViewById(R.id.longitudeText)).setText("Longitude : "
	// + String.valueOf((int) (currentPoint.getLongitudeE6())));
	// ((TextView) findViewById(R.id.accuracyText)).setText("Accuracy : "
	// + String.valueOf("N/A"));
	//
	// drawCurrPositionOverlay();
	// }

	public void setCurrentLocation(Location location) {
		int currLatitude = (int) (location.getLatitude() * 1E6);
		int currLongitude = (int) (location.getLongitude() * 1E6);
		currentPoint = new GeoPoint(currLatitude, currLongitude);

		((TextView) findViewById(R.id.latitudeText)).setText("Latitude : "
				+ String.valueOf((int) (currentLocation.getLatitude() * 1E6)));
		((TextView) findViewById(R.id.longitudeText)).setText("Longitude : "
				+ String.valueOf((int) (currentLocation.getLongitude() * 1E6)));
		((TextView) findViewById(R.id.accuracyText)).setText("Accuracy : "
				+ String.valueOf(location.getAccuracy()) + " m");

		drawCurrPositionOverlay();
	}

	public void drawCurrPositionOverlay() {
		List<Overlay> overlays = mapView.getOverlays();
		overlays.remove(currPosOverlay);
		Drawable marker = getResources().getDrawable(R.drawable.icon_you);
		currPosOverlay = new TrafficOverlay(marker, mapView);
		if (currentPoint != null) {
			OverlayItem overlayitem = new OverlayItem(currentPoint, "Me",
					"Here I am!");
			currPosOverlay.addOverlay(overlayitem);
			overlays.add(currPosOverlay);
			currPosOverlay.setCurrentLocation(currentLocation);
		}
	}

	public void drawIncidentOverlay() {
		List<Overlay> overlays = mapView.getOverlays();
		// create
		Drawable marker = getResources().getDrawable(R.drawable.indicator_jam);
		TrafficOverlay jamPos = new TrafficOverlay(marker, mapView);
		marker = getResources().getDrawable(R.drawable.indicator_accident);
		TrafficOverlay accidentPos = new TrafficOverlay(marker, mapView);
		marker = getResources().getDrawable(R.drawable.indicator_blocked);
		TrafficOverlay blockedPos = new TrafficOverlay(marker, mapView);

		/*
		 * read from file
		 */
		// FileInputStream fis;
		Log.i(TAG, "read file");
		try {
			// fis = openFileInput("incidents.txt");
			String filePath = this.getFilesDir().getPath().toString()
					+ "/incidents.txt";
			File file = new File(filePath);
			InputStream is = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));

			String strLine = null;
			int tmp_lat;
			int tmp_long;
			int tmp_type;
			String des_string;
			String type_string = TRAFFIC_JAM_STRING;
			String imageUri;
			while ((strLine = reader.readLine()) != null) {
				tmp_lat = Integer.parseInt(strLine);
				Log.i(TAG, "tmp_lat:" + strLine);
				strLine = reader.readLine();
				tmp_long = Integer.parseInt(strLine);
				Log.i(TAG, "tmp_long:" + strLine);
				strLine = reader.readLine();
				tmp_type = Integer.parseInt(strLine);
				Log.i(TAG, "tmp_type:" + tmp_type);
				des_string = reader.readLine();
				Log.i(TAG, "tmp_des:" + des_string);
				imageUri = reader.readLine();
				Log.i(TAG, "image uri:" + imageUri);
				// get type string
				switch (tmp_type) {
				case TrafficMap.TRAFFIC_JAM_CODE:
					type_string = TRAFFIC_JAM_STRING;
					break;
				case TrafficMap.ACCIDENT_CODE:
					type_string = ACCIDENT_STRING;
					break;
				case TrafficMap.BLOCKED_CODE:
					type_string = BLOCKED_STRING;
					break;
				default:
					break;
				}
				IncidentOverlayItem overlayItem = new IncidentOverlayItem(
						new GeoPoint(tmp_lat, tmp_long), type_string,
						des_string, imageUri);
				// draw correct icon at that position
				switch (tmp_type) {
				case TrafficMap.TRAFFIC_JAM_CODE:
					jamPos.addOverlay(overlayItem);
					break;
				case TrafficMap.ACCIDENT_CODE:
					accidentPos.addOverlay(overlayItem);
					break;
				case TrafficMap.BLOCKED_CODE:
					blockedPos.addOverlay(overlayItem);
					break;
				default:
					jamPos.addOverlay(overlayItem);
					break;
				}

			}

			reader.close();
			is.close();

		} catch (Exception e) {
		}
		overlays.add(jamPos);
		overlays.add(accidentPos);
		overlays.add(blockedPos);
		// set curent position for each overlay to caculate distance from user
		// to point of incident
		jamPos.setCurrentLocation(currentLocation);
		accidentPos.setCurrentLocation(currentLocation);
		blockedPos.setCurrentLocation(currentLocation);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_traffic_map, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.min10:
			mApplication.setTimeFilter(10);
			Toast.makeText(
					TrafficMap.this,
					getString(R.string.menu_filter_success_toast) + ": "
							+ item.getTitle(), Toast.LENGTH_SHORT).show();
			return true;
		case R.id.min30:
			mApplication.setTimeFilter(30);
			Toast.makeText(
					TrafficMap.this,
					getString(R.string.menu_filter_success_toast) + ": "
							+ item.getTitle(), Toast.LENGTH_SHORT).show();
			return true;
		case R.id.hour1:
			mApplication.setTimeFilter(60);
			Toast.makeText(
					TrafficMap.this,
					getString(R.string.menu_filter_success_toast) + ": "
							+ item.getTitle(), Toast.LENGTH_SHORT).show();
			return true;
		case R.id.hour3:
			mApplication.setTimeFilter(180);
			Toast.makeText(
					TrafficMap.this,
					getString(R.string.menu_filter_success_toast) + ": "
							+ item.getTitle(), Toast.LENGTH_SHORT).show();
			return true;
		case R.id.hour6:
			mApplication.setTimeFilter(360);
			Toast.makeText(
					TrafficMap.this,
					getString(R.string.menu_filter_success_toast) + ": "
							+ item.getTitle(), Toast.LENGTH_SHORT).show();
			return true;
		default:
			Log.i(TAG, "Time Filter : " + mApplication.getTimeFilter());
			return super.onOptionsItemSelected(item);
		}
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

		drawIncidentOverlay();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locationManager.removeUpdates(this);
	}

	public static GeoPoint getCurrentPoint() {
		return currentPoint;
	}

}

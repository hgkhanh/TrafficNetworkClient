package org.k2htm.tnc;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class TrafficOverlay extends BalloonItemizedOverlay<OverlayItem> {
	private Context mContext;
	public static final String TAG = "Traffic Overlay";
	private ArrayList<OverlayItem> incidents = new ArrayList<OverlayItem>();
	private Location currentLocation;

	public TrafficOverlay(Drawable defaultMarker, MapView mapView) {
		super(defaultMarker, mapView);
		// TODO Auto-generated constructor stub
		boundCenterBottom(defaultMarker);
		mContext = mapView.getContext();
		populate();
	}

	public void setCurrentLocation(Location loc) {
		this.currentLocation = loc;
		Log.i(TAG, "set currentLocation:" + currentLocation.toString());
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return incidents.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return incidents.size();
	}

	public void addOverlay(OverlayItem overlay) {
		incidents.add(overlay);
		populate();
	}

	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {

		// CACULATE DISTANCE
		// String tmp = incidents.get(index).getTitle();
		// GeoPoint incidentPoint = incidents.get(index).getPoint();
		// Location tmpLoc = convertGpToLoc(incidentPoint);
		// Log.i(TAG,"incidentPoint toString():" + incidentPoint.toString());
		// Log.i(TAG,"currentLocation toString():" +
		// currentLocation.toString());
		//
		// double distance = ((currentLocation).distanceTo(tmpLoc)) * (0.001);
		// Log.i(TAG,"incidentPoint toString():" + incidentPoint.toString());
		// DecimalFormat df = new DecimalFormat("#.##");
		// tmp = tmp + " is " + String.valueOf(df.format(distance)) +
		// " km away.";
		// Toast.makeText(mContext, tmp, Toast.LENGTH_LONG).show();

		// String incidentType = incidents.get(index).getTitle();
		// if(!incidentType.equals("Me")){
		// String incidentDes = incidents.get(index).getSnippet();
		// GeoPoint incidentPoint = incidents.get(index).getPoint();
		// Intent detailActIntent = new Intent(mContext, IncidentDetail.class);
		// //Bundle : incident type , lat & long , description
		// Bundle oBundle = new Bundle();
		// oBundle.putString(TrafficMap.INCIDENT_TYPE,incidentType);
		// oBundle.putString(TrafficMap.INCIDENT_DESCRIPTION,incidentDes);
		// oBundle.putInt(TrafficMap.LAT, incidentPoint.getLatitudeE6());
		// oBundle.putInt(TrafficMap.LONG, incidentPoint.getLongitudeE6());
		// detailActIntent.putExtras(oBundle);
		// mContext.startActivity(detailActIntent);
		// }
		Log.i(TAG, "onTap(index) :" + index);
		String incidentType = incidents.get(index).getTitle();
		if (!incidentType.equals("Me")) {
			// String incidentDes = incidents.get(index).getSnippet();
			// GeoPoint incidentPoint = incidents.get(index).getPoint();
			Log.i(TAG,"Tap tap tap:"+index);
			TrafficMap.showDetail(index);
		}
		return true;
	}

	public static Location convertGpToLoc(GeoPoint gp) {
		Location convertedLocation = new Location("");
		convertedLocation.setLatitude(gp.getLatitudeE6() / 1e6);
		convertedLocation.setLongitude(gp.getLongitudeE6() / 1e6);
		return convertedLocation;
	}

}

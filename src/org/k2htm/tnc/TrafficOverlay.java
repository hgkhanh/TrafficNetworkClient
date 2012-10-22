package org.k2htm.tnc;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class TrafficOverlay extends BalloonItemizedOverlay<OverlayItem> {
	private Context mContext;
	private ArrayList<OverlayItem> incidents = new ArrayList<OverlayItem>();
	private Location currentLocation;

	public TrafficOverlay(Drawable defaultMarker, MapView mapView) {
		super(defaultMarker, mapView);
		// TODO Auto-generated constructor stub
		boundCenterBottom(defaultMarker);
		mContext = mapView.getContext();
	}

	public void setCurrentLocation(Location loc) {
		this.currentLocation = loc;
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
		String tmp = incidents.get(index).getTitle();
		GeoPoint mallPoint = incidents.get(index).getPoint();
		Location tmpLoc = convertGpToLoc(mallPoint);
		double distance = ((currentLocation).distanceTo(tmpLoc)) * (0.001);
		DecimalFormat df = new DecimalFormat("#.##");
		tmp = tmp + " is " + String.valueOf(df.format(distance)) + " km away.";
		Toast.makeText(mContext, tmp, Toast.LENGTH_LONG).show();
		return true;
	}
   
	public Location convertGpToLoc(GeoPoint gp) {
		Location convertedLocation = new Location("");
		convertedLocation.setLatitude(gp.getLatitudeE6() / 1e6);
		convertedLocation.setLongitude(gp.getLongitudeE6() / 1e6);
		return convertedLocation;
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		// TODO Auto-generated method stub
		Toast.makeText(
				mContext,
				"long : " + p.getLongitudeE6() + "  | lat : "
						+ p.getLatitudeE6(), 500).show();

		return super.onTap(p, mapView);
	}
}

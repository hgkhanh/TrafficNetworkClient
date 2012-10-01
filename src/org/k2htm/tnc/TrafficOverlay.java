package org.k2htm.tnc;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.widget.Toast;

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
		Toast.makeText(mContext, "Overlay Item " + index + " tapped!",
				Toast.LENGTH_LONG).show();
		return true;
	}
}

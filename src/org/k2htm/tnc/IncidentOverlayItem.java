package org.k2htm.tnc;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class IncidentOverlayItem extends OverlayItem {
	private String imageUri;

	public IncidentOverlayItem(GeoPoint geoPoint, String type, String description , String uri) {
		super(geoPoint, type, description);
		setImageUri(uri);
		// TODO Auto-generated constructor stub
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

}

package org.k2htm.tnc;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class IncidentOverlayItem extends OverlayItem {
	private String imageUri;
	private String username;

	public IncidentOverlayItem(GeoPoint geoPoint, String type,
			String description, String uri, String username) {
		super(geoPoint, type, description);
		setImageUri(uri);
		setUsername(username);
		// TODO Auto-generated constructor stub
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

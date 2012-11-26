package org.k2htm.tnc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

public class ReportMap extends MapActivity {
	private MapController mapController;
	private MapView map = null;
	private GeoPoint currentPoint;
	private MyLocationOverlay me = null;
	private Button btnConfirm;
	private ImageView imvImage;
	private TextView tvLat, tvLong;
	private EditText edtDes;
	private Spinner spnType;
	private Uri imageUri;
	private String imageUriStr;
	public static final String TAG = "ReportMapActivity";
	public static final int CODE_IMAGE_PICKER = 201;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_map);
		// get view
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		tvLat = ((TextView) findViewById(R.id.latitudeText));
		tvLong = ((TextView) findViewById(R.id.longitudeText));
		spnType = (Spinner) findViewById(R.id.spn_type);
		imvImage = (ImageView) findViewById(R.id.imbImage);
		edtDes = (EditText) findViewById(R.id.edtDescription);

		// set location get from bundle from trafficMap
		setCurLocation();
		// set text
		tvLat.setText(String.valueOf((int) (currentPoint.getLatitudeE6())));
		tvLong.setText(String.valueOf((int) (currentPoint.getLongitudeE6())));
		// set button listener
		btnConfirm.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// check null type edt

				if (edtDes.getText().toString().equals("")) {
					edtDes.setText(" ");
				}
				Log.i(TAG, "asdf");
				try {
					imageUriStr = imageUri.toString();
				} catch (Exception e) {

				}
				Log.i(TAG, "asdf");
				Log.i(TAG,
						"spinner select : " + spnType.getSelectedItemPosition()
								+ "\nClick confirm "
								+ tvLat.getText().toString() + " "
								+ tvLong.getText().toString() + "\nImage uri "
								+ imageUriStr);
				Log.i(TAG, "asdf");
				writeToFile(Integer.parseInt(tvLat.getText().toString()),
						Integer.parseInt((tvLong.getText().toString())),
						spnType.getSelectedItemPosition(), edtDes.getText()
								.toString(), imageUriStr);
			}
		});

		// mapview setting
		map = (MapView) findViewById(R.id.map);

		map.getController().setCenter(currentPoint);
		map.getController().setZoom(15);

		// turn on zoom controller if device not support multitouch
		if (getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)) {
			// do multitouch
			map.setBuiltInZoomControls(false);
		} else {
			// do magnifying glass
			map.setBuiltInZoomControls(true);
		}

		Drawable marker = getResources()
				.getDrawable(R.drawable.indicator_blank);

		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());

		map.getOverlays().add(new SitesOverlay(marker));

		me = new MyLocationOverlay(this, map);
		map.getOverlays().add(me);
	}

	public void callImagePicker(View view) {
		Intent pickIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File photo = new File(Environment.getExternalStorageDirectory(),
				"Pic.jpg");
		takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
		imageUri = Uri.fromFile(photo);
		String pickTitle = "Select or take a new Picture";
		Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				new Intent[] { takePhotoIntent });

		startActivityForResult(chooserIntent, CODE_IMAGE_PICKER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CODE_IMAGE_PICKER:
			if (resultCode == RESULT_OK) {
				if (data != null) {
					Uri selectedImage = data.getData();
					Log.i(TAG, "Image Uri :  " + selectedImage);
					if (selectedImage != null) {
						// DO STH WITH THE URI
						imvImage.setImageURI(selectedImage);
						imageUri = selectedImage;
						break;
					}
				} else {
					if (imageUri != null) {
						imvImage.setImageURI(imageUri);
					}
				}
			}
		}
	}

	public void centerToCurrentLocation(View view) {
		animateToCurrentLocation();
	}

	public void animateToCurrentLocation() {
		if (currentPoint != null) {
			map.getController().animateTo(currentPoint);
		}

	}

	@Override
	public void onResume() {
		super.onResume();

		me.enableCompass();
	}

	@Override
	public void onPause() {
		super.onPause();

		me.disableCompass();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return (false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_S) {
			map.setSatellite(!map.isSatellite());
			return (true);
		} else if (keyCode == KeyEvent.KEYCODE_Z) {
			map.displayZoomControls(true);
			return (true);
		}

		return (super.onKeyDown(keyCode, event));
	}

	private GeoPoint getPoint(double lat, double lon) {
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
	}

	private class SitesOverlay extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> items = new ArrayList<OverlayItem>();
		private Drawable marker = null;
		private OverlayItem inDrag = null;
		private ImageView dragImage = null;
		private int xDragImageOffset = 0;
		private int yDragImageOffset = 0;
		private int xDragTouchOffset = 0;
		private int yDragTouchOffset = 0;

		public SitesOverlay(Drawable marker) {
			super(marker);
			this.marker = marker;

			dragImage = (ImageView) findViewById(R.id.drag);
			xDragImageOffset = dragImage.getDrawable().getIntrinsicWidth() / 2;
			yDragImageOffset = dragImage.getDrawable().getIntrinsicHeight();

			items.add(new OverlayItem(currentPoint, "Choose Position",
					"Drag to incident's position"));

			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return (items.get(i));
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);

			boundCenterBottom(marker);
		}

		@Override
		public int size() {
			return (items.size());
		}

		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			final int action = event.getAction();
			final int x = (int) event.getX();
			final int y = (int) event.getY();
			boolean result = false;

			if (action == MotionEvent.ACTION_DOWN) {
				for (OverlayItem item : items) {
					Point p = new Point(0, 0);

					map.getProjection().toPixels(item.getPoint(), p);

					if (hitTest(item, marker, x - p.x, y - p.y)) {
						result = true;
						inDrag = item;
						items.remove(inDrag);
						populate();

						xDragTouchOffset = 0;
						yDragTouchOffset = 0;

						setDragImagePosition(p.x, p.y);
						dragImage.setVisibility(View.VISIBLE);

						xDragTouchOffset = x - p.x;
						yDragTouchOffset = y - p.y;

						break;
					}
				}
			} else if (action == MotionEvent.ACTION_MOVE && inDrag != null) {
				setDragImagePosition(x, y);
				result = true;
			} else if (action == MotionEvent.ACTION_UP && inDrag != null) {
				dragImage.setVisibility(View.GONE);

				GeoPoint pt = map.getProjection().fromPixels(
						x - xDragTouchOffset, y - yDragTouchOffset);
				OverlayItem toDrop = new OverlayItem(pt, inDrag.getTitle(),
						inDrag.getSnippet());

				items.add(toDrop);
				populate();

				inDrag = null;
				result = true;
				// settext
				Log.i(TAG,
						"touch up " + pt.getLatitudeE6() + " "
								+ pt.getLongitudeE6());
				tvLat.setText("" + pt.getLatitudeE6());
				tvLong.setText("" + pt.getLongitudeE6());

			}

			return (result || super.onTouchEvent(event, mapView));
		}

		private void setDragImagePosition(int x, int y) {
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) dragImage
					.getLayoutParams();

			lp.setMargins(x - xDragImageOffset - xDragTouchOffset, y
					- yDragImageOffset - yDragTouchOffset, 0, 0);
			dragImage.setLayoutParams(lp);
		}
	}

	private void setCurLocation() {
		Intent iIntent = getIntent();
		Bundle iBundle = iIntent.getExtras();
		currentPoint = new GeoPoint(iBundle.getInt(TrafficMap.LAT),
				iBundle.getInt(TrafficMap.LONG));
		Log.i("report", "lat: " + iBundle.getInt(TrafficMap.LAT) + "\nlong:"
				+ iBundle.getInt(TrafficMap.LONG));
	}

	private void writeToFile(int latitude, int longitude, int type,
			String description, String imageUri) {
		Log.i(TAG, "asdf");
		try { // catches IOException below
			String outputString = new String(latitude + "\n" + longitude + "\n"
					+ type + "\n" + description + "\n" + imageUri);

			// ##### Write a file to the disk #####
			/*
			 * We have to use the openFileOutput()-method the ActivityContext
			 * provides, to protect your file from others and This is done for
			 * security-reasons. We chose MODE_WORLD_READABLE, because we have
			 * nothing to hide in our file
			 */
			Log.i(TAG, "asdf");
			String filePath = this.getFilesDir().getPath().toString()
					+ "/incidents.txt";
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter filewriter = new FileWriter(file);

			BufferedWriter wr = new BufferedWriter(filewriter);
			// Write the string to the file
			wr.write(outputString);
			/*
			 * ensure that everything is really written out and close
			 */
			wr.flush();
			wr.close();
			Log.i("report", "write :" + outputString);
			Toast.makeText(ReportMap.this,
					"Reported point :" + latitude + " " + longitude,
					Toast.LENGTH_SHORT).show();
			// ##### Read the file back in #####

			/*
			 * We have to use the openFileInput()-method the ActivityContext
			 * provides. Again for security reasons with openFileInput(...)
			 */
			FileInputStream fIn = openFileInput("incidents.txt");
			InputStreamReader isr = new InputStreamReader(fIn);
			/*
			 * Prepare a char-Array that will hold the chars we read back in.
			 */
			char[] inputBuffer = new char[outputString.length()];
			// Fill the Buffer with data from the file
			isr.read(inputBuffer);
			// Transform the chars to a String
			String readString = new String(inputBuffer);

			// Check if we read back the same chars that we had written out
			boolean isTheSame = outputString.equals(readString);

			// WOHOO lets Celebrate =)
			Log.i("File Reading stuff", "success = " + isTheSame);
			finish();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}

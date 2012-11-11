package org.k2htm.tnc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

public class ReportMapActivity extends MapActivity {
	private MapView map = null;
	private GeoPoint currentPoint;
	private MyLocationOverlay me = null;
	private Button btnConfirm;
	private TextView tvLat, tvLong;
	private Spinner spnType;
	public static final String TAG = "ReportMapActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_map);
		// set location get from bundle from trafficMap
		setCurLocation();
		// get view
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		tvLat = ((TextView) findViewById(R.id.latitudeText));
		tvLat.setText(String.valueOf((int) (currentPoint.getLatitudeE6())));
		tvLong = ((TextView) findViewById(R.id.longitudeText));
		tvLong.setText(String.valueOf((int) (currentPoint.getLongitudeE6())));
		spnType = (Spinner) findViewById(R.id.spn_type);
		// set button listener
		btnConfirm.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				switch (spnType.getSelectedItemPosition()) {
				case 0:

					break;
				case 1:
				default:
					break;
				}
				Log.i(TAG,
						"spinner select : " + spnType.getSelectedItemPosition()
								+ "Click confirm "
								+ (tvLat.getText().toString()) + " "
								+ tvLong.getText().toString());
				writeToFile(Integer.parseInt(tvLat.getText().toString()),
						Integer.parseInt((tvLong.getText().toString())),
						spnType.getSelectedItemPosition());
			}
		});

		map = (MapView) findViewById(R.id.map);

		map.getController().setCenter(currentPoint);
		map.getController().setZoom(15);
		map.setBuiltInZoomControls(true);

		Drawable marker = getResources().getDrawable(R.drawable.marker);

		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());

		map.getOverlays().add(new SitesOverlay(marker));

		me = new MyLocationOverlay(this, map);
		map.getOverlays().add(me);
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
		currentPoint = new GeoPoint(iBundle.getInt(TrafficMapActivity.LAT),
				iBundle.getInt(TrafficMapActivity.LONG));
		Log.i("report", "lat: " + iBundle.getInt(TrafficMapActivity.LAT)
				+ "\nlong:" + iBundle.getInt(TrafficMapActivity.LONG));
	}

	private void writeToFile(int latitude, int longitude, int type) {
		Log.i(TAG, "asdf");
		try { // catches IOException below
			String outputString = new String(latitude + "\n" + longitude + "\n"
					+ type);

			// ##### Write a file to the disk #####
			/*
			 * We have to use the openFileOutput()-method the ActivityContext
			 * provides, to protect your file from others and This is done for
			 * security-reasons. We chose MODE_WORLD_READABLE, because we have
			 * nothing to hide in our file
			 */
			String filePath = this.getFilesDir().getPath().toString() + "/incidents.txt";
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
			Log.i("reaport","write :" + outputString);
			Toast.makeText(ReportMapActivity.this,
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

package org.k2htm.tnc;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import edu.k2htm.clientHelper.HoaHelper;
import edu.k2htm.datahelper.Comment;
import edu.k2htm.datahelper.CommentGetter;

public class IncidentDetailActivity extends Activity {
	private ListView lvComment;
	private Button btnSendComment;
	private EditText edtContentComment;
	private TextView tvUsername, tvDes;
	private TextView tvType, tvTime;
	private TextView tvUpVote, tvDownVote;
	private ImageView imvSmall, imvBig;
	private Bitmap tmpBitmapImage;
	private int cautionID;
	private TrafficNetworkClient mApplication;
	public static final String USERNAME = "username";
	public static final String TYPE = "type";
	public static final String TIME = "time";
	public static final String UPVOTE = "upvote";
	public static final String DOWNVOTE = "downvote";
	public static final String DESCRIPTION = "description";
	public static final String IMAGE = "image";
	public static final String ID = "cautionID";
	private static final String TAG = "IncidentDetailActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incident_details);
		// get app object
		mApplication = (TrafficNetworkClient) getApplication();
		// get view
		lvComment = (ListView) findViewById(R.id.lvComment);
		btnSendComment = (Button) findViewById(R.id.btnSendComment);
		edtContentComment = (EditText) findViewById(R.id.edtContentComment);
		tvUsername = (TextView) findViewById(R.id.tvUsername);
		tvDes = (TextView) findViewById(R.id.tvDescription);
		tvType = (TextView) findViewById(R.id.tvIncType);
		tvUpVote = (TextView) findViewById(R.id.tvUpVote);
		tvDownVote = (TextView) findViewById(R.id.tvDownVote);
		tvTime = (TextView) findViewById(R.id.tvTime);
		imvSmall = (ImageView) findViewById(R.id.imvSmall);
		imvBig = (ImageView) findViewById(R.id.imvBig);
		// hide imvBig
		imvBig.setVisibility(View.GONE);
		loadInfo();
		loadComment();
	}

	private void loadInfo() {
		// TODO Auto-generated method stub
		// get bundle
		Intent iIntent = getIntent();
		Bundle iBundle = iIntent.getExtras();
		tvUsername.setText(iBundle.getString(USERNAME));
		if (iBundle.containsKey(DESCRIPTION)) {
			tvDes.setText(iBundle.getString(DESCRIPTION));
		} else {
			tvDes.setText("No Description.");
		}
		tvType.setText(iBundle.getString(TYPE));
		tvTime.setText(iBundle.getString(TIME));
		tvUpVote.setText(iBundle.getString(UPVOTE));
		tvDownVote.setText(iBundle.getString(DOWNVOTE));
		// set ID
		cautionID = iBundle.getInt(ID);
		// get image
		GetImageTask getImageTask = new GetImageTask();
		getImageTask.execute(iBundle.getString(IMAGE));
		Log.i(TAG, "image : " + iBundle.getString(IMAGE));
	}

	void loadComment() {

		ArrayList<Comment> comments = new ArrayList<Comment>();
		comments.add(new Comment("abc1", 2, "abc1", new Date().getTime()));
		comments.add(new Comment("abc2", 2, "abc1", new Date().getTime()));
		comments.add(new Comment("abc3", 2, "abc1", new Date().getTime()));
		comments.add(new Comment("abc4", 2, "abc1", new Date().getTime()));
		comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		// Tiep di.
		CommentItemAdapter adapter = new CommentItemAdapter(this, comments);
		lvComment.setAdapter(adapter);

	}

	private class GetImageTask extends AsyncTask<String, String, Boolean> {

		@Override
		protected Boolean doInBackground(String... url) {
			// TODO Auto-generated method stub
			URL myFileUrl = null;
			try {
				// TEST
				// myFileUrl = new URL(
				// "http://www.belovedcars.com/wp-content/uploads/2012/03/2012-traffic-jam.jpg");
				// END TEST
				myFileUrl = new URL(url[0]);
				Log.i(TAG, myFileUrl + "");

				HttpURLConnection conn = (HttpURLConnection) myFileUrl
						.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				tmpBitmapImage = BitmapFactory.decodeStream(is);

				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (tmpBitmapImage != null) {
				imvSmall.setImageBitmap(tmpBitmapImage);
				imvBig.setImageBitmap(tmpBitmapImage);
			}
		}
	}

	private class getCommentTask extends
			AsyncTask<Integer, ArrayList<Comment>, Void> {
		CommentItemAdapter adapter;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Void doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			CommentGetter mCommentGetter = new CommentGetter(params[0].intValue(), new HoaHelper(TrafficNetworkClient.ADDRESS));
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			lvComment.setAdapter(adapter);

			setProgressBarIndeterminateVisibility(true);
		}

	}

	public void showBigImage(View v) {
		imvBig.setVisibility(View.VISIBLE);

	}

	public void hideBigImage(View v) {
		imvBig.setVisibility(View.GONE);
	}

	public void voteUp(View v) {

	}

	public void voteDown(View v) {

	}
}

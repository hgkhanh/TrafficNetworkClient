package org.k2htm.tnc;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
	private boolean isRunning = true;
	private TrafficNetworkClient mApplication;
	private GetCommentTask mGetCommentTask;
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
		btnSendComment = (Button) findViewById(R.id.btnSendComment);
		// set on Click
		btnSendComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i(TAG, "click");
				if (!edtContentComment.getText().toString().equals(""))
					new SendCommentTask().execute(edtContentComment.getText()
							.toString());
				edtContentComment.setText("");
			}
		});
		// hide imvBig
		imvBig.setVisibility(View.GONE);
		loadInfo();
		new GetCommentTask().execute(cautionID);
		// loadComment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.activity_incident_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.comment_refresh:
			new GetCommentTask().execute(cautionID);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		isRunning = false;
		mGetCommentTask.cancel(true);
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

	void loadComment(ArrayList<Comment> comments) {

		// ArrayList<Comment> comments = new ArrayList<Comment>();
		// comments.add(new Comment("abc1", 2, "abc1", new Date().getTime()));
		// comments.add(new Comment("abc2", 2, "abc1", new Date().getTime()));
		// comments.add(new Comment("abc3", 2, "abc1", new Date().getTime()));
		// comments.add(new Comment("abc4", 2, "abc1", new Date().getTime()));
		// comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		// comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		// comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		// comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		// comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		// comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		// comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		// comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		// comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		// comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
		// comments.add(new Comment("abc5", 2, "abc1", new Date().getTime()));
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

	private class GetCommentTask extends
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
			CommentGetter mCommentGetter = new CommentGetter(
					params[0].intValue(), new HoaHelper(
							TrafficNetworkClient.ADDRESS));
			ArrayList<Comment> commentList;

			try {
				commentList = mCommentGetter.getComments(params[0].intValue());
				publishProgress(commentList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i(TAG, e.getMessage());
				if (isRunning) {
					publishProgress(null);
				}

			}

			return null;
		}

		@Override
		protected void onProgressUpdate(ArrayList<Comment>... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			if (values[0] == null) {
				Toast.makeText(IncidentDetailActivity.this,
						getText(R.string.network_error), Toast.LENGTH_SHORT)
						.show();
			} else {
				loadComment(values[0]);
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			lvComment.setAdapter(adapter);

			setProgressBarIndeterminateVisibility(true);
		}

	}

	private class SendCommentTask extends
			AsyncTask<String, String, ArrayList<Comment>> {
		CommentItemAdapter adapter;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Log.i(TAG, "pre");
		}

		@Override
		protected ArrayList<Comment> doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i(TAG, "do");
			Comment mComment = new Comment(mApplication.getUser(), cautionID,
					params[0], new HoaHelper(TrafficNetworkClient.ADDRESS));
			try {
				Log.i(TAG, "send comment");
				mComment.sendComment();
				publishProgress("ok");
				return new CommentGetter(cautionID, new HoaHelper(
						TrafficNetworkClient.ADDRESS)).getComments(cautionID);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i(TAG, e.getMessage());
				publishProgress(getText(R.string.network_error) + "");
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			Toast.makeText(IncidentDetailActivity.this, values[0],
					Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onPostExecute(ArrayList<Comment> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			setProgressBarVisibility(false);
			if (result != null) {
				loadComment(result);
			}
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

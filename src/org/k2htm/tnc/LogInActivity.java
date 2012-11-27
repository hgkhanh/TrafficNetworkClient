package org.k2htm.tnc;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.k2htm.clientHelper.HoaHelper;
import edu.k2htm.datahelper.User;

public class LogInActivity extends Activity {
	private EditText edtUsr, edtPass;
	private TextView btnReg;
	private Button btnConfirm;
	private TrafficNetworkClient mApplication;
	public static final String TAG = "Login Activity";

	private boolean check_usr_ok = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.login_layout);
		// app obj
		mApplication = (TrafficNetworkClient) getApplication();
		// get view
		edtUsr = (EditText) findViewById(R.id.edtUsername);
		edtPass = (EditText) findViewById(R.id.edtPassword);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnReg = (TextView) findViewById(R.id.btnReg);

		// on click listener
		Log.i(TAG, "asdf");
		// tvReg
		btnReg.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				// TODO Auto-generated method stub

				switch (motionEvent.getAction()) {
				case MotionEvent.ACTION_DOWN:
					btnReg.setTextColor(0xFFFF6600); // orange
					break;
				case MotionEvent.ACTION_UP:
					btnReg.setTextColor(0xFF000000); // black
					break;
				}
				return false;
			}
		});
		// tvReg
		btnReg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				register(v);
			}
		});
		// btnConfirm
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (edtUsr.getText().toString().equals("")
						|| edtPass.getText().toString().equals("")) {
					Log.i(TAG, "usr_str == null || pass_str == null");
					Toast.makeText(LogInActivity.this,
							R.string.toast_usr_pass_null, Toast.LENGTH_SHORT)
							.show();
				} else {
					// CHECK USERNAME AND PASSWORD
					String[] para = new String[2];
					para[0] = edtUsr.getText().toString();
					para[1] = edtPass.getText().toString();
					LoginTask task = new LoginTask();

					Log.i(TAG, "task execute");
					task.execute(para[0], para[1]);
				}
			}

		});
	}

	public void register(View view) {

		// call intent
		Intent oIntent = new Intent(LogInActivity.this, RegisterActivity.class);
		startActivity(oIntent);
		Log.i(TAG, "To Register.class ");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		btnReg.setTextColor(0xFF000000); // black
	}

	private class LoginTask extends AsyncTask<String, String, Boolean> {
		Boolean result;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			User mUser = new User(params[0], params[1], new HoaHelper(
					TrafficNetworkClient.ADDRESS));
			try {

				Log.i(TAG, "doinbackground result=checkuser()");
				result = mUser.checkUser();

				Log.i(TAG, "result=checkuser() ook\n result = " + result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i(TAG, e.getMessage());
				publishProgress(getText(R.string.network_error) + "");
				return false;
			}
			return result;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			Toast.makeText(LogInActivity.this, "Network Error",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			check_usr_ok = result;
			Log.i(TAG, check_usr_ok + "!");
			if (check_usr_ok) {
				Log.i(TAG, "check_usr_ok");
				// SET CURRENT USER IN APPLICATION OBJECT
				mApplication.setUser(edtUsr.getText().toString());
				Log.i(TAG, "Logged in as : " + mApplication.getUser());
				// call intent
				Intent oIntent = new Intent(LogInActivity.this,
						TrafficMap.class);
				startActivity(oIntent);
				Log.i(TAG,
						"Login successfully. To TrafficMap.class . Finalize LogInActivity.this");
				try {
					this.finalize();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Log.i(TAG, "check_usr_ok==false");
				Toast.makeText(LogInActivity.this,
						R.string.toast_usr_pass_check_failed,
						Toast.LENGTH_SHORT).show();
			}

			setProgressBarIndeterminateVisibility(false);
		}
	}
}

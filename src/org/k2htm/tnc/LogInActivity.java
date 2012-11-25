package org.k2htm.tnc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogInActivity extends Activity {
	private EditText edtUsr, edtPass;
	private TextView btnReg;
	private Button btnConfirm;
	private TrafficNetworkClient mApplication;
	public static final String TAG = "Login Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
				Log.i(TAG, "asdf");
				if (edtUsr.getText().toString().equals("")
						|| edtPass.getText().toString().equals("")) {
					Log.i(TAG, "usr_str == null || pass_str == null");
					Toast.makeText(LogInActivity.this,
							R.string.toast_usr_pass_null, Toast.LENGTH_SHORT)
							.show();
				} else {

					// CHECK USERNAME AND PASSWORD
					boolean check_usr_ok = true;
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
}

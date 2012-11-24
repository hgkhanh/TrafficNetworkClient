package org.k2htm.tnc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends Activity {
	private EditText edtUsr, edtPass;
	private Button btnConfirm;
	public static final String TAG = "Login Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		// get view
		edtUsr = (EditText) findViewById(R.id.edtUsername);
		edtPass = (EditText) findViewById(R.id.edtPassword);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);

		// on click listener
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String usr_str = edtUsr.getText().toString();
				String pass_str = edtPass.getText().toString();

				if (usr_str == null || pass_str == null) {
					Log.i(TAG, "usr_str == null || pass_str == null");
					Toast.makeText(LogInActivity.this,
							R.string.toast_usr_pass_null, Toast.LENGTH_SHORT)
							.show();
				}
				// CHECK USERNAME AND PASSWORD
				boolean check_usr_ok = false;
				if (check_usr_ok) {
					Log.i(TAG, "check_usr_ok");
					// SET CURRENT USER IN APPLICATION OBJECT
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
		});
	}
}

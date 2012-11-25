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

public class RegisterActivity extends Activity {
	private EditText edtUsr, edtPass,edtRePass;
	private Button btnConfirm;
	public static final String TAG = "Register Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reg_layout);
		// get view
		edtUsr = (EditText) findViewById(R.id.edtUsername);
		edtPass = (EditText) findViewById(R.id.edtPassword);
		edtRePass = (EditText) findViewById(R.id.edtRePassword);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		// on click listener
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (edtUsr.getText().toString().equals("")
						|| edtPass.getText().toString().equals("") || edtRePass.getText().toString().equals("")) {
					Log.i(TAG, "usr_str == null || pass_str == null");
					Toast.makeText(RegisterActivity.this,
							R.string.toast_usr_pass_null, Toast.LENGTH_SHORT)
							.show();
				}else if (edtPass.getText().toString().equals(edtRePass.getText().toString())) {
					Log.i(TAG, "pass != repass");
					Toast.makeText(RegisterActivity.this,
							R.string.toast_pass_repass_error, Toast.LENGTH_SHORT)
							.show();
				} 
				else {

					String usr_str = edtUsr.getText().toString();
					String pass_str = edtPass.getText().toString();

					Log.i(TAG, "adf");

					// CHECK USERNAME AND PASSWORD
					boolean check_usr_ok = true;
					if (check_usr_ok) {
						Log.i(TAG, "check_usr_ok");
						// CREATEM USER HERE
						// call intent
						Intent oIntent = new Intent(RegisterActivity.this,
								LogInActivity.class);
						//BUNDLE ?
						startActivity(oIntent);
						Log.i(TAG,
								"Register successfully. To LogInActivity.class . Finalize RegisterActivity.this");
						try {
							this.finalize();
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						Log.i(TAG, "check_usr_ok==false");
						Toast.makeText(RegisterActivity.this,
								R.string.toast_usr_pass_check_failed,
								Toast.LENGTH_SHORT).show();
					}

				}
			}
		});
	}
}

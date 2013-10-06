package com.mrjellybean.douknow;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends Activity {
	EditText uName,fName,pwd,confirmPwd,profession,mob,email;
	String ws_result,ws_result2;
	JSONObject json_data = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	setContentView(R.layout.register);
	
	uName = (EditText) findViewById(R.id.edtUserName);
	fName = (EditText) findViewById(R.id.edtFullName);
	pwd = (EditText) findViewById(R.id.edtPassword);
	confirmPwd = (EditText) findViewById(R.id.edtConfirmPwd);
	profession = (EditText) findViewById(R.id.edtProfession);
	mob = (EditText) findViewById(R.id.edtMobile);
	email = (EditText) findViewById(R.id.edtEmail);
	}
	
	public void resetForm(View v)
	{
		uName.setText("");
		fName.setText("");
		pwd.setText("");
		confirmPwd.setText("");
		profession.setText("");
		mob.setText("");
		email.setText("");
	}
	
	public void registerUser(View v)
	{
		int flag = 0;
		if(uName.getText().toString().equals(""))
		{
			Toast.makeText(this, "Please enter the User Name", Toast.LENGTH_LONG).show();
			flag = 1;
		}
		if(fName.getText().toString().equals(""))
		{
			Toast.makeText(this, "Please enter the Full Name", Toast.LENGTH_LONG).show();
		flag =1;
		}
		if(pwd.getText().toString().equals(""))
		{
			Toast.makeText(this, "Please enter the Password", Toast.LENGTH_LONG).show();
			flag =1;
		}
		
		if(!pwd.getText().toString().equals(confirmPwd.getText().toString()))
		{
			Toast.makeText(this, "Password confirmation failed", Toast.LENGTH_LONG).show();
			flag =1;
		}
		
		if(flag == 0)
		{
			TelephonyManager tManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
			final String Imei = tManager.getDeviceId();
			Thread t = new Thread() {
				public void run() {
					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("UserName",uName.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("FullName",fName.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("Password",pwd.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("Profession",profession.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("MobileNo",mob.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("Email",email.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("Imei",Imei));
					
					ws_result = HttpCon.getConnection(
							getResources().getString(R.string.ws_SaveUser),
							nameValuePairs);
				}
			};
			t.start();
			try {
				t.join();

				JSONArray jArray = new JSONArray(ws_result.substring(
						ws_result.indexOf("[{"), ws_result.indexOf("}]") + 2));

				json_data = jArray.getJSONObject(0);

			
				String result = json_data.getString("result");
				if(result.equals("1"))
				{
				  Intent i = new Intent(this,DoUKnow.class);
				  this.startActivity(i);
				}
				else if(result.equals("2"))
				{
					Toast.makeText(getBaseContext(), "User already exists...", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getBaseContext(), "Some problem has occurred.. Please try again", Toast.LENGTH_LONG).show();
					
				}
		}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		
		Intent i = new Intent(this, DoUKnow.class);
		this.startActivity(i);
	}
}

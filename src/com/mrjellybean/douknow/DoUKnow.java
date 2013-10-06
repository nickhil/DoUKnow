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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DoUKnow extends Activity {
	EditText uName, pwd;
	String ws_result,ws_result2,userName;
	JSONObject json_data = null;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.douknow);
	userName = getIntent().getStringExtra("userName");
	uName = (EditText) findViewById(R.id.edtUserName);
	pwd = (EditText) findViewById(R.id.edtPassword);
}

public void goToSignUp(View v)
{
	Intent i = new Intent(this, SignUp.class);
	this.startActivity(i);
}

public void loginUser(View v)
{
	int flag = 0;
	if(uName.getText().toString().equals(""))
	{
		Toast.makeText(this, "Please enter the User Name", Toast.LENGTH_LONG).show();
		flag = 1;
	}
	
	if(pwd.getText().toString().equals(""))
	{
		Toast.makeText(this, "Please enter the Password", Toast.LENGTH_LONG).show();
		flag =1;
	}
	
	if(flag == 0)
	{
		
		Thread t = new Thread() {
			public void run() {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("UserName",uName.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("Password",pwd.getText().toString()));
				
				ws_result = HttpCon.getConnection(
						getResources().getString(R.string.ws_LoginUser),
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
			  Intent i = new Intent(this,Home.class).putExtra("userName", uName.getText().toString());
			  this.startActivity(i);
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

}

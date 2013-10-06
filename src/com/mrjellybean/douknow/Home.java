package com.mrjellybean.douknow;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Home extends Activity {
	Spinner spnCategory, spnSubCategory;
	EditText edtQuestion;
	String ws_result, ws_result2;
	JSONObject json_data = null;
	static String userName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		userName = getIntent().getStringExtra("userName");
		spnCategory = (Spinner) findViewById(R.id.ddlCategory);
		spnSubCategory = (Spinner) findViewById(R.id.ddlSubCategory);
		edtQuestion = (EditText) findViewById(R.id.edtQuestion);
		final ArrayList<String> category = new ArrayList<String>();
		category.add("--Select Category--");
		category.add("School");
		category.add("College");
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, category);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCategory.setAdapter(aa);

		spnCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// based on item selection, fill the subcategory
				String Category = spnCategory.getItemAtPosition(position)
						.toString();
				if (Category == "--Select Category--") {
					final ArrayList<String> subCategory = new ArrayList<String>();

					ArrayAdapter<String> aa2 = new ArrayAdapter<String>(
							Home.this, android.R.layout.simple_spinner_item,
							subCategory);
					aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spnSubCategory.setAdapter(aa2);
				} else {
					if (Category == "School") {
						final ArrayList<String> subCategory = new ArrayList<String>();
						subCategory.add("--Select SubCategory--");
						subCategory.add("Language");
						subCategory.add("Social Studies");
						subCategory.add("Maths");
						subCategory.add("General Science");
						subCategory.add("Others");

						ArrayAdapter<String> aa2 = new ArrayAdapter<String>(
								Home.this,
								android.R.layout.simple_spinner_item,
								subCategory);
						aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spnSubCategory.setAdapter(aa2);
					} else if (Category == "College") {
						final ArrayList<String> subCategory = new ArrayList<String>();
						subCategory.add("--Select SubCategory--");
						subCategory.add("Science");
						subCategory.add("Commerce");
						ArrayAdapter<String> aa2 = new ArrayAdapter<String>(
								Home.this,
								android.R.layout.simple_spinner_item,
								subCategory);
						aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spnSubCategory.setAdapter(aa2);
					}
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	public void submitQuestion(View v) {
		Log.e("username", userName);
		if (spnCategory.getSelectedItem().toString() != "--Select Category--"
				&& spnSubCategory.getSelectedItem().toString() != "--Select SubCategory--"
				&& edtQuestion.getText().toString() != "") {
			Thread t = new Thread() {
				public void run() {
					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("AskedBy",
							userName));
					nameValuePairs.add(new BasicNameValuePair("Category",
							spnCategory.getSelectedItem().toString()));
					nameValuePairs.add(new BasicNameValuePair("SubCategory",
							spnSubCategory.getSelectedItem().toString()));
					nameValuePairs.add(new BasicNameValuePair("Question",
							edtQuestion.getText().toString()));

					ws_result = HttpCon.getConnection(
							getResources()
									.getString(R.string.ws_SubmitQuestion),
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
				if (result.equals("1")) {
					Toast.makeText(this, "Question submitted successfully",
							Toast.LENGTH_LONG).show();
					Intent i = new Intent(this, Home.class).putExtra("userName", userName);
					this.startActivity(i);
				} else if (result.equals("2")) {
					Toast.makeText(getBaseContext(),
							"Question already exists...", Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(getBaseContext(),
							"Some problem has occurred.. Please try again",
							Toast.LENGTH_LONG).show();

				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_LONG)
					.show();
		}
	}
@Override
public void onBackPressed() {
	
	Intent i = new Intent(this, DoUKnow.class).putExtra("userName", userName);
	this.startActivity(i);
}
	public void viewQuestions(View v) {
		Intent i = new Intent(this, ViewQuestions.class).putExtra("userName", userName);
		this.startActivity(i);
	}

}

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class ViewQuestions extends Activity {
	Spinner spnCategory, spnSubCategory;
	String ws_result, ws_result2, userName;
	JSONObject json_data = null;
	JSONArray jArray;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.viewqans);
	userName = getIntent().getStringExtra("userName");
	spnCategory = (Spinner) findViewById(R.id.ddlCategory);
	spnSubCategory = (Spinner) findViewById(R.id.ddlSubCategory);
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
						ViewQuestions.this, android.R.layout.simple_spinner_item,
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
					subCategory.add("Science");
					subCategory.add("Others");

					ArrayAdapter<String> aa2 = new ArrayAdapter<String>(
							ViewQuestions.this,
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
							ViewQuestions.this,
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
	
	spnSubCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			final ArrayList<question> QuestionList = new ArrayList<question>();
			// based on item selection, fill the subcategory
			final String SubCategory = spnSubCategory.getItemAtPosition(position)
					.toString();
			if (SubCategory == "--Select SubCategory--") {
				
				
			} else {
				
				// pass selected sub category as parameter to handle and get result
				Thread t = new Thread() {
					public void run() {
						ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair("SubCategory",
								SubCategory));
						ws_result = HttpCon.getConnection(
								getResources().getString(
										R.string.ws_GetQuestions),
								nameValuePairs);
					}
				};
				t.start();
				try {
					t.join();

					try {
						jArray = new JSONArray(ws_result.substring(
								ws_result.indexOf("[{"),
								ws_result.indexOf("}]") + 2));
					} catch (Exception e) {
						jArray = null;
					}
					if (jArray != null) {
						for (int i = 0; i < jArray.length(); i++) {
							json_data = jArray.getJSONObject(i);

							final String Question = json_data.getString("Question");
							final int QuestionNo = json_data.getInt("QNo");
							question q = new question();
							q.setQNo(QuestionNo);
							q.setQuestion(Question);
							QuestionList.add(q);
						}
						
					}
					else
					{
						Toast.makeText(ViewQuestions.this, "No record found", Toast.LENGTH_LONG).show();
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				final ListView lv1 = (ListView) findViewById(R.id.qAnsList);
				lv1.setAdapter(new MyCustomBaseAdapter(ViewQuestions.this, QuestionList));
				lv1.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> a, View v, int position,
							long id) {

						Object o = lv1.getItemAtPosition(position);
						question fullObject = (question) o;
						Intent i = new Intent(ViewQuestions.this,
								Answers.class).putExtra("QNo",
								fullObject.getQNo()+ "");
						i.putExtra("Question", fullObject.getQuestion());
						i.putExtra("userName", userName);
						ViewQuestions.this.startActivity(i);
					}
				});
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}
	});
}

@Override
public void onBackPressed() {

	Intent i = new Intent(this, Home.class).putExtra("userName", userName);
	this.startActivity(i);
}

}

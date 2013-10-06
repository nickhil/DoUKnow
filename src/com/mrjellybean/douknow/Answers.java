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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Answers extends Activity {
	static String Question, QNo, userName;
	EditText edtPostAns;
	TextView txtQuestion;
	String ws_result, ws_result2;
	JSONObject json_data = null;
	JSONArray jArray;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.answer);
	userName = getIntent().getStringExtra("userName");
	txtQuestion = (TextView) findViewById(R.id.txtQuestion);
	Question = getIntent().getStringExtra("Question");
	QNo = getIntent().getStringExtra("QNo");
	
	edtPostAns = (EditText) findViewById(R.id.edtPostAnswer);
	txtQuestion.setText("Q: " +Question);
	final ArrayList<answer> AnswerList = new ArrayList<answer>();
	Thread t = new Thread() {
		public void run() {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("QNo",
					QNo));
			ws_result = HttpCon.getConnection(
					getResources().getString(
							R.string.ws_GetAnswers),
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

				final String Answer = json_data.getString("Answers");
				final String AnsweredBy = json_data.getString("AnsweredBy");
				final String AnswerDate = json_data.getString("ADate");
				answer a = new answer();
				a.setAnswers(Answer);
				a.setAnsweredBy(AnsweredBy);
				a.setAnswerDate(AnswerDate);
				AnswerList.add(a);
			}
			
		}
		else
		{
			Toast.makeText(Answers.this, "No record found", Toast.LENGTH_LONG).show();
		}

	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	final ListView lv1 = (ListView) findViewById(R.id.qAnsList);
	lv1.setAdapter(new MyCustomBaseAdapter2(Answers.this, AnswerList));
}

public void Post(View v)
{
	// insert answer here (Qno, Answer, AnsweredBy
	
	
	if (edtPostAns.getText().toString() != "") {
	Thread t = new Thread() {
		public void run() {
			
			
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("QNo",
					QNo));
			nameValuePairs.add(new BasicNameValuePair("Answers",
					edtPostAns.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("AnsweredBy",
					userName));
			

			ws_result = HttpCon.getConnection(
					getResources()
							.getString(R.string.ws_PostAnswer),
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
			Toast.makeText(this, "Answer submitted successfully",
					Toast.LENGTH_LONG).show();
			Intent i = new Intent(this, Answers.class).putExtra("userName", userName);
			i.putExtra("QNo", QNo);
			i.putExtra("Question", Question);
			this.startActivity(i);
		} else if (result.equals("2")) {
			Toast.makeText(getBaseContext(),
					"Answer already exists...", Toast.LENGTH_LONG)
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
	Toast.makeText(this, "Please enter the answer", Toast.LENGTH_LONG)
			.show();
}

}

@Override
public void onBackPressed() {

	Intent i = new Intent(this, ViewQuestions.class).putExtra("userName", userName);
	this.startActivity(i);
}
}

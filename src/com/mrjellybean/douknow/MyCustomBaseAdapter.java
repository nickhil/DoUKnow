package com.mrjellybean.douknow;

import java.util.ArrayList;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyCustomBaseAdapter extends BaseAdapter {
	private static ArrayList<question> QuestionList;
	
	private LayoutInflater mInflater;

	public MyCustomBaseAdapter(Context context, ArrayList<question> results) {
		QuestionList = results;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return QuestionList.size();
	}

	public Object getItem(int position) {
		return QuestionList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.custom_row_view, null);
			holder = new ViewHolder();
			holder.Question = (TextView) convertView.findViewById(R.id.txtQuestion);
			holder.QuestionNo = (TextView) convertView.findViewById(R.id.txtQuestionNo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.QuestionNo.setText("Q"+QuestionList.get(position).getQNo());
		holder.Question.setText(QuestionList.get(position).getQuestion());
		
		return convertView;
	}

	static class ViewHolder {
		TextView Question;
		TextView QuestionNo;
		
	}
}

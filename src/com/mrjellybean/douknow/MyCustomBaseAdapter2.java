package com.mrjellybean.douknow;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mrjellybean.douknow.MyCustomBaseAdapter.ViewHolder;

public class MyCustomBaseAdapter2 extends BaseAdapter{

private static ArrayList<answer> AnswerList;
	
	private LayoutInflater mInflater;

	public MyCustomBaseAdapter2(Context context, ArrayList<answer> results) {
		AnswerList = results;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return AnswerList.size();
	}

	public Object getItem(int position) {
		return AnswerList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.custom_row_view2, null);
			holder = new ViewHolder();
			holder.Answers = (TextView) convertView.findViewById(R.id.txtAnswer);
			holder.AnsweredBy = (TextView) convertView.findViewById(R.id.txtAnsweredBy);
			holder.AnswerDate = (TextView) convertView.findViewById(R.id.txtAnswerDate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.Answers.setText(AnswerList.get(position).getAnswers());
		holder.AnsweredBy.setText(AnswerList.get(position).getAnsweredBy());
		holder.AnswerDate.setText(AnswerList.get(position).getAnswerDate()+"");
		return convertView;
	}

	static class ViewHolder {
		TextView Answers;
		TextView AnsweredBy;
		TextView AnswerDate;
		
	}
}

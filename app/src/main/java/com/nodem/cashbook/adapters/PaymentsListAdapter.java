package com.nodem.cashbook.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vmk.cashbook.R;

public class PaymentsListAdapter extends BaseAdapter{
	private List<String> names,dates,notes;
	
	private List<Integer> credits,debits;
	private Context context;

	public PaymentsListAdapter(Context context,List<String> names,List<String> dates,List<String> notes,List<Integer> credits,List<Integer> debits) {
		this.names=names;
		this.dates=dates;
		this.notes =notes;
		this.credits =credits;
		this.debits =debits;
		this.context= context;
				
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return names.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView ==null)
			convertView = View.inflate(context, R.layout.row_payment,null);
		
		
		LinearLayout container =  (LinearLayout)convertView.findViewById(R.id.row_payment);
		
		if(position %2 == 0)
			container.setBackgroundColor(Color.parseColor("#DDDDDD"));
		else
			container.setBackgroundColor(Color.parseColor("#FFFFFF"));
		
		TextView name =(TextView)convertView.findViewById(R.id.txt_accName_payment);
		TextView month =(TextView)convertView.findViewById(R.id.txt_payment_month);
		TextView date =(TextView)convertView.findViewById(R.id.txt_payment_day);

		TextView note =(TextView)convertView.findViewById(R.id.txt_payment_note);
		TextView bal =(TextView)convertView.findViewById(R.id.txt_payment_amount);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formater = new SimpleDateFormat("dd-MMM-yyyy");
		
		
		
		name.setText(names.get(position));
		try {
			Date day = format.parse(dates.get(position));
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(day);
			
			
			month.setText(getMonth(cal.get(Calendar.MONDAY)));
			int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
			
			date.setText(day_of_month<10?"0"+day_of_month:""+day_of_month);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		note.setText(notes.get(position));
		bal.setText(""+(credits.get(position)==0?debits.get(position):credits.get(position)));
		bal.setCompoundDrawablesWithIntrinsicBounds(credits.get(position)==0?context.getResources().getDrawable(R.drawable.plus):context.getResources().getDrawable(R.drawable.minus), null, null, null);
		
		
		return convertView;
	}
	
	public String getMonth(int i){
		if(i ==0)
			return "JAN";
		if(i == 1)
			return "FEB";
		if(i == 2)
			return "MAR";
		if(i == 3)
			return "APR";
	    if(i == 5)
			return  "JUN";
	    if(i == 6)
			return  "JUL";
	    if(i == 7)
			return  "AUG";
	    if(i == 8)
			return  "SEP";
	    if(i == 9)
			return  "OCT";
	    if(i == 10)
			return  "NOV";
	    if(i == 11)
			return  "DEC";

      return "XXX";
	}

}

package com.nodem.cashbook.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vmk.cashbook.R;

public class LedgerAdapter extends BaseAdapter{

	private List<String> dates,amnt,dues;
	private Context context;
	public LedgerAdapter(Context context,List<String> dates,List<String> amnt,List<String> dues) {
		this.dates=dates;
		this.amnt=amnt;
		this.dues=dues;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dates.size();
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
	public View getView(int position, View vi, ViewGroup parent) {
		if(vi==null)
			vi= View.inflate(context, R.layout.row_ledger, null);
		
		TextView date= (TextView)vi.findViewById(R.id.txt_ldate);
		TextView amnt= (TextView)vi.findViewById(R.id.txt_lamnt);
		TextView due =(TextView)vi.findViewById(R.id.txt_ldue);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formater = new SimpleDateFormat("dd-MMM-yyyy");
		
		try {
			Date d= format.parse(dates.get(position));
			Date ddue = format.parse(dues.get(position));
			
			date.setText(formater.format(d));
			amnt.setText(this.amnt.get(position));
			due.setText("due on "+formater.format(ddue));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return vi;
	}

}

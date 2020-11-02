package com.nodem.cashbook.adapters;

import java.util.List;



import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vmk.cashbook.R;

public class DashBoardListAdapter extends BaseAdapter {
	
	private Context context;
	private List<String> accounts;
	private List<Integer> totals;

	public DashBoardListAdapter(Context context,List<String> accounts,List<Integer> totals) {
	this.context=context;
	this.accounts =accounts;
	this.totals=totals;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return accounts.size();
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
		
		if(vi ==null)
			vi = View.inflate(context, R.layout.row_dashboard,null);
		
		TextView name =(TextView)vi.findViewById(R.id.txt_acName);
		TextView amnt = (TextView)vi.findViewById(R.id.txt_amnt);		
		
		name.setText(accounts.get(position));
		amnt.setText(""+totals.get(position));
		
		
		return vi;
	}

}

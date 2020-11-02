package com.nodem.cashbook.adapters;



import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vmk.cashbook.R;

public class SettingsPageListAdapter extends BaseAdapter {

	private String[] titles;
	private String[] desc;
	private int[] icons;
	private Context context;

	public SettingsPageListAdapter(Context context,String[] titles, String[] desc, int[] icons) {
		this.titles = titles;
		this.desc = desc;
		this.icons = icons;
		this.context =context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles.length;
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
		
		if(convertView == null){
			convertView = View.inflate(context, R.layout.row_settings, null);
		
			TextView title =(TextView)convertView.findViewById(R.id.txt_title);
			TextView desc =(TextView)convertView.findViewById(R.id.txt_desc);
			ImageView icon =(ImageView)convertView.findViewById(R.id.img_icon);
			
			title.setText(titles[position]);
			desc.setText(this.desc[position]);
			icon.setImageDrawable(context.getResources().getDrawable(icons[position]));
			
		
		}
		
		return convertView;
	}

}

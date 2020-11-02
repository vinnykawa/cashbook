package com.nodem.cashbook.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.vmk.cashbook.R;

public class AccountsListAdapter extends BaseAdapter {

	private List<String> names = new ArrayList<String>();
	private List<String> contacts = new ArrayList<String>();
	private Context context;
	HashMap<String, String> credits, debits;

	public AccountsListAdapter(Context con, List<String> names,
			List<String> contacts, HashMap<String, String> debits,
			HashMap<String, String> creditors) {
		this.names = names;
		this.contacts = contacts;
		this.context = con;
		this.credits = creditors;
		this.debits = debits;

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

		if (convertView == null)
			convertView = View.inflate(context, R.layout.row_account, null);

		TextView acc = (TextView) convertView.findViewById(R.id.txt_acc_name);
		TextView phone = (TextView) convertView.findViewById(R.id.txt_contact);
		TextView bal = (TextView) convertView.findViewById(R.id.txt_bal);

		acc.setText(names.get(position));
		phone.setText("" + contacts.get(position));

		bal.setText((credits.get(names.get(position)) == null && debits
				.get(names.get(position)) == null) ? "" : credits.get(names
				.get(position)) == null ? debits.get(names.get(position))
				+ "Dr" : credits.get(names.get(position)) + "Cr");

		// Toast.makeText(context, this.names.size()+
		// "as size, pos "+position, 1
		// ).show();

		return convertView;
	}

	

}

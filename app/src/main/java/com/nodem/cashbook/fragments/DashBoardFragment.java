package com.nodem.cashbook.fragments;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nodem.cashbook.Ledger;
import com.nodem.cashbook.NewTransaction;

import com.nodem.cashbook.adapters.DashBoardListAdapter;
import com.nodem.cashbook.adapters.DashboardAdapter;
import com.nodem.cashbook.db.DBReader;
import com.vmk.cashbook.R;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DashBoardFragment extends Fragment {

	private ListView list_credit,list_debit;
	private TextView credit,debit,creditTotal,debitTotal;
	FloatingActionButton fab;
	public static DashBoardFragment newInstance() {
		DashBoardFragment fragment = new DashBoardFragment();

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dashboard2, container, false);

		initViews(rootView);
		refreshList();
		return rootView;
	}

	private void initViews(View vi) {
		 debit = (TextView)vi.findViewById(R.id.title_debit);
		 credit =(TextView)vi.findViewById(R.id.title_credit);

		 creditTotal =(TextView)vi.findViewById(R.id.credit_total);
		debitTotal =(TextView)vi.findViewById(R.id.debit_total);


		list_credit = (ListView)vi.findViewById(R.id.list_credit);
		list_debit = (ListView)vi.findViewById(R.id.list_debit);

		fab = (FloatingActionButton)vi.findViewById(R.id.fab);
		
		
	}
	
	@Override
	public void onResume() {
		refreshList();
		
		super.onResume();
	}

	public void refreshList(){
		DBReader db = new DBReader(getActivity());
		DBReader.TransactionsTable transactions = db.new TransactionsTable();
		final DBReader.TransactionsTable.DashBoard dash = transactions.new DashBoard();
		
		int totalCredit = 0;
		for(int i:dash.getCreditTotals())
			totalCredit+=i;
		
		int totalDebit =0;
		for(int i:dash.getDebitTotals())
			totalDebit +=i;
		
			creditTotal.setText("$("+totalCredit+")");
			debitTotal.setText("$("+totalDebit+")");
			
			
			DashBoardListAdapter creditAdapter = new DashBoardListAdapter(getActivity(), dash.getCreditors(), dash.getCreditTotals());
			DashBoardListAdapter debitAdapter = new DashBoardListAdapter(getActivity(), dash.getDebtors(), dash.getDebitTotals());

		DashboardAdapter transaction = new DashboardAdapter(getActivity(), dash.getDebtors(), dash.getDebitTotals());



		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getContext(), NewTransaction.class));

			}
		});



		
			list_credit.setAdapter(creditAdapter);
			list_debit.setAdapter(debitAdapter);
			
			list_credit.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String name =dash.getCreditors().get(position);
					String amnt =dash.getCreditTotals().get(position)+" Cr";
					
					Intent i= new Intent(getActivity(),Ledger.class);
					i.putExtra("name", name);
					i.putExtra("amount", amnt);
					//i.putExtra("phone", dash.getContacts(position));
					startActivity(i);
					
				}
			});
			
			list_debit.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String name =dash.getDebtors().get(position);
					String amnt =dash.getDebitTotals().get(position)+" Dr";
					
					Intent i= new Intent(getActivity(),Ledger.class);
					i.putExtra("name", name);
					i.putExtra("amount", amnt);
					//i.putExtra("phone", dash.getContacts(position));
					startActivity(i);
					
				}
			});
		
	}
}
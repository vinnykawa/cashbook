package com.nodem.cashbook.fragments;


import com.nodem.cashbook.adapters.PaymentsListAdapter;
import com.nodem.cashbook.db.DBReader;
import com.vmk.cashbook.R;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ListView;

public class PaymentFragment extends Fragment {

	private ListView list;
	public static PaymentFragment newInstance() {

		return new PaymentFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.payments, container, false);

		initViews(rootView);

		return rootView;
	}

	private void initViews(View vi) {
		 list = (ListView) vi.findViewById(R.id.list_payments);

		refreshListView();
	}

	private void refreshListView() {
		DBReader db = new DBReader(getActivity());
		DBReader.TransactionsTable transactions = db.new TransactionsTable();

		PaymentsListAdapter adapter = new PaymentsListAdapter(getActivity(),
				transactions.getTransactionAccountNames(),
				transactions.getTransactionDates(),
				transactions.getTransactionNotes(),
				transactions.getCreditTransactions(),
				transactions.getDebitTransactions());
		
		list.setAdapter(adapter);

	}
	
	
	@Override
	public void onResume() {
		refreshListView();
		
		super.onResume();
	}

}

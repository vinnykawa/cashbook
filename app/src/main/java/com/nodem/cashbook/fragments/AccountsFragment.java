package com.nodem.cashbook.fragments;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.nodem.cashbook.Ledger;
import com.nodem.cashbook.NewAccount;
import com.nodem.cashbook.adapters.AccountsListAdapter;
import com.nodem.cashbook.db.DBReader;
import com.nodem.cashbook.net.SyncToServer;
import com.vmk.cashbook.R;

public class AccountsFragment extends Fragment implements OnClickListener {

	private ImageButton addNew, addContacts, addSearch;
	private EditText search;
	private ListView list;
	private static final int PICK_CONTACT = 100;
	private AdView mAdView;

	public static AccountsFragment newInstance() {
		return new AccountsFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View vi = inflater.inflate(R.layout.accounts, container, false);

		MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {

			}
		});
		mAdView = vi.findViewById(R.id.adViewAcc);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		initViews(vi);

		return vi;
	}

	private void initViews(View vi) {
		addNew = (ImageButton) vi.findViewById(R.id.btn_add);
		addContacts = (ImageButton) vi.findViewById(R.id.btn_contacts);
		addSearch = (ImageButton) vi.findViewById(R.id.btn_seach);
		search = (EditText) vi.findViewById(R.id.ed_search);
		list = (ListView) vi.findViewById(R.id.list_accounts);

		registerForContextMenu(list);

		refreshListView();

		addNew.setOnClickListener(this);
		addContacts.setOnClickListener(this);
		addSearch.setOnClickListener(this);

		// s Toast.makeText(getActivity(),
		// db.getAccNames().size()+"records found", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add("Delete Account");

		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// Get the list item position
		DBReader reader = new DBReader(getActivity());
		DBReader.AccountsTable db = reader.new AccountsTable();

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int index = info.position;
		db.dropAccount(db.getAccNames().get(index));
		refreshListView();
		//Toast.makeText(getActivity(), db.getAccNames().get(index), Toast.LENGTH_LONG).show();
		return super.onContextItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (v == addNew)
			startActivity(new Intent(getActivity(), NewAccount.class));
		else if (v == addContacts)
			startActivityForResult(new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI), PICK_CONTACT);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PICK_CONTACT) {

			Uri contactData = data.getData();
			Cursor c = getActivity().managedQuery(contactData, null, null,
					null, null);
			String cNumber = null;
			if (c.moveToFirst()) {

				String id = c.getString(c
						.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

				String hasPhone = c
						.getString(c
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

				if (hasPhone.equalsIgnoreCase("1")) {
					Cursor phones = getActivity().getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + id, null, null);
					phones.moveToFirst();
					cNumber = phones.getString(phones.getColumnIndex("data1"));

				}
				String name = c
						.getString(c
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

				DBReader db = new DBReader(getActivity());

				db.insertIntoAccountsTable(name, cNumber, "", "");

			}
		}

	}

	@Override
	public void onResume() {
		refreshListView();
		syncToServer();
		super.onResume();
	}

	public void syncToServer() {
		new SyncToServer(getActivity(), false).execute();
	}

	HashMap<String, String> debtors, creditors;

	public void refreshListView() {
		DBReader reader = new DBReader(getActivity());
		final DBReader.AccountsTable db = reader.new AccountsTable();
		DBReader.TransactionsTable trans = reader.new TransactionsTable();
		final DBReader.TransactionsTable.DashBoard dash = trans.new DashBoard();

		debtors = new HashMap<String, String>();
		creditors = new HashMap<String, String>();

		for (int i = 0; i < dash.getDebtors().size(); i++) {
			debtors.put(dash.getDebtors().get(i), dash.getDebitTotals().get(i)
					.toString());
		}

		for (int i = 0; i < dash.getCreditors().size(); i++) {
			creditors.put(dash.getCreditors().get(i), dash.getCreditTotals()
					.get(i).toString());
		}

		final AccountsListAdapter adapter = new AccountsListAdapter(
				getActivity(), db.getAccNames(), db.getAccContacts(), debtors,
				creditors);

		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String name = null, amnt = null;
				name = db.getAccNames().get(position);

				amnt = creditors.get(name) == null ? debtors.get(name) + " Dr"
						: creditors.get(name) + " Cr";

				Intent i = new Intent(getActivity(), Ledger.class);
				i.putExtra("name", name);
				i.putExtra("amount", amnt);
				//i.putExtra("phone", dash.getContacts(position));
				startActivity(i);

			}
		});

	}
}

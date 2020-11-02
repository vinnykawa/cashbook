package com.nodem.cashbook.fragments;

import com.nodem.cashbook.Backup;
import com.nodem.cashbook.PasswordSetting;

import com.nodem.cashbook.adapters.SettingsPageListAdapter;
import com.vmk.cashbook.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SettingsFragment extends Fragment implements OnItemClickListener {

	private String[] titles = new String[] { "Share", "Contact us",
			"Password Setting", "Cashbook Backup", "Rate Cashbook", "Help" };
	private String[] desc = new String[] { "share cashbook to your group",
			"communication details", "set - reset/ change your password",
			"back up / restore yourr cashbook entries", "please rate us",
			"checkout help as guidance" };

	private int[] icons = new int[] { R.drawable.share_icon,
			R.drawable.phone_icon, R.drawable.key_icon, R.drawable.backup_icon,
			R.drawable.rate_icon, R.drawable.help_icon

	};

	public static SettingsFragment newInstance() {
		return new SettingsFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View vi = inflater.inflate(R.layout.settings, container, false);

		ListView list = (ListView) vi.findViewById(R.id.list_settings);

		SettingsPageListAdapter adapter = new SettingsPageListAdapter(
				getActivity(), titles, desc, icons);

		list.setAdapter(adapter);

		list.setOnItemClickListener(this);

		return vi;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {

		case 0:
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent
					.putExtra(
							Intent.EXTRA_TEXT,
							"Maintain you accounts and cashbook with http://play.google.com/store/apps/details?id="
									+ getActivity().getPackageName());
			shareIntent.setType("text/plain");

			startActivity(shareIntent);

			break;
		case 1:
			String message = "We are thanking you for using this app. Write us on vinnykawa@gmail.com";
			launchDialog("Contact us", message);
			break;
		case 2:

			startActivity(new Intent(getActivity(),PasswordSetting.class));
			break;
		case 3:
			startActivity(new Intent(getActivity(), Backup.class));
			break;
		case 4:
			rateOnPlaystore();
			break;
		case 5:
			String msg = "Cash Book is to maintain your credit accounts and personal ledger(party wise).\n\n"
					+ "You can also get summary of your cash flow and due reminders .\n\n"
					+ "Note:Please do not loose or share your password with others because each and every information is confidential to you.\n\n"
					+ "Dashboard screen displays summary of all transactions and account which has either credit or debit amount.\n\n"
					+ "Payment screen shows list of all payments. \n\n"
					+ "Account displays all accounts with their details with all transactions remaining credit or debit balance.\n\n"
					+ "You can create a new account or import account from contact book through given options in the account.\n\n"
					+ "You can create a new transaction through menu options\n\n"
					+ "Setting contains information about us, you can also rate our application with your precious review about the app, which helps us to improve our application.\n\n";

			launchDialog("Help", msg);
			break;

		default:
			break;
		}

	}

	private void viewPasswordSettings() {
		// TODO Auto-generated method stub

	}

	private void rateOnPlaystore() {
		Uri uri = Uri.parse("market://details?id="
				+ getActivity().getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
			startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id="
							+ getActivity().getPackageName())));
		}

	}

	private void launchDialog(String title, String message) {
		AlertDialog.Builder bd = new AlertDialog.Builder(getActivity());
		bd.setTitle(title);
		bd.setMessage(message);
		bd.setNegativeButton("Ok", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		bd.show();

	}

}

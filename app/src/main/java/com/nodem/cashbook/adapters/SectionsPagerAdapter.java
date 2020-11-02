package com.nodem.cashbook.adapters;

import java.util.Locale;

import com.nodem.cashbook.fragments.AccountsFragment;
import com.nodem.cashbook.fragments.DashBoardFragment;
import com.nodem.cashbook.fragments.PaymentFragment;
import com.nodem.cashbook.fragments.SettingsFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			
			switch (position) {
			case 0:	
				return DashBoardFragment.newInstance();
				
			case 1:
				return PaymentFragment.newInstance();
			case 2:
				return AccountsFragment.newInstance();
			case 3:
				return SettingsFragment.newInstance();

			default:
				 return null;
			}
		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "Dashboard";
			case 1:
				return "Payment";
			case 2:
				return "Account";
			case 3:
				return "Settings";
			}
			return null;
		}
	}
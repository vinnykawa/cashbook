package com.nodem.cashbook.core;

public final class CashbookAmounts {
	public static final int CREDIT = 1;
	public static final int DEBIT = -1;

	private CashbookAmounts() {
	}

	public static int netCreditBalance(int creditTotal, int debitTotal) {
		return creditTotal - debitTotal;
	}

	public static int absoluteBalance(int creditTotal, int debitTotal) {
		int balance = netCreditBalance(creditTotal, debitTotal);
		return balance < 0 ? -balance : balance;
	}

	public static int balanceSide(int creditTotal, int debitTotal) {
		return netCreditBalance(creditTotal, debitTotal) > 0 ? CREDIT : DEBIT;
	}

	public static String transactionAmountLabel(int credit, int debit) {
		return credit > 0 ? credit + " Cr" : debit + " Dr";
	}
}

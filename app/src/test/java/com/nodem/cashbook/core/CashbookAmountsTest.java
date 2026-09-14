package com.nodem.cashbook.core;

public class CashbookAmountsTest {
	public static void main(String[] args) {
		keepsCreditBalancesPositiveAfterSubtractingDebits();
		keepsDebitBalancesAsPositiveDisplayAmounts();
		formatsSingleTransactionAmountByLedgerSide();
		System.out.println("OK (3 tests)");
	}

	private static void keepsCreditBalancesPositiveAfterSubtractingDebits() {
		assertEquals(75, CashbookAmounts.netCreditBalance(125, 50), "net credit balance");
		assertEquals(75, CashbookAmounts.absoluteBalance(125, 50), "credit display amount");
		assertEquals(CashbookAmounts.CREDIT, CashbookAmounts.balanceSide(125, 50), "credit side");
	}

	private static void keepsDebitBalancesAsPositiveDisplayAmounts() {
		assertEquals(-75, CashbookAmounts.netCreditBalance(50, 125), "net debit balance");
		assertEquals(75, CashbookAmounts.absoluteBalance(50, 125), "debit display amount");
		assertEquals(CashbookAmounts.DEBIT, CashbookAmounts.balanceSide(50, 125), "debit side");
		assertEquals(0, CashbookAmounts.absoluteBalance(100, 100), "settled display amount");
	}

	private static void formatsSingleTransactionAmountByLedgerSide() {
		assertEquals("250 Cr", CashbookAmounts.transactionAmountLabel(250, 0), "credit label");
		assertEquals("400 Dr", CashbookAmounts.transactionAmountLabel(0, 400), "debit label");
	}

	private static void assertEquals(Object expected, Object actual, String label) {
		if (!expected.equals(actual)) {
			throw new AssertionError(label + ": expected <" + expected + "> but was <" + actual + ">");
		}
	}
}
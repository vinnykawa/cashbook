package com.nodem.cashbook.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The ledger arithmetic behind the creditors and debtors lists.
 *
 * A running balance is a credit total minus a debit total, so it is signed, but
 * the screens never show a negative number. They show a positive amount on one
 * side of the ledger. Getting that wrong shows a customer's debt as a credit,
 * which is why these are worth pinning.
 */
public class CashbookAmountsTest {

    @Test
    public void keepsCreditBalancesPositiveAfterSubtractingDebits() {
        assertEquals(75, CashbookAmounts.netCreditBalance(125, 50));
        assertEquals(75, CashbookAmounts.absoluteBalance(125, 50));
        assertEquals(CashbookAmounts.CREDIT, CashbookAmounts.balanceSide(125, 50));
    }

    @Test
    public void keepsDebitBalancesAsPositiveDisplayAmounts() {
        assertEquals(-75, CashbookAmounts.netCreditBalance(50, 125));
        assertEquals(75, CashbookAmounts.absoluteBalance(50, 125));
        assertEquals(CashbookAmounts.DEBIT, CashbookAmounts.balanceSide(50, 125));
    }

    /**
     * A fully settled account has a balance of zero, and DBReader sends it to
     * the debtors list rather than the creditors list. That is the shipped
     * behaviour, recorded here so that changing it has to be deliberate.
     */
    @Test
    public void treatsASettledAccountAsADebtorWithNothingOwing() {
        assertEquals(0, CashbookAmounts.netCreditBalance(100, 100));
        assertEquals(0, CashbookAmounts.absoluteBalance(100, 100));
        assertEquals(CashbookAmounts.DEBIT, CashbookAmounts.balanceSide(100, 100));
    }

    @Test
    public void neverReportsANegativeAmountToTheScreen() {
        int[][] pairs = {{0, 0}, {1, 0}, {0, 1}, {40, 40}, {999, 1}, {1, 999}};

        for (int[] pair : pairs) {
            int shown = CashbookAmounts.absoluteBalance(pair[0], pair[1]);
            assertTrue(
                    "credit " + pair[0] + " and debit " + pair[1] + " produced " + shown,
                    shown >= 0);
        }
    }

    @Test
    public void labelsASingleTransactionByTheSideItWasEnteredOn() {
        assertEquals("250 Cr", CashbookAmounts.transactionAmountLabel(250, 0));
        assertEquals("400 Dr", CashbookAmounts.transactionAmountLabel(0, 400));
    }
}

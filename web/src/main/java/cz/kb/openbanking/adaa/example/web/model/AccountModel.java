package cz.kb.openbanking.adaa.example.web.model;

import javax.annotation.Nullable;

/**
 * Represents account with IBAN, currency and account ID.
 *
 * @since 1.2
 */
public class AccountModel {
    /**
     * Account's ID.
     */
    private String accountId;

    /**
     * Account's IBAN.
     */
    private String iban;

    /**
     * Account's currency.
     */
    private String currency;

    /**
     * Gets account ID.
     *
     * @return ID of account
     */
    @Nullable
    public String getAccountId() {
        return accountId;
    }

    /**
     * Sets ID of account.
     *
     * @param accountId ID of account
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * Gets account IBAN.
     *
     * @return IBAN of account
     */
    @Nullable
    public String getIban() {
        return iban;
    }

    /**
     * Sets IBAN of account.
     *
     * @param iban IBAN of account
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * Gets account currency.
     *
     * @return currency of account
     */
    @Nullable
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets currency of account.
     *
     * @param currency currency of account
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

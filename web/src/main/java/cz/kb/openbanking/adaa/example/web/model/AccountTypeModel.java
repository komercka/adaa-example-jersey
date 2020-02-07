package cz.kb.openbanking.adaa.example.web.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Contains all possible account types.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public enum AccountTypeModel {

    /**
     * Komcercni Banka account.
     */
    KB("KB"),

    /**
     * Account from another bank.
     */
    AG("AG");

    private String accountType;

    /**
     * No instance.
     *
     * @param accountType account type
     */
    AccountTypeModel(String accountType) {
        if (StringUtils.isBlank(accountType)) {
            throw new IllegalArgumentException("accountType must not be null");
        }

        this.accountType = accountType;
    }

    /**
     * Gets account type.
     *
     * @return account type
     */
    public String getAccountType() {
        return accountType;
    }
}

package cz.kb.openbanking.adaa.example.web.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Contains indicators of transaction type: credit or debit.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public enum CreditDebitIndicatorModel {
    CREDIT("CREDIT"),
    DEBIT("DEBIT");

    private String creditDebitIndicator;

    /**
     * No instance.
     *
     * @param creditDebitIndicator indicators of transaction type: credit or debit
     */
    CreditDebitIndicatorModel(String creditDebitIndicator) {
        if (StringUtils.isBlank(creditDebitIndicator)) {
            throw new IllegalArgumentException("creditDebitIndicator must not be null");
        }

        this.creditDebitIndicator = creditDebitIndicator;
    }

    /**
     * Gets indicators of transaction type: credit or debit.
     *
     * @return indicators of transaction type: credit or debit
     */
    public String getCreditDebitIndicator() {
        return creditDebitIndicator;
    }
}

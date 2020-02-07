package cz.kb.openbanking.adaa.example.web.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Contains all possible transaction types.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public enum TransactionTypeModel {
    INTEREST("INTEREST"),
    FEE("FEE"),
    DOMESTIC("DOMESTIC"),
    FOREIGN("FOREIGN"),
    SEPA("SEPA"),
    CASH("CASH"),
    CARD("CARD"),
    OTHER("OTHER");

    private String transactionType;

    /**
     * No instance.
     *
     * @param transactionType type of transaction
     */
    TransactionTypeModel(String transactionType) {
        if (StringUtils.isBlank(transactionType)) {
            throw new IllegalArgumentException("transactionType must not be null");
        }

        this.transactionType = transactionType;
    }

    /**
     * Gets type of transaction.
     *
     * @return type of transaction
     */
    public String getTransactionType() {
        return transactionType;
    }
}

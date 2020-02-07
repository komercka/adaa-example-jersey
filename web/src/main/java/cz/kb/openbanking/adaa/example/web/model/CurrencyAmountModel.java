package cz.kb.openbanking.adaa.example.web.model;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

/**
 * Represents amount with currency object.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public class CurrencyAmountModel {
    private Double value;
    private String currency;

    /**
     * Gets amount value.
     *
     * @return amount value
     */
    @Nullable
    public Double getValue() {
        return value;
    }

    /**
     * Sets amount value.
     *
     * @param value amount value
     */
    public void setValue(Double value) {
        if (value == null) {
            throw new IllegalArgumentException("amount value must not be null");
        }
        this.value = value;
    }

    /**
     * Gets currency.
     *
     * @return currency
     */
    @Nullable
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets currency.
     *
     * @param currency currency
     */
    public void setCurrency(String currency) {
        if (StringUtils.isBlank(currency)) {
            throw new IllegalArgumentException("currency must not be empty");
        }
        this.currency = currency;
    }
}

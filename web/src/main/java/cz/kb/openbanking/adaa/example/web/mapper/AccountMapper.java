package cz.kb.openbanking.adaa.example.web.mapper;

import javax.annotation.Nullable;

import cz.kb.openbanking.adaa.client.model.generated.AccountBalance;
import cz.kb.openbanking.adaa.client.model.generated.AccountTransaction;
import cz.kb.openbanking.adaa.client.model.generated.Statement;
import cz.kb.openbanking.adaa.example.web.model.AccountBalanceModel;
import cz.kb.openbanking.adaa.example.web.model.StatementModel;
import cz.kb.openbanking.adaa.example.web.model.TransactionModel;
import org.mapstruct.Mapper;

/**
 * Mapper interface.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
@Mapper
public interface AccountMapper {

    /**
     * Maps {@link AccountTransaction} to the {@link TransactionModel}.
     *
     * @param transaction {@link AccountTransaction}
     * @return {@code null} if input {@link AccountTransaction} is {@code null}, otherwise - {@link TransactionModel}
     */
    @Nullable
    TransactionModel toTransactionModel(@Nullable AccountTransaction transaction);

    /**
     * Maps {@link AccountBalance} to the {@link AccountBalanceModel}.
     *
     * @param accountBalance {@link AccountBalance}
     * @return {@code null} if input {@link AccountBalance} is {@code null}, otherwise - {@link AccountBalanceModel}
     */
    @Nullable
    AccountBalanceModel toAccountBalanceModel(@Nullable AccountBalance accountBalance);

    /**
     * Maps {@link Statement} to the {@link StatementModel}.
     *
     * @param statement {@link Statement}
     * @return {@code null} if input {@link Statement} is {@code null}, otherwise - {@link StatementModel}
     */
    @Nullable
    StatementModel toStatementModel(@Nullable Statement statement);
}

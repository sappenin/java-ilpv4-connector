package org.interledger.connector.balances;

import static org.assertj.core.api.Assertions.assertThat;

import org.interledger.connector.accounts.AccountId;

import com.google.common.collect.ImmutableList;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import java.util.Collection;
import java.util.UUID;

/**
 * Unit tests for {@link RedisBalanceTracker} that validates the script and balance-change functionality for handling a
 * refund of a prior processed settlement update (processed in response to a fulfill packet).
 */
@RunWith(Parameterized.class)
@ContextConfiguration(classes = {AbstractRedisBalanceTrackerTest.Config.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RedisBalanceTrackerRefundOutgoingSettlementTest extends AbstractRedisBalanceTrackerTest {

  @ClassRule
  public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

  @Rule
  public final SpringMethodRule springMethodRule = new SpringMethodRule();

  @Autowired
  private RedisBalanceTracker balanceTracker;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  /**
   * Required-args Constructor.
   */
  public RedisBalanceTrackerRefundOutgoingSettlementTest(
    final long existingAccountBalance,
    final long existingPrepaidBalance,
    final long prepareAmount,
    final long expectedBalanceInRedis,
    final long expectedPrepaidAmountInRedis
  ) {
    super(
      existingAccountBalance, existingPrepaidBalance, prepareAmount, expectedBalanceInRedis,
      expectedPrepaidAmountInRedis
    );
  }

  @Parameterized.Parameters
  public static Collection<Object[]> errorCodes() {
    return ImmutableList.of(
      // existing_account_balance, existing_prepaid_amount,
      // settle_amount,
      // expected_balance, expected_prepaid_amount

      // clearingBalance = 0, prepaid_amount = 0
      new Object[]{ZERO, ZERO, PREPARE_ONE, ONE, ZERO},
      // clearingBalance = 0, prepaid_amount > 0
      new Object[]{ZERO, ONE, PREPARE_ONE, ONE, ONE},
      // clearingBalance = 0, prepaid_amount < 0
      new Object[]{ZERO, NEGATIVE_ONE, PREPARE_ONE, ONE, NEGATIVE_ONE},

      // clearingBalance > 0, prepaid_amount = 0
      new Object[]{ONE, ZERO, PREPARE_ONE, TWO, ZERO},
      // clearingBalance > 0, prepaid_amount > 0
      new Object[]{ONE, ONE, PREPARE_ONE, TWO, ONE},
      // clearingBalance > 0, prepaid_amount < 0
      new Object[]{ONE, NEGATIVE_ONE, PREPARE_ONE, TWO, NEGATIVE_ONE},

      // clearingBalance < 0, prepaid_amount = 0
      new Object[]{NEGATIVE_ONE, ZERO, PREPARE_ONE, ZERO, ZERO},
      // clearingBalance < 0, prepaid_amount > 0
      new Object[]{NEGATIVE_ONE, ONE, PREPARE_ONE, ZERO, ONE},
      // clearingBalance < 0, prepaid_amount < 0
      new Object[]{NEGATIVE_ONE, NEGATIVE_ONE, PREPARE_ONE, ZERO, NEGATIVE_ONE},

      // Prepaid amt > from_amt
      new Object[]{NEGATIVE_ONE, TEN, PREPARE_ONE, ZERO, TEN},
      // Prepaid_amt < from_amt, but > 0
      new Object[]{TEN, ONE, PREPARE_TEN, 20L, ONE}
    );
  }

  @Override
  protected RedisTemplate getRedisTemplate() {
    return this.redisTemplate;
  }

  /////////////////
  // Fulfill Script (Null Checks)
  /////////////////

  @Test(expected = NullPointerException.class)
  public void updateBalanceForIncomingSettlementWithNullAccountId() {
    try {
      balanceTracker.updateBalanceForOutgoingSettlementRefund(null, ONE);
    } catch (NullPointerException e) {
      assertThat(e.getMessage()).isEqualTo("accountId must not be null");
      throw e;
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void updateBalanceForIncomingSettlementWithNegativeAmount() {
    try {
      balanceTracker.updateBalanceForOutgoingSettlementRefund(ACCOUNT_ID, -10L);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).isEqualTo("amount `-10` must be a positive signed long!");
      throw e;
    }
  }

  /////////////////
  // Fulfill Script (No Account in Redis)
  /////////////////

  /**
   * Verify the correct operation when no account exists in Redis.
   */
  @Test
  public void updateBalanceForSettlementRefundWhenNoAccountInRedis() {
    final AccountId accountId = AccountId.of(UUID.randomUUID().toString());
    balanceTracker.updateBalanceForOutgoingSettlementRefund(accountId, ONE);

    final AccountBalance loadedBalance = balanceTracker.balance(accountId);
    assertThat(loadedBalance.clearingBalance()).isEqualTo(ONE);
    assertThat(loadedBalance.prepaidAmount()).isEqualTo(ZERO);
    assertThat(loadedBalance.netBalance().longValue()).isEqualTo(ONE);
  }

  @Test
  public void updateBalanceForSettlementRefundWithParamterizedValues() {
    this.initializeAccount(ACCOUNT_ID, this.existingClearingBalance, this.existingPrepaidBalance);

    balanceTracker.updateBalanceForOutgoingSettlementRefund(ACCOUNT_ID, this.prepareAmount);

    final AccountBalance loadedBalance = balanceTracker.balance(ACCOUNT_ID);
    assertThat(loadedBalance.clearingBalance()).isEqualTo(expectedClearingBalanceInRedis);
    assertThat(loadedBalance.prepaidAmount()).isEqualTo(expectedPrepaidAmountInRedis);
    assertThat(loadedBalance.netBalance().longValue()).isEqualTo(expectedClearingBalanceInRedis + expectedPrepaidAmountInRedis);
  }

}

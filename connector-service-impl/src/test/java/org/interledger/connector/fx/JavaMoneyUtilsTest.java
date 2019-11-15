package org.interledger.connector.fx;

import static org.assertj.core.api.Assertions.assertThat;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.money.CurrencyUnit;
import javax.money.Monetary;

/**
 * Unit tests for {@link JavaMoneyUtils}.
 */
public class JavaMoneyUtilsTest {

  private JavaMoneyUtils javaMoneyUtils;

  @Before
  public void setUp() {
    this.javaMoneyUtils = new JavaMoneyUtils();
  }

  ///////////////////
  // toMonetaryAmount
  ///////////////////

  @Test
  public void toMonetaryAmountInvalidUnits() {
    final CurrencyUnit currencyUSD = Monetary.getCurrency("USD");
    final int assetScale = 2;

    BigDecimal cents = BigDecimal.valueOf(0.1);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyUSD, cents.toBigInteger(), assetScale).getNumber().intValueExact()).isEqualTo(0);
  }

  @Test
  public void toMonetaryAmountUSDToUSD() {
    final CurrencyUnit currencyUSD = Monetary.getCurrency("USD");
    final int assetScale = 0;

    BigInteger cents = BigInteger.valueOf(1);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyUSD, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("1"));

    cents = BigInteger.valueOf(100);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyUSD, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("100"));

    cents = BigInteger.valueOf(199);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyUSD, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("199"));

    cents = BigInteger.valueOf(1999);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyUSD, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("1999"));

  }

  @Test
  public void toMonetaryAmountCentsToUSD() {
    final CurrencyUnit currencyUSD = Monetary.getCurrency("USD");
    final int assetScale = 2;

    BigInteger cents = BigInteger.valueOf(1);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyUSD, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("0.01"));

    cents = BigInteger.valueOf(100);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyUSD, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("1"));

    cents = BigInteger.valueOf(199);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyUSD, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("1.99"));

    cents = BigInteger.valueOf(1999);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyUSD, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("19.99"));

  }

  @Test
  public void toMonetaryAmountNanoDollarsToUSD() {
    final CurrencyUnit currencyUSD = Monetary.getCurrency("USD");
    final int assetScale = 9;

    BigInteger cents = BigInteger.valueOf(1);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyUSD, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("0.000000001"));

    cents = BigInteger.valueOf(100);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyUSD, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("0.0000001"));

    cents = BigInteger.valueOf(199);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyUSD, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("0.000000199"));

    cents = BigInteger.valueOf(1999);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyUSD, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("0.000001999"));

  }

  @Test
  public void toMonetaryAmountDropsToXRP() {
    final CurrencyUnit currencyXRP = Monetary.getCurrency("XRP");
    final int assetScale = 6;

    BigInteger cents = BigInteger.valueOf(1);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyXRP, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("0.000001"));

    cents = BigInteger.valueOf(100);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyXRP, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("0.0001"));

    cents = BigInteger.valueOf(199);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyXRP, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("0.000199"));

    cents = BigInteger.valueOf(1999);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyXRP, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("0.001999"));

  }

  @Test
  public void toMonetaryAmountXRPToXRP() {
    final CurrencyUnit currencyXRP = Monetary.getCurrency("XRP");
    final int assetScale = 0;

    BigInteger cents = BigInteger.valueOf(1);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyXRP, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("1"));

    cents = BigInteger.valueOf(100);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyXRP, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("100"));

    cents = BigInteger.valueOf(199);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyXRP, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("199"));

    cents = BigInteger.valueOf(1999);
    assertThat(javaMoneyUtils.toMonetaryAmount(currencyXRP, cents, assetScale)
      .getNumber().numberValue(BigDecimal.class)).isEqualTo(new BigDecimal("1999"));

  }

  ///////////////////
  // toMonetaryAmount
  ///////////////////

  @Test(expected = ArithmeticException.class)
  public void toInterledgerInvalidUnits() {
    final CurrencyUnit currencyUSD = Monetary.getCurrency("USD");
    final int assetScale = 2;

    try {
      javaMoneyUtils.toInterledgerAmount(
        Money.of(new BigDecimal("0.001"), currencyUSD),
        assetScale
      );
    } catch (ArithmeticException e) {
      assertThat(e.getMessage()).isEqualTo("Rounding necessary");
      throw e;
    }
  }

  @Test
  public void toInterledgerAmountUSDToUSD() {
    final CurrencyUnit currencyUSD = Monetary.getCurrency("USD");
    final int assetScale = 0;

    Money money = Money.of(BigInteger.valueOf(1), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(1L));

    money = Money.of(BigInteger.valueOf(100), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(100L));

    money = Money.of(BigInteger.valueOf(199), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(199L));

    money = Money.of(BigInteger.valueOf(1999), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(1999L));

    money = Money.of(BigInteger.valueOf(600000000), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(600000000L));
  }

  @Test
  public void toInterledgerCentsToUSD() {
    final CurrencyUnit currencyUSD = Monetary.getCurrency("USD");
    final int assetScale = 2;

    Money money = Money.of(new BigDecimal("0.01"), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(1L));

    money = Money.of(new BigDecimal("1.00"), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(100L));

    money = Money.of(new BigDecimal("1.99"), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(199L));

    money = Money.of(new BigDecimal("19.99"), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(1999L));

    money = Money.of(new BigDecimal("6000000"), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(600000000L));
  }

  @Test
  public void toInterledgerNanoDollarsToUSD() {
    final CurrencyUnit currencyUSD = Monetary.getCurrency("USD");
    final int assetScale = 9;

    Money money = Money.of(new BigDecimal("0.000000001"), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(1L));

    money = Money.of(new BigDecimal("0.0000001"), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(100L));

    money = Money.of(new BigDecimal("0.000000199"), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(199L));

    money = Money.of(new BigDecimal("0.000001999"), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(1999L));

    money = Money.of(new BigDecimal("6000000"), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(6000000000000000L));
  }

  @Test
  public void toInterledgerAmountDropsToXRP() {

    final CurrencyUnit currencyUSD = Monetary.getCurrency("XRP");
    final int assetScale = 6;

    Money money = Money.of(new BigDecimal("0.000001"), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(1L));

    money = Money.of(new BigDecimal("0.0001"), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(100L));

    money = Money.of(new BigDecimal("0.000199"), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(199L));

    money = Money.of(new BigDecimal("0.001999"), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(1999L));

    money = Money.of(new BigDecimal("6000001"), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(6000001000000L));
  }

  @Test
  public void toInterledgerAmountXRPToXRP() {

    final CurrencyUnit currencyUSD = Monetary.getCurrency("XRP");
    final int assetScale = 0;

    Money money = Money.of(BigInteger.valueOf(1), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(1L));

    money = Money.of(BigInteger.valueOf(100), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(100L));

    money = Money.of(BigInteger.valueOf(199), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(199L));

    money = Money.of(BigInteger.valueOf(1999), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(1999L));

    money = Money.of(BigInteger.valueOf(600000000), currencyUSD);
    assertThat(javaMoneyUtils.toInterledgerAmount(money, assetScale)).isEqualTo(BigInteger.valueOf(600000000L));
  }
}

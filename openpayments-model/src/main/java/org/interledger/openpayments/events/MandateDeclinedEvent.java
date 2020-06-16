package org.interledger.openpayments.events;

import org.interledger.openpayments.MandateId;
import org.interledger.openpayments.PayIdAccountId;

import org.immutables.value.Value;

/**
 * Event that is emitted when a mandate is declined by the user.
 */
@Value.Immutable
public interface MandateDeclinedEvent {

  static ImmutableMandateDeclinedEvent.Builder builder() {
    return ImmutableMandateDeclinedEvent.builder();
  }

  PayIdAccountId accountId();

  MandateId mandateId();
}
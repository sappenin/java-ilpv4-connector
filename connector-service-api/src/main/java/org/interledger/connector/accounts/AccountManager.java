package org.interledger.connector.accounts;

import org.interledger.connector.links.LinkManager;
import org.interledger.connector.persistence.repositories.AccountSettingsRepository;

import java.util.List;
import java.util.Optional;

/**
 * <p>This manager provider higher-order logic surrouding accounts in a Connector. Generally, internal Connector
 * services will utilize an instance of {@link AccountSettingsRepository} directly for normal access to Account
 * Settings. However, this manager provides additional logic on top of the repository to enable logic that uses more
 * than the simple API provided by the Repository.</p>
 */
public interface AccountManager {

  /**
   * Accessor for the Link Manager operated by this Connector.
   *
   * @return The {@link LinkManager}.
   */
  LinkManager getLinkManager();

  /**
   * Find account by account id
   * @param accountId lookup id
   * @return account or empty if not found
   */
  Optional<AccountSettings> findAccountById(AccountId accountId);

  /**
   * Fetch all accounts from persistent store
   * @return accounts
   */
  List<AccountSettings> getAccounts();

  /**
   * Create a new account in this connector by storing all account details into the persistent store. This method should
   * also create a Settlement Account inside any configured settlement engines, if appropriate.
   *
   * @param accountSettings The {@link AccountSettings} for this account.
   *
   * @return The newly-created settings.
   * @throws AccountAlreadyExistsProblem if account already exists for account id
   */
  AccountSettings createAccount(AccountSettings accountSettings) throws AccountAlreadyExistsProblem;

  AccountSettings updateAccount(AccountId accountId, AccountSettings accountSettings);

  /**
   * Validates link settings on account
   * @param accountSettings settings to va
   * @return
   */
  AccountSettings validateLinkSettings(AccountSettings accountSettings);

  /**
   * Helper method to initialize the parent account using IL-DCP. If this Connector is starting in `child` mode, it will
   * not have an operator address when it starts up, so this method finds the first account of type `parent` and uses
   * IL-DCP to get the operating address for this Connector.
   *
   * @param primaryParentAccountId The {@link AccountId} for the parent account to use IL-DCP with.
   *
   * @return The newly created parent {@link AccountSettings}.
   */
  AccountSettings initializeParentAccountSettingsViaIlDcp(AccountId primaryParentAccountId);

  /**
   * Soft delete an account such that it's deleted from the main table but copied to a table recording deleted entries
   * @param accountId The {@link AccountId} for the account to be soft deleted
   */
  void deleteByAccountId(AccountId accountId);

}

package org.interledger.ilpv4.connector.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.sappenin.interledger.ilpv4.connector.balances.BalanceTracker;
import com.sappenin.interledger.ilpv4.connector.links.LinkManager;
import com.sappenin.interledger.ilpv4.connector.settlement.HttpResponseInfo;
import com.sappenin.interledger.ilpv4.connector.settlement.IdempotentRequestCache;
import com.sappenin.interledger.ilpv4.connector.settlement.SettlementEngineClient;
import com.sappenin.interledger.ilpv4.connector.settlement.SettlementService;
import okhttp3.HttpUrl;
import org.interledger.ilpv4.connector.persistence.repositories.AccountSettingsRepository;
import org.interledger.ilpv4.connector.settlement.DefaultSettlementService;
import org.interledger.ilpv4.connector.settlement.InMemoryIdempotentRequestCache;
import org.interledger.ilpv4.connector.settlement.RedisIdempotentRequestCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * Configuration for supporting an ILP Settlement Engine.
 */
@Configuration
public class SettlementConfig {

  //private final Logger logger = LoggerFactory.getLogger(this.getClass());

//  @Autowired
//  RedisTemplate<String, String> stringRedisTemplate;

//  @Bean
//  protected IdempotentRequestCache idempotentRequestCache(RedisTemplate<UUID, HttpResponseInfo> redisTemplate) {
//
//    try {
//      if (redisTemplate.getConnectionFactory().getConnection().ping().equalsIgnoreCase("PONG")) {
//        return new RedisIdempotentRequestCache(redisTemplate);
//      } else {
//        logger.error("Redis Ping did not succeed.");
//      }
//    } catch (RedisConnectionFailureException e) {
//      // If debug-output is enabled, then emit the stack-trace.
//      if (logger.isDebugEnabled()) {
//        logger.debug(e.getMessage(), e);
//      }
//    }
//
//    logger.warn(
//      "WARNING: Using InMemoryIdempotentRequestCache. For Clustered/HA deployments, use RedisIdempotentRequestCache " +
//        "instead. HINT: is Redis running on its configured port, by default 6379?"
//    );
//
//    return new InMemoryIdempotentRequestCache();
//  }

  @Bean
  protected SettlementService settlementService(
    BalanceTracker balanceTracker, LinkManager linkManager, AccountSettingsRepository accountSettingsRepository,
    RestTemplate restTemplate, Cache<HttpUrl, SettlementEngineClient> settlementEngineClientCache
  ) {
    return new DefaultSettlementService(balanceTracker, linkManager, accountSettingsRepository,
      restTemplate, settlementEngineClientCache);
  }

}

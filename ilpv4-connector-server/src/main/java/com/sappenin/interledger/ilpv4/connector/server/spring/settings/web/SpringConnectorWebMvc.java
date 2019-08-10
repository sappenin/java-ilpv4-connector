package com.sappenin.interledger.ilpv4.connector.server.spring.settings.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.sappenin.interledger.ilpv4.connector.server.spring.controllers.converters.AccountBalanceSettingsConverter;
import com.sappenin.interledger.ilpv4.connector.server.spring.controllers.converters.AccountSettingsConverter;
import com.sappenin.interledger.ilpv4.connector.server.spring.controllers.converters.OerPreparePacketHttpMessageConverter;
import com.sappenin.interledger.ilpv4.connector.server.spring.controllers.converters.RateLimitSettingsConverter;
import com.sappenin.interledger.ilpv4.connector.server.spring.controllers.converters.SettlementEngineDetailsConverter;
import com.sappenin.interledger.ilpv4.connector.server.spring.settings.blast.BlastConfig;
import org.interledger.encoding.asn.framework.CodecContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static com.sappenin.interledger.ilpv4.connector.server.spring.settings.CodecContextConfig.ILP;
import static com.sappenin.interledger.ilpv4.connector.server.spring.settings.properties.ConnectorProperties.BLAST_ENABLED;
import static com.sappenin.interledger.ilpv4.connector.server.spring.settings.properties.ConnectorProperties.ENABLED_PROTOCOLS;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

/**
 * Web config for the Spring Connector.
 */
@Configuration
@ConditionalOnProperty(prefix = ENABLED_PROTOCOLS, name = BLAST_ENABLED, havingValue = "true")
@EnableWebMvc
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
@ComponentScan(basePackages = "com.sappenin.interledger.ilpv4.connector.server.spring.controllers")
@Import({BlastConfig.class, JacksonConfig.class, SecurityConfiguration.class})
public class SpringConnectorWebMvc implements WebMvcConfigurer {

  // TODO: Configure TLS
  // TODO: Configure HTTP/2
  @Autowired
  @Qualifier(ILP)
  private CodecContext ilpCodecContext;

  @Autowired
  private ObjectMapper objectMapper;

  ////////////////////////
  // SpringConverters
  ////////////////////////

  /**
   * Note: this bean must be registered below in {@link #addFormatters(FormatterRegistry)}.
   */
  @Bean
  RateLimitSettingsConverter rateLimitSettingsConverter() {
    return new RateLimitSettingsConverter();
  }

  /**
   * Note: this bean must be registered below in {@link #addFormatters(FormatterRegistry)}.
   */
  @Bean
  AccountBalanceSettingsConverter accountBalanceSettingsConverter() {
    return new AccountBalanceSettingsConverter();
  }

  /**
   * Note: this bean must be registered below in {@link #addFormatters(FormatterRegistry)}.
   */
  @Bean
  SettlementEngineDetailsConverter settlementEngineDetailsConverter() {
    return new SettlementEngineDetailsConverter();
  }

  /**
   * Note: this bean must be registered below in {@link #addFormatters(FormatterRegistry)}.
   */
  @Bean
  AccountSettingsConverter accountSettingsConverter(
  ) {
    return new AccountSettingsConverter(
      rateLimitSettingsConverter(), accountBalanceSettingsConverter(), settlementEngineDetailsConverter()
    );
  }

  ////////////////////////
  // HttpMessageConverters
  ////////////////////////

  @Bean
  OerPreparePacketHttpMessageConverter oerPreparePacketHttpMessageConverter() {
    return new OerPreparePacketHttpMessageConverter(ilpCodecContext);
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    // For any byte[] payloads (e.g., `/settlements`)
    ByteArrayHttpMessageConverter octetStreamConverter = new ByteArrayHttpMessageConverter();
    octetStreamConverter.setSupportedMediaTypes(Lists.newArrayList(APPLICATION_OCTET_STREAM));
    converters.add(octetStreamConverter);

    converters.add(new MappingJackson2HttpMessageConverter(objectMapper)); // For any JSON payloads.
    converters.add(oerPreparePacketHttpMessageConverter());
  }

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.replaceAll(messageConverter -> {
      if (messageConverter instanceof MappingJackson2HttpMessageConverter) {
        // Necessary to make sure the correct ObjectMapper is used in all Jackson Message Converters.
        return new MappingJackson2HttpMessageConverter(objectMapper);
      } else {
        return messageConverter;
      }
    });
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(rateLimitSettingsConverter());
    registry.addConverter(accountBalanceSettingsConverter());
    registry.addConverter(settlementEngineDetailsConverter());
    registry.addConverter(accountSettingsConverter());
  }
}

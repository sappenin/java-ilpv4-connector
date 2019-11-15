package org.interledger.connector.ccp.codecs;

import static org.assertj.core.api.Assertions.assertThat;

import org.interledger.connector.ccp.CcpRouteProperty;
import org.interledger.connector.ccp.ImmutableCcpRouteProperty;

import com.google.common.io.BaseEncoding;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Unit tests for {@link AsnCcpRoutePropertyCodec} that tests the encoding/decoding.
 */
@RunWith(Parameterized.class)
public class AsnCcpNewRoutePropertyCodecTest extends AbstractAsnCodecTest<CcpRouteProperty> {

  private AsnCcpRoutePropertyCodec codec;

  /**
   * Construct an instance of this parameterized test with the supplied inputs.
   *
   * @param expectedCcpRouteProperty
   * @param asn1OerBytes             The expected value, in binary, of the supplied {@code expectedPayloadLength}.
   */
  public AsnCcpNewRoutePropertyCodecTest(
    final CcpRouteProperty expectedCcpRouteProperty, final byte[] asn1OerBytes
  ) {
    super(expectedCcpRouteProperty, asn1OerBytes, CcpRouteProperty.class);
  }

  /**
   * The data for this test...
   */
  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {
          // Simple, default value.
          ImmutableCcpRouteProperty.builder()
            .id((short) 0)
            .value("".getBytes())
            .build(),
          BaseEncoding.base16().decode("50000000")
        },
        {
          // Simple, default value.
          ImmutableCcpRouteProperty.builder()
            .id((short) 123)
            .value("hello".getBytes())
            .build(),
          BaseEncoding.base16().decode("50007B0568656C6C6F")
        },
        {
          // Simple, default value.
          ImmutableCcpRouteProperty.builder()
            .id((short) 0)
            .optional(false)
            .transitive(true)
            .partial(false)
            .utf8(true)
            .value("hello world".getBytes())
            .build(),
          BaseEncoding.base16().decode("5000000B68656C6C6F20776F726C64")
        },
        {
          // Simple, default value.
          ImmutableCcpRouteProperty.builder()
            .id((short) 1)
            .optional(true)
            .transitive(true)
            .partial(true)
            .utf8(false)
            .value(BaseEncoding.base16().decode("A0A0A0A0"))
            .build(),
          BaseEncoding.base16().decode("E0000104A0A0A0A0")
        }
      }
    );
  }

  @Before
  public void setUp() {
    codec = new AsnCcpRoutePropertyCodec();
  }

  @Test
  public void decodeMeta() {
    CcpRouteProperty properties = codec.decodeBooleanProperties((short) 0x80).id(0).build();
    assertThat(properties.optional()).isTrue();
    assertThat(properties.transitive()).isFalse();
    assertThat(properties.partial()).isFalse();
    assertThat(properties.utf8()).isFalse();

    properties = codec.decodeBooleanProperties((short) 0x40).id(0).build();
    assertThat(properties.optional()).isFalse();
    assertThat(properties.transitive()).isTrue();
    assertThat(properties.partial()).isFalse();
    assertThat(properties.utf8()).isFalse();

    properties = codec.decodeBooleanProperties((short) 0x60).id(0).build();
    assertThat(properties.optional()).isFalse();
    assertThat(properties.transitive()).isTrue();
    assertThat(properties.partial()).isTrue();
    assertThat(properties.utf8()).isFalse();

    properties = codec.decodeBooleanProperties((short) 0x50).id(0).build();
    assertThat(properties.optional()).isFalse();
    assertThat(properties.transitive()).isTrue();
    assertThat(properties.partial()).isFalse();
    assertThat(properties.utf8()).isTrue();

    properties = codec.decodeBooleanProperties((short) 0xC0).id(0).build();
    assertThat(properties.optional()).isTrue();
    assertThat(properties.transitive()).isTrue();
    assertThat(properties.partial()).isFalse();
    assertThat(properties.utf8()).isFalse();

    properties = codec.decodeBooleanProperties((short) 0xE0).id(0).build();
    assertThat(properties.optional()).isTrue();
    assertThat(properties.transitive()).isTrue();
    assertThat(properties.partial()).isTrue();
    assertThat(properties.utf8()).isFalse();

    properties = codec.decodeBooleanProperties((short) 0xF0).id(0).build();
    assertThat(properties.optional()).isTrue();
    assertThat(properties.transitive()).isTrue();
    assertThat(properties.partial()).isTrue();
    assertThat(properties.utf8()).isTrue();
  }

  @Test
  public void encodeMeta() {
    short encodedValue = codec.encodeBooleanProperties(ImmutableCcpRouteProperty.builder().id(0)
      .optional(false)
      .transitive(true)
      .partial(false)
      .utf8(false)
      .build());
    assertThat(encodedValue).isEqualTo((short) 0x40);

    encodedValue = codec.encodeBooleanProperties(ImmutableCcpRouteProperty.builder().id(0)
      .optional(true)
      .transitive(false)
      .partial(false)
      .utf8(false)
      .build());
    assertThat(encodedValue).isEqualTo((short) 0x80);

    encodedValue = codec.encodeBooleanProperties(ImmutableCcpRouteProperty.builder().id(0)
      .optional(false)
      .transitive(true)
      .partial(false)
      .utf8(false)
      .build());
    assertThat(encodedValue).isEqualTo((short) 0x40);

    encodedValue = codec.encodeBooleanProperties(ImmutableCcpRouteProperty.builder().id(0)
      .optional(false)
      .transitive(true)
      .partial(true)
      .utf8(false)
      .build());
    assertThat(encodedValue).isEqualTo((short) 0x40);

    encodedValue = codec.encodeBooleanProperties(ImmutableCcpRouteProperty.builder().id(0)
      .optional(false)
      .transitive(true)
      .partial(false)
      .utf8(true)
      .build());
    assertThat(encodedValue).isEqualTo((short) 0x50);

    encodedValue = codec.encodeBooleanProperties(ImmutableCcpRouteProperty.builder().id(0)
      .optional(true)
      .transitive(true)
      .partial(false)
      .utf8(false)
      .build());
    assertThat(encodedValue).isEqualTo((short) 0xC0);


    encodedValue = codec.encodeBooleanProperties(ImmutableCcpRouteProperty.builder().id(0)
      .optional(true)
      .transitive(true)
      .partial(true)
      .utf8(false)
      .build());
    assertThat(encodedValue).isEqualTo((short) 0xE0);


    encodedValue = codec.encodeBooleanProperties(ImmutableCcpRouteProperty.builder().id(0)
      .optional(true)
      .transitive(true)
      .partial(true)
      .utf8(true)
      .build());
    assertThat(encodedValue).isEqualTo((short) 0xF0);
  }

}

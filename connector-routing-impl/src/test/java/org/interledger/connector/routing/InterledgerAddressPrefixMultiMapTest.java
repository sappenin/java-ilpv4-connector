package org.interledger.connector.routing;

import org.interledger.connector.routing.InterledgerAddressPrefixMap;

/**
 * Unit tests for {@link InterledgerAddressPrefixMap}.
 */
public class InterledgerAddressPrefixMultiMapTest {

//  private static final InterledgerAddressPrefix GLOBAL_ROUTING_TABLE_ENTRY = InterledgerAddressPrefix.of("g");
//  private static final Pattern ACCEPT_SOME_SOURCES_PATTERN = Pattern.compile("g\\.usd\\.(.*)");
//  private static final Pattern ACCEPT_NO_SOURCES_PATTERN = Pattern.compile("(.*)");
//  private static final InterledgerAddressPrefix DEFAULT_TARGET_ADDRESS_PREFIX = GLOBAL_ROUTING_TABLE_ENTRY;
//  private static final InterledgerAddress DEFAULT_CONNECTOR_ACCOUNT = InterledgerAddress.of("g.mainhub.connie");
//  private static final InterledgerAddressPrefix ROUTING_TABLE_ENTRY0_TARGET_PREFIX = GLOBAL_ROUTING_TABLE_ENTRY;
//  private static final InterledgerAddressPrefix ROUTING_TABLE_ENTRY1_TARGET_PREFIX =
//    GLOBAL_ROUTING_TABLE_ENTRY.with("foo");
//  private static final InterledgerAddressPrefix ROUTING_TABLE_ENTRY2_TARGET_PREFIX =
//    GLOBAL_ROUTING_TABLE_ENTRY.with("bar");
//
//  ////////////////////
//  // Test GetSize
//  ////////////////////
//  private static final InterledgerAddressPrefix ROUTING_TABLE_ENTRY3_TARGET_PREFIX =
//    GLOBAL_ROUTING_TABLE_ENTRY.with("baz.boo");
//
//  ////////////////////
//  // Test AddRoutingTableEntry
//  ////////////////////
//  private static final InterledgerAddressPrefix ROUTING_TABLE_ENTRY4_TARGET_PREFIX =
//    GLOBAL_ROUTING_TABLE_ENTRY.with("baz.boo.bar");
//  private static final InterledgerAddressPrefix ROUTING_TABLE_ENTRY5_TARGET_PREFIX =
//    GLOBAL_ROUTING_TABLE_ENTRY.with("baz.boo.bar");
//  private static final InterledgerAddress ROUTING_TABLE_ENTRY5_CONNECTOR_ACCOUNT =
//    DEFAULT_CONNECTOR_ACCOUNT.with("somethingelse");
//
//  ////////////////////
//  // Test RemoveRoutingTableEntry
//  ////////////////////
//  private final Logger logger = LoggerFactory.getLogger(this.getClass());
//  private InterledgerAddressPrefixMap<Route> prefixMap;
//
//  @Before
//  public void setUp() {
//    this.prefixMap = new InterledgerAddressPrefixMap<>();
//  }
//
//  @Test
//  public void testGetSize() {
//    for (int i = 1; i <= 10; i++) {
//      final Route route = ImmutableRoute.builder()
//        .routePrefix(InterledgerAddressPrefix.of("g." + i))
//        .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//        .build();
//      prefixMap.putEntry(route.getPrefix(), route);
//      assertThat(prefixMap.getNumKeys(), is(i));
//    }
//  }
//
//  @Test(expected = NullPointerException.class)
//  public void testAddRoutingTableEntryNull() {
//    try {
//      prefixMap.add(null);
//    } catch (NullPointerException npe) {
//      assertThat(npe.getMessage(), is(nullValue()));
//      throw npe;
//    }
//  }
//
//  ////////////////////
//  // Test RemoveRoutingTableEntrys
//  ////////////////////
//
//  @Test
//  public void testAddSameRoutingTableEntryMultipleTimes() {
//    final Route globalRoute = ImmutableRoute.builder()
//      .routePrefix(DEFAULT_TARGET_ADDRESS_PREFIX)
//      .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//      .build();
//
//    for (int i = 0; i < 10; i++) {
//      prefixMap.add(globalRoute);
//      assertThat("Duplicate Route Keys should not be added more than once!", prefixMap.getNumKeys(), is(1));
//      assertThat("Duplicate RoutingTableEntrys should not be added more than once!", prefixMap.getEntries
//        (DEFAULT_TARGET_ADDRESS_PREFIX).size(), is(1));
//    }
//  }
//
//  ////////////////////
//  // Test GetRoutingTableEntrys
//  ////////////////////
//
//  //  @Test
//  //  public void testAddFilteredRoutingTableEntryMultipleTimes() {
//  //    final Route globalRoutingTableEntry = ImmutableRoute.builder()
//  //      .routePrefix(DEFAULT_TARGET_ADDRESS_PREFIX)
//  //      .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//  //      .build();
//  //
//  //    final Route filteredRoutingTableEntry =
//  //      ImmutableRoute.builder()
//  //        .from(globalRoutingTableEntry)
//  //        // Use this for differentiation because it's different from the default pattern.
//  //        .sourcePrefixRestrictionRegex(ACCEPT_NO_SOURCES_PATTERN)
//  //        .build();
//  //
//  //    for (int i = 0; i < 10; i++) {
//  //      prefixMap.add(globalRoutingTableEntry);
//  //      prefixMap.add(filteredRoutingTableEntry);
//  //      assertThat("Duplicate Route Keys should not be added more than once!", prefixMap.getNumKeys(), is(1));
//  //      assertThat("Duplicate Route should not be added more than once!",
//  //        prefixMap.getEntries(DEFAULT_TARGET_ADDRESS_PREFIX).size(), is(2));
//  //    }
//  //  }
//
//  //  @Test(expected = NullPointerException.class)
//  //  public void testRemoveRoutingTableEntryNull() {
//  //    try {
//  //      prefixMap.removeEntry(null);
//  //    } catch (NullPointerException npe) {
//  //      assertThat(npe.getMessage(), is(nullValue()));
//  //      throw npe;
//  //    }
//  //  }
//
//  /**
//   * Helper method to getEntry a matching routingTableEntry from the prefix map, which might hold multiple routingTableEntrys
//   * for a given routePrefix.
//   */
//  private Route getMatchingRoutingTableEntry(
//    InterledgerAddressPrefix routePrefix, Optional<InterledgerAddress> nextHopAccount
//  ) {
//    // This logic assumes that if nextHopAccount is unspecified, then the next-hop connector account is
//    // DEFAULT_CONNECTOR_ACCOUNT
//    final InterledgerAddress nextHopEqualityCheck = nextHopAccount.orElse(DEFAULT_CONNECTOR_ACCOUNT);
//    return this.prefixMap.getEntries(routePrefix).stream()
//      .filter(r -> r.nextHopAccountId().equals(nextHopEqualityCheck))
//      .findFirst().get();
//  }
//
//  ////////////////////
//  // Test ForEach
//  ////////////////////
//
//  /**
//   * Helper method to getEntry a matching routingTableEntry from the prefix map, which might hold multiple routingTableEntrys
//   * for a given routePrefix.
//   */
//  private Route getMatchingRoutingTableEntry(
//    InterledgerAddressPrefix routePrefix, InterledgerAddress nextHopAccount
//  ) {
//    return getMatchingRoutingTableEntry(routePrefix, Optional.of(nextHopAccount));
//  }
//
//  ////////////////////
//  // TEST getKeys
//  ////////////////////
//
//  /**
//   * Helper method to getEntry a matching routingTableEntry from the prefix map, which might hold multiple routingTableEntrys
//   * for a given routePrefix.
//   */
//  private Route getMatchingRoutingTableEntry(InterledgerAddressPrefix routePrefix) {
//    return getMatchingRoutingTableEntry(routePrefix, Optional.empty());
//  }
//
//  ////////////////////
//  // Test GetRoutingTableEntrys
//  ////////////////////
//
//  //  @Test
//  //  public void testRemoveRoutingTableEntry() {
//  //    this.prefixMap = constructPopulatedPrefixMap();
//  //    assertThat(prefixMap.getNumKeys(), is(5));
//  //
//  //    {
//  //      final Route routingTableEntry0 =
//  //        this.getMatchingRoutingTableEntry(ROUTING_TABLE_ENTRY0_TARGET_PREFIX);
//  //
//  //      final boolean actual = prefixMap.removeEntry(routingTableEntry0);
//  //
//  //      assertThat(actual, is(true));
//  //      assertThat(prefixMap.getEntries(routingTableEntry0.routePrefix()).isEmpty(), is(true));
//  //      assertThat(prefixMap.getNumKeys(), is(4));
//  //    }
//  //    {
//  //      final Route routingTableEntry1 =
//  //        this.getMatchingRoutingTableEntry(ROUTING_TABLE_ENTRY1_TARGET_PREFIX);
//  //
//  //      final boolean actual = prefixMap.removeEntry(routingTableEntry1);
//  //
//  //      assertThat(actual, is(true));
//  //      assertThat(prefixMap.getEntries(routingTableEntry1.routePrefix()).isEmpty(), is(true));
//  //      assertThat(prefixMap.getNumKeys(), is(3));
//  //    }
//  //    {
//  //      final Route routingTableEntry2 =
//  //        this.getMatchingRoutingTableEntry(ROUTING_TABLE_ENTRY2_TARGET_PREFIX);
//  //      final boolean actual = prefixMap.removeEntry(routingTableEntry2);
//  //
//  //      assertThat(actual, is(true));
//  //      assertThat(prefixMap.getEntries(routingTableEntry2.routePrefix()).isEmpty(), is(true));
//  //      assertThat(prefixMap.getNumKeys(), is(2));
//  //    }
//  //    {
//  //      final Route routingTableEntry3 =
//  //        this.getMatchingRoutingTableEntry(ROUTING_TABLE_ENTRY3_TARGET_PREFIX);
//  //
//  //      final boolean actual = prefixMap.removeEntry(routingTableEntry3);
//  //
//  //      assertThat(actual, is(true));
//  //      assertThat(prefixMap.getEntries(routingTableEntry3.routePrefix()).isEmpty(), is(true));
//  //      assertThat(prefixMap.getNumKeys(), is(1));
//  //    }
//  //    {
//  //      final Route routingTableEntry4 =
//  //        this.getMatchingRoutingTableEntry(ROUTING_TABLE_ENTRY4_TARGET_PREFIX);
//  //      this.prefixMap.forEach((key, value) -> logger.info("K: {}, V: {}", key, value));
//  //      final boolean actual = prefixMap.removeEntry(routingTableEntry4);
//  //
//  //      assertThat(actual, is(true));
//  //      assertThat(prefixMap.getEntries(routingTableEntry4.routePrefix()).size(), is(1));
//  //      assertThat(prefixMap.getNumKeys(), is(1));
//  //      this.prefixMap.forEach((key, value) -> logger.info("K: {}, V: {}", key, value));
//  //    }
//  //    {
//  //      final Route routingTableEntry5 =
//  //        this.getMatchingRoutingTableEntry(ROUTING_TABLE_ENTRY5_TARGET_PREFIX, ROUTING_TABLE_ENTRY5_CONNECTOR_ACCOUNT);
//  //
//  //      final boolean actual = prefixMap.removeEntry(routingTableEntry5);
//  //
//  //      assertThat(actual, is(true));
//  //      assertThat(prefixMap.getEntries(routingTableEntry5.routePrefix()).isEmpty(), is(true));
//  //      assertThat(prefixMap.getNumKeys(), is(0));
//  //    }
//  //  }
//  //
//  //  @Test
//  //  public void testRemoveRoutingTableEntrys() {
//  //    this.prefixMap = new InterledgerAddressPrefixMap();
//  //
//  //    // Multiple identicial routingTableEntrys with different next-hops...
//  //    {
//  //      final Route routingTableEntry0a = ImmutableRoute.builder()
//  //        .routePrefix(GLOBAL_ROUTING_TABLE_ENTRY)
//  //        .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//  //        .build();
//  //      this.prefixMap.add(routingTableEntry0a);
//  //
//  //      final Route routingTableEntry0b = ImmutableRoute.builder()
//  //        .routePrefix(GLOBAL_ROUTING_TABLE_ENTRY)
//  //        .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT.with("ha1"))
//  //        .build();
//  //      this.prefixMap.add(routingTableEntry0b);
//  //
//  //      final Route routingTableEntry0c = ImmutableRoute.builder()
//  //        .routePrefix(GLOBAL_ROUTING_TABLE_ENTRY)
//  //        .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT.with("ha2"))
//  //        .build();
//  //      this.prefixMap.add(routingTableEntry0c);
//  //
//  //      assertThat(prefixMap.getNumKeys(), is(1));
//  //
//  //      final Collection<Route> prior = prefixMap.removeAll(GLOBAL_ROUTING_TABLE_ENTRY);
//  //      assertThat(prior.size(), is(3));
//  //      assertThat(prior.contains(routingTableEntry0a), is(true));
//  //      assertThat(prior.contains(routingTableEntry0b), is(true));
//  //      assertThat(prior.contains(routingTableEntry0c), is(true));
//  //      assertThat(prefixMap.getEntries(GLOBAL_ROUTING_TABLE_ENTRY).isEmpty(), is(true));
//  //      assertThat(prefixMap.getNumKeys(), is(0));
//  //    }
//  //
//  //    // Multiple identical routingTableEntrys with different filters...
//  //    {
//  //      final InterledgerAddressPrefix routePrefix = GLOBAL_ROUTING_TABLE_ENTRY.with("2");
//  //      final Route routingTableEntry1a = ImmutableRoute.builder()
//  //        .routePrefix(routePrefix)
//  //        .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//  //        .build();
//  //      this.prefixMap.add(routingTableEntry1a);
//  //
//  //      final Route routingTableEntry1b = ImmutableRoute.builder()
//  //        .routePrefix(routePrefix)
//  //        .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//  //        .sourcePrefixRestrictionRegex(ACCEPT_SOME_SOURCES_PATTERN)
//  //        .build();
//  //      this.prefixMap.add(routingTableEntry1b);
//  //
//  //      final Route routingTableEntry1c = ImmutableRoute.builder()
//  //        .routePrefix(routePrefix)
//  //        .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//  //        .sourcePrefixRestrictionRegex(ACCEPT_NO_SOURCES_PATTERN)
//  //        .build();
//  //      this.prefixMap.add(routingTableEntry1c);
//  //
//  //      assertThat(prefixMap.getNumKeys(), is(1));
//  //
//  //      final Collection<Route> prior = prefixMap.removeAll(routePrefix);
//  //
//  //      assertThat(prior.size(), is(3));
//  //      assertThat(prior.contains(routingTableEntry1a), is(true));
//  //      assertThat(prior.contains(routingTableEntry1b), is(true));
//  //      assertThat(prior.contains(routingTableEntry1c), is(true));
//  //      assertThat(prefixMap.getEntries(routePrefix).isEmpty(), is(true));
//  //      assertThat(prefixMap.getNumKeys(), is(0));
//  //    }
//  //  }
//
//  @Test(expected = NullPointerException.class)
//  public void testGetRoutingTableEntrysNull() {
//    try {
//      prefixMap.getEntries(null);
//    } catch (NullPointerException npe) {
//      assertThat(npe.getMessage(), is("addressPrefix must not be null!"));
//      throw npe;
//    }
//  }
//
//  @Test
//  public void testGetRoutingTableEntrysMultipleTimes() {
//    final Route globalRoute = ImmutableRoute.builder()
//      .routePrefix(DEFAULT_TARGET_ADDRESS_PREFIX)
//      .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//      .build();
//
//    for (int i = 0; i < 10; i++) {
//      prefixMap.add(globalRoute);
//
//      final Collection<Route> routes = prefixMap.getEntries(DEFAULT_TARGET_ADDRESS_PREFIX);
//
//      assertThat("Duplicate RoutingTableEntrys should not be added more than once!", routes.size(), is(1));
//    }
//  }
//
//  //  @Test
//  //  public void testGetFilteredRoutingTableEntryMultipleTimes() {
//  //    final Route globalRoutingTableEntry = ImmutableRoute.builder()
//  //      .routePrefix(DEFAULT_TARGET_ADDRESS_PREFIX)
//  //      .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//  //      .build();
//  //
//  //    final Route filteredRoutingTableEntry = ImmutableRoute.builder()
//  //      .routePrefix(DEFAULT_TARGET_ADDRESS_PREFIX)
//  //      .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//  //      .sourcePrefixRestrictionRegex(ACCEPT_NO_SOURCES_PATTERN)
//  //      .build();
//  //
//  //    for (int i = 0; i < 10; i++) {
//  //      prefixMap.add(globalRoutingTableEntry);
//  //      prefixMap.add(filteredRoutingTableEntry);
//  //
//  //      final Collection<Route> routingTableEntrys = prefixMap.getEntries(DEFAULT_TARGET_ADDRESS_PREFIX);
//  //
//  //      assertThat("Duplicate RoutingTableEntrys should not be added more than once!", routingTableEntrys.size(), is(2));
//  //    }
//  //  }
//  //
//  //  @Test
//  //  public void testForEach() {
//  //    final Route globalRoutingTableEntry = ImmutableRoute.builder()
//  //      .routePrefix(DEFAULT_TARGET_ADDRESS_PREFIX)
//  //      .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//  //      .build();
//  //    prefixMap.add(globalRoutingTableEntry);
//  //
//  //    final Route globalRoutingTableEntry2 = ImmutableRoute.builder()
//  //      .routePrefix(DEFAULT_TARGET_ADDRESS_PREFIX.with("foo"))
//  //      .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//  //      .build();
//  //    prefixMap.add(globalRoutingTableEntry2);
//  //
//  //    final Route filteredRoutingTableEntry1 = ImmutableRoute.builder()
//  //      .routePrefix(DEFAULT_TARGET_ADDRESS_PREFIX)
//  //      .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//  //      .sourcePrefixRestrictionRegex(ACCEPT_NO_SOURCES_PATTERN)
//  //      .build();
//  //    prefixMap.add(filteredRoutingTableEntry1);
//  //
//  //    final AtomicInteger atomicInteger = new AtomicInteger();
//  //    prefixMap.forEach((targetAddress, routingTableEntry) -> atomicInteger.getAndIncrement());
//  //
//  //    assertThat(atomicInteger.getEntry(), is(2));
//  //  }
//
//  @Test
//  public void testGetPrefixMapKeys() {
//    this.prefixMap = this.constructPopulatedPrefixMap();
//    assertThat(this.prefixMap.getKeys().size(), is(5));
//  }
//
//  ////////////////////
//  // Test findLongestPrefix
//  ////////////////////
//
//  @Test
//  public void testGetNextHopRoutingTableEntryForEachPrefix() {
//    for (int i = 1; i <= 10; i++) {
//      final Route route = ImmutableRoute.builder()
//        .routePrefix(InterledgerAddressPrefix.of("g." + i))
//        .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//        .build();
//      prefixMap.putEntry(route.routePrefix(), route);
//
//      assertThat(prefixMap.getNumKeys(), is(i));
//      final InterledgerAddress destinationAddress = InterledgerAddress.of("g." + i + ".bob");
//      assertThat("Each route should be retrieved with only a single value!",
//        prefixMap.findNextHops(destinationAddress).size(), is(1)
//      );
//    }
//  }
//
//  @Test
//  public void testGetNextHopRoutingTableEntryWithDuplicateDestinations() {
//    for (int i = 1; i <= 10; i++) {
//      final Route route = ImmutableRoute.builder()
//        .routePrefix(GLOBAL_ROUTING_TABLE_ENTRY)
//        .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT.with("" + i))
//        .build();
//      prefixMap.putEntry(route.routePrefix(), route);;
//
//      final InterledgerAddress destinationAddress = DEFAULT_CONNECTOR_ACCOUNT.with("bob");
//      assertThat("Each destination address should map to N number of RoutingTableEntries!",
//        prefixMap.findNextHops(destinationAddress).size() > 0, is(true)
//      );
//
//      assertThat("The destination address should return the added Route from the Map!",
//        prefixMap.findNextHops(destinationAddress).stream().filter(r -> r.equals(route))
//          .collect(Collectors.toList()).size() > 0, is(true)
//      );
//    }
//  }
//
//  @Test
//  public void testGetNextHopRoutingTableEntryWithNoRoutingTableEntrysInMap() {
//    assertThat(
//      prefixMap.findNextHops(DEFAULT_CONNECTOR_ACCOUNT.with("bob")).size(), is(0)
//    );
//  }
//
//  ////////////////////
//  // test getRootPrefix
//  ////////////////////
//
//  @Test
//  public void testGetNextHopRoutingTableEntryWithNonMatchingDestination1() {
//    final Route route = ImmutableRoute.builder()
//      .routePrefix(GLOBAL_ROUTING_TABLE_ENTRY)
//      .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//      .build();
//
//    prefixMap.putEntry(route.routePrefix(), route);;
//
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("self.me")).size(), is(0));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.1.me")).size(), is(1));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.1.m")).size(), is(1));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.1")).size(), is(1));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.2")).size(), is(1));
//  }
//
//  ////////////////////
//  // Private Helpers
//  ////////////////////
//
//  @Test
//  public void testGetNextHopRoutingTableEntryWithNonMatchingDestination2() {
//    final Route route = ImmutableRoute.builder()
//      .routePrefix(GLOBAL_ROUTING_TABLE_ENTRY.with("foo"))
//      .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//      .build();
//
//    prefixMap.putEntry(route.routePrefix(), route);;
//
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("self.me")).size(), is(0));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.1.me")).size(), is(0));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.1.m")).size(), is(0));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.1")).size(), is(0));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.2")).size(), is(0));
//  }
//
//  @Test
//  public void testGetNextHopRoutingTableEntryWithDifferringLengthsInTable() {
//    this.prefixMap = this.constructPopulatedPrefixMap();
//
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("self.me")).size(), is(0));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.1.me")).size(), is(1));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.1.m")).size(), is(1));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.1")).size(), is(1));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.2")).size(), is(1));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.foo.bob")).size(), is(1));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.bar.bob")).size(), is(1));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.baz.boo.alice")).size(), is(1));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.baz.boo.bob")).size(), is(1));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.baz.boo.bar.alice")).size(), is(2));
//    assertThat(prefixMap.findNextHops(InterledgerAddress.of("g.baz.boo.bar.bob")).size(), is(2));
//  }
//
//  @Test
//  public void testGetNextHopRoutingTableEntrysValidateReturnedRoutingTableEntrys() {
//    this.prefixMap = this.constructPopulatedPrefixMap();
//
//    final Route newRoute1 = ImmutableRoute.builder()
//      .routePrefix(GLOBAL_ROUTING_TABLE_ENTRY.with("unittest"))
//      .nextHopAccount(InterledgerAddress.of("g.this.account1"))
//      .build();
//    prefixMap.add(newRoute1);
//    Collection<Route> routes =
//      prefixMap.findNextHops(InterledgerAddress.of("g.unittest.receiver"));
//    assertThat(routes.contains(newRoute1), is(true));
//
//    final Route newRoute2 = ImmutableRoute.builder()
//      .routePrefix(GLOBAL_ROUTING_TABLE_ENTRY.with("unittest"))
//      .nextHopAccount(InterledgerAddress.of("g.this.account2"))
//      .build();
//    prefixMap.add(newRoute2);
//
//    routes = prefixMap.findNextHops(InterledgerAddress.of("g.unittest.receiver"));
//    assertThat(routes.contains(newRoute1), is(true));
//    assertThat(routes.contains(newRoute2), is(true));
//  }
//
//  @Test(expected = NullPointerException.class)
//  public void testFindLongestPrefixWithNullAddressPrefix() {
//    this.prefixMap = constructPopulatedPrefixMap();
//    try {
//      final InterledgerAddressPrefix nullAddress = null;
//      prefixMap.findLongestPrefix(nullAddress);
//      fail();
//    } catch (NullPointerException e) {
//      assertThat(e.getMessage(), is("destinationAddressPrefix must not be null!"));
//      throw e;
//    }
//  }
//
//  @Test(expected = IllegalArgumentException.class)
//  public void testFindLongestPrefixWithNonPrefix() {
//    this.prefixMap = constructPopulatedPrefixMap();
//    try {
//      prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.foo."));
//      fail();
//    } catch (IllegalArgumentException e) {
//      assertThat(e.getMessage(),
//        is("An InterledgerAddressPrefix MUST not end with a period (.) character"));
//      throw e;
//    }
//  }
//
//  @Test
//  public void testFindLongestPrefix() {
//    this.prefixMap = constructPopulatedPrefixMap();
//
//    // g.
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.b")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.bo")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.bob")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.b")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.f")).get().getValue(), is("g"));
//    // contains g.foo, but then some...
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.fool")).get().getValue(), is("g"));
//
//    // g.foo.
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.foo.a")).get().getValue(), is("g.foo"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.foo.b")).get().getValue(), is("g.foo"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.foo.bo")).get().getValue(), is("g.foo"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.foo.alice")).get().getValue(), is("g.foo"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.foo.bob")).get().getValue(), is("g.foo"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.foo.foo")).get().getValue(), is("g.foo"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.foo.bar")).get().getValue(), is("g.foo"));
//    // contains g.foo, but then some...
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.foo.fool")).get().getValue(), is("g.foo"));
//
//    // g.bar.
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.bar.a")).get().getValue(), is("g.bar"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.bar.b")).get().getValue(), is("g.bar"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.bar.bo")).get().getValue(), is("g.bar"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.bar.alice")).get().getValue(), is("g.bar"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.bar.bob")).get().getValue(), is("g.bar"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.bar.foo")).get().getValue(), is("g.bar"));
//    // contains g.bar, but then some...
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.bart")).get().getValue(), is("g"));
//
//    // g.baz.boo.
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.baz.boo.a")).get().getValue(),
//      is("g.baz.boo"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.baz.boo.b")).get().getValue(),
//      is("g.baz.boo"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.baz.boo.bo")).get().getValue(),
//      is("g.baz.boo"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.baz.boo.alice")).get().getValue(),
//      is("g.baz.boo"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.baz.boo.bob")).get().getValue(),
//      is("g.baz.boo"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.baz.boo.foo")).get().getValue(),
//      is("g.baz.boo"));
//    // contains g.baz, but then some...
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.baz.bool")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.bazl")).get().getValue(), is("g"));
//
//    // g.baz.boo.bar.
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.baz.boo.bar.a")).get().getValue(),
//      is("g.baz.boo.bar"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.baz.boo.bar.b")).get().getValue(),
//      is("g.baz.boo.bar"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.baz.boo.bar.bo")).get().getValue(),
//      is("g.baz.boo.bar"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.baz.boo.bar.alice")).get().getValue(),
//      is("g.baz.boo.bar"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.baz.boo.bar.bob")).get().getValue(),
//      is("g.baz.boo.bar"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.baz.boo.bar.foo")).get().getValue(),
//      is("g.baz.boo.bar"));
//    // contains g.baz.boo, but then some...
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.baz.bool.bart")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.bazl.boo.bar")).get().getValue(), is("g"));
//
//    // g.notfound --> Absent
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.notfound.a")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.notfound.b")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.notfound.bo")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.notfound.alice")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.notfound.bob")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.notfound.foo")).get().getValue(), is("g"));
//
//    // g.1. --> g.
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.1.b")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.1.bo")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.1.bob")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.1.b")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.1.f")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.1.foo")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.11")).get().getValue(), is("g"));
//    assertThat(prefixMap.findLongestPrefix(InterledgerAddressPrefix.of("g.22")).get().getValue(), is("g"));
//  }
//
//  private InterledgerAddressPrefixMap constructPopulatedPrefixMap() {
//    final Route route0 = ImmutableRoute.builder()
//      .routePrefix(ROUTING_TABLE_ENTRY0_TARGET_PREFIX)
//      .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//      .build();
//
//    final Route route1 = ImmutableRoute.builder()
//      .routePrefix(ROUTING_TABLE_ENTRY1_TARGET_PREFIX)
//      .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//      .build();
//
//    final Route route2 = ImmutableRoute.builder()
//      .routePrefix(ROUTING_TABLE_ENTRY2_TARGET_PREFIX)
//      .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//      .build();
//
//    final Route route3 = ImmutableRoute.builder()
//      .routePrefix(ROUTING_TABLE_ENTRY3_TARGET_PREFIX)
//      .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//      .build();
//
//    final Route route4 = ImmutableRoute.builder()
//      .routePrefix(ROUTING_TABLE_ENTRY4_TARGET_PREFIX)
//      .nextHopAccount(DEFAULT_CONNECTOR_ACCOUNT)
//      .build();
//
//    final Route route5 = ImmutableRoute.builder()
//      .routePrefix(ROUTING_TABLE_ENTRY5_TARGET_PREFIX)
//      .nextHopAccount(ROUTING_TABLE_ENTRY5_CONNECTOR_ACCOUNT)
//      .build();
//
//    return constructTestPrefixMapWithRoutingTableEntrys(route5, route4, route3,
//      route2, route1, route0);
//  }
//
//  private InterledgerAddressPrefixMap constructTestPrefixMapWithRoutingTableEntrys(final Route... route) {
//    final InterledgerAddressPrefixMap testMap = new InterledgerAddressPrefixMap();
//
//    for (int i = 0; i < route.length; i++) {
//      testMap.add(route[i]);
//    }
//
//    return testMap;
//  }

}

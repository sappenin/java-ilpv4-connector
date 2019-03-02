# Java ILPv4 Connector
A Java implementation of an [Interledger v4 Connector](https://github.com/interledger/rfcs/blob/master/0034-connector-requirements/0034-connector-requirements.md).

# Supported Features
This Connector implementation is intended for operation as a server-based ILSP, meaning it will listen for, and accept, and respond to potentially _many_ incoming connections. Specifically, this implementation supports the follwoing ILP features:

* **ILDCP**: Interledger Dynamic Configuration Protocol as specified in [IL-RFC-0031](https://github.com/interledger/rfcs/blob/master/0031-dynamic-configuration-protocol/0031-dynamic-configuration-protocol.md).

* **ILP-over-HTTP**: Also known as BLAST (**B**i**L**ateral **A**synchronous **S**eder **T**ransport as defined in [IL-RFC-0030](https://github.com/interledger/rfcs/pull/504).

* **Route Broadcast Protocol**: Defines how Connectors can exchange routing table updates as defined in [Route Broadcast Protocol](https://github.com/interledger/rfcs/pull/455).

# Architecture & Design
To learn more about how this implementation is designed, see the [docs](./docs) folder, specifically [Connector Design](./docs/connector-design.md).

# Development
To learn more about how to contribute to this project, see the [docs/development](docs/development.md) folder.

# Operating a Connector
**Disclaimer**: This implementation is currently a prototype SHOULD NOT be used in a production deployment!**

To configure this connector, see [Configuration](docs/configuration.md) in the docs folder.

# Connector Releases
This implementation follows [Semantic Versioning](https://semver.org/) as closely as possible. To view releases, see [here](https://github.com/sappenin/java-ilpv4-connector/releases).
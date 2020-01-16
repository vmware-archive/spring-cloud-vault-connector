# Spring Cloud Vault Connector

Spring Cloud Connector for using [HashiCorp's Vault](https://github.com/hashicorp/cf-vault-service-broker) service broker on Cloud Foundry.

NOTE: This project is https://spring.io/blog/2019/02/15/introducing-java-cfenv-a-new-library-for-accessing-cloud-foundry-services[in maintenance mode], in favor of the newer https://github.com/pivotal-cf/java-cfenv[Java CFEnv] project. We will continue to release security-related updates but will not address enhancement requests. 


## Quick Start

### Maven configuration

Add the Maven dependency:

```xml
<dependency>
  <groupId>io.pivotal.spring.cloud</groupId>
  <artifactId>spring-cloud-vault-spring-connector</artifactId>
  <version>${version}.RELEASE</version>
</dependency>
```

If you'd rather like the latest snapshots of the upcoming major version, use our Maven snapshot repository and declare the appropriate dependency version.

```xml
<dependency>
  <groupId>io.pivotal.spring.cloud</groupId>
  <artifactId>spring-cloud-vault-spring-connector</artifactId>
  <version>${version}.BUILD-SNAPSHOT</version>
</dependency>

<repository>
  <id>spring-libs-snapshot</id>
  <name>Spring Snapshot Repository</name>
  <url>https://repo.spring.io/libs-snapshot</url>
</repository>
```

### Compatibility matrix

Component           | Version         |
------------------- | --------------- |
Spring Vault        | 2.x (2.0 - 2.1) | 
Spring Cloud Vault  | 2.x (2.0 - 2.1) | 

### Java Applications

Applications can use this connector to access the information in `VCAP_SERVICES`
environment variable, necessary to connect to a Vault service.

```
CloudFactory cloudFactory = new CloudFactory();
Cloud cloud = cloudFactory.getCloud();
VaultServiceInfo myService = (VaultServiceInfo) cloud.getServiceInfo("MyService");
myService.getUri();
myService.getToken();
```

### Spring Applications

Spring Application can use this connector to auto inject a `VaultTemplate`
which enables the application to talk to the Vault server.

### Spring Boot Applications with Spring Cloud Vault

Spring Boot Applications using Spring Cloud Vault are auto-reconfigured
if a single Vault service is bound to the application. 

Spring Vault is configured to use token authentication 
(using the provided token from the service broker), It loads by default
secrets from the dedicated and shared generic backends:

```properties
spring.cloud.vault.generic.backends: generic, space, organization
```

You can override the configuration by setting the `spring.cloud.vault.generic.backends` property.
Names specified in `spring.cloud.vault.generic.backends` are attempted to be resolved against
`shared_backends` and `backends` maps from `VCAP_SERVICES`.

Supports port-less URL using scheme-specific port defaults. 



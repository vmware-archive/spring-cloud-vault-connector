#Spring Cloud Vault Connector

Spring Cloud Connector for using [HashiCorp's Vault](https://github.com/cloudfoundry-community/vault-boshrelease) service broker on Cloud Foundry.

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



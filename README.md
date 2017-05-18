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


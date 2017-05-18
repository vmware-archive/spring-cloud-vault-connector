Localconfig connector for HashiCorp Vault 
=========================================

A basic implementation of a localconfig connector for HashiCorp Vault, to support local development.

Basically parses a url following this pattern:

    http://<host>:<port>/?token=…


Query parameters:

* token: Required, authentication token.
* backend.ID: Optional, dedicated backend path where ID is the backend Id (generic, transit) and the value the path.
* shared_backend.ID: Optional, shared backend path where ID is the shared backend Id (organization, space) and the value the path.

**Example**

```
http://localhost:8200?token=my-token&backend.generic=cf/secret&backend.transit=cf/transit&shared_backend=space=cf/space.
```

The Spring Cloud localconfig connector documentation quick start, at:
http://cloud.spring.io/spring-cloud-connectors/spring-cloud-connectors.html#_local_configuration_connector

With the Vault localconfig connector, you can do the same for Vault, like so:

    spring.cloud.appId:   myApp
    spring.cloud.my-vault-service: http://localhost:8200?token=my-token

Then, assuming you have a Vault server running locally at `localhost:8200`, then you'll be able to test your application locally without any code changes.  When deploying your app to PCF, the `VaultTemplate` will be constructed and provided by the CloudFoundry connector instead.

Here is one way to construct a `VaultTemplate` bean in your Spring Boot application class:

    @SpringBootApplication
    class ContactApplication extends AbstractCloudConfig {
      ...
      @Bean
      VaultTemplate myVaultTemplate() {
        return cloud().getServiceConnector("my-vault-service", VaultTemplate.class, null);
      }
      ..
    }

The other mechanism, assuming you have a single bound service of type "vault", is to use:

    cloud().getSingletonServiceConnector(VaultTemplate.class);


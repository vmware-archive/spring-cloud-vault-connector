/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.spring.cloud.vault.config.java;

import io.pivotal.spring.cloud.vault.service.VaultServiceConnectorConfig;

import org.springframework.cloud.config.java.ServiceConnectionFactory;
import org.springframework.vault.core.VaultOperations;

/**
 * {@link ServiceConnectionFactory} for {@link VaultOperations}.
 *
 * @author Mark Paluch
 */
public interface VaultServiceConnectionFactory extends ServiceConnectionFactory {

	/**
	 * Get the Spring Vault {@link VaultOperations} object associated with the only Vault
	 * service bound to the app.
	 *
	 * @return {@link VaultOperations} client.
	 * @throws org.springframework.cloud.CloudException if there are either 0 or more than
	 * 1 Vault services.
	 */
	VaultOperations vaultOperations();

	/**
	 * Get the Spring Vault {@link VaultOperations} object associated with the only Vault
	 * service bound to the app.
	 *
	 * @param config configuration for the created {@link VaultOperations}.
	 * @return {@link VaultOperations} client.
	 * @throws org.springframework.cloud.CloudException if there are either 0 or more than
	 * 1 Vault services.
	 */
	VaultOperations vaultOperations(VaultServiceConnectorConfig config);

	/**
	 * Get the Spring Vault {@link VaultOperations} object associated with the specified
	 * Vault service.
	 *
	 * @param serviceId the name of the service.
	 * @return {@link VaultOperations} client.
	 * @throws org.springframework.cloud.CloudException if the specified service doesn't
	 * exist.
	 */
	VaultOperations vaultOperations(String serviceId);

	/**
	 * Get the Spring Vault {@link VaultOperations} object associated with the specified
	 * Vault service.
	 *
	 * @param serviceId the name of the service.
	 * @param config configuration for the created {@link VaultOperations}.
	 * @return {@link VaultOperations} client.
	 * @throws org.springframework.cloud.CloudException if the specified service doesn't
	 * exist.
	 */
	VaultOperations vaultOperations(String serviceId, VaultServiceConnectorConfig config);
}

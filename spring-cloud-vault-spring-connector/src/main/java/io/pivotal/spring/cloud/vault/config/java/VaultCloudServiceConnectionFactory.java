/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.pivotal.spring.cloud.vault.config.java;

import org.springframework.cloud.Cloud;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.config.java.CloudServiceConnectionFactory;
import org.springframework.util.Assert;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.core.VaultTemplate;
import io.pivotal.spring.cloud.vault.service.VaultServiceConnectorConfig;

/**
 * Simple {@link VaultServiceConnectionFactory} implementation based on
 * {@link CloudServiceConnectionFactory}.
 *
 * @author Mark Paluch
 */
public class VaultCloudServiceConnectionFactory extends CloudServiceConnectionFactory
        implements VaultServiceConnectionFactory {

	private final AbstractCloudConfig cloudConfig;

	/**
	 * Create a new {@link VaultCloudServiceConnectionFactory}.
	 *
	 * @param cloudConfig must not be {@literal null}.
	 * @param cloud must not be {@literal null}.
	 */
	public VaultCloudServiceConnectionFactory(AbstractCloudConfig cloudConfig, Cloud cloud) {

		super(cloud);

		Assert.notNull(cloudConfig, "CloudConnectorsConfig must not be null");
		Assert.notNull(cloud, "Cloud must not be null");

		this.cloudConfig = cloudConfig;
	}

	@Override
	public VaultOperations vaultOperations() {
		return vaultOperations((VaultServiceConnectorConfig) null);
	}

	@Override
	public VaultOperations vaultOperations(VaultServiceConnectorConfig config) {
		return cloudConfig.cloud().getSingletonServiceConnector(VaultTemplate.class,
		        config);
	}

	@Override
	public VaultOperations vaultOperations(String serviceId) {
		return vaultOperations(serviceId, null);
	}

	@Override
	public VaultOperations vaultOperations(String serviceId,
	        VaultServiceConnectorConfig config) {
		return cloudConfig.cloud().getServiceConnector(serviceId, VaultTemplate.class,
		        config);
	}
}

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
package io.pivotal.spring.cloud.vault.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.vault.config.ClientHttpRequestFactoryFactory;
import org.springframework.vault.support.ClientOptions;
import org.springframework.vault.support.SslConfiguration;

/**
 * Vault-specific service connector configuration.
 * <p>
 * Please note that {@link ClientHttpRequestFactory} can be {@link InitializingBean
 * lifecycle-aware}. {@link VaultServiceConnectorConfig} does not manage lifecycle nor
 * propagates container lifecycle callbacks to {@link ClientHttpRequestFactory}. You need
 * to initialize/dispose {@link ClientHttpRequestFactory} if it's lifecycle-aware.
 *
 * @author Mark Paluch
 */
public class VaultServiceConnectorConfig implements ServiceConnectorConfig {

	private final ClientHttpRequestFactory clientHttpRequestFactory;

	protected VaultServiceConnectorConfig(
			ClientHttpRequestFactory clientHttpRequestFactory) {

		Assert.notNull(clientHttpRequestFactory,
				"ClientHttpRequestFactory must not be null");

		this.clientHttpRequestFactory = clientHttpRequestFactory;
	}

	/**
	 * Create {@link VaultServiceConnectorConfig} given {@link ClientHttpRequestFactory}.
	 *
	 * @param clientHttpRequestFactory must not be {@literal null}.
	 * @return {@link VaultServiceConnectorConfig} for {@link ClientHttpRequestFactory}.
	 */
	public static VaultServiceConnectorConfig create(
			ClientHttpRequestFactory clientHttpRequestFactory) {

		Assert.notNull(clientHttpRequestFactory,
				"ClientHttpRequestFactory must not be null");

		return new VaultServiceConnectorConfig(clientHttpRequestFactory);
	}

	/**
	 * Factory method to create a new {@link VaultServiceConnectorConfigBuilder}.
	 *
	 * @return a new {@link VaultServiceConnectorConfigBuilder}.
	 */
	public static VaultServiceConnectorConfigBuilder builder() {
		return new VaultServiceConnectorConfigBuilder();
	}

	public ClientHttpRequestFactory getClientHttpRequestFactory() {
		return clientHttpRequestFactory;
	}

	/**
	 * Builder for {@link VaultServiceConnectorConfig}.
	 */
	public static class VaultServiceConnectorConfigBuilder {

		private ClientOptions clientOptions = new ClientOptions();
		private SslConfiguration sslConfiguration = SslConfiguration.unconfigured();

		VaultServiceConnectorConfigBuilder() {

		}

		/**
		 * Configure {@link ClientOptions}.
		 *
		 * @param clientOptions must not be {@literal null}.
		 * @return {@literal this} builder.
		 */
		public VaultServiceConnectorConfigBuilder clientOptions(
				ClientOptions clientOptions) {

			Assert.notNull(clientOptions, "ClientOptions must not be null");
			this.clientOptions = clientOptions;

			return this;
		}

		/**
		 * Configure {@link SslConfiguration}. Defaults to {@link SslConfiguration#unconfigured()}.
		 *
		 * @param sslConfiguration must not be {@literal null}.
		 * @return {@literal this} builder.
		 */
		public VaultServiceConnectorConfigBuilder sslConfiguration(
				SslConfiguration sslConfiguration) {

			Assert.notNull(sslConfiguration, "SslConfiguration must not be null");
			this.sslConfiguration = sslConfiguration;

			return this;
		}

		/**
		 * Build a new {@link VaultServiceConnectorConfig} instance. The {@code build}
		 * method creates {@link ClientHttpRequestFactory} which might be lifecycle-aware.
		 * You need to initialize/dispose the resource according to the lifecycle.
		 *
		 * @return the {@link VaultServiceConnectorConfig}.
		 */
		public VaultServiceConnectorConfig build() {

			ClientHttpRequestFactory clientHttpRequestFactory = ClientHttpRequestFactoryFactory
					.create(clientOptions, sslConfiguration);

			return VaultServiceConnectorConfig.create(clientHttpRequestFactory);
		}
	}
}

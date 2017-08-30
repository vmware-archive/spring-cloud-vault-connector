/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.spring.cloud.vault.service;

import java.time.Duration;
import java.util.Collections;

import io.pivotal.spring.cloud.vault.service.common.VaultServiceInfo;
import org.junit.Test;

import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.ClientOptions;
import org.springframework.vault.support.SslConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link VaultTemplateCreator}.
 *
 * @author Mark Paluch
 */
public class VaultTemplateCreatorUnitTests {

	VaultTemplateCreator creator = new VaultTemplateCreator();

	@Test
	public void shouldCreateVaultTemplate() {

		VaultServiceInfo serviceInfo = new VaultServiceInfo("vault",
				"http://localhost:8200", "token".toCharArray(), Collections.emptyMap(),
				Collections.emptyMap());

		VaultTemplate vaultTemplate = creator.create(serviceInfo, null);

		vaultTemplate.afterPropertiesSet();
		assertThat(vaultTemplate).isNotNull();
	}

	@Test
	public void shouldCreateVaultTemplateWithConfig() {

		VaultServiceConnectorConfig config = VaultServiceConnectorConfig
				.builder()
				.clientOptions(
						new ClientOptions(Duration.ofSeconds(10), Duration.ofSeconds(20))) //
				.sslConfiguration(SslConfiguration.unconfigured()) //
				.build();

		VaultServiceInfo serviceInfo = new VaultServiceInfo("vault",
				"http://localhost:8200", "token".toCharArray(), Collections.emptyMap(),
				Collections.emptyMap());

		VaultTemplate vaultTemplate = creator.create(serviceInfo, config);

		vaultTemplate.afterPropertiesSet();
		assertThat(vaultTemplate).isNotNull();
	}
}

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
package io.pivotal.spring.cloud.vault.config.java;

import java.util.Collection;

import lombok.RequiredArgsConstructor;

import org.springframework.cloud.vault.config.SecretBackendConfigurer;
import org.springframework.cloud.vault.config.SecretBackendMetadata;
import org.springframework.cloud.vault.config.VaultConfigurer;

/**
 * {@link VaultConfigurer} that registers {@link SecretBackendMetadata} with
 * {@link SecretBackendConfigurer}.
 *
 * @author Mark Paluch
 */
@RequiredArgsConstructor
class VaultConnectorConfigurer implements VaultConfigurer {

	private final Collection<SecretBackendMetadata> backends;

	@Override
	public void addSecretBackends(SecretBackendConfigurer configurer) {

		for (SecretBackendMetadata metadata : backends) {
			configurer.add(metadata);
		}

		configurer.registerDefaultDiscoveredSecretBackends(true);
	}
}

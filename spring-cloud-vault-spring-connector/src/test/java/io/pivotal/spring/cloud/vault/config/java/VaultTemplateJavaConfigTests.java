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

import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.vault.core.VaultOperations;

/**
 * Java config tests for {@link VaultOperations}.
 *
 * @author Mark Paluch
 */
public class VaultTemplateJavaConfigTests extends
		AbstractServiceJavaConfigTest<VaultOperations> {
	public VaultTemplateJavaConfigTests() {
		super(VaultOperationsConfigWithId.class, VaultOperationsConfigWithoutId.class);
	}

	protected ServiceInfo createService(String id) {
		return createVaultService(id);
	}

	protected Class<VaultOperations> getConnectorType() {
		return VaultOperations.class;
	}

	static class VaultOperationsConfigWithId extends VaultConnectorsConfig {

		@Bean(name = "my-service")
		public VaultOperations testVaultOperations() {
			return connectionFactory().vaultOperations("my-service");
		}
	}

	static class VaultOperationsConfigWithoutId extends VaultConnectorsConfig {

		@Bean(name = "my-service")
		public VaultOperations testVaultOperations() {
			return connectionFactory().vaultOperations();
		}
	}
}

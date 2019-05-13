/*
 * Copyright 2019 the original author or authors.
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

import java.util.Collections;

import io.pivotal.spring.cloud.vault.service.common.VaultServiceInfo;
import org.junit.Test;

import org.springframework.core.env.PropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link VaultServiceInfoPropertySourceAdapter}.
 *
 * @author Vasyl Zhabko
 * @author Mark Paluch
 */
public class VaultServiceInfoPropertySourceAdapterUnitTests {

	@Test
	public void verifyIfPortSetWhenUrlHasNoPortHttp() {

		PropertySource<?> propertySource = getPropertySource("http://localhost/");

		assertThat(propertySource.getProperty("spring.cloud.vault.port")).isEqualTo(80);
	}

	@Test
	public void verifyIfPortSetWhenUrlHasNoPortHttps() {

		PropertySource<?> propertySource = getPropertySource("https://localhost/");

		assertThat(propertySource.getProperty("spring.cloud.vault.port")).isEqualTo(443);
	}

	@Test
	public void verifyIfPortUnchangedWhenUrlHasPort() {

		PropertySource<?> propertySource = getPropertySource("http://localhost:8080/");

		assertThat(propertySource.getProperty("spring.cloud.vault.port")).isEqualTo(8080);
	}

	private static PropertySource<?> getPropertySource(String vaultUrl) {

		VaultServiceInfo serviceInfo = new VaultServiceInfo("1", vaultUrl,
				"".toCharArray(), Collections.emptyMap(), Collections.emptyMap());

		VaultServiceInfoPropertySourceAdapter adapter = new VaultServiceInfoPropertySourceAdapter();

		return adapter.toPropertySource(serviceInfo);
	}
}

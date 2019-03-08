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

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;
import org.springframework.core.env.PropertySource;

import io.pivotal.spring.cloud.vault.service.common.VaultServiceInfo;

/**
 * Unit tests for {@link ServiceInfoPropertySourceAdapter}.
 *
 * @author Mark Paluch
 */
public class VaultServiceInfoPropertySourceAdapterUnitTests {

	@Test
	public void verifyIfPortSetWhenUrlHasNoPortHttp() {

		VaultServiceInfo serviceInfo = new VaultServiceInfo("1", "http://localhost/",
				"".toCharArray(), Collections.emptyMap(), Collections.emptyMap());

		VaultServiceInfoPropertySourceAdapter adapter = new VaultServiceInfoPropertySourceAdapter();

		PropertySource<?> propertySource = adapter.toPropertySource(serviceInfo);

		assertEquals(80, propertySource.getProperty("spring.cloud.vault.port"));
	}

	@Test
	public void verifyIfPortSetWhenUrlHasNoPortHttps() {

		VaultServiceInfo serviceInfo = new VaultServiceInfo("1", "https://localhost/",
				"".toCharArray(), Collections.emptyMap(), Collections.emptyMap());

		VaultServiceInfoPropertySourceAdapter adapter = new VaultServiceInfoPropertySourceAdapter();

		PropertySource<?> propertySource = adapter.toPropertySource(serviceInfo);

		assertEquals(443, propertySource.getProperty("spring.cloud.vault.port"));
	}

	@Test
	public void verifyIfPortUnchangedWhenUrlHasPort() {

		VaultServiceInfo serviceInfo = new VaultServiceInfo("1", "http://localhost:8080/",
				"".toCharArray(), Collections.emptyMap(), Collections.emptyMap());

		VaultServiceInfoPropertySourceAdapter adapter = new VaultServiceInfoPropertySourceAdapter();

		PropertySource<?> propertySource = adapter.toPropertySource(serviceInfo);

		assertEquals(8080, propertySource.getProperty("spring.cloud.vault.port"));
	}
}

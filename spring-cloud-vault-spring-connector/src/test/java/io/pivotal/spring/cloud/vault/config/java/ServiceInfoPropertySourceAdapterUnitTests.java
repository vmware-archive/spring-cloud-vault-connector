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

import io.pivotal.spring.cloud.vault.service.common.VaultServiceInfo;
import org.junit.Test;

import org.springframework.core.env.PropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ServiceInfoPropertySourceAdapter}.
 *
 * @author Mark Paluch
 */
public class ServiceInfoPropertySourceAdapterUnitTests {

	@Test
	public void shouldDiscoverServiceInfoType() {

		assertThat(new VaultServiceInfoPropertySourceAdapter().getServiceInfoType())
				.isEqualTo(VaultServiceInfo.class);
	}

	static class VaultServiceInfoPropertySourceAdapter extends
			ServiceInfoPropertySourceAdapter<VaultServiceInfo> {

		@Override
		protected PropertySource<?> toPropertySource(VaultServiceInfo serviceInfo) {
			return null;
		}
	}
}

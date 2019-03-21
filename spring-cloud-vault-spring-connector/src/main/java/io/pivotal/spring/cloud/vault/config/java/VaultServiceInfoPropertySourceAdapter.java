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

import java.util.HashMap;
import java.util.Map;

import io.pivotal.spring.cloud.vault.service.common.VaultServiceInfo;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

/**
 * Overriding property to configure
 * {@link org.springframework.cloud.vault.config.VaultProperties} via
 * {@link VaultServiceInfo} property exposure.
 *
 * @author Mark Paluch
 */
class VaultServiceInfoPropertySourceAdapter extends
		ServiceInfoPropertySourceAdapter<VaultServiceInfo> {

	/**
	 * Configure endpoint via property source override.
	 *
	 * @param serviceInfo
	 * @return
	 */
	@Override
	protected PropertySource<?> toPropertySource(VaultServiceInfo serviceInfo) {

		Map<String, Object> properties = new HashMap<String, Object>();

		properties.put("spring.cloud.vault.host", serviceInfo.getHost());
		properties.put("spring.cloud.vault.port", serviceInfo.getPort());
		properties.put("spring.cloud.vault.scheme", serviceInfo.getScheme());

		return new MapPropertySource("spring-cloud-vault-connector", properties);
	}
}

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
package io.pivotal.spring.cloud.vault.service.common;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.service.UriBasedServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 * Information to access Vault services
 *
 * @author Mark Paluch
 * @author Vasyl Zhabko
 */
@ServiceLabel("vault")
public class VaultServiceInfo extends UriBasedServiceInfo {

	private final char[] token;

	private final Map<String, List<String>> backends;

	private final Map<String, String> sharedBackends;

	@SuppressWarnings("unchecked")
	public VaultServiceInfo(String id, String address, char[] token,
			Map<String, ?> backends, Map<String, String> sharedBackends) {

		super(id, address);

		this.token = token;
		this.backends = new LinkedHashMap<>(backends.size());
		this.sharedBackends = sharedBackends;

		backends.forEach((k, v) -> {

			if (v instanceof Collection) {
				this.backends.put(k, (List) v);
			}
			else {
				this.backends.put(k, (List) Collections.singletonList(v));
			}
		});

	}

	@ServiceProperty(category = "connection")
	public char[] getToken() {
		return token;
	}

	public Map<String, List<String>> getBackends() {
		return backends;
	}

	public Map<String, String> getSharedBackends() {
		return sharedBackends;
	}

	@Override
	@Deprecated
	public String getUserName() {
		return super.getUserName();
	}

	@Override
	@Deprecated
	public String getPassword() {
		return super.getPassword();
	}
}

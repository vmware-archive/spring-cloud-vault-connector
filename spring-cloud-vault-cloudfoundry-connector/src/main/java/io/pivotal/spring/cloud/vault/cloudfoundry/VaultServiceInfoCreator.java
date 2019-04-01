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
package io.pivotal.spring.cloud.vault.cloudfoundry;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.pivotal.spring.cloud.vault.service.common.VaultServiceInfo;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

/**
 * Service info creator for Vault services.
 *
 * @author Mark Paluch
 * @author Vasyl Zhabko
 */
public class VaultServiceInfoCreator extends
		CloudFoundryServiceInfoCreator<VaultServiceInfo> {

	public VaultServiceInfoCreator() {
		super(new Tags("vault", "hashicorp-vault"));
	}

	@Override
	@SuppressWarnings("unchecked")
	public VaultServiceInfo createServiceInfo(Map<String, Object> serviceData) {

		String id = (String) serviceData.get("name");

		Map<String, Object> credentials = getCredentials(serviceData);
		Map<String, Object> auth = (Map<String, Object>) credentials.get("auth");
		Map<String, Object> backends = (Map<String, Object>) credentials
				.get("backends");
		Map<String, String> sharedBackends = (Map<String, String>) credentials
				.get("backends_shared");

		String token = getStringFromCredentials(auth, "token");
		String uri = getStringFromCredentials(credentials, "address");

		if (backends == null) {
			backends = Collections.emptyMap();
		}

		if (sharedBackends == null) {
			sharedBackends = Collections.emptyMap();
		}

		return new VaultServiceInfo(id, uri, token.toCharArray(), backends,
				sharedBackends);
	}

	@Override
	public boolean accept(Map<String, Object> serviceData) {

		Map<String, Object> credentials = getCredentials(serviceData);

		return credentials != null && credentials.containsKey("address")
				&& credentials.get("address").toString().startsWith("http")
				&& credentials.containsKey("auth");
	}

}

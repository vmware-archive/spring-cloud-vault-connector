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

import java.net.URI;

import io.pivotal.spring.cloud.vault.service.common.VaultServiceInfo;

import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.vault.authentication.SimpleSessionManager;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;

/**
 * Service creator for {@link VaultTemplate} instances from {@link VaultServiceInfo}.
 *
 * @author Mark Paluch
 */
public class VaultTemplateCreator extends
		AbstractServiceConnectorCreator<VaultTemplate, VaultServiceInfo> {

	@Override
	public VaultTemplate create(VaultServiceInfo serviceInfo,
			ServiceConnectorConfig serviceConnectorConfig) {

		TokenAuthentication tokenAuthentication = new TokenAuthentication(new String(
				serviceInfo.getToken()));
		SimpleSessionManager sessionManager = new SimpleSessionManager(
				tokenAuthentication);

		// early pre-init
		assert sessionManager.getSessionToken() != null;

		ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

		if (serviceConnectorConfig != null
				&& serviceConnectorConfig instanceof VaultServiceConnectorConfig) {

			VaultServiceConnectorConfig config = (VaultServiceConnectorConfig) serviceConnectorConfig;

			requestFactory = config.getClientHttpRequestFactory();
		}

		VaultEndpoint endpoint = VaultEndpoint.from(URI.create(serviceInfo.getUri()));

		return new VaultTemplate(endpoint, requestFactory, sessionManager);
	}
}

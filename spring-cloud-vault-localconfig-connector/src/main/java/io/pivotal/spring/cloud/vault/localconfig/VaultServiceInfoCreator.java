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
package io.pivotal.spring.cloud.vault.localconfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.pivotal.spring.cloud.vault.service.common.VaultServiceInfo;

import org.springframework.cloud.localconfig.LocalConfigServiceInfoCreator;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * {@link LocalConfigServiceInfoCreator} for {@link VaultServiceInfo} for property-based
 * service info creation.
 * <p>
 * {@link VaultServiceInfoCreator} creates {@link VaultServiceInfo} from a URI specifying
 * scheme, host, port and query parameters (token, backends).
 *
 * <h3>Query parameters</h3>
 * <ul>
 * <li>{@code token}: Required, authentication token.</li>
 * <li>{@code backend.ID}: Optional, dedicated backend path where ID is the backend Id
 * (generic, transit) and the value the path.</li>
 * <li>{@code shared_backend.ID}: Optional, shared backend path where ID is the shared
 * backend Id (organization, space) and the value the path.</li>
 * </ul>
 *
 * <h3>Example</h3>
 * {@code http://localhost:8200?token=my-token&backend.generic=cf/secret&backend.transit=cf/transit&shared_backend=space=cf/space}.
 *
 * @author Mark Paluch
 */
public class VaultServiceInfoCreator extends
		LocalConfigServiceInfoCreator<VaultServiceInfo> {

	private static final Pattern backendPattern = Pattern.compile("^backend\\.(.*)");

	private static final Pattern sharedBackendPattern = Pattern
			.compile("^shared\\_backend\\.(.*)");

	public VaultServiceInfoCreator() {
		super("http", "https");
	}

	@Override
	public VaultServiceInfo createServiceInfo(String id, String uri) {

		UriComponents uriInfo = UriComponentsBuilder.fromUriString(uri).build();

		String address = UriComponentsBuilder.newInstance() //
				.scheme(uriInfo.getScheme()) //
				.host(uriInfo.getHost()) //
				.port(uriInfo.getPort()) //
				.build().toString();

		MultiValueMap<String, String> queryParams = uriInfo.getQueryParams();

		String token = queryParams.getFirst("token");
		Assert.hasText(token, "Token (token=…) must not be empty");

		Map<String, String> backends = getBackends(queryParams, backendPattern);
		Map<String, String> sharedBackends = getBackends(queryParams,
				sharedBackendPattern);

		return new VaultServiceInfo(id, address, token.toCharArray(), backends,
				sharedBackends);
	}

	private Map<String, String> getBackends(MultiValueMap<String, String> queryParams,
			Pattern pattern) {

		Map<String, String> backends = new HashMap<String, String>();

		for (Entry<String, List<String>> entry : queryParams.entrySet()) {

			Matcher matcher = pattern.matcher(entry.getKey());
			if (!matcher.find()) {
				continue;
			}

			backends.put(matcher.group(1), queryParams.getFirst(entry.getKey()));
		}

		return backends;
	}
}

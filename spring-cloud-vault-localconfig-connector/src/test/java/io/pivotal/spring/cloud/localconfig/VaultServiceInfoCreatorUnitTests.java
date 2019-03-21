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
package io.pivotal.spring.cloud.localconfig;

import io.pivotal.spring.cloud.vault.localconfig.VaultServiceInfoCreator;
import io.pivotal.spring.cloud.vault.service.common.VaultServiceInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.service.UriBasedServiceData;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link VaultServiceInfoCreator}.
 *
 * @author Mark Paluch
 * @author Vasyl Zhabko
 */
public class VaultServiceInfoCreatorUnitTests {

	@Test
	public void shouldRegisterVaultAsScheme() {

		VaultServiceInfoCreator serviceCreator = new VaultServiceInfoCreator();

		assertThat(
				serviceCreator.accept(new UriBasedServiceData("some-service",
						"http://localhost:10334/"))).isTrue();
		assertThat(
				serviceCreator.accept(new UriBasedServiceData("some-service",
						"https://localhost:10334/"))).isTrue();

		assertThat(
				serviceCreator.accept(new UriBasedServiceData("some-service",
						"ftp://localhost:10334/"))).isFalse();
	}

	@Test
	public void shouldCreateServiceInfoForIdAndUri() {

		VaultServiceInfoCreator serviceInfoCreator = new VaultServiceInfoCreator();
		VaultServiceInfo serviceInfo = serviceInfoCreator
				.createServiceInfo(
						"my-vault-service",
						"http://localhost:10334/?token=foo&backend.transit=cf/transit&shared_backend.generic=cf/secret");

		List<String> backendList = new ArrayList<>();
		backendList.add("cf/transit");

		assertThat(serviceInfo.getHost()).isEqualTo("localhost");
		assertThat(serviceInfo.getPort()).isEqualTo(10334);
		assertThat(serviceInfo.getToken()).isEqualTo("foo".toCharArray());
		assertThat(serviceInfo.getBackends()).hasSize(1).containsEntry("transit",
				backendList);
		assertThat(serviceInfo.getSharedBackends()).hasSize(1).containsEntry("generic",
				"cf/secret");
	}
}

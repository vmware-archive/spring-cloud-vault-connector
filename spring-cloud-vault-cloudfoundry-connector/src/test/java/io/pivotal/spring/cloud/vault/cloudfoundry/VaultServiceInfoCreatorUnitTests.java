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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.spring.cloud.vault.service.common.VaultServiceInfo;
import org.junit.Test;

import org.springframework.cloud.cloudfoundry.AbstractCloudFoundryConnectorTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link VaultServiceInfoCreator}.
 *
 * @author Mark Paluch
 * @author Vasyl Zhabko
 */
public class VaultServiceInfoCreatorUnitTests extends AbstractCloudFoundryConnectorTest {

	VaultServiceInfoCreator creator = new VaultServiceInfoCreator();

	ObjectMapper mapper = new ObjectMapper();

	@Test
	public void shouldCreateServiceInfoWithSingleBackend() throws IOException {

		Map services = readServiceData("test-vault-service-single.json");
		Map<String, Object> serviceData = getServiceData(services, "hashicorp-vault");

		List<String> backends = new ArrayList<>();
		backends.add("cf/20fffe9d-d8d1-4825-9977-1426840a13db/secret");

		VaultServiceInfo info = creator.createServiceInfo(serviceData);

		assertThat(info.getBackends()).hasSize(2).containsEntry("generic", backends);
	}

	@Test
	public void shouldCreateServiceInfoWithMultipleBackends() throws IOException {

		Map services = readServiceData("test-vault-service.json");
		Map<String, Object> serviceData = getServiceData(services, "hashicorp-vault");

		// address: http://192.168.11.11:8200/
		// auth.token: d6754590-7b1a-3f36-5260-5bc68e27d95c
		// backends: secret, generic
		// backends_shared: organization, space

		List<String> backends = new ArrayList<>();
		backends.add("cf/20fffe9d-d8d1-4825-9977-1426840a13db/secret");
		backends.add("cf/20fffe9d-d8d1-4825-9977-1426840a13dc/secret");

		VaultServiceInfo info = creator.createServiceInfo(serviceData);

		assertThat(info.getPort()).isEqualTo(8200);
		assertThat(info.getHost()).isEqualTo("192.168.11.11");
		assertThat(info.getScheme()).isEqualTo("http");
		assertThat(info.getToken()).isEqualTo(
				"d6754590-7b1a-3f36-5260-5bc68e27d95c".toCharArray());

		assertThat(info.getBackends()).hasSize(2).containsEntry("generic", backends);

		assertThat(info.getSharedBackends()).hasSize(2).containsEntry("organization",
				"cf/1a558498-59ad-488c-b395-8b983aacb7da/secret");
	}

	@Test
	public void shouldAcceptService() throws IOException {

		Map services = readServiceData("test-vault-service.json");
		Map<String, Object> serviceData = getServiceData(services, "hashicorp-vault");

		assertThat(creator.accept(serviceData)).isTrue();
	}

	private Map readServiceData(String resource) throws java.io.IOException {
		return mapper.readValue(readTestDataFile(resource), Map.class);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getServiceData(Map services, String name) {
		return (Map<String, Object>) ((List) services.get(name)).get(0);
	}
}

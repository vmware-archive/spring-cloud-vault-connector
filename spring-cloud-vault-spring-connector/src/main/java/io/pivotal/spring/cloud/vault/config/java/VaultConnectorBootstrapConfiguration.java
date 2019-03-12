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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.pivotal.spring.cloud.vault.config.java.VaultConnectorBootstrapConfiguration.OnSingleVaultServiceCondition;
import io.pivotal.spring.cloud.vault.service.common.VaultServiceInfo;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.vault.config.GenericSecretBackendMetadata;
import org.springframework.cloud.vault.config.SecretBackendMetadata;
import org.springframework.cloud.vault.config.VaultBootstrapConfiguration;
import org.springframework.cloud.vault.config.VaultGenericBackendProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.core.VaultOperations;

/**
 * Bootstrap configuration for the Vault connector. Configures
 * {@link ClientAuthentication} and a {@link VaultConnectorConfigurer} for generic backend
 * access.
 * <p>
 * Defaults to {@code generic}, {@code space} and {@code organization} generic backends if
 * {@link VaultConnectorGenericBackendProperties#backends} is not configured.
 *
 * @author Mark Paluch
 * @author Vasyl Zhabko
 */
@Configuration
@ConditionalOnProperty(name = "spring.cloud.vault.enabled", matchIfMissing = true)
@EnableConfigurationProperties({ VaultGenericBackendProperties.class,
		VaultConnectorGenericBackendProperties.class })
@ConditionalOnClass(VaultBootstrapConfiguration.class)
@Conditional(OnSingleVaultServiceCondition.class)
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class VaultConnectorBootstrapConfiguration {

	private final VaultConnectorGenericBackendProperties connectorVaultProperties;

	private final VaultGenericBackendProperties genericBackendProperties;

	private final Environment environment;

	private final VaultServiceInfo vaultServiceInfo;

	public VaultConnectorBootstrapConfiguration(
			VaultConnectorGenericBackendProperties connectorVaultProperties,
			VaultGenericBackendProperties genericBackendProperties,
			Environment environment) {

		this.connectorVaultProperties = connectorVaultProperties;
		this.genericBackendProperties = genericBackendProperties;
		this.environment = environment;

		Cloud cloud;
		VaultServiceInfo vaultServiceInfo = null;

		try {
			CloudFactory cloudFactory = new CloudFactory();
			cloud = cloudFactory.getCloud();

			List<ServiceInfo> serviceInfos = cloud.getServiceInfos(VaultOperations.class);
			if (serviceInfos.size() == 1) {
				vaultServiceInfo = (VaultServiceInfo) serviceInfos.get(0);
			}
		}
		catch (CloudException e) {
			// not running in a Cloud environment
		}

		this.vaultServiceInfo = vaultServiceInfo;
	}

	@Bean
	@ConditionalOnMissingBean
	public ClientAuthentication clientAuthentication() {
		return new TokenAuthentication(new String(vaultServiceInfo.getToken()));
	}

	@Bean
	public VaultConnectorConfigurer cloudVaultConfigurer() {

		List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());

		List<SecretBackendMetadata> backends = new ArrayList<SecretBackendMetadata>();

		List<String> order = connectorVaultProperties.getBackends();

		if (order.size() == 1
				&& order.contains(VaultConnectorGenericBackendProperties.DEFAULT)) {
			order = getDefaultOrder(vaultServiceInfo);
		}

		for (String cloudBackend : order) {

			List<String> backendList = getBackend(cloudBackend, vaultServiceInfo);

			if (backendList == null) {
				throw new IllegalArgumentException(String.format(
						"Cannot resolve backend for %s", cloudBackend));
			}

			List<String> contexts = GenericSecretBackendMetadata.buildContexts(
					genericBackendProperties, activeProfiles);

			for (String backend : backendList) {
				for (String context : contexts) {
					backends.add(GenericSecretBackendMetadata.create(backend, context));
				}
			}
		}

		return new VaultConnectorConfigurer(backends);
	}

	private static List<String> getDefaultOrder(VaultServiceInfo vaultServiceInfo) {

		List<String> backends = new ArrayList<String>();

		backends.addAll(getDefault(vaultServiceInfo.getBackends()));
		backends.addAll(getDefault(vaultServiceInfo.getSharedBackends()));

		return backends;
	}

	private static List<String> getDefault(Map<String, ?> map) {

		List<String> backends = new ArrayList<String>();

		if (map.containsKey("generic")) {
			backends.add("generic");
		}

		if (map.containsKey("space")) {
			backends.add("space");
		}

		if (map.containsKey("organization")) {
			backends.add("organization");
		}

		return backends;
	}

	private static List<String> getBackend(String cloudBackend,
			VaultServiceInfo serviceInfo) {

		if (serviceInfo.getBackends().containsKey(cloudBackend)) {
			return serviceInfo.getBackends().get(cloudBackend);
		}

		if (serviceInfo.getSharedBackends().containsKey(cloudBackend)) {
			return Collections.singletonList(serviceInfo.getSharedBackends().get(
					cloudBackend));
		}

		return null;
	}

	static class OnSingleVaultServiceCondition extends SpringBootCondition implements
			ConfigurationCondition {

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context,
				AnnotatedTypeMetadata metadata) {

			CloudFactory cloudFactory = new CloudFactory();
			try {
				Cloud cloud = cloudFactory.getCloud();
				List<ServiceInfo> serviceInfos = cloud.getServiceInfos();

				for (ServiceInfo serviceInfo : serviceInfos) {
					if (serviceInfo instanceof VaultServiceInfo) {
						return ConditionOutcome.match(String.format(
								"Found Vault service %s", serviceInfo.getId()));
					}
				}

				return ConditionOutcome.noMatch("No Vault service found");
			}
			catch (CloudException e) {
				return ConditionOutcome.noMatch("Not running in a Cloud");
			}
		}

		@Override
		public ConfigurationPhase getConfigurationPhase() {
			return ConfigurationPhase.REGISTER_BEAN;
		}
	}
}

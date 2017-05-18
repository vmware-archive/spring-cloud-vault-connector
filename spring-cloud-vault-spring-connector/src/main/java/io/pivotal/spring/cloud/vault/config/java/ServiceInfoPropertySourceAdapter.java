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

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.PropertySource;

/**
 * Transforms a {@link ServiceInfo} into a {@link PropertySource} with highest precedence
 *
 * @param <T> the {@link ServiceInfo} type
 * @author Scott Frederick
 * @author Will Tran
 * @author Mark Paluch
 * @see <a href=
 * "http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config">
 * http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config</a>
 */
abstract class ServiceInfoPropertySourceAdapter<T extends ServiceInfo> implements
		ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {

	private final Cloud cloud;

	private final Class<T> serviceInfoType;

	@SuppressWarnings("unchecked")
	public ServiceInfoPropertySourceAdapter() {

		Cloud cloud;
		try {
			cloud = new CloudFactory().getCloud();
		}
		catch (CloudException e) {
			// not running on a known cloud environment, so nothing to do
			cloud = null;
		}

		this.cloud = cloud;

		this.serviceInfoType = (Class) ResolvableType.forClass(getClass()).getSuperType()
				.getGeneric(0).getRawClass();
	}

	protected abstract PropertySource<?> toPropertySource(T serviceInfo);

	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {

		if (cloud == null) {
			return;
		}

		for (ServiceInfo serviceInfo : cloud.getServiceInfos()) {

			if (serviceInfoType.isAssignableFrom(serviceInfo.getClass())) {

				PropertySource<?> propertySource = toPropertySource((T) serviceInfo);
				event.getEnvironment().getPropertySources().addFirst(propertySource);
			}
		}

	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 4;
	}

	Class<T> getServiceInfoType() {
		return serviceInfoType;
	}
}

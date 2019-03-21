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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.ObjectUtils;

/**
 * @author Mark Paluch
 */
@ConfigurationProperties("spring.cloud.vault.generic")
public class VaultConnectorGenericBackendProperties {

	public final static String DEFAULT = "__DEFAULT__";

	/**
	 * Names of dedicated and shared backends to use as property sources.
	 */
	private List<String> backends = new ArrayList<String>(Collections.singleton(DEFAULT));

	public VaultConnectorGenericBackendProperties() {
	}

	public List<String> getBackends() {
		return this.backends;
	}

	public void setBackends(List<String> backends) {
		this.backends = backends;
	}

	@Override
	public boolean equals(Object o) {
		return ObjectUtils.nullSafeEquals(this, o);
	}

	@Override
	public int hashCode() {
		return ObjectUtils.nullSafeHashCode(this);
	}

	@Override
	public String toString() {
		return "VaultConnectorGenericBackendProperties(backends=" + this.getBackends()
				+ ")";
	}
}

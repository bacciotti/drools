/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.reliability.core;

import org.drools.core.common.ReteEvaluator;
import org.kie.api.runtime.conf.PersistedSessionOption;

import java.util.Map;
import java.util.Set;

public interface CacheManager {

    void initCacheManager();

    <k, V> Map<k, V> getOrCreateCacheForSession(ReteEvaluator reteEvaluator, String cacheName);

    <k, V> Map<k, V> getOrCreateSharedCache(String cacheName);

    void close();

    void removeCache(String cacheName);

    void removeCachesBySessionId(String sessionId);

    void removeAllSessionCaches();

    Set<String> getCacheNames();

    static String createCacheId(ReteEvaluator reteEvaluator, String cacheName) {
        return CacheManagerFactory.SESSION_CACHE_PREFIX + getSessionIdentifier(reteEvaluator) + CacheManagerFactory.DELIMITER + cacheName;
    }

    private static long getSessionIdentifier(ReteEvaluator reteEvaluator) {
        PersistedSessionOption persistedSessionOption = reteEvaluator.getSessionConfiguration().getPersistedSessionOption();
        if (persistedSessionOption != null) {
            return persistedSessionOption.isNewSession() ? reteEvaluator.getIdentifier() : persistedSessionOption.getSessionId();
        } else {
            throw new ReliabilityConfigurationException("PersistedSessionOption has to be configured when drools-reliability is used");
        }
    }
}

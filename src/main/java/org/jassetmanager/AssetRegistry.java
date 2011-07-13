package org.jassetmanager;

import java.rmi.registry.Registry;
import java.util.*;

public class AssetRegistry {
    private Map<String, List<RegistryEntry>> registryEntryMap;

    public AssetRegistry() {
        this.registryEntryMap = Collections.synchronizedMap(new HashMap<String, List<RegistryEntry>>());
    }

    public void register(String path, String serveAsMimeType, UserAgentMatcher userAgentMatcher, AssetBundle bundle) {
        if (!(this.registryEntryMap.containsKey(path))) {
            this.registryEntryMap.put(path, new ArrayList<RegistryEntry>());
        }
        
        this.registryEntryMap.get(path).add(new RegistryEntry(serveAsMimeType, userAgentMatcher, bundle));
    }

    public RegistryEntry get(String path, String userAgent) {
        List<RegistryEntry> registryEntries = this.registryEntryMap.get(path);
        if (registryEntries == null) {
            return null;
        }

        for (RegistryEntry entry : registryEntries) {
            if (entry.getUserAgentMatcher().matches(userAgent)) {
                return entry;
            }
        }

        return null;
    }

    class RegistryEntry {
        private final String serveAsMimeType;
        private final UserAgentMatcher userAgentMatcher;
        private final AssetBundle bundle;

        public RegistryEntry(final String serveAsMimeType, final UserAgentMatcher userAgentMatcher, final AssetBundle bundle) {
            this.serveAsMimeType = serveAsMimeType;
            this.userAgentMatcher = userAgentMatcher;
            this.bundle = bundle;
        }

        public String getServeAsMimeType() {
            return serveAsMimeType;
        }

        public UserAgentMatcher getUserAgentMatcher() {
            return userAgentMatcher;
        }

        public AssetBundle getBundle() {
            return bundle;
        }
    }
}

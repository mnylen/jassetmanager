package org.jassetmanager;

import java.util.*;

public class BundleRegistry {
    private Map<String, List<RegistryEntry>> registryEntryMap;

    public BundleRegistry() {
        this.registryEntryMap = Collections.synchronizedMap(new HashMap<String, List<RegistryEntry>>());
    }

    public void register(String path,
                         String serveAsMimeType,
                         UserAgentMatcher userAgentMatcher,
                         Bundle bundle, BundleBuilder builder) {
        
        if (!(this.registryEntryMap.containsKey(path))) {
            this.registryEntryMap.put(path, new ArrayList<RegistryEntry>());
        }
        
        this.registryEntryMap.get(path).add(new RegistryEntry(serveAsMimeType, userAgentMatcher, bundle, builder));
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
        private final Bundle bundle;
        private final BundleBuilder builder;

        public RegistryEntry(final String serveAsMimeType,
                             final UserAgentMatcher userAgentMatcher,
                             final Bundle bundle,
                             final BundleBuilder builder) {
            
            this.serveAsMimeType = serveAsMimeType;
            this.userAgentMatcher = userAgentMatcher;
            this.bundle = bundle;
            this.builder = builder;
        }

        public BundleBuilder getBuilder() {
            return this.builder;
        }

        public String getServeAsMimeType() {
            return this.serveAsMimeType;
        }

        public UserAgentMatcher getUserAgentMatcher() {
            return this.userAgentMatcher;
        }

        public Bundle getBundle() {
            return this.bundle;
        }
    }
}

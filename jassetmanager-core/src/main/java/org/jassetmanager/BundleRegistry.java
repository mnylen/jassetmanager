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
                         BundleConfiguration configuration) {

        this.addEntry(path, new RegistryEntry(serveAsMimeType, userAgentMatcher, configuration));
    }

    public void register(String path,
                         String serveAsMimeType,
                         UserAgentMatcher userAgentMatcher,
                         PrebuiltBundle bundle) {

        this.addEntry(path, new RegistryEntry(serveAsMimeType, userAgentMatcher, bundle));
    }

    private void addEntry(String path, RegistryEntry entry) {
        if (!(this.registryEntryMap.containsKey(path))) {
            this.registryEntryMap.put(path, new ArrayList<RegistryEntry>());
        }

        this.registryEntryMap.get(path).add(entry);
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
        private final BundleConfiguration configuration;

        public RegistryEntry(final String serveAsMimeType,
                             final UserAgentMatcher userAgentMatcher,
                             final BundleConfiguration configuration) {
            
            this.serveAsMimeType = serveAsMimeType;
            this.userAgentMatcher = userAgentMatcher;
            this.bundle = new Bundle();
            this.configuration = configuration;
        }

        public RegistryEntry(final String serveAsMimeType,
                             final UserAgentMatcher userAgentMatcher,
                             final PrebuiltBundle prebuiltBundle) {

            this.serveAsMimeType = serveAsMimeType;
            this.userAgentMatcher = userAgentMatcher;
            this.bundle = prebuiltBundle;
            this.configuration = null;
        }

        public boolean isPrebuilt() {
            return (this.bundle instanceof PrebuiltBundle);
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

        public BundleConfiguration getConfiguration() {
            return configuration;
        }
    }
}

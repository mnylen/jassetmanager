package org.jassetmanager;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.*;

public class BundleRegistryTest {
    private static final String CHROME_USER_AGENT = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.815.0 Safari/535.1";
    private static final String MSIE_USER_AGENT = "Mozilla/5.0 (MSIE 7.0; Macintosh; U; SunOS; X11; gu; SV1; InfoPath.2; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648)";

    private BundleRegistry registry;
    private BundleConfiguration jsBundleConfig;
    private BundleConfiguration jsBundleIEConfig;
    private BundleConfiguration cssBundleIEConfig;
    
    @Before
    public void setUp() {
        this.registry = new BundleRegistry();
        this.jsBundleConfig = mock(BundleConfiguration.class);
        this.jsBundleIEConfig = mock(BundleConfiguration.class);
        this.cssBundleIEConfig = mock(BundleConfiguration.class);
    }

    @Test
    public void testFindsEntryForPathAndUserAgent() {
        this.registry.register("/js/application.js", "text/javascript", BrowserMatchers.MSIE, this.jsBundleIEConfig);
        this.registry.register("/js/application.js", "text/javascript", BrowserMatchers.ANY, this.jsBundleConfig);
        
        BundleRegistry.RegistryEntry registryEntry = this.registry.get("/js/application.js", CHROME_USER_AGENT);
        assertThat(registryEntry, notNullValue());
        assertThat(registryEntry.getConfiguration(), is(this.jsBundleConfig));

        registryEntry = this.registry.get("/js/application.js", MSIE_USER_AGENT);
        assertThat(registryEntry, notNullValue());
        assertThat(registryEntry.getConfiguration(), is(this.jsBundleIEConfig));
    }

    @Test
    public void testNullWhenNotFound() {
        assertThat(this.registry.get("/css/ie7.css", MSIE_USER_AGENT), nullValue());

        this.registry.register("/css/ie7.css", "text/javascript", BrowserMatchers.MSIE, this.cssBundleIEConfig);
        assertThat(this.registry.get("/css/ie7.css", CHROME_USER_AGENT), nullValue());
    }
}

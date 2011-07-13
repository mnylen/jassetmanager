package org.jassetmanager;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.*;

import javax.servlet.ServletContext;

public class AssetRegistryTest {
    private static final String CHROME_USER_AGENT = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.815.0 Safari/535.1";
    private static final String MSIE_USER_AGENT = "Mozilla/5.0 (MSIE 7.0; Macintosh; U; SunOS; X11; gu; SV1; InfoPath.2; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648)";
    
    private AssetRegistry registry;
    private AssetBundle jsBundle;
    private AssetBundle jsBundleIE;
    private AssetBundle cssBundleIE;
    private ServletContext mockContext;

    @Before
    public void setUp() {
        this.mockContext = mock(ServletContext.class);
        
        this.registry = new AssetRegistry();

        this.jsBundle = new AssetBundle(new AssetBundleConfiguration(), this.mockContext);
        this.jsBundleIE = new AssetBundle(new AssetBundleConfiguration(), this.mockContext);
        this.cssBundleIE = new AssetBundle(new AssetBundleConfiguration(), this.mockContext);
        
        this.registry.register("/js/application.js", "text/javascript", BrowserMatchers.MSIE, this.jsBundleIE);
        this.registry.register("/js/application.js", "text/javascript", BrowserMatchers.ANY, this.jsBundle);
        this.registry.register("/css/application.css", "text/javascript", BrowserMatchers.MSIE, this.cssBundleIE);
    }

    @Test
    public void testGetReturnsFirstEntryThatMatchesPathAndUserAgent() {
        AssetRegistry.RegistryEntry registryEntry = this.registry.get("/js/application.js", CHROME_USER_AGENT);
        assertThat(registryEntry, notNullValue());
        assertThat(registryEntry.getBundle(), is(this.jsBundle));

        registryEntry = this.registry.get("/js/application.js", MSIE_USER_AGENT);
        assertThat(registryEntry, notNullValue());
        assertThat(registryEntry.getBundle(), is(this.jsBundleIE));
    }

    @Test
    public void testGetReturnsNullWhenNoPathOrUserAgentMatches() {
        assertThat(this.registry.get("/js/somethingelse.js", CHROME_USER_AGENT), nullValue());
        assertThat(this.registry.get("/css/application.css", CHROME_USER_AGENT), nullValue());
    }
}

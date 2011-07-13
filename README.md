# jAssetManager

**NOTE: jAssetManager is a work in progress. This README might not reflect
the reality very accurately. Everything is subject to change and nothing is
thoroughly tested. You've been warned.**

## What?

jAssetManager manages static assets in your Java web application, concatenating
them and manipulating them, so serving static assets produce as little overhead
as possible.

The main principle is to eliminate multiple HTTP requests for single, smaller
assets. For example, a typical web application has multiple CSS files:
_reset.css_, _main.css_, _buttons.css_, and so on. Including them all in a
single page results in three HTTP requests, which adds server overhead and
makes the pages slower to load.


## Usage

With jAssetManager you configure _asset bundles_ that contain multiple
asset files. The asset files are then concatenated together and served as
a single asset to the browser:

	public class MyAssetServlet extends AssetServlet {
		@Override
		public void init(ServletConfig config) throws ServletException {
			super.init(config);
			
			super.configureBundle("/assets/css/application.css", "text/css", BrowserMatcher.ANY,
				new AssetBundleConfiguration()
					.addFilePattern("/static/css/reset.css"))
					.addFilePattern("/static/css/main.css")); // matches all rest CSS files
		}
	}
	
In your _web.xml_ you might configure it as follows:

	<servlet>
		<servlet-name>assets</servlet-name>
		<servlet-class>com.myproject.MyAssetServlet</servlet-class>
	</servlet>
	
	<servlet-mapping>
		<servlet-name>assets</servlet-name>
		<url-pattern>/assets/*</url-pattern>
	</servlet-mapping>
	
Now whenever a request is made to _/assets/css/application.css_, the asset
servlet will match the request against the configured asset bundle and
the _User-Agent_ of the request. The example above will produce a
concatenation of _reset.css_ and _main.css_ files inside _/static/css_
directory for all browsers. However, you can specify different bundles in the
same path to be served for different browsers. One might, for example,
include _ie.css_ for all versions of Internet Explorer as follows:

	public class MyAssetServlet extends AssetServlet {
		@Override
		public void init(ServletConfig config) throws ServletException {
			super.init(config);
			
			AssetBundleConfiguration baseConfiguration = new AssetBundleConfiguration()
				.addFilePattern("/static/css/reset.css"))
				.addFilePattern("/static/css/main.css"));
				
			AssetBundleConfiguration ieConfiguration = new AssetBundleConfiguration(baseConfiguration)
				.addFilePattern("/static/css/ie.css");
			
			super.configureBundle("/assets/css/application.css", "text/css", BrowserMatcher.MSIE,
				ieConfiguration);
			
			super.configureBundle("/assets/css/application.css", "text/css", BrowserMatcher.ANY,
				baseConfiguration);
		}
	}
	
The configured bundle that first matches the request URI and _User-Agent_
header will be served. In the above case, if a browser identifying itself as
Google Chrome would request the asset bundle, the normal version would
be served. If Internet Explorer on the other hand would do the same
request, it would be served the normal bundle plus the _ie.css_ file.

## License

Copyright (&copy;) 2011 Mikko Nylén

See LICENSE
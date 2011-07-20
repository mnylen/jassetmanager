# jAssetManager

Manages your static assets in Java web applications like a boss:

* Concatenates your CSS and JavaScript files into one asset bundle
  that can be served in a single request, reducing the HTTP overhead
  coming from serving multiple assets separately.

* Allows manipulating the input files before concatenation, and
  even after it. Good for compiling some higher level language into
  JavaScript or CSS (think CoffeeScript, LESS, SASS, ...), and for compressing
  the assets - the possibilities are limitless.

* Serve different versions of your asset bundles for different
  browsers. Wan't to include _ie.css_ for only Internet Explorer?
  Can do!

* Caches and rebuilds your bundles when needed. Highly configurable.

* Zero XML configuration from the jAssetManager's part. All configuration
  is done by providing a custom `AssetServlet`.

* Except for the obvious Servlet API and commons-logging, no dependencies on
  the core! You can start using jAssetManager by just dropping the jar to your
  application.

## Usage

First, drop the jar into your application. Then, add the following to your web.xml:

	<servlet>
		<servlet-name>assets</servlet-name>
		<servlet-class>com.myproject.MyAssetServlet</servlet-class>
	</servlet>
			
	<servlet-mapping>
		<servlet-name>assets</servlet>
		<url-pattern>/assets/*</url-pattern>
	</servlet-mapping>
	
Now just extend the `AssetServlet` and configure your bundles:

	package com.myproject;
	
	public class MyAssetServlet extends AssetServlet {
		
		@Override
		public void init(ServletConfig config) throws ServletException {
			super.configureBundle("/assets/application.css", "text/css", BrowserMatchers.ANY,
				new BundleConfiguraton()
					.addFilePattern("/static/css/reset.css") // these should be located
					.addFilePattern("/static/css/main.css")  // inside your servlet context
			);
		}
	}
	
Go on, try it out! When you do a request for _/assets/application.css_, you
should see your _reset.css_ and _main.css_ bundled together. Now, edit one of
the files and you should see the modified content.

The first argument to the `configureBundle` method call tells the request
URI that will be routed to the bundle. The second tells the mime type the
bundle will be served as and the third one provides a matcher for the
user agent. The fourth is the actual bundle configuration.

### File patterns

File patterns are what matches your assets in the servlet's context to be
included to a bundle. You can add as many of them as you like and the resulting
concatenation preserves the order you added them in. On the
background, each file pattern is compiled as a regular expression, so if you
want to include, let's say, all the _.css_ files, you might do something like
this:

	new BundleConfiguration()
		// some other configuration
		.addFilePattern("/static/css/.+\\.css");
		
If the bundled `RegexFilePattern` isn't enough, you can easily roll your own by implementing
the `FilePattern` interface.

By default the `AssetServlet` will try to match all assets inside your servlet's context to
the bundle. If you have a lot of files, you can aid it a bit by providing _context root path_,
the longest common subdirectory where all the assets for the bundle reside:

	new BundleConfiguration()
		.setContextRootPath("/static/css")
		.addFilePattern("/static/css/.+\\.css");

### Manipulators

Manipulators to the rescue when you need to manipulate your assets in any way. They can twist,
bend and even replace your assets on the fly. Manipulators come in two forms:

* _pre manipulators_ manipulate each asset file

* _post manipulators_ manipulate the whole bundle after it's been built
  (i.e. the concatenation)

Adding manipulators is easy: just use the `addPreManipulator(Manipulator)`
and `addPostManipulator(Manipulator)` methods on the configuration.

To create your own manipulator, just implement the `Manipulator` interface.

	package com.myproject;
	
	import org.jassetmanager.*;
	
	public class AddCopyrightNoticeManipulator implements Manipulator {
		public byte[] postManipulate(Bundle bundle) {
			bundle.setManipulatedContent(new StringBuilder()
				.append("/*\r\n    Copyright (C) 2011 Fancy Organization All Rights Reserved\r\n*/\r\n")
				.append(new String(bundle.getContent()))
				.toString()
				.getBytes());
		}
	}

This would append a copyright notice to the top of the bundle. To use:

	new BundleConfiguration()
		.addPostManipulator(new AddCopyrightNoticeManipulator());


### Not all browsers are equal

Each bundle is configured to respond to a specific request URI and _User-Agent_ header.
The first matching bundle will be served, so be sure to get the order right:

	BundleConfiguration baseConfiguration = new BundleConfiguration()
		.addFilePattern("/static/css/reset.css")
		.addFilePattern("/static/css/main.css");
		
	BundleConfiguration ieConfiguration = new BundleConfiguration(baseConfiguration)
		.addFilePattern("/static/css/ie.css");
		
	super.configureBundle("/assets/application.css", "text/css", BrowserMatchers.MSIE, ieConfiguration);
	super.configureBundle("/assets/application.css", "text/css", BrowserMatchers.ANY, baseConfiguration);

The above would serve a bundle with _ie.css_ appended to the bottom of the
bundle for Internet Explorer, and the bundle without the IE specific stuff
for the rest of the internet.

To roll your own matcher, implement the `UserAgentMatcher` interface and give
it as argument for `configureBundle`.

### It's build time!

You can control the if and when your bundles will be rebuilt. The default is to
rebuild the bundle after any modifications have been done to it's asset
files. Other strategies include _build once_ and _always rebuild_:

* `BuildStragies.REBUILD_IF_MODIFIED` - builds the bundle once and after each
  modification to it's asset files (useful in development)

* `BuildStrategies.BUILD_ONCE` - builds the bundle once and never again, unless
  the application server is restarted (useful in production)

* `BuildStrategies.ALWAYS_REBUILD` - rebuilds the bundle on each request
  (useful when developing manipulators)

To set the strategy, use `setBuildStrategy(BuildStrategy)` on the
configuration.

If you aren't happy with the defaults strategies, you can roll your own by
implementing the `BuildStrategy` interface.

### Debugging

Sometimes, somewhere, something goes wrong. To turn on debugging mode, use
`setDebug(true)` on the `AssetServlet`. This will cause any errors to
be printed to the servlet's response. The default is `false`, and is
suitable for production environment. Don't worry though, errors are
logged even when the debug mode is turned off.

## Feature requests and bug reports

Please report any feature requests and bug reports to the
[GitHub issue tracker](http://github.com/mnylen/jassetmanager/issues)

## License

Licensed under the MIT license. See the LICENSE file for more information.
# jAssetManager CoffeeScript

Provides manipulator for compiling CoffeeScript into JavaScript.

## Installation

First, download the JAR for [JCoffeeScript 1.1](https://github.com/yeungda/jcoffeescript)

Then, if you are using Maven, add the following to your pom.xml:

    <dependency>
        <groupId>jcoffeescript</groupId>
        <artifactId>jcoffeescript</artifactId>
        <scope>system</scope>
        <version>1.1</version>
        <systemPath>/path/to/jcoffeescript-1.1.jar</systemPath>
    </dependency>
    
    <dependency>
        <groupId>org.jassetmanager</groupId>
        <artifactId>jassetmanager-coffeescript</artifactId>
        <version>0.1</version>
    </dependency>
    
Otherwise, just download the [jassetmanager-coffeescript-0.1.jar](https://github.com/downloads/mnylen/jassetmanager/jassetmanager-coffeescript-0.1.jar)
and drop it into your project.

## Usage

Use as a pre manipulator:

    BundleConfiguration bundle = new BundleConfiguration()
        .addFilePattern(new RegexFilePattern("/js/.+\\.coffee"))
        .addPreManipulator(new CoffeeScriptCompileManipulator());
        
By default, only assets with file extension `.coffee` will be compiled. If you want to modify
this, you need to provide `CompileOptions` instance to the manipulator's constructor and set
the file extensions that should be compiled:

    CompileOptions options = new CompileOptions()
        .setFileExtensions(new String[] { ".coffee", ".js" });
        
You can also use `setBare(true)` on the configuration to compile without the safe wrapper added by CoffeeScript.
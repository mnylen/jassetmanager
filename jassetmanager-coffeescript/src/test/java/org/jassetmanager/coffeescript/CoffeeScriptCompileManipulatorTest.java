package org.jassetmanager.coffeescript;

import org.jassetmanager.Asset;
import org.jassetmanager.FileSystem;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class CoffeeScriptCompileManipulatorTest {
    private Asset coffeeAsset;
    private Asset jsAsset;
    private FileSystem mockFs;

    @Before
    public void setUp() throws Exception {
        this.mockFs = mock(FileSystem.class);

        this.coffeeAsset = new Asset(this.mockFs, "/application.coffee");
        this.jsAsset = new Asset(this.mockFs, "/application.js");

        when(this.mockFs.getContent(this.coffeeAsset)).thenReturn(
                ("kids =\r\n" +
                 "  brother:\r\n" +
                 "    name: 'Max'\r\n" +
                 "    age: 11\r\n" +
                 "  sister:\r\n" +
                 "    name: 'Ida'\r\n" +
                 "    age: 9\r\n").getBytes());
    }

    @Test
    public void testCompiles() throws Exception {
        CoffeeScriptCompileManipulator manipulator = new CoffeeScriptCompileManipulator();
        manipulator.preManipulate(this.coffeeAsset, false);

        assertThat(this.coffeeAsset.isManipulated(), is(true));
        assertThat(new String(this.coffeeAsset.getContent()), equalTo(
                "(function() {\n" +
                "  var kids;\n" +
                "  kids = {\n" +
                "    brother: {\n" +
                "      name: 'Max',\n" +
                "      age: 11\n" +
                "    },\n" +
                "    sister: {\n" +
                "      name: 'Ida',\n" +
                "      age: 9\n" +
                "    }\n" +
                "  };\n" +
                "}).call(this);\n"
        ));
    }

    @Test
    public void testCompilesWithoutSafeWrapper() throws Exception {
        CoffeeScriptCompileManipulator manipulator = new CoffeeScriptCompileManipulator(
                new CompileOptions()
                    .setBare(true)
        );
        
        manipulator.preManipulate(this.coffeeAsset, false);

        assertThat(this.coffeeAsset.isManipulated(), is(true));
        assertThat(new String(this.coffeeAsset.getContent()), equalTo(
                "var kids;\n" +
                "kids = {\n" +
                "  brother: {\n" +
                "    name: 'Max',\n" +
                "    age: 11\n" +
                "  },\n" +
                "  sister: {\n" +
                "    name: 'Ida',\n" +
                "    age: 9\n" +
                "  }\n" +
                "};"));
    }

    @Test
    public void testCompilesOnlyAssetsWithSpecifiedFileExtensions() throws Exception {
        when(this.mockFs.getContent(this.jsAsset)).thenReturn("a = 1".getBytes());

        CoffeeScriptCompileManipulator manipulator = new CoffeeScriptCompileManipulator(
                new CompileOptions()
                    .setFileExtensions(new String[] { ".coffee" }));

        manipulator.preManipulate(this.jsAsset, false);
        assertThat(this.jsAsset.isManipulated(), is(false));


        manipulator = new CoffeeScriptCompileManipulator(
                new CompileOptions()
                        .setFileExtensions(new String[] { ".js" }));

        manipulator.preManipulate(this.jsAsset, false);
        assertThat(this.jsAsset.isManipulated(), is(true));
    }
}

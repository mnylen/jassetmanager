package org.jassetmanager.manipulator;

import org.jassetmanager.AssetBundle;
import org.jassetmanager.AssetException;
import org.jassetmanager.AssetFile;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class SassCompileManipulatorTest {
    private AssetFile mockAsset;
    private AssetBundle mockBundle;

    @Before
    public void setUp() {
        this.mockAsset = mock(AssetFile.class);
        when(this.mockAsset.getPath()).thenReturn("/scss/styles.scss");

        this.mockBundle = mock(AssetBundle.class);
    }

    @Test
    public void testCompilesSCSS() throws AssetException {
        SassCompileManipulator manipulator = new SassCompileManipulator("sass",
                SassCompileManipulator.LanguageMode.SCSS,
                SassCompileManipulator.OutputStyle.NESTED);

        String content =
                "$main_bg_color: #eee;\r\n" +
                "body {\r\n" +
                "  background-color: $main_bg_color;\r\n" +
                "}\r\n";

        String compiled =
                "body {\n" +
                "  background-color: #eeeeee; }\n";

        assertThat(new String(manipulator.manipulate(this.mockBundle, this.mockAsset, content.getBytes())),
                equalTo(compiled));
    }

    @Test
    public void testCompileSASS() throws AssetException {
        SassCompileManipulator manipulator = new SassCompileManipulator("sass",
                SassCompileManipulator.LanguageMode.SASS,
                SassCompileManipulator.OutputStyle.NESTED);

        String content =
                "$main_bg_color: #eee\r\n" +
                "body\r\n" +
                "  background-color: $main_bg_color\r\n";

        String compiled =
                "body {\n" +
                "  background-color: #eeeeee; }\n";

        assertThat(new String(manipulator.manipulate(this.mockBundle, this.mockAsset, content.getBytes())),
                equalTo(compiled));
    }

    @Test
    public void testCompileInvalidSCSS() throws AssetException {
        SassCompileManipulator manipulator = new SassCompileManipulator("sass",
                SassCompileManipulator.LanguageMode.SCSS,
                SassCompileManipulator.OutputStyle.NESTED);

        String content =
                "body" +
                "  background-color: #eee;\r\n" +
                "}\r\n";

        try {
            manipulator.manipulate(this.mockBundle, this.mockAsset, content.getBytes());
            fail("Should have thrown AssetException");
        } catch (AssetException e) {
            assertThat(e.getMessage(), equalTo(
                    "Failed to compile SASS asset '/scss/styles.scss':\r\n" +
                    "Syntax error: Invalid CSS after \"...ckground-color:\": expected pseudoclass or pseudoelement, was \" #eee;\"\n" +
                    "        on line 1 of standard input\n" +
                    "  Use --trace for backtrace.\n"));
        }
    }
}

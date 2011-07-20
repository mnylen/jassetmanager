package org.jassetmanager;

import org.jassetmanager.testmanipulators.AddCopyrightNoticeManipulator;
import org.jassetmanager.testmanipulators.FileNameAppenderManipulator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BundleBuilderManipulatorsTest {
    private Bundle bundle;

    @Before
    public void setUp() throws Exception {
        FileSystem mockFs = mock(FileSystem.class);
        Asset resetCss = new Asset(mockFs, "/css/reset.css");
        Asset mainCss = new Asset(mockFs, "/css/main.css");
        
        when(mockFs.getAssets("/")).thenReturn(
                new HashSet<Asset>(Arrays.asList(
                        resetCss,
                        mainCss)));

        when(mockFs.getContent(resetCss)).thenReturn(
                "html, body { margin: 0; }".getBytes());

        when(mockFs.getContent(mainCss)).thenReturn(
                "body { background-color: #000; }".getBytes());
        
        BundleConfiguration config = new BundleConfiguration()
                .addFilePattern(new RegexFilePattern("/css/reset.css"))
                .addFilePattern(new RegexFilePattern("/css/main.css"))
                .addPreManipulator(new FileNameAppenderManipulator())
                .addPostManipulator(new AddCopyrightNoticeManipulator());

        this.bundle = new Bundle();

        BundleBuilder builder = new BundleBuilder(config, mockFs);
        builder.build(this.bundle);
    }

    @Test
    public void testRunsPreManipulatorsForEachFileAndPostManipulatorForConcatenation() throws AssetException {
        assertThat(new String(this.bundle.getContent()), equalTo(
                "/**\r\n" +
                " * Copyright (c) 2011 Some Organization All Rights Reserved\r\n" +
                " */\r\n" +
                "\r\n" +
                "/* /css/reset.css */\r\n" +
                "html, body { margin: 0; }\r\n" +
                "/* /css/main.css */\r\n" +
                "body { background-color: #000; }\r\n"));
    }
}

package org.jassetmanager;

import org.jassetmanager.testmanipulators.AddCopyrightNoticeManipulator;
import org.jassetmanager.testmanipulators.FileNameAppenderManipulator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import javax.servlet.ServletContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AssetBundleManipulatorsTest {
    private List<AssetFile> allAssetFiles;
    private AssetBundle bundle;

    @Before
    public void setUp() throws Exception {
        ServletContext mockContext = mock(ServletContext.class);
        when(mockContext.getResourceAsStream("/css/reset.css")).thenReturn(
                new ByteArrayInputStream("html, body { margin: 0; }".getBytes()));

        when(mockContext.getResourceAsStream("/css/main.css")).thenReturn(
                new ByteArrayInputStream("body { background-color: #000; }".getBytes()));

        this.allAssetFiles = new ArrayList<AssetFile>(Arrays.asList(
                new AssetFile("/css/main.css", mockContext), new AssetFile("/css/reset.css", mockContext)));

        AssetBundleConfiguration config = new AssetBundleConfiguration()
                .addFilePattern(new RegexFilePattern("/css/reset.css"))
                .addFilePattern(new RegexFilePattern("/css/main.css"))
                .addPreManipulator(new FileNameAppenderManipulator())
                .addPostManipulator(new AddCopyrightNoticeManipulator());

        this.bundle = new AssetBundle(config, mockContext);
    }

    @Test
    public void testRunsPreManipulatorsForEachFileAndPostManipulatorForConcatenation() throws AssetException {
        this.bundle.build(this.allAssetFiles);

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

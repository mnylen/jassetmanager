package org.jassetmanager.manipulator;

import org.jassetmanager.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class SassCompileManipulator implements Manipulator {
    public enum OutputStyle {
        NESTED,
        COMPACT,
        COMPRESSED,
        EXPANDED
    }

    public enum LanguageMode {
        SCSS,
        SASS
    }

    public static final String DEFAULT_SASS_EXECUTABLE = "sass";
    private final LanguageMode mode;
    private final OutputStyle style;
    private final String sassExecutable;

    public SassCompileManipulator(String sassExecutable, LanguageMode mode, OutputStyle style) {
        this.sassExecutable = sassExecutable;
        this.mode = mode;
        this.style = style;
    }

    public SassCompileManipulator(LanguageMode mode, OutputStyle style) {
        this(DEFAULT_SASS_EXECUTABLE, mode, style);
    }

    public SassCompileManipulator() {
        this(LanguageMode.SCSS, OutputStyle.NESTED);
    }

    public byte[] manipulate(AssetBundle bundle, AssetFile assetFile, byte[] content)
            throws AssetException {

        Process process = createProcess(assetFile);
        writeContent(process, content, assetFile);
        return readContent(process, assetFile);
    }

    private byte[] readContent(Process process, AssetFile asset) throws AssetException {
        try {
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                return ResourceUtil.readInputStream(process.getInputStream());
            } else {
                throw new AssetException(
                        "Failed to compile SASS asset '" + asset.getPath() + "':\r\n" +
                        new String(ResourceUtil.readInputStream(process.getErrorStream())));
            }
        } catch (InterruptedException e) {
            throw new AssetException(
                    "Failed to compile SASS asset '" + asset.getPath() + "':\r\n" +
                    "the '" + this.sassExecutable + "' process was interrupted.", e);
        } catch (IOException e) {
            throw new AssetException(
                    "Failed to compile SASS asset '" + asset.getPath() + "':\r\n" +
                    "the output of '" + this.sassExecutable + "' process could not be read.", e);
        }
    }

    private void writeContent(Process process, byte[] content, AssetFile asset) throws AssetException {
        OutputStream out = process.getOutputStream();

        int bytesWritten = 0;
        int bytesLeft = content.length;

        try {
            while (bytesWritten < content.length) {
                int length = 1024;
                if (length > bytesLeft) {
                    length = bytesLeft;
                }

                out.write(content, bytesWritten, length);
                bytesWritten += length;
                bytesLeft -= bytesWritten;
            }

            out.flush();
        } catch (IOException e) {
            throw new AssetException(
                    "Failed to compile SASS asset '" + asset.getPath() + "':\r\n" +
                    "the asset file content could not be written to process '" + this.sassExecutable + "'.", e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                
            }
        }
    }

    private Process createProcess(AssetFile asset) throws AssetException {
        ArrayList<String> args = new ArrayList<String>();
        args.add(DEFAULT_SASS_EXECUTABLE);
        args.add("--stdin");
        args.add("--style");
        args.add(this.style.toString().toLowerCase());

        if (this.mode == LanguageMode.SCSS) {
            args.add("--scss");
        }

        try {
            return Runtime.getRuntime().exec(args.toArray(new String[0]));
        } catch (IOException e) {
            StringBuilder commandLineBuilder = new StringBuilder();
            Iterator<String> iterator = args.iterator();
            while (iterator.hasNext()) {
                commandLineBuilder.append(iterator.next());

                if (iterator.hasNext()) {
                    commandLineBuilder.append(" ");
                }
            }
            
            throw new AssetException(
                    "Failed to compile SASS asset '" + asset.getPath() + "':\r\n" +
                    "failed to run command line: '" + commandLineBuilder.toString() + "'. " +
                    "Make sure the sass executable '" + this.sassExecutable + "' is in the path and " +
                    "is executable.", e);
        }
    }
}

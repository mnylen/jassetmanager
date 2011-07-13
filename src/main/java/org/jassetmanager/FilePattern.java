package org.jassetmanager;

import com.sun.istack.internal.NotNull;

public interface FilePattern {

    public boolean matches(@NotNull String path);

}

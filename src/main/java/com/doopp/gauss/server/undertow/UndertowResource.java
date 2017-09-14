package com.doopp.gauss.server.undertow;


import org.springframework.core.io.AbstractResource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class UndertowResource extends AbstractResource {

    private final File file;

    private final String path;

    public UndertowResource(File file) {
        Assert.notNull(file, "File must not be null");
        this.file = file;
        this.path = StringUtils.cleanPath(file.getPath());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    @Override
    public String getDescription() {
        return "file [" + this.file.getAbsolutePath() + "]";
    }

    /**
     * This implementation returns the underlying File reference.
     */
    @Override
    public File getFile() {
        return this.file;
    }

    /**
     * Return the file path for this resource.
     */
    public final String getPath() {
        return this.path;
    }
}

package com.doopp.gauss.server.undertow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

@Service
public class WebAppRoot implements Resource {


    private String filePath;

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public URI getURI() throws IOException {
        return null;
    }

    @Override
    public long lastModified() throws IOException {
        return 0;
    }

    @Override
    public long contentLength() throws IOException {
        return 0;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public URL getURL() throws IOException {
        return null;
    }

    @Override
    public boolean isReadable() {
        return false;
    }

    @Override
    public String getFilename() {
        return null;
    }


    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }


    @Override
    public File getFile() throws IOException {
        return new File(this.filePath);
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        return null;
    }


    @Override
    public boolean exists() {
        return false;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

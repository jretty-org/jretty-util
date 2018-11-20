package org.jretty.util.resource;

/**
 * FileSystemResource that explicitly expresses a context-relative path
 * through implementing the ContextResource interface.
 */
public class FileSystemContextResource extends FileSystemResource implements ContextResource {

    public FileSystemContextResource(String path) {
        super(path);
    }

    public String getPathWithinContext() {
        return getPath();
    }

}

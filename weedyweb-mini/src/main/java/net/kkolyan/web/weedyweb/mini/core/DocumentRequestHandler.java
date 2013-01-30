package net.kkolyan.web.weedyweb.mini.core;

import net.kkolyan.web.http.api.HttpRequest;
import net.kkolyan.web.http.api.HttpResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
public class DocumentRequestHandler implements RequestHandler {
    private List<String> documentRoots = new ArrayList<String>();
    private List<ContentType> contentTypes = new ArrayList<ContentType>();

    @Override
    public boolean handle(HttpRequest request, HttpResponse response) {
        //todo add content-type mapping
        for (String documentsRoot: this.documentRoots) {
            InputStream in = getStream(documentsRoot, request.getPath());
            if (in != null) {
                try {
                    String contentType = getContentType(request.getPath());
                    if (contentType != null) {
                        response.getHeaders().put("Content-Type", contentType);
                    }
                    byte[] buffer = new byte[64*1024];
                    while (in.available() > 0) {
                        int n = in.read(buffer);
                        response.getContent().write(buffer, 0, n);
                    }
                    return true;

                } catch (IOException e) {
                    throw new IllegalStateException(e);
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        //
                    }
                }
            }
        }
        return false;
    }
    
    private String getContentType(String path) {
        path = path.toLowerCase();
        for (ContentType mapping: contentTypes) {
            for (String extension: mapping.getExtensions().split("\\s")) {
                extension = extension.toLowerCase();
                if (extension.isEmpty()) {
                    continue;
                }
                if (path.endsWith(extension)) {
                    return mapping.getName();
                }
            }
        }
        return null;
    }
    
    private InputStream getStream(String documentsRoot, String path) {
        if (documentsRoot.startsWith("classpath:")) {
            String document = documentsRoot.substring("classpath:".length()) + path;
            if (document.startsWith("./")) {
                document = document.substring(2);
            }
            return getClass().getClassLoader().getResourceAsStream(document);
        } else {
            File documentFile = new File(documentsRoot, path);
            try {
                return new FileInputStream(documentFile);
            } catch (FileNotFoundException e) {
                return null;
            }
        }
    }

    public List<String> getDocumentRoots() {
        return documentRoots;
    }

    public void setDocumentRoots(List<String> documentRoots) {
        this.documentRoots = documentRoots;
    }

    public List<ContentType> getContentTypes() {
        return contentTypes;
    }

    public void setContentTypes(List<ContentType> contentTypes) {
        this.contentTypes = contentTypes;
    }

    @Override
    public String toString() {
        return "DocumentRequestHandler{" +
                "documentRoots=" + documentRoots +
                ", contentTypes=" + contentTypes +
                '}';
    }

    public static class ContentType {
        private String name;
        private String extensions;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExtensions() {
            return extensions;
        }

        public void setExtensions(String extensions) {
            this.extensions = extensions;
        }

        @Override
        public String toString() {
            return "ContentType{" +
                    "name='" + name + '\'' +
                    ", extensions='" + extensions + '\'' +
                    '}';
        }
    }
}

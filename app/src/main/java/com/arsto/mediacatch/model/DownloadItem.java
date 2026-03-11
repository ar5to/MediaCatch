package com.arsto.mediacatch.model;

public class DownloadItem {
    private String name;
    private String url;
    private String file_extension;
    private String source;

    public DownloadItem() {}

    public DownloadItem(String name, String url, String file_extension, String source) {
        this.name = name;
        this.url = url;
        this.file_extension = file_extension;
        this.source = source;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getFile_extension() { return file_extension; }
    public void setFile_extension(String file_extension) { this.file_extension = file_extension; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}

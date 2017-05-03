package com.oroblam.model;

import java.util.Objects;

public class Resource {

    private String url;
    private String content;

    public Resource() {
    }

    public Resource(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
            return url;
        }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(url, resource.url);
    }

    @Override
    public int hashCode() {
        //TODO: update it when using a DB
        return Math.abs(Objects.hash(url));
    }
}

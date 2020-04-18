package by.chekun.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "images_metadata")
public class ImageMetadata extends AbstractEntity {

    @Column(name = "key", nullable = false, unique = true)
    private String key;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "content_length")
    private Long contentLength;

    @Column(name = "e_tag")
    private String eTag;

    @Column(name = "last_modified")
    private Date lastModified;


    public ImageMetadata() {
    }

    @PrePersist
    @PreUpdate
    public void updateLastModified() {
        this.lastModified = new Date();
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "ImageMetadata{" +
                "key='" + key + '\'' +
                ", contentType='" + contentType + '\'' +
                ", contentLength=" + contentLength +
                ", eTag='" + eTag + '\'' +
                ", lastModified=" + lastModified +
                '}';
    }
}

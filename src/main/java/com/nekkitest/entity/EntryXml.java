package com.nekkitest.entity;


import javax.persistence.*;
import java.util.Date;

/**
 * Created by Alex on 26.12.2016.
 */
@Entity
@Table(name = "entries")
public class EntryXml {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "createdate", nullable = false)
    private Date createDate;

    public EntryXml() { }

    public EntryXml(Long id, String content, Date createDate) {
        this.id = id;
        this.content = content;
        this.createDate = createDate;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public Date getCreateDate() { return createDate; }

    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    @Override
    public String toString() {
        return "Entry{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}

/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.entities;

import net.yazsoft.frame.hibernate.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Slides.findAll", query = "SELECT w FROM Slides w")})
public class Slides extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long tid;
    private Boolean active;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private int version;
    @Size(max = 255)
    @Column(name = "title_tr", length = 255)
    private String titleTr;
    @Size(max = 255)
    @Column(name = "title_en", length = 255)
    private String titleEn;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private int rank;
    @Size(max = 1000)
    @Column(length = 1000)
    private String url;
    @Size(max = 255)
    @Column(name = "subtitle_tr", length = 255)
    private String subtitleTr;
    @Size(max = 255)
    @Column(name = "subtitle_en", length = 255)
    private String subtitleEn;

    @JoinColumn(name = "ref_image", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Images refImage;

    public Slides() {
    }

    public Slides(Long tid) {
        this.tid = tid;
    }

    public Slides(Long tid, int version, int rank) {
        this.tid = tid;
        this.version = version;
        this.rank = rank;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getTitleTr() {
        return titleTr;
    }

    public void setTitleTr(String titleTr) {
        this.titleTr = titleTr;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Images getRefImage() {
        return refImage;
    }

    public void setRefImage(Images refImage) {
        this.refImage = refImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSubtitleTr() {
        return subtitleTr;
    }

    public void setSubtitleTr(String subtitleTr) {
        this.subtitleTr = subtitleTr;
    }

    public String getSubtitleEn() {
        return subtitleEn;
    }

    public void setSubtitleEn(String subtitleEn) {
        this.subtitleEn = subtitleEn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tid != null ? tid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Slides)) {
            return false;
        }
        Slides other = (Slides) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}

/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.entities;

import net.yazsoft.frame.hibernate.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Images.findAll", query = "SELECT i FROM Images i")})
public class Images extends BaseEntity implements Serializable {
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Size(max = 1000)
    @Column(name = "title_tr", length = 1000)
    private String titleTr;
    @Size(max = 1000)
    @Column(name = "title_en", length = 1000)
    private String titleEn;
    @Size(max = 255)
    @Column(length = 255)
    private String extension;
    @Column(name = "ref_tid")
    private Long refTid;
    @OneToMany(mappedBy = "refImage", fetch = FetchType.LAZY)
    private Collection<Users> usersCollection;
    @OneToMany(mappedBy = "refImage", fetch = FetchType.LAZY)
    private Collection<Albums> albumsCollection;

    @OneToMany(mappedBy = "refImage", fetch = FetchType.LAZY)
    private Collection<Schools> schoolsCollection;
    @OneToMany(mappedBy = "refImage", fetch = FetchType.LAZY)
    private Collection<Slides> slidesCollection;
    @OneToMany(mappedBy = "refImage", fetch = FetchType.LAZY)
    private Collection<Articles> articlesCollection;
    @JoinColumn(name = "ref_album", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Albums refAlbum;
    @JoinColumn(name = "ref_image_type", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private ImagesType refImageType;
    @JoinColumn(name = "ref_school", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Schools refSchool;


    public Images() {
    }

    public Images(Long tid) {
        this.tid = tid;
    }

    public Images(Long tid, int version, String name) {
        this.tid = tid;
        this.version = version;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getRefTid() {
        return refTid;
    }

    public void setRefTid(Long refTid) {
        this.refTid = refTid;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    @XmlTransient
    public Collection<Albums> getAlbumsCollection() {
        return albumsCollection;
    }

    public void setAlbumsCollection(Collection<Albums> albumsCollection) {
        this.albumsCollection = albumsCollection;
    }


    @XmlTransient
    public Collection<Schools> getSchoolsCollection() {
        return schoolsCollection;
    }

    public void setSchoolsCollection(Collection<Schools> schoolsCollection) {
        this.schoolsCollection = schoolsCollection;
    }

    public Albums getRefAlbum() {
        return refAlbum;
    }

    public void setRefAlbum(Albums refAlbum) {
        this.refAlbum = refAlbum;
    }

    public ImagesType getRefImageType() {
        return refImageType;
    }

    public void setRefImageType(ImagesType refImageType) {
        this.refImageType = refImageType;
    }

    public Schools getRefSchool() {
        return refSchool;
    }

    public void setRefSchool(Schools refSchool) {
        this.refSchool = refSchool;
    }


    public Collection<Slides> getSlidesCollection() {
        return slidesCollection;
    }

    public void setSlidesCollection(Collection<Slides> slidesCollection) {
        this.slidesCollection = slidesCollection;
    }

    public Collection<Articles> getArticlesCollection() {
        return articlesCollection;
    }

    public void setArticlesCollection(Collection<Articles> articlesCollection) {
        this.articlesCollection = articlesCollection;
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
        if (!(object instanceof Images)) {
            return false;
        }
        Images other = (Images) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}

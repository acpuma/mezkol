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
    @NamedQuery(name = "Schools.findAll", query = "SELECT s FROM Schools s")})
public class Schools extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long tid;
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
    private Boolean active;
    private Boolean useMernis;
    @Size(max = 50)
    @Column(name = "meb_code", length = 50)
    private String mebCode;


    private String telephone;
    private String fax;
    private String email;
    private String address;
    private String mapx;
    private String mapy;
    private String facebook;
    private String linkedin;
    private String twitter;
    private String youtube;



    @JoinTable(name = "UsersSchools", joinColumns = {
        @JoinColumn(name = "ref_school", referencedColumnName = "tid", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "ref_user", referencedColumnName = "tid", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Users> usersCollection;
    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<Uploads> uploadsCollection;

    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<Albums> albumsCollection;
    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<ZlogLogin> zlogLoginCollection;
    @JoinColumn(name = "ref_image", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Images refImage;
    @JoinColumn(name = "ref_city", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cities refCity;
    @JoinColumn(name = "ref_town", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Towns refTown;
    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<Images> imagesCollection;

    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<SchoolsMenus> schoolsMenusCollection;

    @JoinColumn(name = "ref_album", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Albums refAlbum;

    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<Slides> slidesCollection;


    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<Articles> articlesCollection;

    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<WebLinks> webLinksCollection;

    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<ContactForms> contactFormsCollection;

    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<Teams> teamsCollection;

    public Schools() {
    }

    public Schools(Long tid) {
        this.tid = tid;
    }

    public Schools(Long tid, int version, String name) {
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getMebCode() {
        return mebCode;
    }

    public void setMebCode(String mebCode) {
        this.mebCode = mebCode;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    @XmlTransient
    public Collection<Uploads> getUploadsCollection() {
        return uploadsCollection;
    }

    public void setUploadsCollection(Collection<Uploads> uploadsCollection) {
        this.uploadsCollection = uploadsCollection;
    }


    @XmlTransient
    public Collection<Albums> getAlbumsCollection() {
        return albumsCollection;
    }

    public void setAlbumsCollection(Collection<Albums> albumsCollection) {
        this.albumsCollection = albumsCollection;
    }


    @XmlTransient
    public Collection<ZlogLogin> getZlogLoginCollection() {
        return zlogLoginCollection;
    }

    public void setZlogLoginCollection(Collection<ZlogLogin> zlogLoginCollection) {
        this.zlogLoginCollection = zlogLoginCollection;
    }

    public Images getRefImage() {
        return refImage;
    }

    public void setRefImage(Images refImage) {
        this.refImage = refImage;
    }

    public Cities getRefCity() {
        return refCity;
    }

    public void setRefCity(Cities refCity) {
        this.refCity = refCity;
    }

    public Towns getRefTown() {
        return refTown;
    }

    public void setRefTown(Towns refTown) {
        this.refTown = refTown;
    }

    public Boolean getUseMernis() {
        return useMernis;
    }

    public void setUseMernis(Boolean useMernis) {
        this.useMernis = useMernis;
    }

    @XmlTransient
    public Collection<Images> getImagesCollection() {
        return imagesCollection;
    }

    public void setImagesCollection(Collection<Images> imagesCollection) {
        this.imagesCollection = imagesCollection;
    }


    public Albums getRefAlbum() {
        return refAlbum;
    }

    public void setRefAlbum(Albums refAlbum) {
        this.refAlbum = refAlbum;
    }

    public Collection<Slides> getSlidesCollection() {
        return slidesCollection;
    }

    public void setSlidesCollection(Collection<Slides> slidesCollection) {
        this.slidesCollection = slidesCollection;
    }


    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMapx() {
        return mapx;
    }

    public void setMapx(String mapx) {
        this.mapx = mapx;
    }

    public String getMapy() {
        return mapy;
    }

    public void setMapy(String mapy) {
        this.mapy = mapy;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public Collection<Articles> getArticlesCollection() {
        return articlesCollection;
    }

    public void setArticlesCollection(Collection<Articles> articlesCollection) {
        this.articlesCollection = articlesCollection;
    }

    public Collection<WebLinks> getWebLinksCollection() {
        return webLinksCollection;
    }

    public void setWebLinksCollection(Collection<WebLinks> webLinksCollection) {
        this.webLinksCollection = webLinksCollection;
    }

    public Collection<SchoolsMenus> getSchoolsMenusCollection() {
        return schoolsMenusCollection;
    }

    public void setSchoolsMenusCollection(Collection<SchoolsMenus> schoolsMenusCollection) {
        this.schoolsMenusCollection = schoolsMenusCollection;
    }

    public Collection<ContactForms> getContactFormsCollection() {
        return contactFormsCollection;
    }

    public void setContactFormsCollection(Collection<ContactForms> contactFormsCollection) {
        this.contactFormsCollection = contactFormsCollection;
    }

    public Collection<Teams> getTeamsCollection() {
        return teamsCollection;
    }

    public void setTeamsCollection(Collection<Teams> teamsCollection) {
        this.teamsCollection = teamsCollection;
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
        if (!(object instanceof Schools)) {
            return false;
        }
        Schools other = (Schools) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}

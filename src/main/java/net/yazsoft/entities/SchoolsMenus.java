/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.yazsoft.frame.hibernate.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SchoolsMenus.findAll", query = "SELECT sm FROM SchoolsMenus sm")})
@Data
@EqualsAndHashCode(callSuper=false)
public class SchoolsMenus extends BaseEntity implements Serializable {
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @JoinColumn(name = "ref_menu", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menus refMenu;
    @JoinColumn(name = "ref_school", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Schools refSchool;

    public SchoolsMenus() {
    }

    public SchoolsMenus(Long tid) {
        this.tid = tid;
    }

    public SchoolsMenus(Long tid, int version) {
        this.tid = tid;
        this.version = version;
    }


}

/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.entities;

import java.io.Serializable;

import lombok.Data;
import net.yazsoft.frame.hibernate.BaseEntity;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Careers.findAll", query = "SELECT c FROM Careers c")})
@Data
public class Careers extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long tid;
    private Boolean active;
    private Boolean publish;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private int version;


    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(nullable = false, length = 45)
    private String mernisno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(nullable = false, length = 45)
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(nullable = false, length = 45)
    private String surname;
    @Column(name = "birth_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;
    @Size(max = 45)
    @Column(name = "birth_place", length = 45)
    private String birthPlace;
    @Column(name = "status_gender")
    private Integer statusGender;
    @Column(name = "status_marial")
    private Integer statusMarial;
    private Integer kids;
    @Column(name = "status_military")
    private Integer statusMilitary;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(length = 100)
    private String email;
    @Size(max = 255)
    @Column(length = 255)
    private String address;
    @Size(max = 255)
    @Column(length = 255)
    private String street;
    @Size(max = 100)
    @Column(length = 100)
    private String city;
    @Size(max = 45)
    @Column(name = "phone_home", length = 45)
    private String phoneHome;
    @Size(max = 45)
    @Column(name = "phone_cell", length = 45)
    private String phoneCell;
    @Column(name = "status_prison")
    private Integer statusPrison;
    @Column(name = "status_lawyer")
    private Integer statusLawyer;
    @Column(name = "status_health")
    private Integer statusHealth;
    @Size(max = 100)
    @Column(name = "highschool_name", length = 100)
    private String highschoolName;
    @Size(max = 100)
    @Column(name = "highschool_branch", length = 100)
    private String highschoolBranch;
    @Column(name = "highschool_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date highschoolDate;
    @Size(max = 100)
    @Column(name = "academy_name", length = 100)
    private String academyName;
    @Size(max = 100)
    @Column(name = "academy_branch", length = 100)
    private String academyBranch;
    @Column(name = "academy_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date academyDate;
    @Size(max = 100)
    @Column(name = "university_name", length = 100)
    private String universityName;
    @Size(max = 100)
    @Column(name = "university_branch", length = 100)
    private String universityBranch;
    @Column(name = "university_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date universityDate;
    @Size(max = 100)
    @Column(name = "master_name", length = 100)
    private String masterName;
    @Size(max = 100)
    @Column(name = "master_branch", length = 100)
    private String masterBranch;
    @Column(name = "master_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date masterDate;
    @Size(max = 100)
    @Column(name = "doctor_name", length = 100)
    private String doctorName;
    @Size(max = 100)
    @Column(name = "doctor_branch", length = 100)
    private String doctorBranch;
    @Column(name = "doctor_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doctorDate;
    @Column(name = "english_speak")
    private Integer englishSpeak;
    @Column(name = "english_write")
    private Integer englishWrite;
    @Size(max = 45)
    @Column(name = "second_language", length = 45)
    private String secondLanguage;
    @Column(name = "second_speak")
    private Integer secondSpeak;
    @Column(name = "second_write")
    private Integer secondWrite;
    @JoinColumn(name = "ref_school", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Schools refSchool;

    @Size(max = 255)
    @Column(length = 255)
    private String job1Company;
    @Size(max = 255)
    @Column(length = 255)
    private String job1Department;
    @Size(max = 255)
    @Column(length = 255)
    private String job1Date;
    @Size(max = 255)
    @Column(length = 255)
    private String job2Company;
    @Size(max = 255)
    @Column(length = 255)
    private String job2Department;
    @Size(max = 255)
    @Column(length = 255)
    private String job2Date;
    @Size(max = 255)
    @Column(length = 255)
    private String job3Company;
    @Size(max = 255)
    @Column(length = 255)
    private String job3Department;
    @Size(max = 255)
    @Column(length = 255)
    private String job3Date;
    @Size(max = 1000)
    @Column(length = 1000)
    private String message;

}

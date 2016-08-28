package net.yazsoft.frame.controller;

import net.yazsoft.entities.Albums;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.utils.Constants;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.entities.Schools;
import net.yazsoft.entities.Users;
import net.yazsoft.entities.ZlogLogin;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@Transactional
public class SchoolsDao extends BaseGridDao<Schools> implements Serializable{
    private static final Logger logger = Logger.getLogger(SchoolsDao.class);
    Schools selected;
    List<Schools> userSchools;

    @Inject UsersDao usersDao;
    @Inject ZlogLoginDao zlogLoginDao;
    @Inject AlbumsDao albumsDao;

    Long schoolId;

    public void initSelected() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            if (schoolId==null) schoolId=1L;
            selected = getById(schoolId);
        }
    }

    public SchoolsDao() {
        super(Schools.class);
    }

    @Override
    public void delete() {
        for (Users tempuser:getItem().getUsersCollection()) {
            tempuser.setRefActiveSchool(null);
            usersDao.update(tempuser);
        }
        for (ZlogLogin log:getItem().getZlogLoginCollection()) {
            log.setRefSchool(null);
            zlogLoginDao.update(log);
        }
        for (ZlogLogin log:getItem().getZlogLoginCollection()) {
            log.setRefSchool(null);
            zlogLoginDao.update(log);
        }
        super.delete();
    }

    @Transactional
    @Override
    public Long create() {
        //create school album
        Albums schoolAlbum=new Albums();
        schoolAlbum.setActive(Boolean.TRUE);
        schoolAlbum.setEditable(Boolean.FALSE);
        schoolAlbum.setName("SCHOOL ALBUM");
        Long albumId=albumsDao.create(schoolAlbum);
        schoolAlbum=albumsDao.getById(albumId);

        getItem().setActive(Boolean.TRUE);
        getItem().setUseMernis(Boolean.FALSE);
        Long pk=super.create();
        Schools newschool=getById(pk);
        newschool.setRefAlbum(schoolAlbum);
        newschool.setUsersCollection(usersDao.findAdmins());
        saveOrUpdate(newschool);

        schoolAlbum.setRefSchool(newschool);
        albumsDao.saveOrUpdate(schoolAlbum);

        String imageFolder=Util.getImagesFolder();
        String dirName="/"+albumId;
        File targetFolder = new File(imageFolder+dirName);
        // if the APP directory does not exist, create it
        if (!targetFolder.exists()) {
            logger.info("creating directory: " + targetFolder.toString());
            boolean dirCreated = targetFolder.mkdir();

            if(dirCreated) {
                logger.info("DIR created");
            }
        }
        return pk;
    }

    public List<Schools> findActiveUserSchools() {
        Users user= Util.getActiveUser();
        if (user.getRefRole().getName().equals(Constants.ROLE_ADMIN)) {
            return getAll();
        }
        return new ArrayList<>(Util.getActiveUser().getSchoolsCollection());
    }

    public List<Schools> findSchools() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Schools> findOutSchools() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.ne("name","MERKEZ"));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public Schools getSelected() {
        return selected;
    }

    public void setSelected(Schools selected) {
        this.selected = selected;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }
}

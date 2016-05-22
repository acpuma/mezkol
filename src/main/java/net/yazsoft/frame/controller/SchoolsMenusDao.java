package net.yazsoft.frame.controller;

import net.yazsoft.entities.Schools;
import net.yazsoft.entities.SchoolsMenus;
import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.utils.Util;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class SchoolsMenusDao extends BaseGridDao<SchoolsMenus> implements Serializable{
    private static final Logger logger = Logger.getLogger(SchoolsMenusDao.class);
    SchoolsMenus selected;

    List<SchoolsMenus> schoolsMenus;

    @Inject SchoolsDao schoolsDao;

    public List<SchoolsMenus> findBySchool(Schools school) {
        if (school==null) school=schoolsDao.getById(1L);
        logger.info("SCHOOL MENUS FIND : " +school);
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refSchool", school));
            list = c.list();
        } catch (Exception e) {
            Util.catchException(e);
        }
        logger.info("SCHOOL MENUS :" + list);
        return list;
    }

    public SchoolsMenusDao() {
        super(SchoolsMenus.class);
    }

    public SchoolsMenus getSelected() {
        return selected;
    }

    public void setSelected(SchoolsMenus selected) {
        this.selected = selected;
    }
}

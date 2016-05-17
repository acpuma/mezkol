package net.yazsoft.frame.controller;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.entities.Users;
import net.yazsoft.entities.UsersMenus;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UsersMenusDao extends BaseGridDao<UsersMenus> implements Serializable{
    private static final Logger logger = Logger.getLogger(UsersMenusDao.class);
    UsersMenus selected;

    List<UsersMenus> userMenus;

    public List<UsersMenus> findByUser(Users user) {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refUser", user));
            list = c.list();
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }

    public UsersMenusDao() {
        super(UsersMenus.class);
    }

    public UsersMenus getSelected() {
        return selected;
    }

    public void setSelected(UsersMenus selected) {
        this.selected = selected;
    }
}

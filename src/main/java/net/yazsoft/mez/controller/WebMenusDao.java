package net.yazsoft.mez.controller;

import net.yazsoft.entities.*;
import net.yazsoft.frame.controller.ContentsDao;
import net.yazsoft.frame.controller.MenusTypeDao;
import net.yazsoft.frame.controller.SchoolsMenusDao;
import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.utils.Constants;
import net.yazsoft.frame.utils.Util;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.ReorderEvent;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class WebMenusDao extends BaseGridDao<Menus> implements Serializable{
    private static final Logger logger = Logger.getLogger(WebMenusDao.class);
    Menus selected;
    List<Menus> menus;
    Boolean listChanged=true;
    Contents content;

    List<Schools> menuSchools;

    Long menuId;

    @Inject ContentsDao contentsDao;
    @Inject MenusTypeDao menusTypeDao;
    @Inject SchoolsMenusDao schoolsMenusDao;

    public void initSelected() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            selected=getById(menuId);
        }
    }


    public Menus findByName(String name) {
        Menus menu=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("nameEn",name));
            menu=(Menus)c.uniqueResult();
            listChanged=false;
        } catch (Exception e) {
            Util.catchException(e);
        }
        return menu;
    }

    public void findSchoolsByMenu(){
        menuSchools=schoolsMenusDao.findSchoolsByMenu(getItem());
    }

    public void select() {
        menuSchools=schoolsMenusDao.findSchoolsByMenu(getItem());
        super.select();
    }

    @PostConstruct
    public void init() {
        content=new Contents();
        getItem().setRefContent(content);
        getItem().setForm("/index");
    }

    public void onRowReorder(ReorderEvent event) {
        //Util.setFacesMessage("Row Moved From: " + event.getFromIndex() + ", To:" + event.getToIndex());
        try {
            menus.get(event.getFromIndex()).setRank(event.getToIndex());
            logger.info("LOG02290: MOVED : " + menus.get(event.getFromIndex()).getNameTr());
            update(menus.get(event.getFromIndex()));
            for (int i = 0; i < menus.size(); i++) {
                menus.get(i).setRank(i + 1);
                update(menus.get(i));
            }
            listChanged = true;
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public String update() {
        logger.info("UPDATING MENU...........");
        logger.info("MAIN ID : " + getItem().getMainId());
        if (getItem().getMainId()==null) {
            getItem().setMainId(getById(1L));
        }
        super.update();
        return null;
    }

    @Transactional
    public Long save() {
        Long pk=null;
        try {
            listChanged = true;
            content.setActive(true);
            if (getItem().getTid() == null) {
                getItem().setActive(true);
                if (getItem().getMainId()==null) {
                    getItem().setMainId(getById(1L));
                }
                getItem().setRefMenutype(menusTypeDao.getById(Constants.MENUTYPE_WEB));
                getItem().setRank(menus.size() + 1);
                content.setRefContentType((ContentsType) getSession().load(
                        ContentsType.class, Constants.CONTENT_MENU));
            }

            if (getItem().getRefContent() == null) {
                if (content.getContentTr() != null) {
                    //logger.info("LOG02310: SAVING CONTENT");
                    contentsDao.saveOrUpdate(content);
                }
                getItem().setRefContent(content);
            } else {
                //logger.info("LOG02310: SAVING CONTENT");
                contentsDao.saveOrUpdate(getItem().getRefContent());
            }

            getItem().setActive(true);
            //pk = super.save();
            if (status== Status.UPDATE) {
                update();
            } else {
                pk=create();
            }
            if (content.getRefTid() == null) {
                content.setRefTid(pk);
                //contentsDao.update();
            }
            logger.info("SAVING MENU SCHOOLS : " + menuSchools);
            schoolsMenusDao.deleteSchoolsByMenu(getItem());
            for (Schools mschool:menuSchools) {
                SchoolsMenus smenu=new SchoolsMenus();
                smenu.setRefMenu(getItem());
                smenu.setRefSchool(mschool);
                smenu.setActive(true);
                schoolsMenusDao.create(smenu);
            }

            reset();
        } catch (Exception e) {
            Util.catchException(e);
        }
        return pk;
    }

    @Transactional
    public void delete() {
        contentsDao.delete(getItem().getRefContent());
        super.delete();
        listChanged=true;
    }

    public List<Menus> findMenus() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refMenutype",menusTypeDao.getById(Constants.MENUTYPE_WEB)));
            //c.add(Restrictions.eq("isDeleted", false));
            c.addOrder(Order.asc("rank"));
            list = c.list();
            listChanged=false;
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }

    public WebMenusDao() {
        super(Menus.class);
    }

    public Menus getSelected() {
        return selected;
    }

    public void setSelected(Menus selected) {
        this.selected = selected;
    }

    public List<Menus> getMenus() {
        if (listChanged) {
            menus=findMenus();
        }
        return menus;
    }

    public void setMenus(List<Menus> menus) {
        this.menus = menus;
    }

    public Contents getContent() {
        if (getItem().getRefContent()!=null) {
            return getItem().getRefContent();
        }
        return content;
    }

    public void setContent(Contents content) {
        this.content = content;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public List<Schools> getMenuSchools() {
        return menuSchools;
    }

    public void setMenuSchools(List<Schools> menuSchools) {
        this.menuSchools = menuSchools;
    }
}

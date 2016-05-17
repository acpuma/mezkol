/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.frame.controller;

import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.entities.Menus;
import net.yazsoft.entities.UsersMenus;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.log4j.Logger;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named
@ViewScoped
@Transactional
public class MenusBus implements Serializable{
    private static final Logger logger = Logger.getLogger(MenusBus.class);
    private MenuModel model = new DefaultMenuModel();
    //private List<Menus> net.yazsoft.frame.menus;
    private List<UsersMenus> usersMenus;
    private List<Menus> webmenus;

    @Autowired
    MenusDao menusDao;

    public List<Menus> getWebmenus() {
        if (webmenus==null) {
            webmenus=menusDao.getMenus(2L);
        }
        return webmenus;
    }

    public MenuModel getMenuModel(Long menutype) {
        //net.yazsoft.frame.menus=menusDao.getMenus(menutype, Util.getActiveUser());
        if (Util.getActiveUser()==null) {
            return null;
        }
        usersMenus=new ArrayList(Util.getActiveUser().getUsersMenusCollection());
        getSubmenus(1L,null);
        logger.info("LOG01690: USERMENUS: " + usersMenus);
        return model;
    }

    public List<UsersMenus> getSubmenus(Long menuid,DefaultSubMenu subm) {
        
        List<UsersMenus> list=null;
        try {            
            list=findSubmenus(menuid);//dao.getSubmenus(menuid);
            for (UsersMenus menu:list) {
                List<UsersMenus> submenus=findSubmenus(menu.getRefMenu().getTid());
                if (submenus.isEmpty()) {
                    DefaultMenuItem item = new DefaultMenuItem(menu.getRefMenu().getNameTr());
                    item.setOutcome(menu.getRefMenu().getForm());
                    if (menu.getRefMenu().getIcon()!=null) {
                      item.setIcon(menu.getRefMenu().getIcon());
                    }
                    if (subm==null) {
                        model.addElement(item);                         
                    } else {
                        subm.addElement(item);
                    }
                } else {
                    DefaultSubMenu submenu = new DefaultSubMenu(menu.getRefMenu().getNameTr());
                    if (menu.getRefMenu().getIcon()!=null) {
                        submenu.setIcon(menu.getRefMenu().getIcon());
                    }
                    if (subm==null) {
                        model.addElement(submenu);
                    } else {
                        subm.addElement(submenu);
                    }
                    getSubmenus(menu.getRefMenu().getTid(),submenu);

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
    
    public List<UsersMenus> findSubmenus(Long menuid) {
        List<UsersMenus> submenus=new ArrayList<>();
        for (UsersMenus umenu:usersMenus) {
            if (umenu.getRefMenu().getMainId().getTid().equals(menuid)) {
                if (umenu.getRefMenu().getActive()==true) {
                    submenus.add(umenu);
                }
            }
        }
        BeanComparator fieldComparator=new BeanComparator("refMenu.rank");
        Collections.sort(submenus,fieldComparator);
        return submenus;
    }


    
}

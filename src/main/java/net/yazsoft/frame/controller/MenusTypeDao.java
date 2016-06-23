package net.yazsoft.frame.controller;

import net.yazsoft.entities.ImagesType;
import net.yazsoft.entities.MenusType;
import net.yazsoft.frame.controller.scopes.ViewScoped;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class MenusTypeDao extends BaseDao<MenusType> implements Serializable{
    public MenusTypeDao() {
        super(MenusType.class);
    }
}

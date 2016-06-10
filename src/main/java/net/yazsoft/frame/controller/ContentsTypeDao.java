package net.yazsoft.frame.controller;


import net.yazsoft.entities.ArticlesType;
import net.yazsoft.entities.ContentsType;
import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.hibernate.BaseGridDao;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ContentsTypeDao extends BaseGridDao<ContentsType> implements Serializable{
    private ContentsType selected;

    public ContentsTypeDao() {
        super(ContentsType.class);
    }

    public ContentsType getSelected() {
        return selected;
    }

    public void setSelected(ContentsType selected) {
        this.selected = selected;
    }
}

package net.yazsoft.frame.controller;


import net.yazsoft.entities.ArticlesType;
import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.hibernate.BaseGridDao;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ArticlesTypeDao extends BaseGridDao<ArticlesType> implements Serializable{
    private ArticlesType selected;

    public ArticlesTypeDao() {
        super(ArticlesType.class);
    }

    public ArticlesType getSelected() {
        return selected;
    }

    public void setSelected(ArticlesType selected) {
        this.selected = selected;
    }
}

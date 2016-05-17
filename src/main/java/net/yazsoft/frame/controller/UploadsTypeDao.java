package net.yazsoft.frame.controller;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.entities.UploadsType;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class UploadsTypeDao extends BaseGridDao<UploadsType> implements Serializable{
    UploadsType selected;

    public UploadsTypeDao() {
        super(UploadsType.class);
    }

    public UploadsType getSelected() {
        return selected;
    }

    public void setSelected(UploadsType selected) {
        this.selected = selected;
    }
}

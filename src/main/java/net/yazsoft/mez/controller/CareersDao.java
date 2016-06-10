package net.yazsoft.mez.controller;

import net.yazsoft.entities.Careers;
import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.utils.Util;
import org.apache.log4j.Logger;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class CareersDao extends BaseGridDao<Careers> implements Serializable {
    private static final Logger logger = Logger.getLogger(CareersDao.class);
    Careers selected;

    public Long save() {
        getItem().setActive(Boolean.TRUE);
        getItem().setCreated(Util.getNow());
        return super.save();
    }

    public CareersDao() {
        super(Careers.class);
    }

    public Careers getSelected() {
        return selected;
    }

    public void setSelected(Careers selected) {
        this.selected = selected;
    }

}
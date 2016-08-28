package net.yazsoft.frame.controller;


import net.yazsoft.entities.TeamsType;
import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.utils.Util;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.RowEditEvent;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class TeamsTypeDao extends BaseGridDao<TeamsType> implements Serializable{

    private TeamsType selected;
    private String newtypename;

    public Long create() {
        Long pk=null;
        try {
            TeamsType productsType = new TeamsType();
            productsType.setActive(true);
            productsType.setName(newtypename);
            pk = create(productsType);
        } catch (Exception e) {
            Util.catchException(e);
        }
        return pk;
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            TeamsType parameter=(TeamsType) event.getObject();
            DataTable table = (DataTable) event.getSource();
            parameter = (TeamsType) table.getRowData();
            super.update(parameter);
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void onRowCancel(RowEditEvent event) {
        //Util.setFacesMessage("Edit Cancelled : " + ((LessonsDto) event.getObject()).getTid());
    }


    public TeamsTypeDao() {
        super(TeamsType.class);
    }

    public TeamsType getSelected() {
        return selected;
    }

    public void setSelected(TeamsType selected) {
        this.selected = selected;
    }

    public String getNewtypename() {
        return newtypename;
    }

    public void setNewtypename(String newtypename) {
        this.newtypename = newtypename;
    }
}

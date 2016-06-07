package net.yazsoft.frame.controller;

import net.yazsoft.entities.ContactForms;
import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.utils.Constants;
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
public class ContactFormsDao extends BaseGridDao<ContactForms> implements Serializable{
    private static final Logger logger = Logger.getLogger(ContactFormsDao.class);
    ContactForms selected;
    List<ContactForms> items;
    Boolean itemsChanged=true;

    @Inject Email email;

    public void checkboxChange(ContactForms cform) {
        try {
            update(cform);
            //Util.setFacesMessage("CHANGED");
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void sendMessage() {
        try {
            getItem().setActive(true);
            getItem().setCreated(Util.getNow());
            create();
            String body=" İletişim Formu - Yeni Mesaj : "
                    +"\nAd : "  + getItem().getName()
                    +"\nTelefon : " + getItem().getPhone()
                    +"\nEposta : " + getItem().getEmail()
                    +"\nMesaj : " + getItem().getMessage();
            //email.sendMail(Constants.EMAIL_NOTIFICATION, "Mezopotamya İletisim Formu Yeni Mesaj", body);
            //Util.setFacesMessage("MESAJ GÖNDERİLDİ");
            reset();
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void delete() {
        super.delete();
        itemsChanged=true;
    }

    public List<ContactForms> findItems() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
            itemsChanged=false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public ContactFormsDao() {
        super(ContactForms.class);
    }

    public ContactForms getSelected() {
        return selected;
    }

    public void setSelected(ContactForms selected) {
        this.selected = selected;
    }

    public List<ContactForms> getItems() {
        if (itemsChanged) {
            items=findItems();
        }
        return items;
    }

    public void setItems(List<ContactForms> items) {
        this.items = items;
    }


}

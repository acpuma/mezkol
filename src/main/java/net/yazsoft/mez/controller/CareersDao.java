package net.yazsoft.mez.controller;

import net.yazsoft.entities.Careers;
import net.yazsoft.frame.controller.Email;
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
public class CareersDao extends BaseGridDao<Careers> implements Serializable {
    private static final Logger logger = Logger.getLogger(CareersDao.class);
    Careers selected;

    @Inject Email email;

    public void checkboxChange(Careers cform) {
        try {
            update(cform);
            //Util.setFacesMessage("CHANGED");
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public Long save() {
        Long pk=null;
        try {
            getItem().setActive(Boolean.TRUE);
            getItem().setCreated(Util.getNow());
            getItem().setReaden(Boolean.FALSE);

            String body=" Sayın " + getItem().getName() + " " + getItem().getSurname() + ","
                    + "\n\nBaşvurunuz alınmıştır olumlu olursa geri dönüş yapacağız. "
                    +"\n\n MEZOPOTAMYA EĞİTİM KURUMLARI";

            pk=super.save();
            email.sendMail(getItem().getEmail(), "Mezopotamya Eğitim Kurumları Kariyer Başvurusu",body);
        } catch (Exception e) {
            Util.catchException(e);
        }
        return pk;

    }

    public List<Careers> findItemsUnread() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("readen", Boolean.FALSE));
            list = c.list();
            logger.info("UNREADEN CAREERS: " + list);
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
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
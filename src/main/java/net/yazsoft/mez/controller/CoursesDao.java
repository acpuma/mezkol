package net.yazsoft.mez.controller;

import net.yazsoft.entities.Contents;
import net.yazsoft.entities.ContentsType;
import net.yazsoft.entities.Courses;
import net.yazsoft.frame.controller.Archives;
import net.yazsoft.frame.controller.ContentsDao;
import net.yazsoft.frame.controller.ContentsTypeDao;
import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.utils.Constants;
import net.yazsoft.frame.utils.Util;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.ReorderEvent;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.*;

@Named
@ViewScoped
public class CoursesDao extends BaseGridDao<Courses> implements Serializable {
    private static final Logger logger = Logger.getLogger(CoursesDao.class);
    Courses selected;
    List<Courses> items;
    List<Courses> webitems;
    Boolean itemsChanged = true;
    Contents content;
    Integer month,year;
    Long courseId;

    @Inject ContentsDao contentsDao;
    @Inject ContentsTypeDao contentsTypeDao;

    public void initSelected() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            selected = getById(courseId);
        }
    }

    @PostConstruct
    public void init() {
        content = new Contents();
        getItem().setRefContent(content);
        webitems=new ArrayList<>();
    }


    public void checkboxChange(Courses course) {
        try {
            update(course);
            //Util.setFacesMessage("CHANGED");
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void onRowReorder(ReorderEvent event) {
        //Util.setFacesMessage("Row Moved From: " + event.getFromIndex() + ", To:" + event.getToIndex());
        try {
            items.get(event.getFromIndex()).setRank(event.getToIndex());
            logger.info("LOG02290: MOVED : " + items.get(event.getFromIndex()).getTitleTr());
            update(items.get(event.getFromIndex()));
            for (int i = 0; i < items.size(); i++) {
                items.get(i).setRank(i + 1);
                update(items.get(i));
            }
            itemsChanged = true;
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public Long save() {
        Long pk = null;
        try {
            itemsChanged = true;
            content.setActive(true);
            if (getItem().getTid() == null) {
                getItem().setActive(true);
                getItem().setRank(items.size() + 1);
                content.setRefContentType(contentsTypeDao.getById(Constants.CONTENT_COURSE));
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
            if (status == Status.UPDATE) {
                update();
            } else {
                pk = create();
            }
            if (content.getRefTid() == null) {
                content.setRefTid(pk);
                //contentsDao.update();
            }
            reset();
        } catch (Exception e) {
            Util.catchException(e);
        }
        return pk;
    }

    public void delete() {
        try {
            contentsDao.delete(getItem().getRefContent());
            super.delete();
            itemsChanged = true;
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public List<Courses> findItems(Boolean active) {
        List list = null;
        try {
            Criteria c = getCriteria();
            //c.add(Restrictions.eq("isDeleted", false));
            //c.add(Restrictions.eq("publish",true));
            if (active!=null) {
                c.add(Restrictions.eq("active", active));
            }
            c.addOrder(Order.desc("date"));
            list = c.list();
            itemsChanged = false;
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }

    public CoursesDao() {
        super(Courses.class);
    }

    public Courses getSelected() {
        return selected;
    }

    public void setSelected(Courses selected) {
        this.selected = selected;
    }

    public List<Courses> getItems() {
        if (itemsChanged) {
            items = findItems(null);
        }
        return items;
    }

    public void setItems(List<Courses> items) {
        this.items = items;
    }

    public List<Courses> getWebitems() {
        webitems = findItems(true);
        return webitems;
    }

    public void setWebitems(List<Courses> webitems) {
        this.webitems = webitems;
    }

    public Contents getContent() {
        if (getItem().getRefContent() != null) {
            return getItem().getRefContent();
        }
        return content;
    }

    public void setContent(Contents content) {
        this.content = content;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
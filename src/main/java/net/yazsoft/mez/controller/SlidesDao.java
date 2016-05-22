package net.yazsoft.mez.controller;

import net.yazsoft.entities.Slides;
import net.yazsoft.frame.controller.ImagesDao;
import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.utils.Util;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.ReorderEvent;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class SlidesDao extends BaseGridDao<Slides> implements Serializable{
    private static final Logger logger = Logger.getLogger(SlidesDao.class);
    Slides selected;
    List<Slides> slides;
    Boolean listChanged=true;

    @Inject ImagesDao imagesDao;


    public void onRowReorder(ReorderEvent event) {
        //Util.setFacesMessage("Row Moved From: " + event.getFromIndex() + ", To:" + event.getToIndex());
        try {
            int size=slides.size();
            slides.get(event.getFromIndex()).setRank(size-event.getToIndex());
            logger.info("LOG02290: MOVED : " + slides.get(event.getFromIndex()).getTitleTr());
            update(slides.get(event.getFromIndex()));
            for (int i = 0; i < slides.size(); i++) {
                slides.get(i).setRank(size - i);
                update(slides.get(i));
            }
            listChanged = true;
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void addSlide() {
        Slides temp=new Slides();
        temp.setActive(true);
        temp.setTitleTr("Slide");
        temp.setTitleTr("Subtitle");
        temp.setRank(slides.size() + 1);
        create(temp);
        selected=temp;
        listChanged = true;
    }

    public void delete() {
        try {
            if (getItem().getRefImage()!=null) {
                String uploadsFolder = Util.getUploadsFolder();
                String extension = getItem().getRefImage().getExtension();
                String dirName;
                dirName = uploadsFolder + "/images/slide";
                File file = new File(dirName + "/" + getItem().getTid().toString() + "." + extension);
                file.delete();
                imagesDao.delete(getItem().getRefImage());
                logger.info("LOG01740: DELETED : " + file.getName());
            }
            super.delete();
            listChanged = true;
            //Util.setFacesMessage("SLIDE SİLİNDİ");
        } catch (Exception e) {
            logger.error("EXCEPTION: ", e);
            Util.setFacesMessageError(e.getMessage());
            throw e;
        }
    }

    public List<Slides> findSlides() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.addOrder(Order.desc("rank"));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
            listChanged=false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public SlidesDao() {
        super(Slides.class);
    }

    public Slides getSelected() {
        return selected;
    }

    public void setSelected(Slides selected) {
        this.selected = selected;
    }

    public List<Slides> getSlides() {
        if (listChanged) {
            slides = findSlides();
        }
        return slides;
    }

    public void setSlides(List<Slides> slides) {
        this.slides = slides;
    }
}

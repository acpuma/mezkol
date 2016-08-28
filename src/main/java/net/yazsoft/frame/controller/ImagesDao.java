package net.yazsoft.frame.controller;

import net.yazsoft.entities.*;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.mez.controller.CoursesDao;
import net.yazsoft.mez.controller.SlidesDao;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.Select;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.context.ApplicationContext;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class ImagesDao extends BaseGridDao<Images> implements Serializable{
    private static final Logger logger = Logger.getLogger(ImagesDao.class);
    Images selected;
    List<String> imageFolders;
    String selectUpdate;
    BaseGridDao selectDao;

    public void findImagePathJs() {
        RequestContext reqCtx = RequestContext.getCurrentInstance();
        String ctxPath=FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath();
        reqCtx.addCallbackParam("selectedImagePathStr", ctxPath +findImagePath(selected));
    }
    public String findImagePath(Images image) {
        String path = "";
        if (image!=null) {
            path= "/images/" + findFolder(image) + "/" + image.getTid() + "." + image.getExtension();
            logger.info("IMAGE :" + image + " PATH : " + path);
            return path;
        }
        return path;
    }

    public String findImageSmallPath(Images image) {
        String path = "";
        if (image!=null) {
            path= "/images/" + findFolder(image) + "/SMALL/" + image.getTid() + "." + image.getExtension();
            logger.info("IMAGE :" + image + " PATH : " + path);
            return path;
        }
        return path;
    }

    public void onImageSelect(Images image) {
        try {
            selected=image;
            if (selectDao instanceof SlidesDao) {
                logger.info("image select slidesdao : " + selectDao);
                ((SlidesDao) selectDao).getItem().setRefImage(image);
            } else if (selectDao instanceof SchoolsDao) {
                logger.info("image select schoolsdao : " + selectDao);
                ((SchoolsDao) selectDao).getItem().setRefImage(image);
            } else if (selectDao instanceof ArticlesDao) {
                logger.info("image select articlesdao : " + selectDao);
                ((ArticlesDao) selectDao).getItem().setRefImage(image);
            } else if (selectDao instanceof CoursesDao) {
                logger.info("image select coursesdao : " + selectDao);
                ((CoursesDao) selectDao).getItem().setRefImage(image);
            } else if (selectDao instanceof WebLinksDao) {
                logger.info("image select weblinksdao : " + selectDao);
                ((WebLinksDao) selectDao).getItem().setRefImage(image);
            } else if (selectDao instanceof TeamsDao) {
                logger.info("image select teamsdao : " + selectDao);
                ((TeamsDao) selectDao).getItem().setRefImage(image);
            }
            logger.info("SELECTED IMAGE : " + image);
            logger.info("UPDATING : " + selectUpdate);
            logger.info("SELECT DAO : " + selectDao);
            if (selectUpdate!=null) {
                RequestContext.getCurrentInstance().update(selectUpdate);
            }
            //Util.setFacesMessage("SELECTED IMAGE : " + image);
        } catch (Exception e) {
            Util.catchException(e);
        }

    }

    public Images findImageByTidAndType(Long tid,ImagesType imageType) {
        logger.info("LOG01720: TID , IMAGETYPE : " + tid + "," + imageType);
        Images temp=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refTid", tid));
            c.add(Restrictions.eq("refImageType", imageType));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            temp = (Images) c.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return temp;
    }

    public void removeImage(Images image) {
        try {
            logger.info("REMOVING IMAGE : " + image);
            delete(image);
            deleteImage(image);
            Util.setFacesMessage("RESIM SILINDI");
        } catch (Exception e) {
            //e.printStackTrace();
            logger.log(Level.ERROR, e.getMessage(), e);
            Util.setFacesMessage(e.getMessage());
        }
    }

    private void deleteImage(Images image) {
        String imageFolder = Util.getImagesFolder();
        String dirName = imageFolder + "/" + findFolder(image);
//        dirName = dirName + ("/" + image.getRefAlbum().getTid().toString());

        File file = new File(dirName + "/" + image.getName());
        if (file.delete()) {
            logger.info(file.getName() + " is deleted!");
        } else {
            logger.info("DELETING : " + dirName + "/" + image.getName());
            Util.setFacesMessageError("Fiziksel resim dosyasi silinemedi");
        }
        //deleting thumbnail
        file = new File(dirName + "/SMALL/" + image.getName());
        if (file.delete()) {
            logger.info(file.getName() + " is deleted(small)!");
        } else {
            logger.info("DELETING(small) : " + dirName + "/" + image.getName());
            Util.setFacesMessageError("Fiziksel resim onizleme dosyasi silinemedi");
        }
    }

    public List<Images> getAlbumImages(Albums album) {
        Criteria query;
        List list=null;
        try {
            query = getSession().createCriteria(Images.class);
            query.add(Restrictions.eq("refAlbum", album));
            query.add(Restrictions.eq("active", true));
            //query.setProjection(Projections.rowCount());
            list = query.list();
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }



    public String findFolder(Images image) {
        String albumPath = "";
        if (image!=null) {
            Albums album = image.getRefAlbum();
            if (album != null) {
                imageFolders = new ArrayList<>();
                if (album.getRefAlbum() != null) {
                    imageFolders.add(album.getTid().toString());
                    findSubAlbumFolder(album.getRefAlbum());
                } else {
                    return album.getTid().toString();
                }

                logger.info("ALBUM FOLDERS : " + imageFolders + imageFolders.size());
                Collections.reverse(imageFolders);
                for (String fold : imageFolders) {
                    albumPath = albumPath + fold + "/";
                    logger.info("PATH : " + albumPath);
                }
                //remove last context
                albumPath = albumPath.substring(0, albumPath.length() - 1);
                logger.info("PATH : " + albumPath);
            } else {
                logger.info("ALBUM IS NULL");
            }
        } else {
            logger.info("IMAGE IS NULL");
        }
        return albumPath;
    }

    public void findSubAlbumFolder(Albums album) {
        imageFolders.add(album.getTid().toString());
        if (album.getRefAlbum()!=null) {
            findSubAlbumFolder(album.getRefAlbum());
        }
        logger.info("ALBUM FOLDERS : " + imageFolders);
    }

    public ImagesDao() {
        super(Images.class);
    }

    public Images getSelected() {
        return selected;
    }

    public void setSelected(Images selected) {
        this.selected = selected;
    }

    public String getSelectUpdate() {
        return selectUpdate;
    }

    public void setSelectUpdate(String selectUpdate) {
        this.selectUpdate = selectUpdate;
    }

    public BaseGridDao getSelectDao() {
        return selectDao;
    }

    public void setSelectDao(BaseGridDao selectDao) {
        this.selectDao = selectDao;
    }

}

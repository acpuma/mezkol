/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.frame.controller;

import net.yazsoft.entities.*;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.utils.Constants;
import net.yazsoft.frame.utils.Util;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named
@ViewScoped
public class AlbumsDao extends BaseGridDao<Albums> implements Serializable{
    
    static final Logger logger= Logger.getLogger(AlbumsDao.class.getName());

    private Albums selected;
    //private String albumPath="";
    private List<String> albumFolders;

    @Inject ImagesDao imagesDao;
    @Inject ImagesTypeDao imagesTypeDao;
    
    public AlbumsDao() {
        super(Albums.class);
    }

    public void setSelectedById(Long tid) {
        selected=getById(tid);
    }

    public void findActiveSchoolAlbum() {
        selected=Util.getActiveSchool().getRefAlbum();
        logger.info("SELECTED ALBUM : " + selected);
    }

    public void setStatus(Integer newStatus) {
        switch(newStatus) {
            case 0: status=Status.CREATE; reset();break;
            case 2: status=Status.UPDATE; break;
        }
    }

    public void addAlbum() {
        logger.info("STATUS : " + getStatus());

        try {
            if (status==Status.UPDATE) {
                logger.info("UPDATING ALBUM");
                update();
            } else {
                logger.info("ADDING ALBUM : " + getItem().getName());
                getItem().setRefAlbum(selected);
                getItem().setActive(Boolean.TRUE);
                getItem().setEditable(Boolean.TRUE);
                getItem().setRefSchool(Util.getActiveSchool());
                Long newid = save();
                //make album dir
                logger.info("NEW ALBUM ID : " + newid);
                String imageFolder = Util.getImagesFolder();
                File targetFolder = new File(imageFolder + "/" + findAlbumFolder(selected), "/" + newid);
                // if the APP directory does not exist, create it
                if (!targetFolder.exists()) {
                    logger.info("creating directory: " + targetFolder.toString());
                    boolean dirCreated = targetFolder.mkdir();

                    if (dirCreated) {
                        logger.info("DIR created");
                    }
                }
            }

        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public List<Albums> findSubAlbums(Albums album) {
        Criteria query;
        List<Albums> subalbums=null;
        logger.info("GET ALBUM : " + album);
        try {
            query = getSession().createCriteria(Albums.class);
            query.add(Restrictions.eq("refAlbum", album));
            query.add(Restrictions.eq("active",true));
            //query.setProjection(Projections.rowCount());
            subalbums =query.list();
            logger.info("GET ALBUM : " + album);
        } catch (Exception e) {
            Util.catchException(e);
        }

        return subalbums;
    }

    public Albums getAlbum(Schools school) {
        Criteria query;
        Albums album =null;
        logger.info("GET ALBUM : " + album);
        try {
            query = getSession().createCriteria(Albums.class);
            query.add(Restrictions.eq("refSchool", school));
            //query.setProjection(Projections.rowCount());
            album = (Albums) query.uniqueResult();
            logger.info("GET ALBUM : " + album);
        } catch (Exception e) {
            Util.catchException(e);
        }

        return album;
    }

    public void deleteAlbum(Albums album) {
        try {
            logger.info("DELETING ALBUM : " + album.getTid());

            //delete sub albums
            for (Albums subalbum:album.getAlbumsCollection()) {
                deleteAlbum(subalbum);
            }

            // DELETE ALBUM IMAGES
            deleteAlbumImages(album);

            //DELETE ALBUM
            super.delete(album);
            Util.setFacesMessage("ALBUM SILINDI");
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public List<AlbumsImagesDto> findAlbumAndImages(Albums album) {
        List<AlbumsImagesDto> tempList=null;
        logger.info("find album : " + album);
        AlbumsImagesDto dto=null;
        try {
            tempList=new ArrayList<>();
            List<Albums> albums=findSubAlbums(album);
            for (Albums tempAlbum:albums) {
                dto=new AlbumsImagesDto();
                dto.setAlbum(tempAlbum);
                dto.setIsAlbum(Boolean.TRUE);
                tempList.add(dto);
            }
            for (Images image:imagesDao.getAlbumImages(album)) {
                dto=new AlbumsImagesDto();
                dto.setImage(image);
                dto.setIsAlbum(Boolean.FALSE);
                tempList.add(dto);
            }
        } catch (Exception e) {
            Util.catchException(e);
        }
        return tempList;
    }

    public String findAlbumFolder(Albums album) {
        albumFolders=new ArrayList<>();
        if (album.getRefAlbum()!=null) {
            albumFolders.add(album.getTid().toString());
            findSubAlbumFolder(album.getRefAlbum());
        } else {
            return album.getTid().toString();
        }
        String albumPath="";
        logger.info("ALBUM FOLDERS : " + albumFolders + albumFolders.size());
        Collections.reverse(albumFolders);
        for (String fold:albumFolders) {
            albumPath=albumPath+fold+ "/";
            logger.info("PATH : " + albumPath);
        }
        //remove last context
        albumPath=albumPath.substring(0,albumPath.length()-1);
        logger.info("PATH : " + albumPath);
        return albumPath;
    }

    public void findSubAlbumFolder(Albums album) {
        albumFolders.add(album.getTid().toString());
        if (album.getRefAlbum()!=null) {
            findSubAlbumFolder(album.getRefAlbum());
        }
        logger.info("ALBUM FOLDERS : " + albumFolders);
    }


    public void deleteAlbumImages(Albums album) throws IOException {
        ArrayList<Images> images=new ArrayList<>(imagesDao.getAlbumImages(album));
        //TODO : define delete images
        for (Images image:images) {
            imagesDao.delete(image);
        }

        //DELETE ALBUM DIRECTORY
        FileUtils.deleteDirectory(new File(Util.getImagesFolder()
                + "/" + findAlbumFolder(album)) );
    }


    public Albums getSelected() {
        return selected;
    }

    public void setSelected(Albums selected) {
        this.selected = selected;
    }

}

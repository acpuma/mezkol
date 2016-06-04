package net.yazsoft.frame.controller;

import net.yazsoft.entities.Albums;
import net.yazsoft.entities.Images;
import net.yazsoft.frame.utils.Util;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import java.awt.image.BufferedImage;
import java.io.*;

/* @author acpuma */

@Named
@ViewScoped
public class UploadBean implements Serializable{
    private static final Logger logger = Logger.getLogger(UploadBean.class.getName());
    //TODO: + Resize big images
    private static final int BUFFER_SIZE = 6124;
    private UploadedFile uploadedFile;
    private Boolean albumUpload;
    //private Albums album;
    

    @Inject ImagesDao imagesDao;
    @Inject AlbumsDao albumsDao;


    @PostConstruct
    public void init() {
        logger.info("UPLOAD CONSTRUCTOR");
        albumUpload=false;
        //album=activeSchool.getId();
    }

    
    public void remove(Images image) {
        //ImagesDao imageDao=new ImagesDao();
        imagesDao.delete(image);
        Util.setFacesMessage("Deleted image id : " + image.getTid());
    }

    public void handleFileUpload(FileUploadEvent event) {
        Albums album=albumsDao.getSelected();
       logger.info("UPLOADING FILE.......");
       logger.info("ALBUM ID : " + album.getTid());
       
       ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        
        //File result = new File(extContext.getRealPath("//WEB-INF//files//"+event.getFile().getFileName()));
        //ImagesDao imageDao=new ImagesDao();
        try {
            String filenameOriginal=event.getFile().getFileName();
            String extension=filenameOriginal.substring(filenameOriginal.lastIndexOf(".")+1).toUpperCase();
            logger.info("EXTENSION : " + extension);
            
            Images image=new Images();
            image.setActive(Boolean.TRUE);
            image.setExtension(extension);
            image.setRefSchool(album.getRefSchool());
            //image.getRefApp().setTblId(activeApp.getId());//setRef_app_id(Util.getAppId());
            image.setName(filenameOriginal);
            image.setTitleTr(filenameOriginal);
            //Albums album = new Albums();
            //album.setTid(activeSchool.getId());
            image.setRefAlbum(album);
            //image.getRefAlbums().setTblId(albumId);//setRef_app_album_id(albumId);
            
            imagesDao.setItem(image);
            Long imageid=imagesDao.save();
            image.setTid(imageid);
            
            //String filename=filenameOriginal;
            String filename=image.getTid().toString()+"."+extension;
            image.setName(filename);
            image.setTitleTr(filenameOriginal);
            imagesDao.update(image);
            
            String imageFolder=Util.getImagesFolder();
            String dirName="/"+albumsDao.findAlbumFolder(albumsDao.getSelected());
            File targetFolder = new File(imageFolder,dirName);

            // if the APP directory does not exist, create it
            if (!targetFolder.exists()) {
              //logger.info("creating directory: " + targetFolder.toString());
              boolean dirCreated = targetFolder.mkdir();  

               if(dirCreated) {    
                 logger.info("DIR created");  
               }
            }
            
            // if the ALBUM directory does not exist, create it
            //dirName=dirName+("/"+album.getTid().toString());
            targetFolder = new File(imageFolder,dirName);
            if (!targetFolder.exists()) {
              //logger.info("creating directory: " + targetFolder.toString());
              boolean dirCreated = targetFolder.mkdir();  

               if(dirCreated) {    
                 logger.info("DIR created");  
               }
            }

            BufferedImage bufferedImage= ImageIO.read(event.getFile().getInputstream());
            Integer imageWidth=bufferedImage.getWidth();
            InputStream inputStream;
            if (imageWidth>1600) {
                inputStream = Util.imageResize(event.getFile().getInputstream(), extension, 1600, 800);
            } else {
                inputStream = event.getFile().getInputstream();
            }
            OutputStream out = new FileOutputStream(new File(targetFolder,filename));
            int read = 0;
            byte[] bytes = new byte[BUFFER_SIZE];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();
            
            
            //CREATING THUMBNAIL
            dirName=dirName+("/SMALL");
            targetFolder = new File(imageFolder,dirName);
            // if the SMALL directory does not exist, create it
            if (!targetFolder.exists()) {
              //logger.info("creating directory: " + targetFolder.toString());
              boolean dirCreated = targetFolder.mkdir();  

               if(dirCreated) {    
                 logger.info("DIR created");  
               }
            }
            
            inputStream = Util.imageThumbnail(event.getFile().getInputstream(),extension);
            out = new FileOutputStream(new File(targetFolder,filename));
            read = 0;
            bytes = new byte[BUFFER_SIZE];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();
            
            imagesDao.setSelected(image);
            
            FacesMessage msg = new FacesMessage("File Description","ID : "+ imageid +  " ,file name: "
                    + event.getFile().getFileName() +" File size: "+ event.getFile().getSize()/1024 +" Kb Content type: "
                    + event.getFile().getContentType() + " the file was uploaded.");
            FacesContext.getCurrentInstance().addMessage(null,msg);
        } catch(Exception e){
            Util.catchException(e);
        }
        
    }
    
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public Boolean getAlbumUpload() {
        return albumUpload;
    }

    public void setAlbumUpload(Boolean albumUpload) {
        this.albumUpload = albumUpload;
    }

    
}

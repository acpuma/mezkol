package net.yazsoft.frame.controller;

import net.yazsoft.entities.*;
import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.mez.controller.SlidesDao;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;

@Named
@ViewScoped
public class UploadsBean extends BaseDao implements Serializable{
    private static final Logger logger = Logger.getLogger(UploadsBean.class.getName());
    private static final int BUFFER_SIZE = 6124;
    public static final long IMAGE = 1L;
    public static final long LOGO = 2L;
    public static final long FILE = 3L;
    public static final String IMAGE_SCHOOL = "school";
    public static final String IMAGE_USER ="user";
    public static final String IMAGE_STUDENT ="student";
    public static final String IMAGE_SLIDE ="slide";
    public static final String IMAGE_TEAM ="team";
    public static final String IMAGE_PRODUCT ="product";
    public static final String IMAGE_WEBLINK ="weblink";
    private UploadedFile uploadedFile;
    Schools activeSchool;
    File uploadDirectory;
    Long fileType;
    Albums album;
    UploadsType uploadType;
    String detail;
    String imageType;
    Integer imageWidth;
    Integer imageHeight;

    @Inject UploadsDao uploadsDao;
    @Inject UploadsTypeDao uploadsTypeDao;
    @Inject ImagesDao imagesDao;
    @Inject ImagesTypeDao imagesTypeDao;

    @Inject SchoolsDao schoolsDao;
    @Inject UsersDao usersDao;
    @Inject SlidesDao slidesDao;
    @Inject TeamsDao teamsDao;

    @PostConstruct
    public void init() {
        logger.info("UPLOAD CONSTRUCTOR");
        //album=activeSchool.getId();
        fileType=FILE;
    }

    @Transactional
    public void handleImageUpload(FileUploadEvent event) {
        logger.info("UPLOADING IMAGE.......");
        fileType=IMAGE;
        Long tid=null;
        switch (imageType){
            case IMAGE_SCHOOL: tid=schoolsDao.getItem().getTid();break;
            case IMAGE_USER: tid=usersDao.getItem().getTid();break;
            case IMAGE_SLIDE: tid=slidesDao.getItem().getTid();break;
            case IMAGE_TEAM: tid=teamsDao.getItem().getTid();break;
        }

        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            getUploadDirectory(null);
            String filenameOriginal=event.getFile().getFileName();
            String extension=UploadsDao.getFileExtension(filenameOriginal);

            InputStream inputStream;
            inputStream = event.getFile().getInputstream();
            BufferedImage bufferedImage= ImageIO.read(event.getFile().getInputstream());
            Integer imgWidth=bufferedImage.getWidth();
            Integer imgHeight=bufferedImage.getHeight();
            if (imageWidth==null) {
                imageWidth=256;
            }

            if (imgWidth>imageWidth) {
                imageHeight=imgHeight*(imageWidth/imgWidth); //scale
                inputStream = Util.imageResize(event.getFile().getInputstream(), extension, imageWidth, imageHeight);
            }
            String filename=tid.toString()+"."+extension.toLowerCase();
            OutputStream out = new FileOutputStream(new File(uploadDirectory,filename));
            int read = 0;
            byte[] bytes = new byte[BUFFER_SIZE];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();

            //Util.setFacesMessage(" RESIM YUKLENDI ");

            ImagesType imagesType=null;
            switch (imageType) {
                case (IMAGE_USER) : imagesType=(ImagesType)getSession().load(ImagesType.class,1L);break;
                case (IMAGE_SCHOOL) : imagesType=(ImagesType)getSession().load(ImagesType.class,2L);break;
                case (IMAGE_SLIDE) : imagesType=(ImagesType)getSession().load(ImagesType.class,3L);break;
                case (IMAGE_TEAM) : imagesType=(ImagesType)getSession().load(ImagesType.class,7L);break;
            }

            Images image=null;
            image=imagesDao.findImageByTidAndType(tid,imagesType);
            if (image==null) {
                image=new Images();
            }
            image.setName(filenameOriginal);
            image.setExtension(extension);
            image.setRefSchool(Util.getActiveSchool());
            image.setActive(true);
            image.setRefTid(tid);
            image.setRefImageType(imagesType);

            imagesDao.saveOrUpdate(image);
            switch (imageType) {
                case (IMAGE_USER) : usersDao.getItem().setRefImage(image); usersDao.update();break;
                case (IMAGE_SCHOOL) : schoolsDao.getItem().setRefImage(image); schoolsDao.update();break;
                case (IMAGE_SLIDE) : slidesDao.getItem().setRefImage(image); slidesDao.update();break;
                case (IMAGE_TEAM) : teamsDao.getItem().setRefImage(image); teamsDao.update();break;
            }
        } catch (Exception e) {
            logger.error("EXCEPTION: ", e);
            Util.setFacesMessageError(e.getMessage());
            //throw e;
        }
    }


    public void downloadFile(Uploads upload) throws IOException{
        try {
            //Uploads net.yazsoft.frame.upload=uploadsDao.getExamBooklet(exam,detail);
            //if (net.yazsoft.frame.upload!=null) {

            //}
            if (upload==null) {
                logger.info("LOG01490: UPLOAD IS NULL");
                return;
            }
            fileType=upload.getRefUploadType().getTid();

            String uploadsFolder = Util.getUploadsFolder();
            String extension = UploadsDao.getFileExtension(upload.getName());
            String dirName;

            dirName = uploadsFolder + "/files/";

            File file = new File(dirName + "/" + upload.getTid().toString() + "." + extension);

            HttpServletResponse httpServletResponse = (HttpServletResponse)
                    FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + upload.getName());

            FacesContext.getCurrentInstance().responseComplete();

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            BufferedInputStream input = null;
            //BufferedOutputStream output = null;
            input = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);

            // Write file net.yazsoft.frame.contents to response.
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                servletOutputStream.write(buffer, 0, length);
            }

            servletOutputStream.flush();
            servletOutputStream.close();
            input.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            logger.error("EXCEPTION: ", e);
            Util.setFacesMessageError(e.getMessage());
            throw e;
        }
    }


    @Transactional
    public void deleteUpload(Uploads upload) {
        try {

            if (upload.getRefUploadType()==null) {
                fileType=FILE;
            } else {
                fileType=upload.getRefUploadType().getTid();
            }


            String uploadsFolder = Util.getUploadsFolder();
            String extension=UploadsDao.getFileExtension(upload.getName());
            String dirName;
            dirName = uploadsFolder + "/files/";



            File file = new File(dirName + "/" + upload.getTid().toString()+"."+extension);
            if (file.delete()) {
                logger.info(file.getName() + " is deleted!");
            } else {
                logger.info("DELETING : " + dirName + "/" + upload.getTid().toString()+"."+extension);
                logger.error("Delete operation is failed.");
                Util.setFacesMessageError("Kayit silindi, dosya silineMEdi.");
            }
            Util.setFacesMessage("Deleted file : " + upload.getName());
            uploadsDao.delete(upload);
            uploadsDao.setUploads(null);
            uploadsDao.setFileUploads(null);
            //deleting
        } catch (Exception e) {
            logger.error("EXCEPTION: ", e);
            Util.setFacesMessageError(e.getMessage());
            throw e;
        }
        //ImagesDao imageDao=new ImagesDao();
    }

    /**
     * Creates and return the directory
     * @param subdir if the directory will include a subdir like albumid directory under images folder
     *               if no subdirectory send null
     * @return UploadDirectory name
     */
    public File getUploadDirectory(String subdir) {
        String dirName="";
        File targetFolder;

        if (fileType.equals(IMAGE)) {
            Util.createDirectory("images");
            Util.createDirectory("images/" +imageType);
            dirName= "/images/" +imageType;
            /*
            switch (imageType) {
                case (IMAGE_SCHOOL) :
                    Util.createDirectory("net.yazsoft.frame.images/school");
                    dirName="/net.yazsoft.frame.images/school"; break;
                case (IMAGE_USER) :
                    Util.createDirectory("net.yazsoft.frame.images/users/");
                    dirName="/net.yazsoft.frame.images/user"; break;
                case (IMAGE_STUDENT) :
                    Util.createDirectory("net.yazsoft.frame.images/students/");
                    dirName="/net.yazsoft.frame.images/student"; break;
            }
            */
        } else if (fileType.equals(FILE)) {
            Util.createDirectory("files");
            dirName="/files/";
        } else {
            logger.error("LOG02410: UNSUPPORTED UPLOAD TYPE !!!");
        }
        targetFolder=Util.createDirectory(dirName);

        // if the  directory does not exist, create it
        if (subdir!=null) {
            dirName = dirName + ("/" +subdir);
            targetFolder=Util.createDirectory(dirName);
        }
        uploadDirectory=targetFolder;
        return targetFolder;
    }



    @Transactional
    public void handleFileUpload(FileUploadEvent event) {
        logger.info("UPLOADING FILE.......");
        activeSchool=Util.getActiveSchool();

        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            if (fileType==FILE){
                getUploadDirectory(null);
            } else {
                //getUploadDirectory(Util.getActiveExam().getTid().toString());
            }
            String filenameOriginal=event.getFile().getFileName();
            String extension=UploadsDao.getFileExtension(filenameOriginal);

            Long tid=1L;
            Uploads upload=new Uploads();
            upload.setActive(Boolean.TRUE);
            upload.setCreated(Util.getNow());
            upload.setExtension(extension);
            if (fileType!=FILE) {
                upload.setRefSchool(Util.getActiveSchool());
            }
            upload.setRefUser(Util.getActiveUser());
            upload.setName(filenameOriginal);
            upload.setRefUploadType(uploadsTypeDao.getById(fileType));
            upload.setDetail(detail);
            tid=uploadsDao.create(upload);
            detail=null; //for next net.yazsoft.frame.upload

            InputStream inputStream;
            inputStream = event.getFile().getInputstream();
            if (fileType.equals(IMAGE)) {
                BufferedImage bufferedImage= ImageIO.read(event.getFile().getInputstream());
                Integer imageWidth=bufferedImage.getWidth();
                if (imageWidth>1600) {
                    inputStream = Util.imageResize(event.getFile().getInputstream(), extension, 1600, 800);
                }
            }
            String filename=tid.toString()+"."+extension;
            OutputStream out = new FileOutputStream(new File(uploadDirectory,filename));
            int read = 0;
            byte[] bytes = new byte[BUFFER_SIZE];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();

            uploadsDao.setUploads(null); //for refresh grid
            uploadsDao.setFileUploads(null);
            Util.setFacesMessage("ID : " + tid.toString() + " ,file name: "
                    + event.getFile().getFileName() + " File size: "
                    + event.getFile().getSize() / 1024 + " Kb Content type: "
                    + event.getFile().getContentType() + " the file was uploaded.");
        } catch(Exception e){
            Util.setFacesMessageError(" the files were not uploaded : " + e.getMessage());
            e.printStackTrace();
        }
    }



    @PreDestroy 
    public void Destroy() {
        //if uploadedImages are not saved into an album, then they should be deleted before destroying view
        logger.info("DESTROY VIEW. DELETING unsaved UPLOADED IMAGES");
    }
    
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public Albums getAlbum() {
        return album;
    }

    public void setAlbum(Albums album) {
        this.album = album;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }
}


//CREATING THUMBNAIL
            /*
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
            */
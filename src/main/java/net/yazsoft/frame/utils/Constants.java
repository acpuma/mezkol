package net.yazsoft.frame.utils;


import net.yazsoft.entities.ArticlesType;
import net.yazsoft.frame.controller.BaseDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


@Component
public class Constants extends BaseDao{
    private static final Logger log = Logger.getLogger(Constants.class);
    public static final String ROLE_ADMIN ="ADMIN";
    public static final Long ROLE_ADMIN_NO =1L;
    public static final String ROLE_COMPANY ="COMPANY";
    public static final String ROLE_SCHOOL ="SCHOOL";
    public static final String ROLE_MANAGER ="MANAGER";
    public static final String ROLE_TEACHER ="TEACHER";
    public static final String ROLE_FAMILY ="FAMILY";
    public static final String ROLE_STUDENT ="STUDENT";
    public static final Long CONTENT_MENU = 1L;
    public static final Long CONTENT_ARTICLE = 2L;
    public static final String EMAIL_NOTIFICATION ="admin@yazsoft.net";
    public static final Integer PAGE_ARTICLES = 5;
    public static final Long CANCELTYPE_NO = 1L; //not canceled
    public static final Long CANCELTYPE_YES = 2L; //canceled
    public static final Long CANCELTYPE_TRUE = 3L; //true for everybody
    public static final Long CANCELTYPE_FALSE = 4L; //false for everybody
    public static final int  REPORT_LESSON_CLASS= 1;
    public static final int  REPORT_LESSON_SCORE= 2;
    public static Long ARTICLETYPE_NEWS = 1L;
    public static Long ARTICLETYPE_EVENTS = 2L;
    public static Long ARTICLETYPE_ANNOUNCEMENT = 3L;

}

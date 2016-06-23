package net.yazsoft.frame.controller;

import net.yazsoft.entities.Articles;
import net.yazsoft.entities.ArticlesType;
import net.yazsoft.entities.Contents;
import net.yazsoft.entities.ContentsType;
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
public class ArticlesDao extends BaseGridDao<Articles> implements Serializable {
    private static final Logger logger = Logger.getLogger(ArticlesDao.class);
    Articles selected;
    List<Articles> items;
    List<Articles> webitems;
    Boolean itemsChanged = true;
    Contents content;
    Integer page;
    List<Integer> pages;
    Integer month,year;
    Long articleId;
    List<Archives> archives;

    @Inject ContentsDao contentsDao;
    @Inject ArticlesTypeDao articlesTypeDao;

    public void initSelected() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            selected = getById(articleId);
        }
    }

    @PostConstruct
    public void init() {
        page = 1;
        pages = new ArrayList<>();
        content = new Contents();
        getItem().setRefContent(content);
        webitems=new ArrayList<>();
    }

    public List<Articles> findArchive(Integer month, Integer year) {
        List list=null;
        try {
            if ((month!=null) && (year!=null)){
                String hql = "SELECT A FROM Articles A " + " where month(date)=:month and year(date)=:year"
                        + " and publish=true and refArticleType=" + Constants.ARTICLETYPE_EVENTS
                        + " order by date desc";
                Query query = getSession().createQuery(hql);
                query.setInteger("month", month);
                query.setInteger("year", year);
                list = query.list();
                webitems = list;
            }
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }

    public void findArchives() {
        archives = new ArrayList<>();
        try {
            String hql = "SELECT month(date), year(date) " +  //,count(A.date),monthname(date)
                     "FROM Articles A " +
                    "WHERE publish=true and refArticleType=" + Constants.ARTICLETYPE_EVENTS +
                    " GROUP BY month(date),year(date) " +
                    " order by year(date) desc,month(date) desc";
            Query query = getSession().createQuery(hql);
            for (Iterator it = query.iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                Archives month = new Archives();
                month.month = (Integer) row[0];
                month.year = (Integer) row[1];
                //month.count = (Long) row[2];
                month.monthname= new DateFormatSymbols(new Locale("TR")).getMonths()[month.month-1];
                //month.monthname = (String) row[3];
                logger.info("LOG02400: MONTHNAME :" + month.monthname);
                archives.add(month);
                logger.info("LOG02380: MONTH : " + month);
            }
        } catch (Exception e) {
            Util.catchException(e);
        }

    }


    public List<Articles> findNews() {
        ArticlesType atype=articlesTypeDao.getById(Constants.ARTICLETYPE_NEWS);
        return findArticlesByType(atype);
    }

    public List<Articles> findNews5() {
        ArticlesType atype=articlesTypeDao.getById(Constants.ARTICLETYPE_NEWS);
        List<Articles> list=findArticlesByType(atype);
        if (list.size()>5) {
            return list.subList(0,4);
        }
        return list;
    }

    public List<Articles> findEvents() {
        ArticlesType atype=articlesTypeDao.getById(Constants.ARTICLETYPE_EVENTS);
        return findArticlesByType(atype);
    }
    public List<Articles> findEvents5() {
        ArticlesType atype=articlesTypeDao.getById(Constants.ARTICLETYPE_EVENTS);
        List<Articles> list=findArticlesByType(atype);
        if (list.size()>5) {
            return list.subList(0,4);
        }
        return list;
    }

    public List<Articles> findArticlesByType(ArticlesType articleType) {
        List list = null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refArticleType",articleType));
            //c.add(Restrictions.eq("isDeleted", false));
            c.add(Restrictions.eq("publish",true));
            c.addOrder(Order.desc("date"));
            list = c.list();
            itemsChanged = false;
            logger.info("ARTICLES : " + list );
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }

    public List<Articles> findWebItems() {
        Integer rowcount;
        if (month!=null) {
            rowcount=webitems.size();
        } else {
            rowcount=rowCount();
        }
        int pageCount = rowcount / Constants.PAGE_ARTICLES;
        pages.clear();
        for (int i = 0; i < pageCount + 1; i++) {
            pages.add(i + 1);
        }
        int start = (page - 1) * Constants.PAGE_ARTICLES;
        logger.info("LOG02350: START : " + start);
        logger.info("LOG02390: year, month : " + month + "," + year);
        Map<String,Object> filters= new HashMap<>();
        filters.put("publish",true);
        return super.load(start, Constants.PAGE_ARTICLES,
                "date", SortOrder.DESCENDING, filters);
    }

    public void checkboxChange(Articles article) {
        try {
            update(article);
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
                content.setRefContentType((ContentsType) getSession().load(
                        ContentsType.class, Constants.CONTENT_ARTICLE));
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
        contentsDao.delete(getItem().getRefContent());
        super.delete();
        itemsChanged = true;
    }

    public List<Articles> findItems() {
        List list = null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            //c.add(Restrictions.eq("publish",true));
            c.addOrder(Order.desc("date"));
            list = c.list();
            itemsChanged = false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public ArticlesDao() {
        super(Articles.class);
    }

    public Articles getSelected() {
        return selected;
    }

    public void setSelected(Articles selected) {
        this.selected = selected;
    }

    public List<Articles> getItems() {
        if (itemsChanged) {
            items = findItems();
        }
        return items;
    }

    public void setItems(List<Articles> items) {
        this.items = items;
    }

    public List<Articles> getWebitems() {
        webitems = findWebItems();
        return webitems;
    }

    public void setWebitems(List<Articles> webitems) {
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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
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

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
    public List<Archives> getArchives() {
        if (archives==null) {
            findArchives();
        }
        return archives;
    }

    public void setArchives(List<Archives> archives) {
        this.archives = archives;
    }
}
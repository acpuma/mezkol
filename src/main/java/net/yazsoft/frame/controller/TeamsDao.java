package net.yazsoft.frame.controller;

import net.yazsoft.entities.*;
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
public class TeamsDao extends BaseGridDao<Teams> implements Serializable {
    private static final Logger logger = Logger.getLogger(TeamsDao.class);
    Teams selected;
    List<Teams> items;
    List<Teams> webitems=null;
    Boolean itemsChanged = true;
    Contents content;
    Long teamId;
    List<TeamsType> teamsTypes;
    TeamsType selectedType;

    @Inject ContentsDao contentsDao;
    @Inject TeamsTypeDao teamsTypeDao;
    @Inject SchoolsDao schoolsDao;

    public void initSelected() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            selected = getById(teamId);
        }
    }

    @PostConstruct
    public void init() {
        content = new Contents();
        getItem().setRefContent(content);
    }

    public List<Teams> findTeamsByType() {
        List<Teams> tempList=null;
        try {
            if (selectedType==null) {
                webitems=items;
                return webitems;
            }
            tempList=new ArrayList<>();
            for (Teams team:items) {
                if (team.getRefTeamType().getTid().equals(selectedType.getTid())) {
                    if (!tempList.contains(team)) {
                        tempList.add(team);
                    }
                }
            }
            webitems=tempList;
            itemsChanged=true;
        } catch (Exception e) {
            Util.catchException(e);
        }
        return tempList;
    }

    public void checkboxChange(Teams team) {
        try {
            update(team);
            //Util.setFacesMessage("CHANGED");
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void onRowReorder(ReorderEvent event) {
        //Util.setFacesMessage("Row Moved From: " + event.getFromIndex() + ", To:" + event.getToIndex());
        try {
            items.get(event.getFromIndex()).setRank(event.getToIndex());
            logger.info("LOG02290: MOVED : " + items.get(event.getFromIndex()).getName());
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
                getItem().setRefSchool(Util.getActiveSchool());
                getItem().setActive(true);
                getItem().setRank(items.size() + 1);
                content.setRefContentType((ContentsType) getSession().load(
                        ContentsType.class, Constants.CONTENT_TEAM));
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

    public List<Teams> findItems(Schools school) {
        List list = null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refSchool", school));
            //c.add(Restrictions.eq("isDeleted", false));
            //c.add(Restrictions.eq("publish",true));
            //c.addOrder(Order.desc("date"));
            list = c.list();
            itemsChanged = false;
            items=list;
            logger.info("FIND ITEMS : " + list);
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }

    public void findTeamTypes() {
        try {
            if ((items!=null) && (items.size()>0)) {
                logger.info("FINDING TEAM TYPES...");
                if (teamsTypes == null) {
                    teamsTypes = new ArrayList<>();
                } else {
                    teamsTypes.clear();
                }
                logger.info("ITEMS : " + items);
                for (Teams team : items) {
                    boolean contained=false;
                    for (TeamsType teamsType:teamsTypes) {
                        if (teamsType.getTid().equals(team.getRefTeamType().getTid())){
                            contained=true;
                        }
                    }
                    if (!contained) {
                        teamsTypes.add(team.getRefTeamType());
                    }
                }
                logger.info("TEAM TYPES: " + teamsTypes);
            }
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public TeamsDao() {
        super(Teams.class);
    }

    public Teams getSelected() {
        return selected;
    }

    public void setSelected(Teams selected) {
        this.selected = selected;
    }

    public List<Teams> getItems() {
        if (itemsChanged) {
            items=findItems(Util.getActiveSchool());
        }
        return items;
    }

    public void setItems(List<Teams> items) {
        this.items = items;
    }


    public void setWebitems(List<Teams> webitems) {
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

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public List<TeamsType> getTeamsTypes() {
        if (teamsTypes==null) {
            findItems(schoolsDao.selected);
        }
        return teamsTypes;
    }

    public void setTeamsTypes(List<TeamsType> teamsTypes) {
        this.teamsTypes = teamsTypes;
    }

    public TeamsType getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(TeamsType selectedType) {
        this.selectedType = selectedType;
    }

    public List<Teams> getWebitems() {
        //if ((webitems==null) || (webitems.size()==0)) {
            logger.info("SELECTED SCHOOL : " + schoolsDao.getSelected());
            findItems(schoolsDao.getSelected());
            findTeamTypes();
            findTeamsByType();
        //}
        return webitems;
    }
}
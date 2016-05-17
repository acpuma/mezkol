package net.yazsoft.frame.controller;

import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.entities.Users;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
@Transactional
public class UsersSer extends BaseSer<Users> {
    private Logger logger= Logger.getLogger(UsersSer.class);

    @Inject
    UsersDao usersDao;

    @Inject
    private UsersLazyModel lazyModel;

    @PostConstruct
    public void init() {
        //lazyModel=usersDao.getLazyModel();
    }

    public Users findByUsername(final String username) {
        return usersDao.findByUserName(username);
    }

    public LazyDataModel<Users> getLazyModel() {
        return lazyModel;
    }
}

package net.yazsoft.frame.controller;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.yazsoft.entities.Schools;
import net.yazsoft.entities.Users;
import net.yazsoft.frame.controller.scopes.SessionScoped;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
@Data
public class SessionInfo implements Serializable{

    Users user;
    Schools school;
    String theme;

    @Inject UsersDao usersDao;

    @PostConstruct
    public void init() {
        theme="none";
    }
    public void setSchool(Schools school) {
        this.school = school;
        if (user!=null) {
            user.setRefActiveSchool(school);
            usersDao.update(user);
        }
    }

}

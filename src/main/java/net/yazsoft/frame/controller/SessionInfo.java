package net.yazsoft.frame.controller;

import net.yazsoft.entities.Schools;
import net.yazsoft.entities.Users;
import net.yazsoft.frame.controller.scopes.SessionScoped;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class SessionInfo implements Serializable{

    Users user;
    Schools school;

    @Inject UsersDao usersDao;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Schools getSchool() {
        return school;
    }

    public void setSchool(Schools school) {
        this.school = school;
        if (user!=null) {
            user.setRefActiveSchool(school);
            usersDao.update(user);
        }
    }
}

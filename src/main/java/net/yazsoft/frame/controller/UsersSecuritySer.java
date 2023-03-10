package net.yazsoft.frame.controller;

import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.entities.Users;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

@Named
@ViewScoped
@Transactional
public class UsersSecuritySer implements UserDetailsService {
    Logger logger= Logger.getLogger(UsersSecuritySer.class);

    //get user from the database, via Hibernate
    @Inject
    private UsersDao userDao;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        Users user = userDao.findByUserName(username);
        logger.info("USER : " + user);
        if (user==null) throw new UsernameNotFoundException("Username not found");
        if (user.getActive()==false) throw new UsernameNotFoundException("Username is passive");
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getActive(),
                user.getAccountNonExpired(),
                user.getCredentialsNonExpired(),
                user.getAccountNonLocked(),
                getAuthorities(user)
        );
    }

    public Collection<? extends GrantedAuthority> getAuthorities(Users user) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        //Set<Roles> userRoles = user.getRoles();
        String roleName=user.getRefRole().getName();
        logger.info("ROLE : " + roleName);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
        authorities.add(authority);
        return authorities;
    }

    public UsersDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UsersDao userDao) {
        this.userDao = userDao;
    }
}

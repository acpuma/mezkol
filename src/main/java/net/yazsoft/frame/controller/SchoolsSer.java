package net.yazsoft.frame.controller;

import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.entities.Schools;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
@Transactional
public class SchoolsSer extends BaseSer<Schools> {
    private Logger logger = Logger.getLogger(SchoolsSer.class);

    @Inject
    SchoolsDao schoolsDao;

    @Inject
    SchoolsLazy lazyModel;

    @Transactional
    public LazyDataModel<Schools> getLazyModel() {
        return lazyModel;
    }
}

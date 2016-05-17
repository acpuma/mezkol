package net.yazsoft.frame.controller;

import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.entities.ImagesType;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ImagesTypeDao extends BaseDao<ImagesType> implements Serializable{
    public ImagesTypeDao() {
        super(ImagesType.class);
    }
}

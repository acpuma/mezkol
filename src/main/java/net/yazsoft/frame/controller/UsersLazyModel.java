package net.yazsoft.frame.controller;

import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.entities.Users;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;


@Named
@ViewScoped
public class UsersLazyModel extends LazyDataModel<Users> {

    @Inject
    UsersDao usersDao;

    @Override
    public Users getRowData(String rowKey) {
        return usersDao.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(Users user) {
        return user.getTid();
    }

    @Override
    public List<Users> load(int first, int pageSize, String sortField, SortOrder sortOrder,
                            Map<String,Object> filters) {
        this.setRowCount(usersDao.rowCount());// TODO make safe cast
        return usersDao.load(first, pageSize, sortField, sortOrder, filters);
    }
}

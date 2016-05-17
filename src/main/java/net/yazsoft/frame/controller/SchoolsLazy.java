package net.yazsoft.frame.controller;

import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.entities.Schools;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class SchoolsLazy extends LazyDataModel<Schools> {

    @Inject
    SchoolsDao schoolsDao;

    @Override
    public Schools getRowData(String rowKey) {
        return schoolsDao.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(Schools school) {
        return school.getTid();
    }

    @Override
    public List<Schools> load(int first, int pageSize, String sortField, SortOrder sortOrder,
                            Map<String,Object> filters) {
        this.setRowCount(schoolsDao.rowCount());// TODO make safe cast
        return schoolsDao.load(first, pageSize, sortField, sortOrder, filters);
    }
}

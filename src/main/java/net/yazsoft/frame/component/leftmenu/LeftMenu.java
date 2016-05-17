/*
 * Generated, Do Not Modify
 */
/*
 * Copyright 2009-2013 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.yazsoft.frame.component.leftmenu;

import org.primefaces.component.menu.AbstractMenu;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
        import org.primefaces.component.menu.AbstractMenu;
        import javax.faces.context.FacesContext;
        import javax.faces.component.UINamingContainer;
        import javax.el.ValueExpression;
        import javax.el.MethodExpression;
        import javax.faces.render.Renderer;
        import java.io.IOException;
        import javax.faces.component.UIComponent;
        import javax.faces.event.AbortProcessingException;
        import javax.faces.application.ResourceDependencies;
        import javax.faces.application.ResourceDependency;
        import java.util.List;
        import java.util.ArrayList;

@FacesComponent(value = LeftMenu.COMPONENT_TYPE)
@ResourceDependencies({
        @ResourceDependency(library="primefaces", name="primefaces.css"),
        @ResourceDependency(library="primefaces", name="jquery/jquery.js"),
        @ResourceDependency(library="primefaces", name="jquery/jquery-plugins.js"),
        @ResourceDependency(library="primefaces", name="primefaces.js")
})
public class LeftMenu extends AbstractMenu implements org.primefaces.component.api.Widget {


    public static final String COMPONENT_TYPE = "net.yazsoft.frame.component.LeftMenu";
    public static final String COMPONENT_FAMILY = "net.yazsoft.frame.component";
    public static final String DEFAULT_RENDERER = "net.yazsoft.frame.component.LeftMenuRenderer";

    protected enum PropertyKeys {

        widgetVar
        ,model
        ,style
        ,styleClass
        ,stateful;

        String toString;

        PropertyKeys(String toString) {
            this.toString = toString;
        }

        PropertyKeys() {}

        public String toString() {
            return ((this.toString != null) ? this.toString : super.toString());
        }
    }

    public LeftMenu() {
        setRendererType(DEFAULT_RENDERER);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(net.yazsoft.frame.component.leftmenu.LeftMenu.PropertyKeys.widgetVar, null);
    }
    public void setWidgetVar(java.lang.String _widgetVar) {
        getStateHelper().put(net.yazsoft.frame.component.leftmenu.LeftMenu.PropertyKeys.widgetVar, _widgetVar);
    }

    public org.primefaces.model.menu.MenuModel getModel() {
        return (org.primefaces.model.menu.MenuModel) getStateHelper().eval(net.yazsoft.frame.component.leftmenu.LeftMenu.PropertyKeys.model, null);
    }
    public void setModel(org.primefaces.model.menu.MenuModel _model) {
        getStateHelper().put(net.yazsoft.frame.component.leftmenu.LeftMenu.PropertyKeys.model, _model);
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(net.yazsoft.frame.component.leftmenu.LeftMenu.PropertyKeys.style, null);
    }
    public void setStyle(java.lang.String _style) {
        getStateHelper().put(net.yazsoft.frame.component.leftmenu.LeftMenu.PropertyKeys.style, _style);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(net.yazsoft.frame.component.leftmenu.LeftMenu.PropertyKeys.styleClass, null);
    }
    public void setStyleClass(java.lang.String _styleClass) {
        getStateHelper().put(net.yazsoft.frame.component.leftmenu.LeftMenu.PropertyKeys.styleClass, _styleClass);
    }

    public boolean isStateful() {
        return (java.lang.Boolean) getStateHelper().eval(net.yazsoft.frame.component.leftmenu.LeftMenu.PropertyKeys.stateful, true);
    }
    public void setStateful(boolean _stateful) {
        getStateHelper().put(net.yazsoft.frame.component.leftmenu.LeftMenu.PropertyKeys.stateful, _stateful);
    }


    public final static String CONTAINER_CLASS = "sidebar-category sidebar-category-visible";
    public final static String CONTAINER_CLASS_2 = "category-content no-padding";
    public final static String INACTIVE_HEADER_CLASS = "ui-widget ui-panelmenu-header ui-state-default ui-corner-all";
    public final static String ACTIVE_HEADER_CLASS = "ui-widget ui-panelmenu-header ui-state-default ui-state-active ui-corner-all";
    public final static String INACTIVE_TAB_HEADER_ICON_CLASS = "ui-icon ui-icon-triangle-1-e";
    public final static String ACTIVE_TAB_HEADER_ICON_CLASS = "ui-icon ui-icon-triangle-1-s";
    public final static String INACTIVE_ROOT_SUBMENU_CONTENT = "ui-panelmenu-content ui-widget-content ui-helper-hidden";
    public final static String ACTIVE_ROOT_SUBMENU_CONTENT = "ui-panelmenu-content ui-widget-content";
    public final static String MENUITEM_CLASS = "ui-menuitem ui-widget ui-corner-all";
    public final static String DESCENDANT_SUBMENU_CLASS = "ui-widget ui-menuitem ui-corner-all ui-menu-parent";
    public final static String DESCENDANT_SUBMENU_EXPANDED_ICON_CLASS = "ui-panelmenu-icon ui-icon ui-icon-triangle-1-s";
    public final static String DESCENDANT_SUBMENU_COLLAPSED_ICON_CLASS = "ui-panelmenu-icon ui-icon ui-icon-triangle-1-e";
    public static final String DESCENDANT_SUBMENU_EXPANDED_LIST_CLASS = "ui-menu-list ui-helper-reset";
    public static final String DESCENDANT_SUBMENU_COLLAPSED_LIST_CLASS = "ui-menu-list ui-helper-reset ui-helper-hidden";
    public final static String MAIN_UL = "navigation navigation-main navigation-accordion";
    public final static String MENUITEM_LINK_WITH_ICON_CLASS = "ui-menuitem-link ui-menuitem-link-hasicon ui-corner-all";



    /*
    public final static String CONTAINER_CLASS = "ui-panelmenu ui-widget";
    public final static String INACTIVE_HEADER_CLASS = "ui-widget ui-panelmenu-header ui-state-default ui-corner-all";
    public final static String ACTIVE_HEADER_CLASS = "ui-widget ui-panelmenu-header ui-state-default ui-state-active ui-corner-all";
    public final static String INACTIVE_TAB_HEADER_ICON_CLASS = "ui-icon ui-icon-triangle-1-e";
    public final static String ACTIVE_TAB_HEADER_ICON_CLASS = "ui-icon ui-icon-triangle-1-s";
    public final static String INACTIVE_ROOT_SUBMENU_CONTENT = "ui-panelmenu-content ui-widget-content ui-helper-hidden";
    public final static String ACTIVE_ROOT_SUBMENU_CONTENT = "ui-panelmenu-content ui-widget-content";
    public final static String MENUITEM_CLASS = "ui-menuitem ui-widget ui-corner-all";
    public final static String DESCENDANT_SUBMENU_CLASS = "ui-widget ui-menuitem ui-corner-all ui-menu-parent";
    public final static String DESCENDANT_SUBMENU_EXPANDED_ICON_CLASS = "ui-panelmenu-icon ui-icon ui-icon-triangle-1-s";
    public final static String DESCENDANT_SUBMENU_COLLAPSED_ICON_CLASS = "ui-panelmenu-icon ui-icon ui-icon-triangle-1-e";
    public static final String DESCENDANT_SUBMENU_EXPANDED_LIST_CLASS = "ui-menu-list ui-helper-reset";
    public static final String DESCENDANT_SUBMENU_COLLAPSED_LIST_CLASS = "ui-menu-list ui-helper-reset ui-helper-hidden";
    public final static String PANEL_CLASS = "ui-panelmenu-panel";
    public final static String MENUITEM_LINK_WITH_ICON_CLASS = "ui-menuitem-link ui-menuitem-link-hasicon ui-corner-all";
    */

    public String resolveWidgetVar() {
        FacesContext context = getFacesContext();
        String userWidgetVar = (String) getAttributes().get("widgetVar");

        if(userWidgetVar != null)
            return userWidgetVar;
        else
            return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }
}
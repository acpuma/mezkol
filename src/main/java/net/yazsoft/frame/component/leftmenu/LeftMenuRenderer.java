package net.yazsoft.frame.component.leftmenu;

/**
 *   Created by fec on 30/04/16.
 * Copyright 2009-2015 PrimeTek.
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
import java.io.IOException;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.primefaces.component.menu.AbstractMenu;
import org.primefaces.component.menu.BaseMenuRenderer;
import org.primefaces.component.menu.Menu;
import org.primefaces.model.menu.*;
import org.primefaces.util.WidgetBuilder;

@FacesRenderer(componentFamily = LeftMenu.COMPONENT_FAMILY, rendererType = LeftMenu.DEFAULT_RENDERER)
public class LeftMenuRenderer extends BaseMenuRenderer {

    protected void encodeScript(FacesContext context, AbstractMenu abstractMenu) throws IOException{
        LeftMenu menu = (LeftMenu) abstractMenu;
        String clientId = menu.getClientId(context);

        WidgetBuilder wb = getWidgetBuilder(context);
        wb.initWithDomReady("LeftMenu", menu.resolveWidgetVar(), clientId)
                .attr("stateful", menu.isStateful());

        wb.finish();
    }

    protected void encodeMarkup(FacesContext context, AbstractMenu abstractMenu) throws IOException {
        LeftMenu menu = (LeftMenu) abstractMenu;
        String style = menu.getStyle();
        String styleClass = menu.getStyleClass();
        String defaultStyleClass = LeftMenu.CONTAINER_CLASS ;
        styleClass = styleClass == null ? defaultStyleClass : defaultStyleClass + " " + styleClass;

        encodeMenu(context, menu, style, styleClass, "menu");
    }

    protected void encodeMenu(FacesContext context, AbstractMenu menu, String style, String styleClass, String role) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        UIComponent optionsFacet = menu.getFacet("options");


        writer.startElement("div", menu);
        writer.writeAttribute("id", menu.getClientId(context), "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if(style != null) {
            writer.writeAttribute("style", style, "style");
        }
        //writer.writeAttribute("role", "menubar", null);
        writer.startElement("div",menu);
        writer.writeAttribute("class", LeftMenu.CONTAINER_CLASS_2, null);
        encodeKeyboardTarget(context, menu);

        writer.startElement("ul", null);
        writer.writeAttribute("class", LeftMenu.MAIN_UL, null);

        if(menu.getElementsCount() > 0) {
            encodeElements(context, menu, menu.getElements());
        }

        if(optionsFacet != null) {
            writer.startElement("li", null);
            writer.writeAttribute("class", Menu.OPTIONS_CLASS, null);
            writer.writeAttribute("role", "menuitem", null);
            optionsFacet.encodeAll(context);
            writer.endElement("li");
        }

        writer.endElement("ul");

        writer.endElement("div");
        writer.endElement("div");
    }

    protected void encodeElements(FacesContext context, AbstractMenu menu, List<MenuElement> elements) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        for(MenuElement element : elements) {
            if(element.isRendered()) {
                if(element instanceof MenuItem) {
                    MenuItem menuItem = (MenuItem) element;
                    String containerStyle = menuItem.getContainerStyle();
                    String containerStyleClass = menuItem.getContainerStyleClass();
                    containerStyleClass = (containerStyleClass == null) ? Menu.MENUITEM_CLASS: Menu.MENUITEM_CLASS + " " + containerStyleClass;

                    writer.startElement("li", null);
                    //writer.writeAttribute("class", containerStyleClass, null);
                    //writer.writeAttribute("role", "menuitem", null);
                    if(containerStyle != null) {
                        writer.writeAttribute("style", containerStyle, null);
                    }

                    encodeMenuItem(context, menu, menuItem);
                    writer.endElement("li");
                }
                else if(element instanceof Submenu) {
                    Submenu submenu = (Submenu) element;
                    String style = submenu.getStyle();
                    String styleClass = submenu.getStyleClass();
                    //styleClass = styleClass == null ? Menu.Left_SUBMENU_CLASS : Menu.Left_SUBMENU_CLASS + " " + styleClass;

                    writer.startElement("li", null);
                    if(shouldRenderId(submenu)) {
                        writer.writeAttribute("id", submenu.getClientId(), null);
                    }
                    //writer.writeAttribute("class", styleClass, null);
                    if(style != null) {
                        writer.writeAttribute("style", style, null);
                    }
                    //writer.writeAttribute("role", "menuitem", null);
                    //writer.writeAttribute("aria-haspopup", "true", null);
                    encodeSubmenu(context, menu, submenu);
                    writer.endElement("li");
                }
                else if(element instanceof Separator) {
                    encodeSeparator(context, (Separator) element);
                }
            }
        }
    }

    protected void encodeSubmenu(FacesContext context, AbstractMenu menu, Submenu submenu) throws IOException{
        ResponseWriter writer = context.getResponseWriter();
        String icon = submenu.getIcon();
        String label = submenu.getLabel();

        //title
        writer.startElement("a", null);
        writer.writeAttribute("href", "#", null);
        //writer.writeAttribute("class", Menu.SUBMENU_LINK_CLASS, null);
        writer.writeAttribute("tabindex", "-1", null);

        if(icon != null) {
            writer.startElement("i", null);
            writer.writeAttribute("class", icon, null);
            //writer.writeAttribute("class", "icon-home4", null);
            writer.endElement("i");
        }

        if(label != null) {
            writer.startElement("span", null);
            //writer.writeAttribute("class", Menu.MENUITEM_TEXT_CLASS, null);
            writer.writeText(submenu.getLabel(), "value");
            writer.endElement("span");
        }

        //encodeSubmenuIcon(context, submenu);

        writer.endElement("a");

        //submenus and menuitems
        if(submenu.getElementsCount() > 0) {
            writer.startElement("ul", null);
            //writer.writeAttribute("class", Menu.Left_CHILD_SUBMENU_CLASS, null);
            writer.writeAttribute("role", "menu", null);
            encodeElements(context, menu, submenu.getElements());
            writer.endElement("ul");
        }
    }

    protected void encodeSubmenuIcon(FacesContext context, Submenu submenu) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("i", null);
        writer.writeAttribute("class", "icon-home4", null);
        writer.endElement("i");
    }
    /*

    @Override
    protected void encodeScript(FacesContext context, AbstractMenu abstractMenu) throws IOException {
        LeftMenu menu = (LeftMenu) abstractMenu;
        String clientId = menu.getClientId(context);
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.initWithDomReady("LeftMenu", menu.resolveWidgetVar(), clientId)
                .attr("stateful", menu.isStateful());
        wb.finish();
    }

    @Override
    protected void encodeMarkup(FacesContext context, AbstractMenu abstractMenu) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        LeftMenu menu = (LeftMenu) abstractMenu;
        String clientId = menu.getClientId(context);
        String style = menu.getStyle();
        String styleClass = menu.getStyleClass();
        styleClass = styleClass == null ?  LeftMenu.CONTAINER_CLASS : LeftMenu.CONTAINER_CLASS + " " + styleClass;


        writer.startElement("div", menu);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if(style != null) {
            writer.writeAttribute("style", style, "style");
        }
        //writer.writeAttribute("role", "menu", null);

        writer.startElement("div",menu);
        writer.writeAttribute("class", LeftMenu.CONTAINER_CLASS_2, "class");

        writer.startElement("ul",menu);
        writer.writeAttribute("class", LeftMenu.PANEL_CLASS_UL, "class");



        if(menu.getElementsCount() > 0) {
            List<MenuElement> elements = menu.getElements();
            System.out.println("ELEMENTS SIZE : " + elements.size());

            for(MenuElement element : elements) {
                if(element.isRendered() && element instanceof Submenu) {
                    encodeRootSubmenu(context, menu, (Submenu) element);
                }
                if(element.isRendered() && element instanceof DefaultMenuItem) {
                    System.out.println("default menu item: " + ((DefaultMenuItem)element).getTitle());
                }
            }
        }
        writer.endElement("ul");
        writer.endElement("div");

        writer.endElement("div");
    }

    protected void encodeRootSubmenu(FacesContext context, LeftMenu menu, Submenu submenu) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String style = submenu.getStyle();
        String styleClass = submenu.getStyleClass();
        //styleClass = styleClass == null ? LeftMenu.PANEL_CLASS : LeftMenu.PANEL_CLASS + " " + styleClass;
        boolean expanded = submenu.isExpanded();
        //String headerClass = expanded ? LeftMenu.ACTIVE_HEADER_CLASS : LeftMenu.INACTIVE_HEADER_CLASS;
        //String headerIconClass = expanded ? LeftMenu.ACTIVE_TAB_HEADER_ICON_CLASS : LeftMenu.INACTIVE_TAB_HEADER_ICON_CLASS;
        //String contentClass = expanded ? LeftMenu.ACTIVE_ROOT_SUBMENU_CONTENT : LeftMenu.INACTIVE_ROOT_SUBMENU_CONTENT;
        // <li><a href="../index.html"><i class="icon-home4"></i> <span>Dashboard</span></a></li>
        //wrapper
        writer.startElement("li", null);
        //writer.writeAttribute("class", styleClass, null);
        if(style != null) {
            writer.writeAttribute("style", style, null);
        }

        //header
        //writer.startElement("h3", null);
        //writer.writeAttribute("class", headerClass, null);
        //writer.writeAttribute("role", "tab", null);
        //writer.writeAttribute("tabindex", "0", null);

        //icon
        //writer.startElement("span", null);
        //writer.writeAttribute("class", headerIconClass, null);
        //writer.endElement("span");

        writer.startElement("a", null);
        writer.writeAttribute("href", "#", null);
        writer.writeAttribute("tabindex", "-1", null);
        writer.startElement("i", null);
        writer.writeAttribute("class", "icon-home4", null);
        writer.endElement("i");
        writer.startElement("span", null);
        writer.writeText(submenu.getLabel(), null);
        writer.endElement("span");
        writer.endElement("a");

        System.out.println("LABEL : " + submenu.getLabel());

        //writer.endElement("h3");

        //content

        //writer.startElement("div", null);
        //writer.writeAttribute("class", contentClass, null);
        //writer.writeAttribute("role", "tabpanel", null);
        //writer.writeAttribute("id", menu.getClientId(context) + "_" + submenu.getId(), null);
        //writer.writeAttribute("tabindex", "0", null);


        if(submenu.getElementsCount() > 0) {
            List<MenuElement> elements = submenu.getElements();

            writer.startElement("ul", null);
            writer.writeAttribute("class", LeftMenu.LIST_CLASS, null);

            for(MenuElement element : elements) {
                if(element.isRendered()) {
                    if(element instanceof MenuItem) {
                        System.out.println("MENU ITEM ");
                        MenuItem menuItem = (MenuItem) element;
                        String containerStyle = menuItem.getContainerStyle();
                        String containerStyleClass = menuItem.getContainerStyleClass();
                        containerStyleClass = (containerStyleClass == null) ? Menu.MENUITEM_CLASS: Menu.MENUITEM_CLASS + " " + containerStyleClass;

                        writer.startElement("ul", null);
                        //writer.writeAttribute("class", containerStyleClass, null);
                        if(containerStyle != null) {
                            writer.writeAttribute("style", containerStyle, null);
                        }
                        //encodeMenuItem(context, menu, menuItem);
                        writer.endElement("ul");
                    }
                    else if(element instanceof Submenu) {
                        System.out.println("SUBMENU ITEM");
                        encodeDescendantSubmenu(context, menu, (Submenu) element);
                    }
                }
            }

            writer.endElement("ul");
        }

        //writer.endElement("div");   //content

        writer.endElement("div");   //wrapper
    }

    protected void encodeDescendantSubmenu(FacesContext context, LeftMenu menu, Submenu submenu) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String icon = submenu.getIcon();
        String style = submenu.getStyle();
        String styleClass = submenu.getStyleClass();
        //styleClass = styleClass == null ? LeftMenu.DESCENDANT_SUBMENU_CLASS : LeftMenu.DESCENDANT_SUBMENU_CLASS + " " + styleClass;
        boolean expanded = submenu.isExpanded();
        String toggleIconClass = expanded ? LeftMenu.DESCENDANT_SUBMENU_EXPANDED_ICON_CLASS : LeftMenu.DESCENDANT_SUBMENU_COLLAPSED_ICON_CLASS;
        String listClass = expanded ? LeftMenu.DESCENDANT_SUBMENU_EXPANDED_LIST_CLASS :LeftMenu.DESCENDANT_SUBMENU_COLLAPSED_LIST_CLASS;
        boolean hasIcon = (icon != null);
        String linkClass = (hasIcon) ? LeftMenu.MENUITEM_LINK_WITH_ICON_CLASS : LeftMenu.MENUITEM_LINK_CLASS;

        writer.startElement("li", null);
        writer.writeAttribute("id", submenu.getClientId(), null);
        writer.writeAttribute("class", styleClass, null);
        if(style != null) {
            writer.writeAttribute("style", style, null);
        }

        writer.startElement("a", null);
        writer.writeAttribute("class", linkClass, null);

        //toggle icon
        writer.startElement("span", null);
        writer.writeAttribute("class", toggleIconClass, null);
        writer.endElement("span");

        //user icon
        if(hasIcon) {
            writer.startElement("span", null);
            writer.writeAttribute("class", "ui-icon " + icon, null);
            writer.endElement("span");
        }

        //submenu label
        writer.startElement("span", null);
        writer.writeAttribute("class", LeftMenu.MENUITEM_TEXT_CLASS, null);
        writer.writeText(submenu.getLabel(), null);
        writer.endElement("span");

        writer.endElement("a");

        //submenu children
        if(submenu.getElementsCount() > 0) {
            List<MenuElement> elements = submenu.getElements();

            writer.startElement("ul", null);
            writer.writeAttribute("class", listClass, null);

            for(MenuElement element : elements) {
                if(element.isRendered()) {
                    if(element instanceof MenuItem) {
                        writer.startElement("li", null);
                        writer.writeAttribute("class", Menu.MENUITEM_CLASS, null);
                        encodeMenuItem(context, menu, (MenuItem) element);
                        writer.endElement("li");
                    }
                    else if(element instanceof Submenu) {
                        encodeDescendantSubmenu(context, menu, (Submenu) element);
                    }
                }
            }

            writer.endElement("ul");
        }

        writer.endElement("li");
    }
    */
}
package net.yazsoft.frame.controller.scopes;

import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(WebApplicationContext.SCOPE_REQUEST)
public @interface RequestScoped {

}

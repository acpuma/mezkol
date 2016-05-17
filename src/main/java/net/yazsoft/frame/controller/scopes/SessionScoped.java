package net.yazsoft.frame.controller.scopes;

import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(WebApplicationContext.SCOPE_SESSION)
public @interface SessionScoped {

}

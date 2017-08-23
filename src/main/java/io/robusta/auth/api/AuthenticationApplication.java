package io.robusta.auth.api;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("auth")
public class AuthenticationApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(UserResource.class);
        return s;
    }
}

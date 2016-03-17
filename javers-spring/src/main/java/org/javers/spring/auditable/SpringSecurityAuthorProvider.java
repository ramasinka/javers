package org.javers.spring.auditable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author bartosz walacik
 */
public class SpringSecurityAuthorProvider implements AuthorProvider {
    @Override
    public String provide() {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return "unauthenticated";
        }

        return auth.getName();
    }
}

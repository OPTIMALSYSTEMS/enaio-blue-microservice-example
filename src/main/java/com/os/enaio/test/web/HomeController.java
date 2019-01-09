package com.os.enaio.test.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by cschulze on 18.11.2016.
 */
@Controller
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequestMapping("/")
public class HomeController {

    // ********************************************************************************************
    /**
     * see http://docs.spring.io/autorepo/docs/spring-security/4.2.x/reference/html/el-access.html
     * for 'expression based access control' by spring-security
     */
    // ********************************************************************************************

    /**
     * @return returns the index.html web page
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "index.html";
    }

    /**
     * redirects to the swagger API documentation page.
     */
    @RequestMapping(value = "/api", method = RequestMethod.GET)
    public String documentation() {
        return "redirect:swagger-ui.html";
    }

    /**
     * This method is only accessible by an admin user. To know if a user is an admin, enable the
     * 'ecm.session.load-user-data' property.
     *
     * @return returns the index.html web page, if the request was sent by an admin user
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminScope(){
        return "index.html";
    }

    /**
     * This method is accessible by everyone without any authentication. Note that no session depending beans, e.g.
     * certain user templates, will be injectable by spring.
     *
     * @return returns the index.html web page, even without any authentication.
     */
    @RequestMapping(value = "/public", method = RequestMethod.GET)
    public String publicScope(){
        return "index.html";
    }

}

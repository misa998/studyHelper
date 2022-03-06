package com.studyhelper.rest;

import com.studyhelper.entity.User;
import com.studyhelper.entity.UserDetails;
import com.studyhelper.service.UserDetailsService;
import com.studyhelper.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.log4j.Log4j2;

/**
 * This controller is used for all the {@link com.studyhelper.entity.UserDetails} related rest requests.
 *
 * @author Milos
 * @since 2.0
 */
@Log4j2
@Controller
@RequestMapping("/profile")
@ResponseBody
public class ProfileRestController {

    private final UserDetailsService userDetailsService;

    @Autowired
    public ProfileRestController(@Lazy final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    @ResponseBody
    public UserDetails getUserDetails(@PathVariable("username") final String username) {
        log.debug("Get method called for username " + username);
        return userDetailsService.getByUsername(username);
    }
}

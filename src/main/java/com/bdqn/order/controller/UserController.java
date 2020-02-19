package com.bdqn.order.controller;


import com.bdqn.order.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bdqn.order.pojo.Userinfo;

import javax.servlet.http.HttpSession;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("")
    public Map doLogin(Userinfo userinfo, HttpSession session){
        Map retMap=userService.doLogin(userinfo);
        if (retMap.get("retCode").equals("1000")){
            session.setAttribute("user",retMap.get("user"));
        }
        return retMap;
    }

















}

package cn.anytec.controller;

import cn.anytec.config.GeneralConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class MainController{

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    GeneralConfig generalConfig;

    @RequestMapping(value = "/test")
    public String test(Map<String,String> map){
        map.put("test",generalConfig.getTest());
        return "index";
    }
    @RequestMapping(value = "/admin/authorities")
    @ResponseBody
    public void login(Map<String,String> map){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        logger.info("username: "+userDetails.getUsername());
        Object[] s = userDetails.getAuthorities().toArray();
        StringBuilder info = new StringBuilder("用户角色：");
        for (Object s2 : s) {
            SimpleGrantedAuthority simpleGrantedAuthority = (SimpleGrantedAuthority)s2;
            info.append(simpleGrantedAuthority.getAuthority()+"\n");
        }
        logger.info(info.toString());
    }
    @RequestMapping(value = "/login")
    public String login(Map<String,String> map,HttpServletRequest request){
        String msg = "";
        if(request.getParameter("status")!=null){
            if(request.getParameter("status").equals("-1")){
                msg = "用户名或密码错误";
            }
        }
        map.put("msg",msg);
        return "login";

    }

    @RequestMapping(value = "/logout")
    public String loginout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            logger.info("用户"+userDetails.getUsername()+"进行注销");
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";

    }

}

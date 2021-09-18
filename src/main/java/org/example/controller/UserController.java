package org.example.controller;

import org.example.domain.Role;
import org.example.domain.User;
import org.example.service.RoleService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {
    private User user;

    @Autowired
    private UserService userService;

    @Resource
    private RoleService roleService;

    @RequestMapping("login")
    public String login(String username, String password, HttpSession session){
        if(session.getAttribute("user")!=this.user){
            return "redirect:/index.jsp";
        }else{
            this.user = userService.login(username,password);
            if(user!=null){
                session.setAttribute("user",user);
                return "redirect:/index.jsp";
            }
        }
        return  "redirect:/login.jsp";
    }

    @RequestMapping("save")
    public String save(User user,Integer[] roleId){
        System.out.println(user);
        userService.save(user,roleId);
        return "redirect:/user/list";
    }

    @RequestMapping("delete/{userId}")
    public String delete(@PathVariable("userId")Long id){
        int delete = userService.delete(id);
        if(delete!=0){
            return "redirect:/user/list";
        }
        return null;
    }

    @RequestMapping("saveUI")//由于我是要返回页面所以返回值是ModelAndView
    public ModelAndView saveUI(){
        ModelAndView modelAndView = new ModelAndView();
        List<Role> roles = roleService.list();
        //将数据带到新增页面
        modelAndView.addObject("roleList",roles);
        modelAndView.setViewName("user-add");
        return modelAndView;
    }

    @RequestMapping("list")
    public ModelAndView list(){
        List<User> userList = userService.list();

        //创建模型视图解析器，用来接收数据，并传递给视图解析器
        ModelAndView modelAndView = new ModelAndView();

        //userList这个名字是我们定义的，等下jstl会用到这个名字
        modelAndView.addObject("userList",userList);

        //设置要跳转到的视图
        modelAndView.setViewName("user-list");
        return modelAndView;
    }
}


package org.example.controller;

import org.example.domain.Role;
import org.example.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {
    /*@Resource//byName方式注入*/
    @Autowired
    private RoleService roleService;

    @RequestMapping("/roleList")
    public ModelAndView list(){
        ModelAndView modelAndView = new ModelAndView();
        List<Role> roleList = roleService.list();
        //设置模型
        modelAndView.addObject("roleList",roleList);//将数据发送到视图
        //设置视图
        modelAndView.setViewName("role-list");//要跳转哪个视图，前后缀设置过了，所以省略了
        return modelAndView;
    }

    //前端页面请求发送的数据被SpringMVC自动封装成了Role
    @RequestMapping("/save")
    public String save(Role role){
        roleService.save(role);
        return "redirect:/role/roleList";
    }
}

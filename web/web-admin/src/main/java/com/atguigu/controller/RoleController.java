package com.atguigu.controller;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
    
    private final static String PAGE_INDEX = "role/index";
    private static final String PAGE_CREATE = "role/create";
    private static final String PAGE_EDIT = "role/edit";
    private static final String LIST_ACTION = "redirect:/role";
    private static final String PAGE_ASSIGN_SHOW = "role/assignShow";

    @Reference
    private RoleService roleService;
    @Reference
    private PermissionService permissionService;

    /**
     * 跳转role/index.html页面
     * @param filters : 搜索条件(roleName, pageNum, pageSize)
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.show')")
    @RequestMapping
    public String index(@RequestParam Map filters, Model model) {
        //数据回显
        model.addAttribute("filters", filters);
        //根据filters(搜索条件)进行分页查询
        PageInfo<Role> pageInfo = roleService.findPage(filters);
        model.addAttribute("page", pageInfo);
        return PAGE_INDEX;
    }

    /**
     * 跳转role/create.html页面
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.create')")
    @RequestMapping("/create")
    public String create() {
        return PAGE_CREATE;
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.create')")
    @PostMapping("/save")
    public String save(Role role, Model model){
        roleService.insert(role);
        //跳转添加角色成功的界面
        return successPage(model, "添加角色成功");
    }

    /**
     * 跳转到role/edit.html页面
     * @param id
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.edit')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        Role role = roleService.getById(id);
        model.addAttribute("role", role);
        return PAGE_EDIT;
    }

    /**
     * 点击修改按钮后,表单提交请求的方法,修改角色
     * @param role
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.edit')")
    @PostMapping("/update")
    public String update(Role role, Model model){
        roleService.update(role);
        //跳转修改角色成功的界面
        return successPage(model, "修改角色成功");
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.delete')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        roleService.delete(id);
        return LIST_ACTION;
    }

    /**
     * 跳转role/assignShow.html页面
     * @param id
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.assgin')")
    @RequestMapping("/assignShow/{id}")
    public String assignShow(@PathVariable Long id, Model model) throws IOException {
        model.addAttribute("roleId", id);
        List<Map<String, Object>> zNodes = permissionService.findZNodesByRoleId(id);
        model.addAttribute("zNodes", new ObjectMapper().writeValueAsString(zNodes));
        return PAGE_ASSIGN_SHOW;
    }

    /**
     * 给角色分配权限
     * @param roleId : 角色id
     * @param permissionIds : 一个角色可以分配多个权限
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.assgin')")
    @RequestMapping("assignPermission")
    public String assignPermission(Long roleId, Long[] permissionIds, Model model){
        roleService.assignPermission(roleId, permissionIds);
        return successPage(model, "分配权限成功~");
    }
}
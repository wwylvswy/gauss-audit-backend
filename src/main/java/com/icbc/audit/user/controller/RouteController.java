package com.icbc.audit.user.controller;

import com.icbc.audit.user.entity.MenuEntity;
import com.icbc.audit.user.mapper.MenuMapper;
import com.icbc.audit.user.vo.RouteVO;
import com.icbc.audit.web.ApiResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/route")
public class RouteController {

    private final MenuMapper menuMapper;

    public RouteController(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @GetMapping("/get-async-routes")
    public ApiResponse<List<RouteVO>> getAsyncRoutes() {
        // 模拟从 SecurityContext 获取用户角色（实际应从 JWT 解析）

        // 演示用，实际应从 JWT 获取
        List<String> roleKeys = Arrays.asList("admin");

        Set<MenuEntity> menus = new HashSet<>();
        for (String role : roleKeys) {
            List<MenuEntity> list = menuMapper.selectMenusByRole(role);
            menus.addAll(list);
        }

        List<RouteVO> routes = buildRoutes(new ArrayList<>(menus));
        return ApiResponse.ok(routes);
    }

    private List<RouteVO> buildRoutes(List<MenuEntity> menus) {
        Map<Long, RouteVO> map = new HashMap<>();

        // 构建所有节点
        for (MenuEntity menu : menus) {
            RouteVO route = new RouteVO();
            BeanUtils.copyProperties(menu, route);
            Map<String, Object> meta = new HashMap<>();
            meta.put("title", menu.getTitle());
            meta.put("icon", menu.getIcon());
            meta.put("mrank", menu.getMrank());
            meta.put("roles", menu.getRoles());
            route.setMeta(meta);
            map.put(menu.getId(), route);
        }

        // 构建树
        List<RouteVO> root = new ArrayList<>();
        for (RouteVO route : map.values()) {
            if (route.getParentId() == 0) {
                root.add(route);
            } else {
                RouteVO parent = map.get(route.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(route);
                }
            }
        }

        return root;
    }
}


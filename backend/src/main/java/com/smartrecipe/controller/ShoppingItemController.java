package com.smartrecipe.controller;

import com.smartrecipe.dto.ShoppingItemDTO;
import com.smartrecipe.entity.ShoppingItem;
import com.smartrecipe.service.ShoppingItemService;
import com.smartrecipe.utils.JwtUtils;
import com.smartrecipe.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 购物清单控制器
 */
@RestController
@RequestMapping("/shopping")
public class ShoppingItemController {

    @Autowired
    private ShoppingItemService shoppingItemService;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/list")
    public Result<List<ShoppingItem>> list(HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromToken(request.getHeader("Authorization").substring(7));
        return Result.success(shoppingItemService.getItemsByUserId(userId));
    }

    @PostMapping
    public Result<Long> add(@Valid @RequestBody ShoppingItemDTO itemDTO, HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromToken(request.getHeader("Authorization").substring(7));
        return Result.success("添加成功", shoppingItemService.addItem(itemDTO, userId));
    }

    @PutMapping
    public Result<Void> update(@Valid @RequestBody ShoppingItemDTO itemDTO) {
        shoppingItemService.updateItem(itemDTO);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        shoppingItemService.deleteItem(id);
        return Result.success("删除成功", null);
    }

    @PutMapping("/toggle/{id}")
    public Result<Void> togglePurchased(@PathVariable Long id) {
        shoppingItemService.togglePurchased(id);
        return Result.success("操作成功", null);
    }

    /**
     * 根据膳食计划生成购物清单
     */
    @PostMapping("/generate")
    public Result<Void> generateFromMealPlan(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromToken(request.getHeader("Authorization").substring(7));
        shoppingItemService.generateFromMealPlan(userId, start, end);
        return Result.success("购物清单已生成", null);
    }
}

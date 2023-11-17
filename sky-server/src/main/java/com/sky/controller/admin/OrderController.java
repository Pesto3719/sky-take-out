package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import com.sun.org.apache.xpath.internal.operations.String;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单管理接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        PageResult pageResult=orderService.conditionSearch(ordersPageQueryDTO);

        return Result.success(pageResult);
    }
    /**
     * 各个状态的订单数量统计
     *
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation("各个状态的订单数量统计")
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> details4admin(@PathVariable("id") Long id) {
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * 接单
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result<String> confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        orderService.confirm(ordersConfirmDTO);

        return Result.success();
    }

    /**
     * 拒单
     * @param ordersRejectionDTO
     * @return
     */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result<String> rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        orderService.rejection(ordersRejectionDTO);

        return Result.success();
    }

    /**
     * 取消订单
     * @return
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result<String> cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        orderService.cancel(ordersCancelDTO);

        return Result.success();
    }

    /**
     * 派送订单
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result<String> delivery(@PathVariable Long id){
        orderService.delivery(id);

        return Result.success();
    }

    /**
     * 完成订单
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result<String> complete(@PathVariable Long id){
        orderService.complete(id);

        return Result.success();
    }


}

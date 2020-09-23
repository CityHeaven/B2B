package tiane.org.ssm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tiane.org.ssm.entity.BizOrdersR1;
import tiane.org.ssm.service.BizOrdersR1Service;
import tiane.org.ssm.util.ReResponse;
import tiane.org.ssm.util.Result;
import tiane.org.ssm.vo.BizOrderVO;
import tiane.org.ssm.vo.BizRtnTicketVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 国际订单
 */
@RestController
@RequestMapping("/api/order_r1")
@Api(value="/api/order_r1",description="国际订单API")
public class BizOrdersR1Controller {
    @Autowired
    private BizOrdersR1Service bizOrdersService;

    @ApiOperation(value = "订单列表", notes = "订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderStartDate",value="订单开始时间",paramType="query",required=false),
            @ApiImplicitParam(name="orderEndDate",value="订单结束时间",paramType="query",required=false),
            @ApiImplicitParam(name="flightStartDate",value="航班开始时间",paramType="query",required=false),
            @ApiImplicitParam(name="flightEndDate",value="航班结束时间",paramType="query",required=false),
            @ApiImplicitParam(name="orderCode",value="订单编号",paramType="query",required=false),
            @ApiImplicitParam(name="ticketCode",value="票号",paramType="query",required=false),
            @ApiImplicitParam(name="passenger",value="乘坐人",paramType="query",required=false),
            @ApiImplicitParam(name="srcPoint",value="出发地",paramType="query",required=false),
            @ApiImplicitParam(name="dstPoint",value="目的地",paramType="query",required=false),
            @ApiImplicitParam(name="orderType",value="订单类型",paramType="query",required=false),
            @ApiImplicitParam(name="orderStatus",value="订单状态",paramType="query",required=false),
            @ApiImplicitParam(name="ticketStatus",value="客票状态",paramType="query",required=false),
            @ApiImplicitParam(name="pnr",value="PNR",paramType="query",required=false),
            @ApiImplicitParam(name="flightType",value="国内/国际",paramType="query",required=false),
            @ApiImplicitParam(name="salesTarget",value="散客/团队",paramType="query",required=false),
            @ApiImplicitParam(name="size",value="页面大小",paramType="query",required=false),
            @ApiImplicitParam(name="current",value="当前页",paramType="query",required=false),
            @ApiImplicitParam(name="userId",value="用户id",paramType="query",required=true)
    })
    @GetMapping("/list")
    public Result<IPage<BizOrdersR1>> list(
            @RequestParam(name = "size",defaultValue = "10") String size,
            @RequestParam(name = "current",defaultValue = "1") String current,
            @RequestParam(name = "orderStartDate",required = false) String orderStartDate,
            @RequestParam(name = "orderEndDate",required = false) String orderEndDate,
            @RequestParam(name = "flightStartDate",required = false) String flightStartDate,
            @RequestParam(name = "flightEndDate",required = false) String flightEndDate,
            @RequestParam(name = "orderCode",required = false) String orderCode,
            @RequestParam(name = "ticketCode",required = false) String ticketCode,
            @RequestParam(name = "passenger",required = false) String passenger,
            @RequestParam(name = "srcPoint",required = false) String srcPoint,
            @RequestParam(name = "dstPoint",required = false) String dstPoint,
            @RequestParam(name = "orderType",required = false) String orderType,
            @RequestParam(name = "orderStatus",required = false) String orderStatus,
            @RequestParam(name = "ticketStatus",required = false) String ticketStatus,
            @RequestParam(name = "pnr",required = false) String pnr,
            @RequestParam(name = "userId",required = true) String userId,
            @RequestParam(name = "flightType",required = false) String flightType,
            @RequestParam(name = "salesTarget",required = false)String salesTarget){
        Page page = new Page<BizOrdersR1>(Integer.parseInt(current),Integer.parseInt(size));
        Map<String,Object> map = new HashMap<>();
        map.put("orderStartDate",orderStartDate == null || orderStartDate == ""? null: orderStartDate + " 00:00:00");
        map.put("orderEndDate",orderEndDate == null || orderEndDate == ""? null: orderEndDate + " 23:59:59");
        map.put("flightStartDate",flightStartDate == null || flightStartDate == ""? null: flightStartDate + " 00:00:00");
        map.put("flightEndDate",flightEndDate == null || flightEndDate == ""? null: flightEndDate + " 23:59:59");

        map.put("orderCode",orderCode);
        map.put("ticketCode",ticketCode);
        map.put("passenger",passenger);
        map.put("srcPoint",srcPoint);
        map.put("dstPoint",dstPoint);

        map.put("orderType",orderType);
        map.put("orderStatus",orderStatus);
        map.put("ticketStatus",ticketStatus);
        map.put("pnr",pnr);
        map.put("userId",userId);
        map.put("flightType",flightType);
        map.put("salesTarget",salesTarget);
        return ReResponse.success(bizOrdersService.queryPages(page,map));
    }


    @ApiOperation(value = "根据订单编号查询所有订单", notes = "根据订单编号查询所有订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderCode",value="订单编号",paramType="query",required=true)
    })
    @GetMapping("/getOrdersByCode")
    public Result<List<BizOrdersR1>> getOrdersByCode(@RequestParam(name = "orderCode") String orderCode){
        return ReResponse.success(bizOrdersService.getListByCode(orderCode));
    }

    @ApiOperation(value = "根据订单id查询订单", notes = "根据订单id查询订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderId",value="订单id",paramType="query",required=true)
    })
    @GetMapping("/getOrderById")
    public Result getOrderById(@RequestParam(name = "orderId") String orderId){
        return ReResponse.success(bizOrdersService.getOrderDetail(orderId));
    }

    @ApiOperation(value = "创建订单", notes = "创建订单")
    @PostMapping("/save")
    public Result save(@RequestBody BizOrderVO vo){
        boolean b = bizOrdersService.saveOrder(vo);
        return b?ReResponse.success():ReResponse.error("当前仓位票量不足！");
    }

    @ApiOperation(value = "退票", notes = "退票")
    @PostMapping("/rtnTicket")
    public Result rtnTicket(@RequestBody BizRtnTicketVO vo){
        boolean b = bizOrdersService.rtnTicket(vo);
        return b?ReResponse.success():ReResponse.error("fail");
    }

    @ApiOperation(value = "改签", notes = "改签")
    @PostMapping("/updateTicket")
    public Result updateTicket(@RequestBody BizRtnTicketVO vo){
        boolean b = bizOrdersService.updateTicket(vo);
        return b?ReResponse.success():ReResponse.error("fail");
    }
}

package tiane.org.ssm.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tiane.org.ssm.service.BizGoodsService;
import tiane.org.ssm.util.ReResponse;
import tiane.org.ssm.util.Result;
import tiane.org.ssm.vo.BizProductsVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@Api(value="/api/product",description="产品API")
public class BizGoodsController {
    @Autowired
    private BizGoodsService productsService;

    @ApiOperation(value = "产品列表", notes = "产品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",value="用户id",paramType="query",required=true),
            @ApiImplicitParam(name="departure",value="出发地",paramType="query",required=true),
            @ApiImplicitParam(name="arrival",value="目的地",paramType="query",required=true),
            @ApiImplicitParam(name="salesTarget",value="散客/团队",paramType="query",required=true),
            @ApiImplicitParam(name="flightType",value="国际/国内",paramType="query",required=true),
            @ApiImplicitParam(name="startDate",value="开始时间",paramType="query",required=false)
    })
    @GetMapping(value = "/list")
    public Result list(@RequestParam(name = "userId")String userId,
                       @RequestParam(name = "departure")String departure,
                       @RequestParam(name = "arrival")String arrival,
                       @RequestParam(name = "salesTarget")String salesTarget,
                       @RequestParam(name = "flightType")String flightType,
                       @RequestParam(name = "startDate",required = false)String startDate){
        List<BizProductsVO> list = new ArrayList<>();
        try{
            if(startDate != null && startDate.length() > 7){
                // 具体某一天的全部产品
                list = productsService.queryOneWayAllTicketsByDate(userId, salesTarget, departure, arrival, startDate,flightType);
            }else{
                // 产品日历
                String nowStr = startDate;
                if(startDate == null || startDate == "" || startDate.length() < 1){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                    nowStr = format.format(new Date());
                }
                list = productsService.queryOneWayAllTicketsByDate(userId, salesTarget, departure, arrival, nowStr,flightType);
            }
        }catch (Exception e){
            //处理输出日志
        }
        return ReResponse.success(list);
    }
}

package tiane.org.ssm.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tiane.org.ssm.entity.BizAgency;
import tiane.org.ssm.service.BizAgencyService;
import tiane.org.ssm.util.IdUtil;
import tiane.org.ssm.util.ReResponse;
import tiane.org.ssm.util.Result;

import java.util.List;

@RestController
@RequestMapping("/api/agency")
@Api(value="/api/agency",description="代理商API")
public class BizAgencyController {
    @Autowired
    BizAgencyService bizAgencyService;

    @ApiOperation(value = "代理商列表", notes = "代理商列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="agencyId",value="代理商id",paramType="query",required=true)
    })
    @GetMapping("/list")
    public Result list(@RequestParam(value = "agencyId") String agencyId){
        List<BizAgency> list = bizAgencyService.getListByAgencyId(agencyId);
        return ReResponse.success(list);
    }

    @ApiOperation(value = "新增代理商", notes = "新增代理商")
    @PostMapping("/save")
    public Result save(@RequestBody BizAgency agency){
        agency.setId(IdUtil.exchangeIdString());
        agency.setDelFlag(0);
        boolean save = bizAgencyService.saveOrUpdate(agency);
        return save?ReResponse.success():ReResponse.error("保存失败！");
    }

    @ApiOperation(value = "更新代理商", notes = "更新代理商")
    @PostMapping("/update")
    public Result update(@RequestBody BizAgency agency){
        boolean save = bizAgencyService.saveOrUpdate(agency);
        return save?ReResponse.success():ReResponse.error("更新失败！");
    }

    @ApiOperation(value = "删除代理商", notes = "删除代理商")
    @ApiImplicitParams({
            @ApiImplicitParam(name="agencyId",value="代理商id",paramType="query",required=true)
    })
    @GetMapping("/delete")
    public Result delete(@RequestParam(value = "agencyId") String agencyId){
        BizAgency agency = bizAgencyService.getById(agencyId);
        agency.setDelFlag(1);
        boolean delete = bizAgencyService.saveOrUpdate(agency);;
        return delete?ReResponse.success():ReResponse.error("删除失败！");
    }

}

package tiane.org.ssm.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tiane.org.ssm.entity.BizCity;
import tiane.org.ssm.service.BizCityService;
import tiane.org.ssm.util.ReResponse;
import tiane.org.ssm.util.Result;

import java.util.List;

@RestController
@RequestMapping("/api/city")
@Api(value="/api/city",description="城市API")
public class BizCityController {
    @Autowired
    private BizCityService bizCityService;

    @GetMapping("/list")
    public Result list(){
        List<BizCity> all = bizCityService.getAll();
        return ReResponse.success(all);
    }

    @GetMapping("/listExceptOtherCountries")
    public Result getAllExceptOtherCountries(){
        List<BizCity> all = bizCityService.getAllExceptOtherCountries();
        return ReResponse.success(all);
    }
}

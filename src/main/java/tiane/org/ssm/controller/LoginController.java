package tiane.org.ssm.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tiane.org.ssm.entity.BizAgency;
import tiane.org.ssm.service.LoginService;
import tiane.org.ssm.util.JWTUtil;
import tiane.org.ssm.util.ReResponse;
import tiane.org.ssm.util.Result;
import tiane.org.ssm.vo.BizAgencyVO;

@RestController
@Api(value="/",description="登录")
public class LoginController {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    @Autowired
    LoginService loginService;

//    @RequestMapping("/login.html")
//    public String loginTemplate() {
//        return "login";
//    }


    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping("/login")
    public Result login(@RequestBody BizAgencyVO agency) {
//        agency = new BizAgencyVO();
//        agency.setUsername("admin");
//        agency.setPassword("admin");
        BizAgency user = loginService.getAgencyByName(agency.getUsername());
        if (user != null && user.getPassword().equals(agency.getPassword())) {
            agency.setToken(JWTUtil.sign(agency.getUsername(), user.getPassword()));
            agency.setAgency(user);
            return ReResponse.success(agency);
        } else {
            return ReResponse.error("账户密码不匹配！");
        }
    }
}

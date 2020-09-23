package tiane.org.ssm.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiane.org.ssm.dao.BizAgencyMapper;
import tiane.org.ssm.entity.BizAgency;
import tiane.org.ssm.service.LoginService;
import tiane.org.ssm.util.ReResponse;
import tiane.org.ssm.util.Result;
import tiane.org.ssm.vo.BizAgencyVO;

import java.util.HashMap;
import java.util.HashSet;


@Service
public class LoginServiceImpl extends ServiceImpl<BizAgencyMapper, BizAgency> implements LoginService {
    @Autowired
    BizAgencyMapper bizAgencyMapper;

    @Override
    public BizAgency getAgencyByName(String name) {
        QueryWrapper<BizAgency> wrapper = new QueryWrapper<>();
        wrapper.eq("name",name);
        wrapper.eq("del_flag",0);
        return this.getOne(wrapper);
    }

    @Override
    public Result login(BizAgencyVO agency) {

        return ReResponse.success();
    }
}

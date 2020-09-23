package tiane.org.ssm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tiane.org.ssm.dao.BizAgencyMapper;
import tiane.org.ssm.entity.BizAgency;
import tiane.org.ssm.service.BizAgencyService;

import java.util.List;

@Service
public class BizAgencyServiceImpl extends ServiceImpl<BizAgencyMapper, BizAgency> implements BizAgencyService {
    @Override
    public List<BizAgency> getListByAgencyId(String agencyId) {
        QueryWrapper<BizAgency> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",agencyId);
        wrapper.eq("del_flag",0);
        return this.list(wrapper);
    }
}

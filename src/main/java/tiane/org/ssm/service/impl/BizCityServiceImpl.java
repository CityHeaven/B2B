package tiane.org.ssm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tiane.org.ssm.dao.BizCityMapper;
import tiane.org.ssm.entity.BizCity;
import tiane.org.ssm.service.BizCityService;

import java.util.List;

@Service
public class BizCityServiceImpl extends ServiceImpl<BizCityMapper, BizCity> implements BizCityService {
    @Override
    public List<BizCity> getCrccByName(String name) {
        QueryWrapper<BizCity> cityQueryWrapper = new QueryWrapper<>();
        cityQueryWrapper.eq("name",name);
        return this.list(cityQueryWrapper);
    }

    @Override
    public List<BizCity> getAll() {
        return this.list();
    }

    @Override
    public List<BizCity> getAllExceptOtherCountries() {
        QueryWrapper<BizCity> cityQueryWrapper = new QueryWrapper<>();
        cityQueryWrapper.in("country","中国");
        return this.list(cityQueryWrapper);
    }
}

package tiane.org.ssm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tiane.org.ssm.entity.BizCity;

import java.util.List;

public interface BizCityService extends IService<BizCity> {
    List<BizCity> getCrccByName(String name);

    List<BizCity> getAll();

    List<BizCity> getAllExceptOtherCountries();

}

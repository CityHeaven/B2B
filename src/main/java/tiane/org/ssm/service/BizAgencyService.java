package tiane.org.ssm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tiane.org.ssm.entity.BizAgency;

import java.util.List;

public interface BizAgencyService extends IService<BizAgency> {
    List<BizAgency> getListByAgencyId(String agencyId);
}

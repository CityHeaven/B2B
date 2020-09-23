package tiane.org.ssm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tiane.org.ssm.entity.BizAgency;
import tiane.org.ssm.util.Result;
import tiane.org.ssm.vo.BizAgencyVO;

public interface LoginService extends IService<BizAgency> {

    BizAgency getAgencyByName(String name);

    Result login(BizAgencyVO agency);
}

package tiane.org.ssm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tiane.org.ssm.dao.BizScheduleMapper;
import tiane.org.ssm.entity.BizSchedule;
import tiane.org.ssm.service.BizScheduleService;

@Service
public class BizScheduleServiceImpl extends ServiceImpl<BizScheduleMapper, BizSchedule> implements BizScheduleService {
}

package tiane.org.ssm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tiane.org.ssm.dao.SysNoticeMapper;
import tiane.org.ssm.entity.SysNotice;
import tiane.org.ssm.service.SysNoticeService;

@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {
}

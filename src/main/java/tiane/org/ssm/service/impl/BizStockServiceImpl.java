package tiane.org.ssm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tiane.org.ssm.dao.BizStockMapper;
import tiane.org.ssm.entity.BizStock;
import tiane.org.ssm.service.BizStockService;

@Service
public class BizStockServiceImpl extends ServiceImpl<BizStockMapper, BizStock> implements BizStockService {
}

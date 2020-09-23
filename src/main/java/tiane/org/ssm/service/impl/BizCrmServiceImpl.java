package tiane.org.ssm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiane.org.ssm.dao.BizCredentialsMapper;
import tiane.org.ssm.dao.BizCrmMapper;
import tiane.org.ssm.entity.BizCredentials;
import tiane.org.ssm.entity.BizCrm;
import tiane.org.ssm.entity.BizOrdersR1;
import tiane.org.ssm.entity.BizOrdersR2;
import tiane.org.ssm.service.BizCrmService;
import tiane.org.ssm.vo.BizPasserVO;

import java.util.List;

@Service
public class BizCrmServiceImpl extends ServiceImpl<BizCrmMapper, BizCrm> implements BizCrmService {

    @Autowired
    private BizCredentialsMapper bizCredentialsMapper;

    @Override
    public String getIdByCardOrIdCard(BizPasserVO passer) {
        QueryWrapper<BizCredentials> wrapper = new QueryWrapper<>();
        wrapper.eq("number",passer.getCardNumber());
        wrapper.eq("type",passer.getCardType());
        List<BizCredentials> bizCredentials = bizCredentialsMapper.selectList(wrapper);
        for(BizCredentials bizCredential:bizCredentials){
            String crmId = bizCredential.getCrmId();
            BizCrm crm = this.getById(crmId);
            if(crm != null && crm.getName().equals(passer.getName())){
                return crmId;
            }
        }
        return null;
    }

    /**
     * 判断是否需要新增
     * @param type 身份证
     * @param cardNumber 证件号
     * @param passerName
     * @return
     */
    @Override
    public String isNeedInsert(String type, String cardNumber, String passerName) {
        QueryWrapper<BizCredentials> wrapper = new QueryWrapper<>();
        wrapper.eq("type",type);
        wrapper.eq("number",cardNumber);
        List<BizCredentials> bizCredentials = bizCredentialsMapper.selectList(wrapper);
        if(bizCredentials.size() > 0){
            for(BizCredentials credentials:bizCredentials){
                String crmId = credentials.getCrmId();
                BizCrm byId = this.getById(crmId);
                if(byId == null || !byId.getName().equals(passerName)){
                    return null;
                }
            }
            return  bizCredentials.get(0).getCrmId();
        }
        return null;

    }

    @Override
    public BizCrm getCrmByCardNo(BizOrdersR2 order) {
        QueryWrapper<BizCredentials> wrapper = new QueryWrapper<>();
        wrapper.eq("number",order.getCredential());
        List<BizCredentials> bizCredentials = bizCredentialsMapper.selectList(wrapper);
        for(BizCredentials credential:bizCredentials){
            String crmId = credential.getCrmId();
            BizCrm bizCrm = this.getById(crmId);
            if(bizCrm != null && bizCrm.getName().equals(order.getPassenger())){
                return bizCrm;
            }
        }
        return null;
    }

    @Override
    public BizCrm getCrmByCardNoByR1(BizOrdersR1 order) {
        QueryWrapper<BizCredentials> wrapper = new QueryWrapper<>();
        wrapper.eq("number",order.getPassport());
        List<BizCredentials> bizCredentials = bizCredentialsMapper.selectList(wrapper);
        for(BizCredentials credential:bizCredentials){
            String crmId = credential.getCrmId();
            BizCrm bizCrm = this.getById(crmId);
            if(bizCrm != null && bizCrm.getName().equals(order.getPassenger())){
                return bizCrm;
            }
        }
        return null;
    }
}

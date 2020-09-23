package tiane.org.ssm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tiane.org.ssm.entity.BizCrm;
import tiane.org.ssm.entity.BizOrdersR1;
import tiane.org.ssm.entity.BizOrdersR2;
import tiane.org.ssm.vo.BizPasserVO;

public interface BizCrmService extends IService<BizCrm> {
    /**
     * 根据人员证件号或者身份证号 查询ID
     * @param passer
     * @return
     */
    String getIdByCardOrIdCard(BizPasserVO passer);

    /**
     * 判断是否需要新增
     * @param cardType 证件类别
     * @param cardNumber 证件号
     * @param passerName
     * @return
     */
    String isNeedInsert(String cardType, String cardNumber, String passerName);

    /**
     *
     * @param credential 订单
     * @return
     */
    BizCrm getCrmByCardNo(BizOrdersR2 credential);

    BizCrm getCrmByCardNoByR1(BizOrdersR1 ordersR1);
}

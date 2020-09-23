package tiane.org.ssm.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * biz_country
 * @author 
 */
@Data
public class BizCountry implements Serializable {
    /**
     * 国家编号
     */
    private String id;

    /**
     * 国家名称
     */
    private String name;

    /**
     * 国家二字码
     */
    private String nwcc;

    /**
     * 国家三字码
     */
    private String nrcc;

    private static final long serialVersionUID = 1L;

}
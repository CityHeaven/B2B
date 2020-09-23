package tiane.org.ssm.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * biz_city
 * @author 
 */
@Data
public class BizCity implements Serializable {
    /**
     * 城市编号
     */
    private String id;

    /**
     * 城市名称
     */
    private String name;

    /**
     * 城市三字码
     */
    private String crcc;

    /**
     * 所在国家
     */
    private String country;

    private static final long serialVersionUID = 1L;
}
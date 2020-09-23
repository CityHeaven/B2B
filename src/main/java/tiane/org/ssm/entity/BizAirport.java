package tiane.org.ssm.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * biz_airport
 * @author 
 */
@Data
public class BizAirport implements Serializable {
    /**
     * 机场编号
     */
    private String id;

    /**
     * 机场名称
     */
    private String name;

    /**
     * 机场三字码
     */
    private String nrcc;

    /**
     * 所在国家
     */
    private String country;

    /**
     * 所在城市
     */
    private String city;

    private static final long serialVersionUID = 1L;
}
package tiane.org.ssm.vo.xc;

public enum CardType {
    ID("身份证", 1),
    PASSPORT("护照", 2),
    BD("出生日期", 0),
    STC("学生证", 3),
    MTC("军人证", 4),
    DRLC("驾驶证", 5),
    RP("回乡证", 6),
    MTP("台胞证", 7),
    HMP("港澳通行证", 8),
    ISC("国际海员证", 9),
    PRC("外国人永久居留证", 10),
    TC("旅行证", 11),
    SOC("士兵证", 12),
    TID("临时身份证", 13),
    RBT("户口薄", 14),
    PLC("警官证", 15),
    BRC("出生证明", 16),
    OTHER("其他", 17),
    HMTRP("港澳台居住证", 18);
    // 成员变量
    private String name;
    private int index;
    // 构造方法
    private CardType(String name, int index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(int index) {
        for (CardType c : CardType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
    // get set 方法
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}

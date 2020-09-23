package tiane.org.ssm.vo.xc;

public enum PassengerType {
    ADT("成人", 0),
    CHD("儿童", 1),
    INF("婴儿", 2);
    // 成员变量
    private String name;
    private int index;
    // 构造方法
    private PassengerType(String name, int index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(int index) {
        for (PassengerType c : PassengerType.values()) {
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

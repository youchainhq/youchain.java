package cc.youchain.protocol.core.enums;

public enum YOUValidatorRole {
    ALL(0), // 全部角色
    CHANCELLOR(1), // 议长
    SENATOR(2), // 议员
    HOUSE(3) // 众议
    ;

    private Integer role;

    YOUValidatorRole(Integer role) {
        this.role = role;
    }

    public Integer getRole() {
        return role;
    }
}

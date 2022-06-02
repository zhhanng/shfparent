package com.atguigu.en;

/**
 * 包名:com.atguigu.en
 *枚举,用自定义的static final变量关联想要的信息,即message,变量代表message
 * @author Leevi
 * 日期2022-05-21  15:58
 */
public enum DictCode {
    HOUSETYPE("houseType"),
    FLOOR("floor"), BUILDSTRUCTURE("buildStructure"),
    DECORATION("decoration"), DIRECTION("direction"), HOUSEUSE("houseUse");
    private String message;

    //构造器
    DictCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

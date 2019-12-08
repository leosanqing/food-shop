package com.leosanqing.enums;

/**
 * @Author: leosanqing
 * @Date: 2019-12-07 09:18
 */
public enum Sex {
    /**
     * 表示性别的枚举
     */
    WOMAN(0, "女"),
    MAN(1, "男"),
    SECRET(2, "保密");

    public final int type;
    public final String value;

    Sex(int type, String value) {
        this.type = type;
        this.value = value;
    }
}

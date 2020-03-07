package com.leosanqing.enums;

/**
 * @Author: leosanqing
 * @Date: 2019-12-07 09:18
 */
public enum CommentLevel {
    /**
     * 表示评价等级的枚举
     */
    GOOD(1, "好评"),
    NORMAL(2, "中评"),
    BAD(3, "差评")
    ;

    public final int type;
    public final String value;

    CommentLevel(int type, String value) {
        this.type = type;
        this.value = value;
    }
}

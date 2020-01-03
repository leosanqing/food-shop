package com.leosanqing.enums;

/**
 * @Author: leosanqing
 * @Date: 2019-12-07 09:18
 */
public enum YesOrNo {
    /**
     * 表示是或不是的枚举
     */
    NO(0, "否"),
    YES(1, "是")
    ;

    public final int type;
    public final String value;

    YesOrNo(int type, String value) {
        this.type = type;
        this.value = value;
    }
}

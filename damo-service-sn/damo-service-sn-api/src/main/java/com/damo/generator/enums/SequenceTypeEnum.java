package com.damo.generator.enums;

public enum SequenceTypeEnum {
    //（1:纯数字,2:纯字母,3:混排）
    NUMBER(1),
    ALPHABETIC(2),
    SPECIAL(3),
    ;

    private byte code;

    SequenceTypeEnum(int code) {
        this.code = (byte) code;
    }

    public byte getCode() {
        return code;
    }

    public static SequenceTypeEnum getByCode(int code) {
        for(SequenceTypeEnum s: SequenceTypeEnum.values()) {
            if (s.getCode() == code) {
                return s;
            }
        }
        return null;
    }
}
package com.damo.generator.enums;


public enum SequenceIdTypeEnum {

    //（1:自增,2:随机,3:雪花,4:uuid,5:redis）',

    SEQUENCE(1,"seq"),  //本地，自增长，带步长，可退id；
    RANDOM(2,"rad"),   //随机
    SNOWFLAKE(3,"snw"),  //雪花
    UUID(4,"uud"),    //uuid。不带-
    REDIS(5,"rds"),  //按reids_key，自增，
    VERIFI(6,"vrf"),  //带校验
    ;

    private String name;
    private byte code;

    SequenceIdTypeEnum(int code,String name) {
        this.code = (byte)code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }
    public String getName() {
        return name;
    }

    public static SequenceIdTypeEnum getByCode(int code) {
        for(SequenceIdTypeEnum s: SequenceIdTypeEnum.values()) {
            if (s.getCode() == code) {
                return s;
            }
        }
        return null;
    }

    public static SequenceIdTypeEnum getByName(String name) {
        for(SequenceIdTypeEnum s: SequenceIdTypeEnum.values()) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }
}

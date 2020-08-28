# sn_starter 序号服务


   取序号：SequenceService.getNext(code);
   说明：code在库里不存在时，会新建一个。然后累加。
 
   GidConfigDAO 配置表(gid_config) 字段说明：
   code 唯一标识。可用模块+业务表达，VARCHAR(64)
   name 中文名
   idType id类型对应 SequenceIdTypeEnum
   sn 存当前序号值
   snLimit 序号限制值，比sn优先级高。有值时，sn无效。
   dateFormat 日期格式，配置循环的Key。比如按年，按月，按小时。
   idFormat 序号输出的格式。使用了String.format加SimpleDateFormat进行格式化输出。'Y'不转意
   step 步长；(VERIFI，RANDOM时表示长度)
   seqType 序号类型对应 SequenceTypeEnum (随机数时有用)
 
   case1:业务单号：年+月+6位流水号；效果：202002000001
   config.setIdType(SequenceIdTypeEnum.REDIS.getCode());  //SEQUENCE存数据库，REDIS性能更好，不怕并发。
   config.setDateFormat("yyyyMM");  //到月就按月自增，到小时就按小时自增
   config.setIdFormat("yyyyMM%06d");  //不足6位补零。自增超过6位时，会超长。
 
   case2:随机数；效果：026971，可用于短信验证码，图形验证码
   config.setIdType(SequenceIdTypeEnum.RANDOM.getCode());
   config.setSeqType(SequenceTypeEnum.NUMBER.getCode()); //数字，字母，混编 三种可选
   config.setSn(6L);  //长度
 
   case3:兑换券；效果：ZKU4Z4RCT
   config.setIdType(SequenceIdTypeEnum.VERIFI.getCode());
   config.setSn(9L);  //活动码，可校验。
   config.setStep(8);  //长度
   VerifiableSerialService.getActId("ZKU4Z4RCT") 可以读出9；
   VerifiableSerialService.verify("ZKU4Z4RCT"，true) 可校验券码是否有效。
 
   case4:雪花；效果：6632827278378307584 2020-02-11 10:47:35:137,41,0
   config.setIdType(SequenceIdTypeEnum.SNOWFLAKE.getCode());
   Snowflake.show(id) 可以解析出时间，机器码，序号。
 
   case5:uuid；效果：32位 a2e7eabb25a64600b9589d9ab952afbb
   config.setIdType(SequenceIdTypeEnum.UUID.getCode());
   调用UUID.randomUUID()
 
   case6:可退回序号；比如业务要求序号是连续的，有的序号暂时领用了却没有实际使用时，需要退回给下一次使用。
   ent.setIdType(SequenceIdTypeEnum.SEQUENCE.getCode());
   config.setSnLimit("1-100"); //序号范围，用完报错。VARCHAR(5000)，如果序号太过碎片化，字段超长时会有异常。
   取号：SequenceService.getNext(code);
   退回：SequenceService.backSn(code,"1");
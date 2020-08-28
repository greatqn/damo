CREATE TABLE `gid_config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '名称',
  `code` varchar(64) NOT NULL DEFAULT '' COMMENT '代码，唯一键',
  `date_format` varchar(64) COMMENT '日期格式化:yyyyMM；用于按月生成序号的模式',
  `id_type` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '序号类型（1:自增,2:随机,3:雪花,4:uuid,5:redis）',
  `seq_type` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '序号类型（1:纯数字,2:纯字母,3:混排）',
  `id_key` varchar(64)  COMMENT '暂不用',
  `id_format` varchar(64) COMMENT 'yyyyMM%06d',
  `sn` bigint(20) unsigned COMMENT '累计值',
  `sn_limit` varchar(64) COMMENT '取值范围 1-100',
  `step` int(10) unsigned COMMENT '步长(默认为1)',
  `operator` varchar(64) COMMENT '操作人',
  `gmt_create` datetime(3)  COMMENT '创建时间',
  `gmt_modify` datetime(3) COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='序号表';

CREATE TABLE `gid_worker` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `ip` varchar(64) NOT NULL DEFAULT '' COMMENT '',
  `pid` varchar(64) NOT NULL DEFAULT '' COMMENT '',
  `info` varchar(64) NOT NULL DEFAULT '' COMMENT '',
  `operator` varchar(64) NOT NULL DEFAULT '' COMMENT '',
  `gmt_create` datetime(3)  COMMENT '创建时间',
  `gmt_modify` datetime(3) COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='序号工作表';


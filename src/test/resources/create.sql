CREATE TABLE `parent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=301 DEFAULT CHARSET=utf8;


CREATE TABLE `child` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_child_1_idx` (`parent_id`),
  CONSTRAINT `fk_child_1` FOREIGN KEY (`parent_id`) REFERENCES `parent` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=301 DEFAULT CHARSET=utf8;


CREATE TABLE `test_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `c_varchar` varchar(100) DEFAULT NULL,
  `c_varbinary` varbinary(4000) DEFAULT NULL,
  `c_text` text,
  `c_blob` blob,
  `c_time` time DEFAULT NULL,
  `c_timestamp` timestamp NULL DEFAULT NULL,
  `c_date` date DEFAULT NULL,
  `c_datetime` datetime DEFAULT NULL,
  `c_decimal` decimal(22,6) DEFAULT NULL,
  `c_double` double DEFAULT NULL,
  `c_float` float DEFAULT NULL,
  `c_bigint` bigint(20) DEFAULT NULL,
  `c_int` int(11) DEFAULT NULL,
  `c_mediumint` mediumint(9) DEFAULT NULL,
  `c_smallint` smallint(6) DEFAULT NULL,
  `c_tinyint` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1258252 DEFAULT CHARSET=utf8;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `sex` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=armscii8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
BEGIN;
INSERT INTO `t_user` VALUES (1, 'json', '123', '2019-07-27 16:01:21', 1);
INSERT INTO `t_user` VALUES (2, 'jack jo', '123', '2019-07-24 16:01:37', 1);
INSERT INTO `t_user` VALUES (3, 'manistal', '123', '2019-07-24 16:01:37', 1);
INSERT INTO `t_user` VALUES (4, 'landengdeng', '123', '2019-07-24 16:01:37', 1);
INSERT INTO `t_user` VALUES (5, 'max', '123', '2019-07-24 16:01:37', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
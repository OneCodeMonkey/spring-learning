CREATE TABLE `hc_user`
(
    `Id`       int(11) NOT NULL AUTO_INCREMENT,
    `Email`    varchar(50)  NOT NULL DEFAULT '',
    `Password` varchar(32)  NOT NULL DEFAULT '',
    `Username` varchar(16)  NOT NULL DEFAULT '',
    `Intro`    varchar(255) NOT NULL DEFAULT '',
    `Avatar`   varchar(50)  NOT NULL DEFAULT '',
    PRIMARY KEY (`Id`),
    UNIQUE KEY `Email` (`Email`),
    UNIQUE KEY `Username` (`Username`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4;

INSERT INTO `hc_user`
VALUES ('1', '123@qq.com', '4d4590e6029c59e4ff79b3b58df2028c', '张三', '这个人很懒，什么也没留下！', 'avatar1.jpg');

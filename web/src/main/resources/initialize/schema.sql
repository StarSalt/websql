set names utf8;
/**
create database table
 */
CREATE TABLE data_base (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  db_name VARCHAR(255) NOT NULL UNIQUE COMMENT '数据库名',
  url VARCHAR(300) NOT NULL COMMENT '链接地址',
  user_name VARCHAR(255) NOT NULL COMMENT '数据库名',
  password VARCHAR(255) NOT NULL COMMENT '密码',
  db_type VARCHAR(50) NOT NULL COMMENT '数据库类型',
  create_user_id BIGINT(20) NOT NULL COMMENT '创建者',
  update_user_id BIGINT(20) NOT NULL COMMENT '更新着',
  create_time DATETIME NOT NULL COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  del INT(3) NOT NULL DEFAULT 0 COMMENT '删除状态'
) ENGINE=InnoDB charset=utf8 DEFAULT CHARACTER SET utf8 COMMENT 'database table';

/**
create db resource table
 */
CREATE TABLE db_resource (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  database_id BIGINT(20) NOT NULL COMMENT '数据库id',
  table_name VARCHAR(255) NOT NULL COMMENT 'table name',
  is_view INT(2) NOT NULL DEFAULT 0 COMMENT '是否视图',
  create_time DATETIME NOT NULL COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  del INT(3) NOT NULL DEFAULT 0 COMMENT '删除状态',
  index idx_database_id_table_name(database_id, table_name)
) ENGINE=InnoDB charset=utf8 DEFAULT CHARACTER SET utf8 COMMENT '数据库资源 表';

/**
depart table
 */
CREATE TABLE depart (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  depart_name VARCHAR(255) NOT NULL COMMENT '部门名称',
  parent_id BIGINT(20) COMMENT '父id',
  root_id BIGINT(20),
  create_user_id BIGINT(20) NOT NULL COMMENT '创建者',
  update_user_id BIGINT(20) NOT NULL COMMENT '更新着',
  create_time DATETIME NOT NULL COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  del INT(3) NOT NULL DEFAULT 0 COMMENT '删除状态'
) ENGINE=InnoDB charset=utf8 DEFAULT CHARACTER SET utf8 COMMENT '部门 表';

/**
role table
 */
CREATE TABLE role (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  depart_id BIGINT(20) NOT NULL ,
  role_name VARCHAR(255) NOT NULL ,
  role_type int(3) NOT NULL DEFAULT 0,
  create_user_id BIGINT(20) NOT NULL COMMENT '创建者',
  update_user_id BIGINT(20) NOT NULL COMMENT '更新着',
  create_time DATETIME NOT NULL COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  del INT(3) NOT NULL DEFAULT 0 COMMENT '删除状态',
  index idx_role_name (role_name)
) ENGINE=InnoDB CHARSET=utf8 DEFAULT CHARACTER SET utf8 COMMENT '角色 表';

/**
role resource
 */
CREATE TABLE role_resource (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  role_id BIGINT(20) NOT NULL ,
  resource_id BIGINT(20) NOT NULL ,
  database_id BIGINT(20) NOT NULL ,
  table_name VARCHAR(255) NOT NULL ,
  create_user_id BIGINT(20) NOT NULL COMMENT '创建者',
  update_user_id BIGINT(20) NOT NULL COMMENT '更新着',
  create_time DATETIME NOT NULL COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  index idx_role_id(role_id)
) ENGINE=InnoDB CHARSET=utf8 DEFAULT CHARACTER SET utf8 COMMENT '角色资源 表';

/**
create user entity
 */
CREATE TABLE user_info (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  depart_id BIGINT(20) NOT NULL ,
  user_name VARCHAR(255) NOT NULL UNIQUE ,
  password VARCHAR(255) NOT NULL ,
  salt VARCHAR(50) NOT NULL ,
  email VARCHAR(50) NOT NULL UNIQUE ,
  admin TINYINT(1) NOT NULL DEFAULT 0,
  user_type TINYINT(1) NOT NULL DEFAULT 0,
  create_time DATETIME NOT NULL COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  lock_time DATETIME ,
  status TINYINT(2) NOT NULL DEFAULT 0,
  create_user_id BIGINT(20) NOT NULL COMMENT '创建者',
  update_user_id BIGINT(20) NOT NULL COMMENT '更新着',
  del INT(3) NOT NULL DEFAULT 0 COMMENT '删除状态',
  index idx_depart_id(depart_id)
) ENGINE=InnoDB CHARSET=utf8 DEFAULT CHARACTER SET utf8 ;

/**

create table user_role
 */
CREATE TABLE user_role (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  user_name VARCHAR(255) NOT NULL ,
  user_id BIGINT(20) NOT NULL ,
  role_id BIGINT(20) NOT NULL ,
  create_user_id BIGINT(20) NOT NULL COMMENT '创建者',
  update_user_id BIGINT(20) NOT NULL COMMENT '更新着',
  create_time DATETIME NOT NULL COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  index idx_user_id(user_id),
  index idx_user_name(user_name)
) ENGINE=InnoDB CHARSET=utf8 DEFAULT CHARACTER SET utf8 ;

/**
log
 */
CREATE TABLE operate_log (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT(20) NOT NULL ,
  user_name VARCHAR(255) NOT NULL ,
  user_email VARCHAR(50) NOT NULL ,
  event_type VARCHAR(50) NOT NULL ,
  event_name VARCHAR(50) NOT NULL ,
  op_time DATETIME NOT NULL ,
  request_id VARCHAR(50) NOT NULL ,
  params LONGTEXT ,
  remark VARCHAR(255) NOT NULL ,
  create_time DATETIME,
  index idx_user_id_1(user_id),
  index idx_user_name_1(user_name),
  index idx_user_email_1(user_email),
  index idx_event_type(event_type)
) ENGINE=InnoDB CHARSET=utf8 DEFAULT CHARACTER SET utf8 ;


CREATE TABLE query_log (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT(20) NOT NULL ,
  user_name VARCHAR(255) NOT NULL ,
  user_email VARCHAR(255) NOT NULL ,
  db_id BIGINT(20) NOT NULL ,
  db_name VARCHAR(255) NOT NULL ,
  table_name VARCHAR(255) NOT NULL ,
  sql_text LONGTEXT NOT NULL ,
  cost_time INT(11) NOT NULL DEFAULT 0,
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  index idx_user_id(user_name, table_name),
  index (user_email, table_name)
) ENGINE=InnoDB CHARSET=utf8 DEFAULT CHARACTER SET utf8 ;


CREATE TABLE sql_collector (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) ,
  sql_text LONGTEXT NOT NULL ,
  user_id BIGINT(20) NOT NULL ,
  user_name VARCHAR(255) NOT NULL ,
  use_count BIGINT(10) NOT NULL ,
  create_time DATETIME,
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  index idx_user_id2(user_id)
) ENGINE=InnoDB CHARSET=utf8 DEFAULT CHARACTER SET utf8;

ALTER TABLE query_log ADD COLUMN collect_id bigint(20) NULL DEFAULT NULL AFTER table_name;
/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2011/7/8 22:22:51                            */
/*==============================================================*/


drop table if exists s_indexmicroblog;

drop table if exists u_comment;

drop table if exists u_favorites;

drop table if exists u_microblogging;

drop index userid on u_user;

drop index phone on u_user;

drop index email on u_user;

drop table if exists u_user;

/*==============================================================*/
/* Table: s_indexmicroblog                                      */
/*==============================================================*/
create table s_indexmicroblog
(
   id                   varchar(20) not null comment '所有文章使用同一的ID生成器',
   userid               varchar(30),
   title                varchar(100),
   content              varchar(300),
   createdate           date,
   isoriginal           tinyint,
   reproducedurl        varchar(300),
   sourcetype           tinyint,
   reproducedauthor     varchar(50),
   apprstatus           tinyint,
   approver             varchar(30),
   apprtime             date,
   validstatus          tinyint comment '是否公开等等',
   validoptdate         date,
   articletype          varchar(10) comment '是微博、还是评论、还是其他的什么种类',
   forumtype            varchar(10),
   priority             tinyint,
   usetype              tinyint,
   ismodify             smallint,
   lastdate             date,
   lastopter            varchar(30),
   isused               boolean,
   useddate             date,
   primary key (id)
);

alter table s_indexmicroblog comment 's_系统主页内容微语录';

/*==============================================================*/
/* Table: u_comment                                             */
/*==============================================================*/
create table u_comment
(
   id                   varchar(20) not null,
   articleid            varchar(20),
   validstatus          tinyint,
   userid               varchar(30),
   apprstatus           tinyint,
   approver             varchar(30),
   apprtime             date,
   comtent              varchar(100),
   supporttype          tinyint,
   primary key (id)
);

alter table u_comment comment '评论';

/*==============================================================*/
/* Table: u_favorites                                           */
/*==============================================================*/
create table u_favorites
(
   id                   varbinary(20) not null,
   articleid            varchar(20),
   createdate           date,
   validstatus          tinyint,
   userid               varchar(30),
   primary key (id)
);

alter table u_favorites comment '收藏夹,收入文章、评论等等';

/*==============================================================*/
/* Table: u_microblogging                                       */
/*==============================================================*/
create table u_microblogging
(
   id                   varchar(20) not null comment '所有文章使用同一的ID生成器',
   userid               varchar(30),
   title                varchar(100),
   content              varchar(300),
   createdate           date,
   lastdate             date,
   isoriginal           tinyint,
   reproducedurl        varchar(300),
   sourcetype           tinyint,
   reproducedauthor     varchar(50),
   apprstatus           tinyint,
   approver             varchar(30),
   apprtime             date,
   validstatus          tinyint comment '是否公开等等',
   articletype          varchar(10) comment '是微博、还是评论、还是其他的什么种类',
   forumtype            varchar(10),
   validoptdate         date,
   primary key (id)
);

alter table u_microblogging comment '发表言论、微语录、微博';

/*==============================================================*/
/* Table: u_user                                                */
/*==============================================================*/
create table u_user
(
   userid               varchar(30) not null,
   username             varchar(30),
   password             varchar(30),
   email                varchar(50),
   phone                bigint,
   logintype            tinyint,
   statustype           tinyint,
   createdate           date,
   securitytip          tinyint,
   receivetype          tinyint,
   primary key (userid)
);

alter table u_user comment '用户信息表';

/*==============================================================*/
/* Index: email                                                 */
/*==============================================================*/
create unique fulltext index email on u_user
(
   
);

/*==============================================================*/
/* Index: phone                                                 */
/*==============================================================*/
create unique fulltext index phone on u_user
(
   
);

/*==============================================================*/
/* Index: userid                                                */
/*==============================================================*/
create unique index userid on u_user
(
   
);


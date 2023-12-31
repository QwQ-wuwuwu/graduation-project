use `project`;
create table if not exists `user`(
    id char(19) primary key,
    number char(10) not null unique,
    password varchar(64) not null,
    role int not null,
    description varchar(600) null,
    insert_time datetime not null default CURRENT_TIMESTAMP,
    update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    index(number)
    );
create table if not exists `teacher` (
    id char(19) primary key,
    user_number char(10) not null unique,
    t_name varchar(20) not null,
    total int not null,
    left_select int not null,
    group_id int not null , /*老师所在的评审小组*/
    insert_time datetime not null default CURRENT_TIMESTAMP,
    update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    foreign key(user_number) references `user`(number),
    index(user_number),
    index(group_id)
    );
create table if not exists `student`(
    id char(19) primary key,
    s_name varchar(20) not null,
    teacher_id char(19),
    user_number char(10) not null unique,
    group_id int null , /*加入的评审小组*/
    project_title varchar(50) not null , /*毕设题目*/
    insert_time datetime not null default CURRENT_TIMESTAMP,
    select_time datetime,
    foreign key(teacher_id) references `teacher`(id),
    foreign key(user_number) references `user`(number),
    index(teacher_id),
    index(user_number)
    );
create table if not exists `process` ( /*开题答辩/期中检查/毕业答辩/演示四个过程分值比例*/
    id char(19) primary key ,
    process_name varchar(50) not null,
    items json null comment '[{"name","number","score","detail"}]',/*打分项目，编号，分值比例，细节描述*/
    insert_time datetime not null default current_timestamp,
    update_time datetime not null default current_timestamp on update current_timestamp
    );
create table if not exists `process_score` ( /*过程得分情况*/
    id char(19) primary key,
    student_id char(19) not null,
    process_id char(19) not null ,
    detail json null comment '{"teacherName","score",detail:[{"number","point"}]}', /*打分老师，得分细节*/
    insert_time datetime not null default current_timestamp,
    update_time datetime not null default current_timestamp on update current_timestamp,
    foreign key(process_id) references `process`(id),
    foreign key(student_id) references `student`(id),
    index(student_id),
    index(process_id)
    );
create table if not exists `file` (
    id char(19) primary key ,
    student_number char(19) not null,
    detail varchar(100) not null, /*开题或毕设报告*/
    process_id char(19) not null ,/*表明属于哪个过程*/
    insert_time datetime not null default current_timestamp,
    update_time datetime not null default current_timestamp on update current_timestamp,
    index(student_number),
    index(process_id)
    );
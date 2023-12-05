use `project`;

explain (
select * from user u , student s where u.number=s.user_number
                                   and u.role=0 and u.number='2021213145'
    );
explain (
select * from user u where u.number in (
    select s.user_number from student s where
            s.user_number='2021213145'
)
    );
explain (
    select * from user u where u.number='0000000000'
);
explain (
            select * from user u where u.number=0000000000
        );
explain (
    select * from process p where p.student_detail->>'$.number' in
                            (select s.user_number from student s where s.user_number='2021213145')
        );/*命中索引*/
explain (
    select * from student s, process p where p.student_detail->>'$.number'=s.user_number and
                                             p.student_detail->>'$.number'='2021213145'
        );/*命中索引*/
explain (
    select t.* from student s , teacher t where s.user_number='2021213145' and
                                              s.teacher_id=t.id
        );/*命中索引*/
explain (
    select p.process_name, p.items->>'$[0]' from process p where p.student_detail->>'$.number'='2021213145'
        );
select p.process_name, p.items->>'$[0].name' from process p where p.student_detail->>'$.number'='2021213145';
explain (
    select password from user where number='2021213145'
        );
select s.s_name,s.user_number,s.group_id from student s where s.teacher_id='1158269468159320064' lock in share mode ;
CREATE INDEX idx_group_id ON teacher(group_id);
explain (
            select distinct group_id from teacher
        );
explain(
    select s.s_name, s.user_number as s_number, s.group_id, t.t_name, t.user_number as t_number from
             teacher t, student s where s.id=1168482466475876400
             and s.teacher_id=t.id
);
update student s set s.teacher_id=1168482466475876352, s.group_id=1 where s.user_number=2021213145;
select s.s_name, s.user_number as s_number, s.group_id, t.t_name, t.user_number as t_number from
             teacher t, student s where s.user_number=2021213145
             and s.teacher_id=t.id;
explain(
           select s.user_number from student s,user u where u.id=1168561854001258496 and u.number=s.user_number
       );
update teacher t set t.left_select=t.left_select-1 where t.id=1168482466475876352 and t.left_select>0;
select distinct count(group_id) from teacher
explain(
           select f.detail from file f where f.student_number=2021213145 and f.process_id=532129
       );
explain (
            select t.id from user u ,teacher t where u.id=1168482466324881408 and u.number=t.user_number
         ); # 使用索引了
explain (
            select p.items from process p where p.id=1169839316334116864
        );
select p.id, p.process_name from process p;
select * from process p;
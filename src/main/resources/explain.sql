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

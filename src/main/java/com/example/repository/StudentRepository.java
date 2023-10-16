package com.example.repository;

import com.example.dto.StudentDTO;
import com.example.pojo.Student;
import com.example.pojo.Teacher;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<Student, String> {
    @Query("select * from teacher t where t.left_select > 0")
    List<Teacher> getInfo();
    @Query("select s.s_name, s.user_number as s_number, s.group_id, t.t_name, t.user_number as t_number from" +
            " teacher t, student s where s.id=:id" +
            " and s.teacher_id=t.id")
    StudentDTO getStudent(@Param("id") String id);
    @Modifying
    @Query("update teacher t set t.left_seloect=:t.left_select-1 where t.id=:tid and t.left_select>0")
    Integer updateTeacher(@Param("tid") String tid);
    @Modifying
    @Query("update student s set s.teacher_id=:tid, s.group_id=:group_id where s.id=:uid")
    Integer updateStudent(@Param("uid") String uid,
                          @Param("tid") String tid,
                          @Param("group_id") Integer groupId);
    @Query("select t.group_id from teacher t where t.id=:tid")
    Integer getGroupId(@Param("tid") String tid);
    @Query("select distinct group_id from teacher")
    Integer getCountGroupId();
    @Modifying
    @Query("update student s set s.group_id=:groupId where s.user_number=:number")
    Integer updateGroup(@Param("groupId") Integer groupId,@Param("number") String number);
}

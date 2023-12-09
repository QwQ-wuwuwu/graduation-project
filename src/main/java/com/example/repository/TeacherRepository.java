package com.example.repository;

import com.example.dox.Process;
import com.example.dto.StudentDTO;
import com.example.dox.Student;
import com.example.dox.Teacher;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, String> {
    @Query("select s.id, s.s_name,s.user_number,s.group_id,s.project_title from student s where s.teacher_id=:id")
    List<Student> students1(@Param("id") String id);
    @Query("select s.s_name,s.user_number,s.group_id from student s where s.teacher_id is null")
    List<Student> students2();
    @Query(value = "select s.s_name,s.user_number as s_number,s.group_id, t.t_name,t.user_number as t_number from " +
            "teacher t,student s where s.teacher_id is not null " +
            "and s.teacher_id=t.id")
    List<StudentDTO> students3();
    @Query("select f.detail from file f where f.student_number=:number and f.process_id=:pid")
    String getFileDetail(@Param("number") String number,@Param("pid") String pid);
    @Query("select t.id from user u ,teacher t where u.id=:uid and u.number=t.user_number")
    String getTeacherId(@Param("uid") String uid);
    @Query("select t.t_name from user u, teacher t where u.id=:uid and u.number=t.user_number")
    String getTeacherName(@Param("uid") String uid);
    @Query("select p.id, p.process_name,p.items from process p")
    List<Process> getProcesses();
    @Query("select s.user_number,s.s_name,s.project_title from student s where s.group_id=:groupId")
    List<Student> getStudentByGroup(@Param("groupId") int groupId);
    @Query("select t.group_id from user u, teacher t where u.id=:uid and u.number=t.user_number")
    int teacherGroupId(@Param("uid") String uid);
    @Query("select p.process_name,p.items from process p where p.id=:pid")
    Process getProcessById(@Param("pid") String pid);
}

package com.example.repository;

import com.example.dox.ProcessScore;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessScoreRepository extends CrudRepository<ProcessScore, String> {
    @Query("select ps.id as id, " +
            "ps.student_id as student_id, " +
            "ps.process_id as process_id, " +
            "ps.detail as detail from process_score ps, student s " +
            "where ps.process_id=:pid and ps.student_id=:s.id and " +
            "s.group_id=:groupId")
    List<ProcessScore> getProcessScores(@Param("groupId") Integer groupId,@Param("pid") String pid);
    @Query("select ps.id as id," +
            "ps.student_id as student_id," +
            "ps.process_id as process_id," +
            "ps.detail as detail " +
            "from student s, process_score ps " +
            "where ps.process_id=:pid and s.id=:ps.student_id and s.teacher_id=:tid")
    List<ProcessScore> getProcessScores(@Param("tid") String tid,@Param("pid") String pid);
    @Modifying
    @Query("update process_score ps set ps.detail=:detali where ps.id=:psid")
    Integer updateProcessScore(@Param("psid") String psid,@Param("detail") String detail);
}

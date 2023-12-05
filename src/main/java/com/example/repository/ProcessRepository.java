package com.example.repository;

import com.example.dox.Process;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProcessRepository extends CrudRepository<Process, Long> {
    @Modifying
    @Query("insert into `process` (id,s_name,s_number,process_name,items,teacher_id) values " +
            "(:id , :sName, :sNumber, :processName, :items, :teacherId)")
    void saveProcessJson(@Param("id") String id, @Param("processName") String processName, @Param("sName") String sName,
                         @Param("sNumber") String sNumber, @Param("teacherId") String teacherId,
                         @Param("items") String items);
    @Query("select * from process p where p.student_detail->>'$.number'=:number")
    List<Process> getProcess(@Param("number") String number);
    @Query("select * from process p")
    List<Process> getAllProcess();
}

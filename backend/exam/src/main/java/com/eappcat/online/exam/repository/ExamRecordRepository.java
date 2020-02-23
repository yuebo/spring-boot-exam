package com.eappcat.online.exam.repository;

import com.eappcat.online.exam.entity.ExamRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExamRecordRepository extends JpaRepository<ExamRecord, String> {
    /**
     * 获取指定用户参加过的所有考试
     *
     * @param userId 用户id
     * @return 用户参加过的所有考试
     */
    List<ExamRecord> findByExamJoinerId(String userId);

    @Query("select r from ExamRecord r where r.examJoinerId=?1 and r.examId=?2 and r.examJoinScore<0")
    ExamRecord findByExamJoinerIdAndExamId(String userId,String examId);
}

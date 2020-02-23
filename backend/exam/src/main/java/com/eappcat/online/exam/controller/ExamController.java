package com.eappcat.online.exam.controller;

import com.eappcat.online.exam.entity.ExamRecord;
import com.eappcat.online.exam.service.ExamService;
import com.eappcat.online.exam.entity.Exam;
import com.eappcat.online.exam.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@Api(tags = "Exam APIs")
@RequestMapping("/api/exam")
public class ExamController {
    @Autowired
    private ExamService examService;

    @GetMapping("/question/list")
    @ApiOperation("获取问题的列表")
    ResultVO<QuestionPageVo> getQuestionList(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        ResultVO<QuestionPageVo> resultVO;
        try {
            QuestionPageVo questionPageVo = examService.getQuestionList(pageNo, pageSize);
            resultVO = new ResultVO<>(0, "获取问题列表成功", questionPageVo);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "获取问题列表失败", null);
        }
        return resultVO;
    }

    @PostMapping("/question/update")
    @ApiOperation("更新问题")
    ResultVO<String> questionUpdate(@RequestBody QuestionVo questionVo) {
        // 完成问题的更新
        System.out.println(questionVo);
        try {
            examService.updateQuestion(questionVo);
            return new ResultVO<>(0, "更新问题成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO<>(-1, "更新问题失败", null);
        }
    }

    @DeleteMapping("/question/delete/{id}")
    @ApiOperation("更新问题")
    ResultVO<String> questionUpdate(@PathVariable String id) {
        try {
            examService.deleteQuestion(id);
            return new ResultVO<>(0, "删除问题成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO<>(-1, "删除问题失败", null);
        }
    }

    @PostMapping("/question/create")
    @ApiOperation("创建问题")
    ResultVO<String> questionCreate(@RequestBody QuestionCreateSimplifyVo questionCreateSimplifyVo, HttpServletRequest request) {
        QuestionCreateVo questionCreateVo = new QuestionCreateVo();
        // 把能拷贝过来的属性都拷贝过来
        BeanUtils.copyProperties(questionCreateSimplifyVo, questionCreateVo);
        // 设置创建者信息
        String userId = (String) request.getAttribute("user_id");
        questionCreateVo.setQuestionCreatorId(userId);
        System.out.println(questionCreateVo);
        try {
            examService.questionCreate(questionCreateVo);
            return new ResultVO<>(0, "问题创建成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO<>(-1, "创建问题失败", null);
        }
    }

    @GetMapping("/question/selection")
    @ApiOperation("获取问题分类的相关选项")
    ResultVO<QuestionSelectionVo> getSelections() {
        QuestionSelectionVo questionSelectionVo = examService.getSelections();
        if (questionSelectionVo != null) {
            return new ResultVO<>(0, "获取问题分类选项成功", questionSelectionVo);
        } else {
            return new ResultVO<>(-1, "获取问题分类选项失败", null);
        }
    }

    @GetMapping("/question/detail/{id}")
    @ApiOperation("根据问题的id获取问题的详细信息")
    ResultVO<QuestionDetailVo> getQuestionDetail(@PathVariable String id) {
        //  根据问题id获取问题的详细信息
        System.out.println(id);
        ResultVO<QuestionDetailVo> resultVO;
        try {
            QuestionDetailVo questionDetailVo = examService.getQuestionDetail(id);
            resultVO = new ResultVO<>(0, "获取问题详情成功", questionDetailVo);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "获取问题详情失败", null);
        }
        return resultVO;
    }

    @GetMapping("/list")
    @ApiOperation("获取考试列表")
    ResultVO<ExamPageVo> getExamList(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        // 需要拼接前端需要的考试列表对象
        ResultVO<ExamPageVo> resultVO;
        try {
            ExamPageVo examPageVo = examService.getExamList(pageNo, pageSize);
            resultVO = new ResultVO<>(0, "获取考试列表成功", examPageVo);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "获取考试列表失败", null);
        }
        return resultVO;
    }

    @GetMapping("/question/type/list")
    @ApiOperation("获取问题列表，按照单选、多选和判断题分类返回")
    ResultVO<ExamQuestionTypeVo> getExamQuestionTypeList() {
        // 获取问题的分类列表
        ResultVO<ExamQuestionTypeVo> resultVO;
        try {
            ExamQuestionTypeVo examQuestionTypeVo = examService.getExamQuestionType();
            resultVO = new ResultVO<>(0, "获取问题列表成功", examQuestionTypeVo);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "获取问题列表失败", null);
        }
        return resultVO;
    }

    @PostMapping("/create")
    @ApiOperation("创建考试")
    ResultVO<Exam> createExam(@RequestBody ExamCreateVo examCreateVo, HttpServletRequest request) {
        // 从前端传参数过来，在这里完成考试的入库
        ResultVO<Exam> resultVO;
        String userId = (String) request.getAttribute("user_id");
        try {
            Exam exam = examService.create(examCreateVo, userId);
            resultVO = new ResultVO<>(0, "创建考试成功", exam);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "创建考试失败", null);
        }
        return resultVO;
    }

    @GetMapping("/card/list")
    @ApiOperation("获取考试列表，适配前端卡片列表")
    ResultVO<List<ExamCardVo>> getExamCardList() {
        // 获取考试列表卡片
        ResultVO<List<ExamCardVo>> resultVO;
        try {
            List<ExamCardVo> examCardVoList = examService.getExamCardList();
            resultVO = new ResultVO<>(0, "获取考试列表卡片成功", examCardVoList);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "获取考试列表卡片失败", null);
        }
        return resultVO;
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("根据考试的id，获取考试详情")
    ResultVO<ExamDetailVo> getExamDetail(@PathVariable String id) {
        // 根据id获取考试详情
        ResultVO<ExamDetailVo> resultVO;
        try {
            ExamDetailVo examDetail = examService.getExamDetail(id);
            resultVO = new ResultVO<>(0, "获取考试详情成功", examDetail);
        } catch (Exception e) {
            resultVO = new ResultVO<>(-1, "获取考试详情失败", null);
        }
        return resultVO;
    }

    @GetMapping("/getLastExamRecord/{examId}")
    ResultVO<ExamRecord> getLastExamRecord(@PathVariable String examId, HttpServletRequest request){
        String userId = (String) request.getAttribute("user_id");
        ExamRecord record=examService.getLastExamRecord(userId,examId);
        ResultVO<ExamRecord> resultVO=new ResultVO<>(0, "获取上次考试成功", record);
        return resultVO;
    }

    @PostMapping("/start/{examId}")
    ResultVO<ExamRecord> startExam(@PathVariable String examId,HttpServletRequest request){
        String userId = (String) request.getAttribute("user_id");
        ExamRecord record=examService.startExam(userId,examId);
        ResultVO<ExamRecord> resultVO=new ResultVO<>(0, "考试开始成功", record);
        return resultVO;
    }
    @PostMapping("/saveRecord/{examRecordId}")
    @ApiOperation("根据用户提交的答案对指定id的考试判分")
    ResultVO<String> saveRecord(@PathVariable String examRecordId, @RequestBody HashMap<String, List<String>> answersMap, HttpServletRequest request) {
        ResultVO<String> resultVO;
        try {
            // 拦截器里设置上的用户id
            String userId = (String) request.getAttribute("user_id");
            // 下面根据用户提交的信息进行判分,返回用户的得分情况
            examService.saveExamRecord(userId, examRecordId, answersMap);
            resultVO = new ResultVO<>(0, "保存成功","");
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "保存失败", "");
        }
        return resultVO;
    }


    @PostMapping("/finish/{examRecordId}")
    @ApiOperation("根据用户提交的答案对指定id的考试判分")
    ResultVO<ExamRecord> finishExam(@PathVariable String examRecordId, @RequestBody HashMap<String, List<String>> answersMap, HttpServletRequest request) {
        ResultVO<ExamRecord> resultVO;
        try {
            // 拦截器里设置上的用户id
            String userId = (String) request.getAttribute("user_id");
            // 下面根据用户提交的信息进行判分,返回用户的得分情况
            ExamRecord examRecord = examService.judge(userId, examRecordId, answersMap);
            resultVO = new ResultVO<>(0, "考卷提交成功", examRecord);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "考卷提交失败", null);
        }
        return resultVO;
    }

    @GetMapping("/record/list")
    @ApiOperation("获取当前用户的考试记录")
    ResultVO<List<ExamRecordVo>> getExamRecordList(HttpServletRequest request) {
        ResultVO<List<ExamRecordVo>> resultVO;
        try {
            // 拦截器里设置上的用户id
            String userId = (String) request.getAttribute("user_id");
            // 下面根据用户账号拿到他(她所有的考试信息)，注意要用VO封装下
            List<ExamRecordVo> examRecordVoList = examService.getExamRecordList(userId);
            resultVO = new ResultVO<>(0, "获取考试记录成功", examRecordVoList);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "获取考试记录失败", null);
        }
        return resultVO;
    }

    @GetMapping("/record/detail/{recordId}")
    @ApiOperation("根据考试记录id获取考试记录详情")
    ResultVO<RecordDetailVo> getExamRecordDetail(@PathVariable String recordId) {
        ResultVO<RecordDetailVo> resultVO;
        try {
            RecordDetailVo recordDetailVo = examService.getRecordDetail(recordId);
            resultVO = new ResultVO<>(0, "获取考试记录详情成功", recordDetailVo);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO<>(-1, "获取考试记录详情失败", null);
        }
        return resultVO;
    }
}

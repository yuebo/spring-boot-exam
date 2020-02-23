package com.eappcat.online.exam.listener;


import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.eappcat.online.exam.dto.ExamUploadDTO;
import com.eappcat.online.exam.entity.Exam;
import com.eappcat.online.exam.entity.Question;
import com.eappcat.online.exam.entity.QuestionOption;
import com.eappcat.online.exam.repository.ExamRepository;
import com.eappcat.online.exam.repository.QuestionOptionRepository;
import com.eappcat.online.exam.repository.QuestionRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ExamUploadListener extends AnalysisEventListener<ExamUploadDTO> {
    private final List<String> levels= Lists.newArrayList("易","中","难");
    private final List<String> types= Lists.newArrayList("单选题","多选题","判断题");
    private final List<String> categories= Lists.newArrayList("其他","医疗","教育","金融");

    private List<String> radios = new ArrayList<String>();
    private List<String> checks = new ArrayList<String>();
    private List<String> judges = new ArrayList<String>();

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionOptionRepository questionOptionRepository;

    @Autowired
    private ExamRepository examRepository;

    @Override
    public void invoke(ExamUploadDTO data, AnalysisContext context) {
        String userId = (String) request.getAttribute("user_id");

        Question question=new Question();
        question.setQuestionName(data.getQuestionName());
        question.setCreateTime(new Date());
        question.setQuestionCreatorId(userId);
        question.setUpdateTime(new Date());
        question.setQuestionId(IdUtil.simpleUUID());
        question.setQuestionLevelId(levels.indexOf(data.getQuestionLevel())+1);
        question.setQuestionScore(5);
        question.setQuestionTypeId(types.indexOf(data.getQuestionType())+1);

        List<String> options=new ArrayList<>();
        List<String> answerOptions=new ArrayList<>();
        if (!StringUtils.isEmpty(data.getOptionA())){
            QuestionOption questionOption=new QuestionOption();
            questionOption.setQuestionOptionId(IdUtil.simpleUUID());
            questionOption.setQuestionOptionContent(data.getOptionA());
            questionOptionRepository.save(questionOption);
            options.add(questionOption.getQuestionOptionId());
            if(data.getQuestionAnswer().contains("A")){
                answerOptions.add(questionOption.getQuestionOptionId());
            }
        }
        if (!StringUtils.isEmpty(data.getOptionB())){
            QuestionOption questionOption=new QuestionOption();
            questionOption.setQuestionOptionId(IdUtil.simpleUUID());
            questionOption.setQuestionOptionContent(data.getOptionB());
            questionOptionRepository.save(questionOption);
            options.add(questionOption.getQuestionOptionId());
            if(data.getQuestionAnswer().contains("B")){
                answerOptions.add(questionOption.getQuestionOptionId());
            }
        }
        if (!StringUtils.isEmpty(data.getOptionC())){
            QuestionOption questionOption=new QuestionOption();
            questionOption.setQuestionOptionId(IdUtil.simpleUUID());
            questionOption.setQuestionOptionContent(data.getOptionC());
            questionOptionRepository.save(questionOption);
            options.add(questionOption.getQuestionOptionId());
            if(data.getQuestionAnswer().contains("C")){
                answerOptions.add(questionOption.getQuestionOptionId());
            }
        }
        if (!StringUtils.isEmpty(data.getOptionD())){
            QuestionOption questionOption=new QuestionOption();
            questionOption.setQuestionOptionId(IdUtil.simpleUUID());
            questionOption.setQuestionOptionContent(data.getOptionD());
            questionOptionRepository.save(questionOption);
            options.add(questionOption.getQuestionOptionId());
            if(data.getQuestionAnswer().contains("D")){
                answerOptions.add(questionOption.getQuestionOptionId());
            }
        }
        question.setQuestionOptionIds(String.join("-",options));
        question.setQuestionAnswerOptionIds(String.join("-",answerOptions));
        question.setQuestionCategoryId(categories.indexOf(data.getQuestionCategory())+1);
        questionRepository.save(question);
        switch (question.getQuestionTypeId()){
            case 1:
                radios.add(question.getQuestionId());
                break;
            case 2:
                checks.add(question.getQuestionId());
                break;
            case 3:
                judges.add(question.getQuestionId());
                break;
            default:
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        String userId = (String) request.getAttribute("user_id");

        Exam exam=new Exam();
        exam.setExamName(context.readSheetHolder().getSheetName());
        exam.setCreateTime(new Date());
        exam.setExamCreatorId(userId);
        exam.setExamStartDate(new Date());
        exam.setExamEndDate(new Date());
        exam.setExamId(IdUtil.simpleUUID());
        exam.setExamScore(5);
        exam.setExamScoreRadio(5);
        exam.setExamScoreCheck(5);
        exam.setExamScoreJudge(5);
        exam.setExamQuestionIdsRadio(String.join("-",radios));
        exam.setExamQuestionIdsCheck(String.join("-",checks));
        exam.setExamQuestionIdsJudge(String.join("-",judges));
        exam.setExamQuestionIds(exam.getExamQuestionIdsRadio() + "-" + exam.getExamQuestionIdsCheck() + "-" + exam.getExamQuestionIdsJudge());
        exam.setExamTimeLimit(90);
        exam.setExamAvatar("https://i.loli.net/2019/11/02/sVrTotyQUXEx6ic.jpg");
        examRepository.save(exam);
    }
}

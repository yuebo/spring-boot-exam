package com.eappcat.online.exam.vo;

import com.eappcat.online.exam.entity.QuestionCategory;
import com.eappcat.online.exam.entity.QuestionLevel;
import com.eappcat.online.exam.entity.QuestionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuestionSelectionVo {
    @JsonProperty("types")
    private List<QuestionType> questionTypeList;

    @JsonProperty("categories")
    private List<QuestionCategory> questionCategoryList;

    @JsonProperty("levels")
    private List<QuestionLevel> questionLevelList;
}

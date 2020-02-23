package com.eappcat.online.exam.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExamUploadDTO {
    @ExcelProperty("题干")
    private String questionName;
    @ExcelProperty("解析")
    private String questionDescription;
    @ExcelProperty("题目类型")
    private String questionType;
    @ExcelProperty("难度")
    private String questionLevel;
    @ExcelProperty("答案")
    private String questionAnswer;
    @ExcelProperty("选项A")
    private String optionA;
    @ExcelProperty("选项B")
    private String optionB;
    @ExcelProperty("选项C")
    private String optionC;
    @ExcelProperty("选项D")
    private String optionD;
    @ExcelProperty("分类")
    private String questionCategory;

}

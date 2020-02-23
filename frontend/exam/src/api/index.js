const api = {
  // get my info
  UserInfo: '/user/info',
  // 下面是自己的用户认证的接口
  UserRegister: '/user/register',
  UserLogin: '/user/login',
  SystemReset: '/user/reset',

  // 考试的接口
  ExamQuestionList: '/exam/question/list',
  ExamQuestionUpdate: '/exam/question/update',
  ExamQuestionDelete: '/exam/question/delete/',
  ExamQuestionSelection: '/exam/question/selection',
  ExamQuestionCreate: '/exam/question/create',
  ExamList: '/exam/list',
  // 获取问题列表，按照单选、多选和判断进行分类
  ExamQuestionTypeList: '/exam/question/type/list',
  ExamCreate: '/exam/create',
  ExamCardList: '/exam/card/list',
  // 获取考试详情
  ExamDetail: '/exam/detail/',
  LastExamRecord: '/exam/getLastExamRecord/',
  // 获取考试详情
  QuestionDetail: '/exam/question/detail/',
  // 交卷
  FinishExam: '/exam/finish/',
  StartExam: '/exam/start/',
  SaveExamRecord: '/exam/saveRecord/',
  ExamRecordList: '/exam/record/list',
  recordDetail: '/exam/record/detail/'
}
export default api

<template>
  <a-modal
    title="创建考试"
    :width="640"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <a-steps :current="currentStep" :style="{ marginBottom: '28px' }" size="small">
        <a-step title="考试描述"/>
        <a-step title="每题分数"/>
        <a-step title="选择题目"/>
      </a-steps>
      <a-form :form="form">
        <!-- step1 -->
        <div v-show="currentStep === 0">
          <a-form-item
            label="考试名称"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
          >
            <a-input v-decorator="['name', {rules: [{required: true}]}]"/>
          </a-form-item>
          <a-form-item
            label="考试限时"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
          >
            <a-input-number
              :min="1"
              :max="200"
              v-decorator="['elapse', {initialValue: '90',rules: [{required: true}]}]"
            />
            分钟
          </a-form-item>
          <a-form-item
            label="考试简述"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
          >
            <a-textarea :rows="2" v-decorator="['desc', {rules: [{required: true}]}]"></a-textarea>
          </a-form-item>
          <a-form-item
            label="考试小图"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
          >
            <a-textarea :rows="2" v-decorator="['avatar', {rules: [{required: true}]}]"></a-textarea>
          </a-form-item>
        </div>
        <div v-show="currentStep === 1">
          <a-form-item
            label="单选题"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
          >
            <a-input-number
              :min="1"
              :max="20"
              v-decorator="['radioScore', {initialValue: '5',rules: [{required: true}]}]"
            />
            分
          </a-form-item>

          <a-form-item
            label="多选题"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
          >
            <a-input-number
              :min="1"
              :max="20"
              v-decorator="['checkScore', {initialValue: '5',rules: [{required: true}]}]"
            />
            分
          </a-form-item>

          <a-form-item
            label="判断题"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
          >
            <a-input-number
              :min="1"
              :max="20"
              v-decorator="['judgeScore', {initialValue: '5',rules: [{required: true}]}]"
            />
            分
          </a-form-item>
        </div>

        <div v-show="currentStep === 2">
          <a-form-item
            label="选择单选题"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            enterButton="Search"
          >
            <!-- 单选 -->
            <a-select
              mode="multiple"
              :size="size"
              placeholder="请选择单选题"
              style="width: 100%"
              @popupScroll="popupScroll"
              @change="handleRadioChange"
              v-decorator="['radios']"
              optionFilterProp="children"
            >
              <a-select-option v-for="radio in radios" :value="radio.id" :key="radio.id">
                {{ radio.name }}
              </a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item
            label="选择多选题"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            enterButton="Search"
          >
            <!-- 单选 -->
            <a-select
              mode="multiple"
              :size="size"
              placeholder="请选择多选题"
              style="width: 100%"
              @popupScroll="popupScroll"
              @change="handleCheckChange"
              v-decorator="['checks']"
              optionFilterProp="children"
            >
              <a-select-option v-for="check in checks" :value="check.id" :key="check.id">
                {{ check.name }}
              </a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item
            label="选择判断题"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            enterButton="Search"
          >
            <!-- 单选 -->
            <a-select
              mode="multiple"
              :size="size"
              placeholder="请选择判断题"
              style="width: 100%"
              @popupScroll="popupScroll"
              @change="handleJudgeChange"
              v-decorator="['judges']"
              optionFilterProp="children"
            >
              <a-select-option v-for="judge in judges" :value="judge.id" :key="judge.id">
                {{ judge.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </div>
      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button key="back" @click="backward" v-if="currentStep > 0" :style="{ float: 'left' }">上一步</a-button>
      <a-button key="cancel" @click="handleCancel">取消</a-button>
      <a-button key="forward" :loading="confirmLoading" type="primary" @click="handleNext(currentStep)">
        {{ currentStep === 2 && '完成' || '下一步' }}
      </a-button>
    </template>
  </a-modal>
</template>

<script>
// import pick from 'lodash.pick'
import { getExamQuestionTypeList, examCreate } from '../../../api/exam'

const stepForms = [
  ['name', 'elapse', 'desc', 'avatar'],
  ['radioScore', 'checkScore', 'judgeScore'],
  ['option']
]

export default {
  name: 'StepByStepExamModal',
  data () {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 7 }
      },
      size: 'default',
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 13 }
      },
      visible: false,
      confirmLoading: false,
      currentStep: 0,
      mdl: {},

      form: this.$form.createForm(this),
      // 单选题对象列表
      radios: [],
      // 多选题对象列表
      checks: [],
      // 判断题对象列表
      judges: [],
      id: ''
    }
  },
  methods: {
    edit (record) {
      this.currentStep = 0
      this.create(record)
    },
    create (record) {
      this.visible = true
      this.form.resetFields()
      // 从后端数据获取单选题、多选题和判断题的列表
      getExamQuestionTypeList().then(res => {
        console.log(res)
        if (res.code === 0) {
          console.log(res.data)
          this.radios = res.data.radios
          this.checks = res.data.checks
          this.judges = res.data.judges
          if (record){
            this.$nextTick(() => {
              this.form.setFieldsValue({
                name: record.name,
                elapse: record.elapse,
                desc: record.desc,
                avatar: record.avatar,
                radioScore: record.radioScore,
                checkScore: record.checkScore,
                judgeScore: record.judgeScore,
                radios: record.radios.map(data => data.id),
                checks: record.checks.map(data => data.id),
                judges: record.judges.map(data => data.id)
              })
              for (let i = 0; i < this.radios.length; i++) {
                const values = record.radios.map(data => data.id)
                if (values.indexOf(this.radios[i].id) >= 0) {
                  this.radios[i].checked = true
                }
              }
              for (let i = 0; i < this.checks.length; i++) {
                const values = record.checks.map(data => data.id)
                if (values.indexOf(this.checks[i].id) >= 0) {
                  this.checks[i].checked = true
                }
              }
              for (let i = 0; i < this.judges.length; i++) {
                const values = record.judges.map(data => data.id)
                if (values.indexOf(this.judges[i].id) >= 0) {
                  this.judges[i].checked = true
                }
              }
              this.id = record.id
            })
          }
        } else {
          this.$notification.error({
            message: '获取问题列表失败',
            description: res.msg
          })
        }
      }).catch(err => {
        // 失败就弹出警告消息
        this.$notification.error({
          message: '获取问题列表失败',
          description: err.message
        })
      })
    },
    popupScroll () {
      console.log('popupScroll')
    },
    handleNext (step) {
      // 处理下一步或者完成事件
      const { form: { validateFields } } = this
      const currentStep = step + 1
      if (currentStep <= 2) {
        // stepForms
        validateFields(stepForms[this.currentStep], (errors, values) => {
          if (!errors) {
            this.currentStep = currentStep
          }
        })
        return
      }
      // last step，最后一步，代表完成考试编写，需要点击"完成"创建考试
      this.confirmLoading = true
      validateFields((errors, values) => { // values就是表单中校验的值，后面提交到后端接口时主要就是用这个values
        console.log('提交数据到后端')
        console.log('errors:', errors, 'val:', values)
        // 设置单选题、多选题和判断题的内容，但是提交前需要保证都已经被正确更新了
        values.radios = this.radios
        values.checks = this.checks
        values.judges = this.judges
        this.confirmLoading = false
        if (!errors) {
          // 在这里把创建的考试的内容(存放在values中)提交给后端接口，需要的参数都已经封装成values这个json啦
          console.log('values:', values)
          // 把data中的question属性提交到后端，待写完后端接口.把前端的创建的题型提交到后端
          if (this.id) {
            values.id = this.id
          }
          examCreate(values).then(res => {
            // 成功就跳转到结果页面
            console.log(res)
            if (res.code === 0) {
              this.$notification.success({
                message: '创建成功',
                description: '考试创建成功'
              })
              // 关闭弹出框
              this.visible = false
            }
          }).catch(err => {
            // 失败就弹出警告消息
            this.$notification.error({
              message: '考试创建失败',
              description: err.message
            })
          })
        } else {
          this.confirmLoading = false
        }
      })
    },
    backward () {
      this.currentStep--
    },
    handleCancel () {
      // clear form & currentStep
      this.visible = false
      this.currentStep = 0
    },
    // 改变选择的题目列表,这里需要分单选、多选和判断进行单独更新，下面的代码要针对radios、checks和judges分别适配
    handleRadioChange (values) {
      console.log(values)
      // 更新单选题的信息
      for (let i = 0; i < this.radios.length; i++) { // 遍历所有的题目的选项
        // 取出一个选项的id
        const id = this.radios[i].id
        // 当前问题是否被问题创建者选中
        let checked = false
        for (let j = 0; j < values.length; j++) { // 拿着
          const value = values[j]
          if (id === value) {
            // 说明这个问题被考试创建者选中
            checked = true
            this.radios[i].checked = true
            break // 跳出内部的for循环
          }
        }
        // 这个选项遍历到最后，发现还不是答案(不在答案数组中)，那么就把这个选项的answer属性设置为false
        if (checked === false) {
          this.radios[i].checked = false
        }
      }
    },

    // 更新多选题信息
    handleCheckChange (values) {
      console.log(values)
      // 更新单选题的信息
      for (let i = 0; i < this.checks.length; i++) { // 遍历所有的题目的选项
        // 取出一个选项的id
        const name = this.checks[i].name
        // 当前问题是否被问题创建者选中
        let checked = false
        for (let j = 0; j < values.length; j++) { // 拿着
          const value = values[j]
          if (name === value) {
            // 说明这个问题被考试创建者选中
            checked = true
            this.checks[i].checked = true
            break // 跳出内部的for循环
          }
        }
        // 这个选项遍历到最后，发现还不是答案(不在答案数组中)，那么就把这个选项的answer属性设置为false
        if (checked === false) {
          this.checks[i].checked = false
        }
      }
    },

    // 更新判断题信息
    handleJudgeChange (values) {
      console.log(values)
      // 更新单选题的信息
      for (let i = 0; i < this.judges.length; i++) { // 遍历所有的题目的选项
        // 取出一个选项的id
        const name = this.judges[i].name
        // 当前问题是否被问题创建者选中
        let checked = false
        for (let j = 0; j < values.length; j++) { // 拿着
          const value = values[j]
          if (name === value) {
            // 说明这个问题被考试创建者选中
            checked = true
            this.judges[i].checked = true
            break // 跳出内部的for循环
          }
        }
        // 这个选项遍历到最后，发现还不是答案(不在答案数组中)，那么就把这个选项的answer属性设置为false
        if (checked === false) {
          this.judges[i].checked = false
        }
      }
    }

  }
}
</script>

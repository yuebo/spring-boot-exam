# 在线考试系统

## 编译运行
### 使用maven编译
```yaml
mvn clean install -P prod -Dmaven.test.skip=true
```

### 使用docker编译
```yaml
sh docker/build.sh
```

### 使用helm编译chart
```yaml
helm package exam
```

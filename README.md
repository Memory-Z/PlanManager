# 计划管理App

这是一个使用MVI架构和XML布局的Android计划管理应用。

## 功能特性

- 添加新计划
- 查看计划列表
- 标记计划为完成/未完成
- 删除计划

## 技术架构

- **架构模式**: MVI (Model-View-Intent)
- **布局**: XML
- **异步处理**: RxJava3
- **数据存储**: 内存存储（模拟）

## 项目结构

```
app/src/main/java/com/planmanager/
├── model/          # 数据模型
│   └── Plan.java   # 计划模型类
├── intent/         # 用户意图
│   └── PlanIntent.java  # 计划相关意图
├── state/          # UI状态
│   └── PlanState.java   # 计划状态类
├── repository/     # 数据存储
│   └── PlanRepository.java  # 计划存储库
├── processor/      # 意图处理器
│   └── PlanProcessor.java   # 计划处理器
└── view/           # 视图
    ├── MainActivity.java    # 主活动
    └── PlanAdapter.java     # 计划列表适配器

app/src/main/res/layout/
├── activity_main.xml    # 主活动布局
└── plan_item.xml        # 计划项布局
```

## 安装和运行

1. 克隆项目到本地
2. 使用Android Studio打开项目
3. 同步Gradle依赖
4. 运行项目到模拟器或真机

## 使用说明

1. 在顶部输入框中输入计划标题和描述
2. 点击"添加计划"按钮添加新计划
3. 在计划列表中，点击复选框标记计划为完成/未完成
4. 点击"删除"按钮删除计划

## 技术依赖

- AndroidX
- RxJava3
- RxAndroid

## 注意事项

- 本项目使用内存存储，应用重启后数据会丢失
- 实际应用中应使用持久化存储（如SQLite或Room）

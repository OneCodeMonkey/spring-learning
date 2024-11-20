## Spring 状态机

### 定义类

States： 状态可能值 Events： 导致状态变更，完整流转的所有事件

### 实现类：

StateMachineConfig：extends EnumStateMachineConfigurerAdapter<States, Events>
定义必要的 config(), states(), transition(), listener() 等行为。

### 理解：

1. 貌似这个 StateMachine 类是单例的，试验在不同地方 注入实例，发现 A 类的 stateMachine send event 后，B 类的 stateMachine 再 send event，
   不是从初始状态开始变更。也就是全局在公用一个 state machine。
   

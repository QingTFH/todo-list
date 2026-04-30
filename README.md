## v1

理论上，一个“输入指令，操作数据，存储数据”的程序，它有5个部件： 
1. input 输入器 接受用户输入并处理成需要的指令command 
2. controller 调度器 接受command，调度其他部件完成要求 
3. Dao 数据小助手，面向数据库，作load/save
4. Util 工具箱，存放各种米奇妙妙工具(数据规范化等)
5. DataManager 数据管理器，直接与数据库/存储数据的地方交互，或者它就是存储数据的地方

 启动时：硬盘数据读进内存 Dao → Manager 
 运行时：正常指令流程 Input → Controller → Util / Manager 
 数据改动后：内存落地存硬盘 Manager → Dao
#### 1. 需要哪些功能(面向用户)：
1. 停止程序
2. 增加一条todo
3. 完成一条todo
4. 查询
#### 2. 为实现功能，需要哪些模块(面向程序员)
1. 输入指令
2. 根据指令，完成操作，输出结果
	1. 向todo list增加一个todo
	2. 向finish list增加一个finish
	3. 按某个顺序排列todo list
3. 结束后，存储到文件中
#### 3. 为实现模块，需要哪些对象
1. input, output
2. 调度器controller
3. todo处理器/存储器 todoUtil

## v2_2026-04-30
1. 靠近Linux形式的控制台输入：
	- 指令 \[-选项] 内容 
2. finish 可以不使用"finish -n num" 而直接 "finish num"
3. 思考还需要加入什么功能，
	1. 撤销？
	2. 记录finish数量？
	3. 记录tag？
	4. 记录todoToken的难易程度/重要程度(作四象限优先级: 重要/紧急，线性加权，自动排序)？
	5. 增加关闭钩子以增加健壮性？

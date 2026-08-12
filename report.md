# 优先级排序（重要 × 紧急）落地思路

目标：`query` 的结果不再只按 ddl 排，而是综合"重要度 + 紧急度"排序，优先完成更重要、更临期的事项；`query` 时列出每条事件的 score。

## 一、重要程度：4 级（0~3），只输入这一个维度

- 不用 0~10：虚假精度，跨天输入不一致，且会诱导人反复微调分数而不是去干活。
- 不用 5 级：中间档会成为默认锚点，区分度下降；偶数级迫使向两边靠。
- 关键前提：**紧急度由 ddl 自动推导**（见二），用户只需判断"重要不重要"，4 级足够。
- 定义：`0=不重要，1=一般（默认），2=重要，3=关键`（`Config.IMPORTANCE_MIN/MAX/DEFAULT`）。
- 输入：`add -m ... -i 2`（`-i` / `--importance`）；`edit -n 1 -i 3`；缺省为 1。

## 二、紧急程度：由截止时间自动推导，用反比例函数

```
剩余天数 = 截止时间 − 当前时间（ChronoUnit.DAYS，可为负 = 已超期）
urgency = URGENCY_SCALE / max(剩余天数, 1)      （URGENCY_SCALE = 6）
```

- 反比例：今天到期/超期取满值 6；**剩 1 天 = 6，剩 2 天 = 3，正好差一倍**——符合"1 天 vs 2 天是飞跃"的直觉。
- 超期天数取负数时被 clamp 到 1，紧迫度封顶 6，不再无限增长。
- 调研参考：常见做法是线性衰减（`100 − 天数`）、分段离散分、或调度理论里的指数衰减（ATC）。本项目按需求选用反比例，直观且 1~2 天区分度最强。

## 三、综合权重

```
score = IMPORTANCE_WEIGHT × 重要度 + urgency      （IMPORTANCE_WEIGHT = 3）
排序 = score 降序；score 相同再按 ddl 升序
```

- 重要度范围 0~9，紧迫度范围 0~6，量级可比；权重都放 `Config` 可调。
- 超期/今天到期的任务紧迫度封顶 6，此时排序主要由重要度拉开。

### 效果示例（以"现在 ≈ 08-12 晚"为例，实际按运行时时刻计算）

| 任务 | 重要度 | 剩余天数 | urgency | score |
| --- | --- | --- | --- | --- |
| A | 3 | 2 | 3.0 | 12.0 |
| B | 2 | 1 | 6.0 | 12.0 |
| C | 3 | 4 | 1.5 | 10.5 |
| D | 1 | 超期 | 6.0 | 9.0 |
| E | 0 | 30 | 0.2 | 0.2 |

A、B 并列（3级·2天后 ≈ 2级·1天后）：近临期时 1 天紧迫度抵 1 级重要度，正是反比例带来的效果；远期的 C 靠重要度压过超期但不重要的 D，保持四象限"重要主导"。

## 四、数据模型与落盘

- `TodoToken` 新增字段 `importance`（int，默认 1）。
- 文件格式：`pri:2; ddl:2026-05-20 00:00; content:xxx`。
- **兼容旧数据**：`TodoUtil.parseTodoToken` 读取时若无 `pri:` 前缀，按默认重要度 1 解析；旧 `todo.txt` / `finish.txt` 无需迁移，下次保存自动带上 `pri:`。
- todo 与 finished 两个文件同格式。

## 五、代码改动点

1. `Config`：`IMPORTANCE_MIN/MAX`、`DEFAULT_IMPORTANCE`、`IMPORTANCE_WEIGHT`、`URGENCY_SCALE`。
2. `TodoToken`：加 `importance` 字段，`toString()` 输出 `pri:...`。
3. `TodoUtil` / `TodoTokenFactory`：解析支持可选 `pri:`，工厂支持重要度重载。
4. `TodoManager.priorityScore()`：`score = 3×重要度 + 6/max(剩余天数,1)`；`sort()` 比较器改为 score 降序 + ddl 升序。
5. `AddHandler` / `EditHandler`：新增 `-i` / `--importance` 选项（`Handler.importanceOf` 解析并校验 0~3）。
6. `Command`：别名表加 `"importance"→"i"`。
7. `HelpHandler`：add / edit 帮助补 `-i`。
8. `query`：每条输出 `[score x.x]` 前缀。

## 六、测试要点

1. 旧 `todo.txt`（无 `pri:`）能加载、默认重要度 1、正常排序。
2. score 计算与排序：今天到期 > 1 天 > 2 天（差一倍）；重要度主导远期排序。
3. 超期任务（剩余天数为负）紧迫度封顶，排最前。
4. `add -i 0~3` 与越界（5）/非数字报错；`edit -i` 后立即重排。
5. 长选项 `--importance` / `--message` / `--date` 与短选项等效。
6. `query -f` 已完成列表展示 `pri`，保持完成顺序不参与排序。
7. `finish` 落盘 `finish.txt` 带 `pri`，重启后仍能加载。

## 七、可调参数

- `IMPORTANCE_WEIGHT` 调大 → 重要度更主导；调小 → 紧迫度更主导。
- `URGENCY_SCALE` 调大 → 紧迫度整体抬升（更大范围的临期区分）。
- 想换成指数衰减（ATC 风格）只需替换 `priorityScore` 里的 urgency 表达式。

# ServerSwitch - Velocity 服务器切换插件

<p align="center">
  <strong>🔄 轻量、美观的 Velocity 服务器切换插件</strong>
  <br/>
  支持命令与 GUI 菜单，一键连接任意服务器
</p>

---

## ✨ 特性

- **📋 交互式菜单** - 彩色文本菜单显示所有服务器状态
- **⚡ 快速连接** - 点击服务器名称或使用命令直接连接
- **📊 实时状态** - 显示服务器在线/离线状态、玩家数量、延迟信息
- **🎨 美观界面** - 使用 Adventure API 实现彩色格式化文本
- **🔧 完全可配置** - 所有消息文本均可自定义
- **🚀 轻量高效** - 无数据库依赖，纯内存操作
- **🛡️ 安全权限** - 支持权限控制（可选）

## 📦 安装

### 前置要求
- [Velocity Proxy](https://velocitypowered.com/) 3.4.0 或更高版本
- Java 17 或更高版本

### 安装步骤
1. 下载最新版本的 `server-switch-1.0.0.jar`
2. 将 JAR 文件放入 Velocity 的 `plugins/` 目录
3. 重启 Velocity 服务器
4. 插件会自动生成配置文件

## 🚀 使用方法

### 基本命令
```
/stp - 打开服务器选择菜单
/stp <服务器名称> - 直接连接到指定服务器
```

### 菜单使用
1. 玩家输入 `/stp` 命令
2. 显示所有可用服务器的彩色列表
3. 每个服务器显示：
   - 服务器名称（可点击）
   - 在线状态（绿色在线/红色离线）
   - 当前在线玩家数量
   - 服务器延迟（在线时显示）
4. 点击服务器名称或使用命令直接连接

### 权限节点
- 默认所有玩家都可以使用 `/stp` 命令
- 如需权限控制，可在 Velocity 配置文件中配置

## ⚙️ 配置

### 配置文件位置
```
plugins/server-switch/messages.properties
```

### 默认配置内容
```properties
# ServerSwitch 消息配置文件

# 菜单相关
menu.title=§6§l服务器列表
menu.online=§a在线
menu.offline=§c离线
menu.players=§e玩家: §f
menu.ping=§e延迟: §f
menu.click=§7点击连接
menu.hover=§a点击连接到此服务器

# 消息相关
message.no-servers=§c没有可用的服务器！
message.server-list=§6§l可用服务器:
message.server-info=  §e- §f{server} §7({players} 人在线, 延迟: {ping}ms)
message.connected=§a正在连接到 §f{server}...
message.already-connected=§c你已经在这个服务器上了！
message.server-not-found=§c服务器 §f{server} §c不存在！
message.server-offline=§c服务器 §f{server} §c当前离线！
```

### 配置说明
- 所有消息文本均可自定义
- 支持颜色代码（使用 `§` 符号）
- 占位符：
  - `{server}` - 服务器名称
  - `{players}` - 在线玩家数量
  - `{ping}` - 服务器延迟（ms）

## 🛠️ 开发

### 项目结构
```
src/main/java/com/example/serverswitch/
├── ServerSwitchPlugin.java      # 插件主类
├── config/
│   └── Config.java              # 配置管理器
├── command/
│   └── ServerCommand.java       # 命令处理器
├── menu/
│   └── ServerMenu.java          # 菜单界面
└── listener/                    # 事件监听器（预留）
```

### 构建项目
```bash
# 克隆项目
git clone <repository-url>

# 进入项目目录
cd server-switch

# 编译打包
mvn clean package

# 输出文件
target/server-switch-1.0.0.jar
```

### Maven 依赖
```xml
<dependency>
    <groupId>com.velocitypowered</groupId>
    <artifactId>velocity-api</artifactId>
    <version>3.4.0-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

## 🔌 API 集成

### 获取插件实例
```java
@Inject
private ServerSwitchPlugin serverSwitchPlugin;

// 或通过事件获取
Optional<ServerSwitchPlugin> plugin = proxyServer.getPluginManager()
    .getPlugin("server-switch")
    .map(ServerSwitchPlugin.class::cast);
```

### 打开服务器菜单
```java
ServerMenu menu = new ServerMenu(serverSwitchPlugin, player);
menu.open();
```

### 连接玩家到服务器
```java
plugin.getServer().getServer("lobby").ifPresent(server -> {
    player.createConnectionRequest(server).connect();
});
```

## 📝 自定义开发

### 添加新功能
1. 在 `command` 包中添加新的命令类
2. 在 `menu` 包中添加新的界面类
3. 在 `listener` 包中添加事件监听器
4. 在配置类中添加相应的配置项

### 扩展菜单样式
继承 `ServerMenu` 类并重写 `showServerInfo` 方法：
```java
public class CustomMenu extends ServerMenu {
    @Override
    protected void showServerInfo(RegisteredServer server) {
        // 自定义显示逻辑
    }
}
```

## 🤝 贡献

欢迎贡献代码！请按以下步骤操作：

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

### 贡献指南
- 遵循现有的代码风格
- 添加适当的注释
- 更新相关文档
- 确保向后兼容性

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 支持

### 常见问题
**Q: 菜单不显示怎么办？**  
A: 确保玩家有权限使用 `/stp` 命令，检查 Velocity 日志是否有错误。

**Q: 如何更改命令名称？**  
A: 修改 `ServerSwitchPlugin.java` 中的命令注册部分。

**Q: 可以集成到其他插件吗？**  
A: 可以，通过 API 集成部分提到的方法获取插件实例。

**Q: 支持多语言吗？**  
A: 目前支持通过配置文件自定义消息文本，未来计划支持多语言文件。

### 报告问题
请在 GitHub Issues 中报告问题，包括：
1. Velocity 版本
2. 插件版本
3. 错误日志
4. 复现步骤

---

## 📊 版本历史

### v1.0.0 (当前)
- 初始版本发布
- 支持命令切换服务器
- 交互式文本菜单
- 完全可配置的消息系统
- 实时服务器状态显示

### 计划功能
- [ ] 支持 GUI 库存菜单（Inventory GUI）
- [ ] 添加权限节点控制
- [ ] 多语言支持
- [ ] 服务器分组功能
- [ ] 玩家收藏服务器
- [ ] 热重载配置

---

<p align="center">
  Made with ❤️ for the Velocity community
  <br/>
  <sub>如有问题或建议，欢迎提交 Issue 或 Pull Request</sub>
</p>

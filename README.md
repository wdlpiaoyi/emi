# EMI

简体中文（默认） ｜ [English](README.en.md)

> 给 Minecraft 1.20.1 的物品与配方查看器：搜物品、看配方，配方树能从成品一路点到原材料。

| 项目 | 内容 |
|---|---|
| 游戏版本 | 1.20.1 |
| 加载器 | Forge |
| 模组 ID | `emi` |
| 安装端 | 客户端（服务端装不装都行） |
| 前置 | 无（JEI 可选） |
| 许可证 | MIT |

本仓库是上游 [EMI](https://github.com/emilyploszaj/emi) 的 fork，托管在 [wdlpiaoyi/emi](https://github.com/wdlpiaoyi/emi)，由 wdlpiaoyi 与 deepseek 维护，**暂时只维护 Minecraft 1.20.1 的 Forge 端**。上游代码同时带 Fabric 与 Forge 两端，本 fork 只用 Forge 端，Fabric 端不维护、不保证可构建。展示名仍是 **EMI**，`modId`、包名与注册命名空间仍是 `emi` / `dev.emi`，按 modId 写死兼容的模组与资源包继续可用。

## 特性

- **物品与配方查看器**：在列表里搜物品，点开就能看合成、熔炼等配方。
- **配方树**：从成品一层层展开到原材料，点树上的节点可以继续往下查。
- **搜索**：按名称搜；`@模组id` 只看某个模组，`#标签` 按标签过滤，提示文本（tooltip）也能搜。
- **查看全部配方**：键位可配置，鼠标放到物品上按一下，列出它作为产物的全部配方。
- **JEMI 兼容**：内置 JEMI，直接加载各模组自带的 JEI 插件，整合包里大量模组不用单独写 EMI 适配。
- **配方 API**：模组可以用 `dev.emi.emi.api` 注册自己的配方类型，发布物带 `api` 分类 jar。
- **游戏内设置**：内置配置界面，侧边栏、行为等选项都能在游戏里改。
- **客户端 mod**：装客户端即可，多人游戏时服务端不要求安装。

## 安装

1. 游戏版本 **1.20.1**，加载器 **Forge**（47.x）。
2. 把 Forge 端 jar 放进 `mods/`。
3. 客户端即可；服务端不装也能进服务器。

## 使用

### 打开与搜索

- 打开物品列表的键位在「控制 → 按键绑定」的 EMI 分类里，可以按习惯改。
- 搜索框里 `@modid` 限定模组，`#tag` 限定标签，其余按名称和提示文本匹配。

### 看配方

- 鼠标放在物品上，用配方键位打开配方详情。
- 详情页里可以切换这个物品的配方、用途等视图。

### 配方树

打开任意配方详情后切到「配方树」，能看到这个成品从原材料一步步合成过来的完整树；点树上的节点可以继续展开。

### 查看全部配方

在键位设置里给「查看全部配方」绑定一个按键，鼠标放到物品上按下，会列出它作为产物的全部配方。

## 与上游的关系

- 上游 EMI 按 Minecraft 版本分分支（1.18.2、1.19.x、1.20.x、1.21），持续更新。
- 本 fork 只维护 **1.20.1 + Forge**（`1.20.1` 分支的 Forge 端）；Fabric 端与其它版本分支都是上游副本，不保证能构建，也不接受反馈。
- 上游 1.20 分支的修复会按需同步，本 fork 的改动以「1.20.1 Forge 能跑」为准。

## 开发

### 作为依赖

把 EMI 加进项目需要先加仓库：

```gradle
repositories {
	maven {
		name = "Sleeping Town"
		url = "https://repo.sleeping.town/"
	}
}
```

`emi_version` 形如 `1.1.24+1.20.1`（EMI 版本 + Minecraft 版本）。下面坐标照上游文档列出，本 fork 只维护 Forge 端，Fabric / Architectury 坐标仅作参考：

```gradle
dependencies {
	// Fabric
	modCompileOnly "dev.emi:emi-fabric:${emi_version}:api"
	modLocalRuntime "dev.emi:emi-fabric:${emi_version}"

	// Forge（如果你用 Forge Gradle，再看下面的 mixin refmap 设置）
	compileOnly fg.deobf("dev.emi:emi-forge:${emi_version}:api")
	runtimeOnly fg.deobf("dev.emi:emi-forge:${emi_version}")

	// Architectury
	modCompileOnly "dev.emi:emi-xplat-intermediary:${emi_version}:api"

	// MultiLoader Template / VanillaGradle
	compileOnly "dev.emi:emi-xplat-mojmap:${emi_version}:api"
}
```

Forge Gradle 用户需要在客户端 run 里打开 Mixin refmap：

```gradle
runs {
	client {
		// 加这两行
		property 'mixin.env.remapRefMap', 'true'
		property 'mixin.env.refMapRemappingFile', "${projectDir}/build/createSrgToMcp/output.srg"

		// 其余保持原样
		// ...
	}
}
```

### 构建

需要 **JDK 17**。本 fork 只维护 Forge 端：

```bash
./gradlew :forge:build   # 产物：forge/build/libs/
```

`./gradlew build` 会把 Fabric 端一起构建（上游副本，不维护）。发布构建时设置 `RELEASE=1` 环境变量，版本号会去掉 `-SNAPSHOT`。

## 常见问题

**支持 1.21 / 1.19 / NeoForge / Fabric 吗？**
不支持。本仓库只维护 1.20.1 的 Forge 端；Fabric 端和其它版本分支不在本 fork 的维护范围内。

**和 JEI / REI 能一起装吗？**
一般只装 EMI 就够，JEMI 会复用 JEI 插件；两个查看器同时装会各自显示，按喜好保留一个即可。

**整合包能直接用吗？**
可以。Forge 端把 jar 放进 `mods/` 即可，客户端安装。

**为什么版本号是 `1.1.24+1.20.1`？**
沿用上游版本号规则（EMI 版本 + Minecraft 版本），方便和上游对应。

**上游更新会跟吗？**
按需同步上游 1.20 分支的修复；其它版本不做。

## 许可证

MIT License。上游代码与美术版权归 Emi（emilyploszaj）；本 fork 的改动归 wdlpiaoyi 与 deepseek。

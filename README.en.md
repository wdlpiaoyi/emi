# EMI

[简体中文](README.md) ｜ English

> An item and recipe viewer for Minecraft 1.20.1: search items, browse recipes, and walk a recipe tree from the finished product down to raw materials.

| | |
|---|---|
| Minecraft | 1.20.1 |
| Loader | Fabric / Forge |
| Mod ID | `emi` |
| Sides | Client (server installation optional) |
| Dependencies | Fabric API on Fabric; JEI optional |
| License | MIT |

This repository is a fork of upstream [EMI](https://github.com/emilyploszaj/emi), hosted at [wdlpiaoyi/emi](https://github.com/wdlpiaoyi/emi) and maintained by wdlpiaoyi and deepseek. Only **Minecraft 1.20.1** is maintained. The display name stays **EMI**, and the `modId`, packages and registry namespace stay `emi` / `dev.emi`, so mods and resource packs that hard-code the mod ID keep working.

## Features

- **Item and recipe viewer**: search in a single list, click an item to see its crafting, smelting and other recipes.
- **Recipe tree**: expand a finished product down to raw materials, layer by layer; nodes on the tree can be expanded further.
- **Search**: search by name; `@modid` narrows to one mod, `#tag` filters by tag, and tooltips are searchable too.
- **Display all recipes**: a configurable keybind lists every recipe the hovered item is an output of.
- **JEMI compatibility**: built-in JEMI loads the JEI plugins shipped by mods directly, so most modpacks need no dedicated EMI adapter.
- **Recipe API**: mods can register their own recipe types through `dev.emi.emi.api`; releases include an `api` classifier jar.
- **In-game settings**: an in-game config screen covers the sidebar, behavior and more.
- **Client-side mod**: install it on the client; servers do not need it.

## Installation

1. Minecraft **1.20.1** with **Fabric** or **Forge**.
2. On Fabric, install **Fabric API** as well.
3. Put the jar into `mods/`.
4. Client only — the server does not need it either.

## Usage

### Opening and searching

- The keybind that opens the item list lives in Options → Controls → Key Binds, under the EMI category; rebind it as you like.
- In the search box, `@modid` limits results to one mod, `#tag` filters by tag, and everything else matches names and tooltips.

### Browsing recipes

- Hover an item and press the recipe keybind to open its recipe details.
- The details screen switches between this item's recipes, uses and other views.

### Recipe tree

Open any recipe's details and switch to the recipe tree view to see the full tree from raw materials up to the finished product; click nodes on the tree to expand them further.

### Display all recipes

Bind a key to “Display all recipes” in the key binds screen; hover an item and press it to list every recipe the item is an output of.

## Relation to upstream

- Upstream EMI keeps one branch per Minecraft version (1.18.2, 1.19.x, 1.20.x, 1.21) and keeps updating them.
- This fork only maintains **1.20.1** (the `1.20.1` branch); all other branches are copies from upstream, are not guaranteed to build, and are not supported.
- Fixes from upstream's 1.20 branch are merged as needed; changes here are validated against 1.20.1 only.

## Development

### As a dependency

Add the repository first:

```gradle
repositories {
	maven {
		name = "Sleeping Town"
		url = "https://repo.sleeping.town/"
	}
}
```

`emi_version` looks like `1.1.24+1.20.1` (EMI version + Minecraft version). Pick the dependency for your loader and build system:

```gradle
dependencies {
	// Fabric
	modCompileOnly "dev.emi:emi-fabric:${emi_version}:api"
	modLocalRuntime "dev.emi:emi-fabric:${emi_version}"

	// Forge (see the mixin refmap setup below if you use Forge Gradle)
	compileOnly fg.deobf("dev.emi:emi-forge:${emi_version}:api")
	runtimeOnly fg.deobf("dev.emi:emi-forge:${emi_version}")

	// Architectury
	modCompileOnly "dev.emi:emi-xplat-intermediary:${emi_version}:api"

	// MultiLoader Template / VanillaGradle
	compileOnly "dev.emi:emi-xplat-mojmap:${emi_version}:api"
}
```

Forge Gradle users need to enable Mixin refmaps in the client runs:

```gradle
runs {
	client {
		// Add these two lines
		property 'mixin.env.remapRefMap', 'true'
		property 'mixin.env.refMapRemappingFile', "${projectDir}/build/createSrgToMcp/output.srg"

		// The rest stays as it is
		// ...
	}
}
```

### Building

Requires **JDK 17**:

```bash
./gradlew build   # Output: fabric/build/libs/, forge/build/libs/
```

Set the `RELEASE=1` environment variable for release builds to drop the `-SNAPSHOT` suffix from the version.

## FAQ

**Does it support 1.21 / 1.19 / NeoForge?**
No. This fork only maintains 1.20.1; upstream's other version branches are out of scope here.

**Can it be installed alongside JEI / REI?**
EMI alone is usually enough — JEMI reuses JEI plugins. Installing both viewers makes each render its own screen; keep whichever you prefer.

**Can a modpack ship it?**
Yes. On Fabric include Fabric API, and put the jar on the client.

**Why is the version `1.1.24+1.20.1`?**
It follows upstream's scheme (EMI version + Minecraft version) to stay traceable to upstream.

**Will upstream updates be merged?**
Fixes from upstream's 1.20 branch are merged as needed; other versions are not.

## License

MIT License. Upstream code and art are copyright Emi (emilyploszaj); changes in this fork are copyright wdlpiaoyi and deepseek.

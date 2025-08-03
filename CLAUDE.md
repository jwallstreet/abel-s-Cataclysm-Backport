# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

### Build
```bash
./gradlew build
```

### Run Client
```bash
./gradlew runClient
```

### Run Server
```bash
./gradlew runServer
```

### Run Tests
```bash
./gradlew test
./gradlew check
```

### Clean Build
```bash
./gradlew clean build
```

### Generate Eclipse/IntelliJ Files
```bash
# For Eclipse
./gradlew genEclipseRuns
./gradlew eclipse

# For IntelliJ
./gradlew genIntellijRuns
```

### Refresh Dependencies
```bash
./gradlew --refresh-dependencies
```

## Architecture

This is a Minecraft Forge mod for version 1.19.2 that adds boss battles and dungeon structures. The codebase follows standard Forge mod patterns:

### Core Components

1. **Main Class**: `com.github.L_Ender.cataclysm.Cataclysm` - Entry point, handles mod initialization, network messages, and event registration

2. **Registry Pattern**: All game elements use Forge's deferred registry system in `init/` package:
   - `ModItems` - Items and equipment
   - `ModBlocks` - Blocks and tile entities
   - `ModEntities` - Mobs and bosses
   - `ModStructures` - Dungeon structures
   - `ModSounds`, `ModParticle`, `ModEffect` - Audio/visual effects

3. **Entity System**: Boss monsters extend custom animation frameworks:
   - `LLibrary_Boss_Monster` - Legacy animation system bosses
   - `IABoss_monster` - Internal animation system bosses
   - Boss entities are in `entity/AnimationMonster/BossMonsters/` and `entity/InternalAnimationMonster/IABossMonsters/`

4. **Network Architecture**: Client-server communication through `SimpleChannel` with custom message classes in `message/` package

5. **Configuration**: Mod settings in `config/` package using Forge's config system, with damage caps, cooldowns, and feature toggles

6. **Structure Generation**: NBT structure files in `src/main/resources/data/cataclysm/structures/` loaded by custom structure pieces

### Key Patterns

- Uses Mixin for vanilla modifications (`cataclysm.mixins.json`)
- Capabilities system for entity data (`ModCapabilities`)
- Custom AI goals for complex boss behaviors
- Particle and sound effects tied to boss actions
- Curios API integration for equipment slots
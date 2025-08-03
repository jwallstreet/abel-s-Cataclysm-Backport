# Cataclysm Mod - Claude Analysis & Extension Guide

## Project Overview
**L_Ender's Cataclysm** is a sophisticated Minecraft 1.19.2 Forge mod featuring complex boss battles, advanced animation systems, and extensive gameplay mechanics. This document provides a comprehensive guide for extending the mod with custom mobs, skills, and gameplay features.

## Build & Development Setup

### Requirements
- Java 17 (compatible with Gradle 7.6)
- Minecraft Forge 1.19.2
- IntelliJ IDEA or Eclipse

### Build Commands
```bash
# Set Java 17 for compatibility
export JAVA_HOME="/path/to/java17"

# Build the mod
./gradlew build

# Run development client
./gradlew runClient

# Run development server  
./gradlew runServer

# Generate IDE run configurations
./gradlew genIntellijRuns  # For IntelliJ
./gradlew genEclipseRuns   # For Eclipse
```

## Architecture Overview

### Core Packages Structure
```
src/main/java/com/github/L_Ender/cataclysm/
├── entity/               # All mob implementations
│   ├── AnimationMonster/ # LionFish animated entities  
│   ├── InternalAnimationMonster/ # Internal state entities
│   ├── Deepling/        # Semi-aquatic creatures
│   ├── Pet/             # Tameable companions
│   └── AI/              # Custom AI behaviors
├── items/               # Weapons, armor, tools, accessories
├── capabilities/        # Player skill systems & special abilities
├── init/                # Registration systems for all content
├── config/              # Configuration system
├── event/               # Event handling & game mechanics
├── client/              # Client-side rendering & animations
├── blocks/              # Custom blocks & altars
└── effects/             # Status effects & buffs/debuffs
```

## Extension Points

### 1. Custom Mobs & Entities

#### Base Classes for Extension:
- **`LLibrary_Monster`** - Complex animated entities with LionFish integration
- **`Internal_Animation_Monster`** - Internal state-driven entities with attack patterns  
- **`AbstractDeepling`** - Semi-aquatic mobs with moisture systems
- **`IABoss_monster`** - Multi-phase boss encounters
- **`InternalAnimationPet`** - Tameable companion entities

#### Animation System:
```java
public static final Animation CUSTOM_ATTACK = Animation.create(40);
public AnimationState attackAnimationState = new AnimationState();

// Frame-perfect timing in tick()
if (this.getAnimation() == CUSTOM_ATTACK && this.getAnimationTick() == 15) {
    // Trigger damage/effects at specific frame
}
```

#### AI Goals System:
```java
this.goalSelector.addGoal(1, new InternalAttackGoal(this, 0, 1, 0, 50, 15, 3.6F) {
    @Override
    public boolean canUse() {
        return super.canUse() && this.entity.getRandom().nextFloat() * 100.0F < 12f;
    }
});
```

### 2. Items & Weapons

#### Weapon Implementation Pattern:
```java
public class CustomWeapon extends Item implements More_Tool_Attribute, ILeftClick {
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }
    
    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        // Charging attack implementation
    }
}
```

#### Material Tiers:
- **Tools**: `Tooltier.java` - Ancient Metal, Black Steel, Monstrosity
- **Armor**: `Armortier.java` - Ignitium, Cursium, Bone Reptile

### 3. Player Capabilities System

#### Existing Capabilities:
- **ChargeCapability** - Charging attacks and collision detection
- **RenderRushCapability** - Dash/rush movement abilities  
- **ParryCapability** - Timed defensive mechanics
- **HookCapability** - Grappling hook mechanics
- **TidalTentacleCapability** - Tentacle attack systems

#### Creating Custom Capabilities:
```java
public interface ICustomCapability {
    int getCustomValue();
    void setCustomValue(int value);
    void tick();
}

// Register in ModCapabilities.java and attach to entities
```

### 4. Registration System

All content uses Forge's DeferredRegister pattern:
```java
public static final DeferredRegister<T> REGISTRY = DeferredRegister.create(ForgeRegistries.TYPE, Cataclysm.MODID);
public static final RegistryObject<T> OBJECT = REGISTRY.register("name", () -> new ObjectClass());

// Register in Cataclysm.java constructor:
REGISTRY.register(bus);
```

#### Key Registration Files:
- **`ModItems.java`** - Items, weapons, tools, armor
- **`ModEntities.java`** - Mobs, projectiles, effects
- **`ModBlocks.java`** - Blocks with special functionality  
- **`ModSounds.java`** - Audio integration
- **`ModParticle.java`** - Visual effects
- **`ModStructures.java`** - Dungeon generation

## Configuration System

### Config Files:
- **`CommonConfig.java`** - Main configuration options
- **`CMConfig.java`** - Runtime config values  
- **`ConfigHolder.java`** - Config spec management

### Config Categories:
- **Visual/Client**: Screen shake, boss bars, shader compatibility
- **Weapons**: Cooldowns, damage values, durability
- **Entities**: Health/damage multipliers, spawn settings  
- **World Generation**: Structure placement rules
- **Gameplay**: Block breaking permissions, limits

### Adding New Config:
```java
// In CommonConfig.java constructor
public final ForgeConfigSpec.IntValue newConfigValue = buildInt(builder, "NewConfigValue", "category", 100, 0, 1000, "Description");

// In CMConfig.java  
public static int NewConfigValue = 100;

// In CMConfig.bake() method
NewConfigValue = ConfigHolder.COMMON.newConfigValue.get();
```

## Development Workflow

### Adding a New Mob:
1. Choose base class (`LLibrary_Monster` for animations)
2. Create entity class with AI goals and animations
3. Register in `ModEntities.java`
4. Add spawn egg in `ModItems.java`
5. Create client model and renderer
6. Add configuration options
7. Test with spawn commands

### Adding a New Weapon:
1. Create weapon class extending appropriate base
2. Implement special abilities and mechanics
3. Register in `ModItems.java`
4. Add recipes in `/data/cataclysm/recipes/`
5. Create item model and textures
6. Add configuration for balance

### Adding Player Skills:
1. Create capability interface and implementation
2. Register in `ModCapabilities.java`
3. Add activation triggers (items/keybinds)
4. Implement progression mechanics
5. Add configuration options

## Important Notes

### Java Version:
- **Build requires Java 17** for Gradle 7.6 compatibility
- Modern Java features used throughout codebase

### Dependencies:
- **LionFish API** for complex entity animations
- **Curios API** for accessory slots
- **JEI Integration** for recipe viewing

### Performance Considerations:
- Complex boss entities with multi-phase mechanics
- Particle-heavy visual effects
- Large structure generation

### Testing:
- Use development environment for testing
- Test with multiplayer for networking
- Verify config changes apply correctly

## Useful Development Commands

```bash
# Clean build directory
./gradlew clean

# Refresh dependencies  
./gradlew --refresh-dependencies

# Run with debug info
./gradlew runClient --debug-jvm

# Generate data (if data generators exist)
./gradlew runData
```

## Resources

- **Minecraft Forge Documentation**: https://docs.minecraftforge.net/
- **Mod Development Tutorial**: https://docs.minecraftforge.net/en/latest/gettingstarted/
- **LionFish API**: Animation system integration
- **Curios API**: Accessory system integration

## Extension Recommendations

1. **Custom Mobs**: Use `Internal_Animation_Monster` for most cases
2. **Boss Mechanics**: Extend `IABoss_monster` for complex encounters  
3. **Player Skills**: Create capabilities for persistent systems
4. **Balance**: Use configuration system extensively
5. **Consistency**: Follow existing naming and code patterns

This mod provides an excellent foundation for creating sophisticated Minecraft content with professional-level systems and architecture.

## Recent Extensions & Development Notes

### Food Item Implementation: Bacon

**Date**: August 2025  
**Implementation**: Added new consumable food item "Bacon" following mod conventions

#### Technical Implementation:
- **Item Registration**: Added to `ModItems.java:602-603` using standard `Item.Properties()` pattern
- **Food Properties**: 3 hunger points, 0.6F saturation, stack size 64
- **Crafting Recipe**: Raw Porkchop + Coal = Bacon (shaped crafting)
- **Recipe Advancement**: Created unlock system using `minecraft:inventory_changed` trigger
- **Localization**: Added "Bacon" to `en_us.json` language file

#### Files Created/Modified:
```
src/main/java/com/github/L_Ender/cataclysm/init/ModItems.java
src/main/resources/data/cataclysm/recipes/bacon.json
src/main/resources/data/cataclysm/advancements/recipes/food/bacon.json
src/main/resources/assets/cataclysm/models/item/bacon.json
src/main/resources/assets/cataclysm/lang/en_us.json
src/main/resources/assets/cataclysm/textures/item/bacon.png
```

#### Asset Creation Process:
1. **Initial Generation**: Python PIL script created basic bacon pixel art
2. **Quality Control**: User intervention to review and improve artwork quality
3. **Custom Design**: User provided improved pixel art code for bacon strip with bone detail
4. **Final Artwork**: User created and provided final 16x16 bacon texture with transparency

#### Key Lessons:
- **Recipe Visibility**: Modern Minecraft requires advancement unlock conditions for recipes to appear in crafting book
- **Asset Quality**: Automated generation benefits from human quality control and artistic direction
- **User Collaboration**: Developer-designer collaboration improves final asset quality significantly

#### Recipe Unlock System:
```json
{
  "criteria": {
    "has_porkchop": { "trigger": "minecraft:inventory_changed", "conditions": {"items": [{"items": ["minecraft:porkchop"]}]} },
    "has_coal": { "trigger": "minecraft:inventory_changed", "conditions": {"items": [{"items": ["minecraft:coal"]}]} }
  },
  "rewards": { "recipes": ["cataclysm:bacon"] }
}
```

This implementation demonstrates proper food item integration following all mod conventions and Minecraft 1.19.2 standards.

### Tameable Slimes Implementation

**Date**: August 2025  
**Implementation**: Added taming functionality to vanilla Slimes using Mixin approach

#### Technical Implementation:
- **Mixin Target**: `net.minecraft.world.entity.monster.Slime` class
- **Taming Item**: Slimeball (33% success rate, same as wolves)
- **Behavior**: Wolf-like pet behavior (follow, sit, defend owner)
- **Anti-Splitting**: Tamed slimes don't split when damaged/killed
- **Size Agnostic**: All slime sizes (big, medium, small) can be tamed equally

#### Files Created/Modified:
```
src/main/java/com/github/L_Ender/cataclysm/mixin/SlimeMixin.java
src/main/resources/cataclysm.mixins.json
```

#### Mixin Implementation Details:
1. **Data Storage**: Uses `EntityDataAccessor` for taming state, owner UUID, and sitting status
2. **Interaction Handler**: `@Inject` into `mobInteract` method for slimeball taming
3. **AI Goals**: Dynamically adds pet AI goals when tamed (SitWhenOrderedToGoal, FollowOwnerGoal, etc.)
4. **Splitting Prevention**: `@Inject` into `remove` method to prevent tamed slimes from splitting
5. **Persistence**: Save/load taming data via NBT with unique keys to avoid conflicts

#### Key Technical Features:
- **Unique Naming**: All mixin methods prefixed with `cataclysm$` to avoid conflicts
- **Safe Casting**: Uses `(Object)this` casting pattern for type compatibility with AI goals
- **Data Syncing**: Proper client-server synchronization via `EntityDataAccessor`
- **Visual Feedback**: Heart particles on successful taming, smoke on failure
- **Sit/Follow Toggle**: Right-click tamed slime to toggle sitting state

#### Mixin Architecture:
```java
@Mixin(Slime.class)
public abstract class SlimeMixin {
    @Unique private static final EntityDataAccessor<Byte> CATACLYSM_DATA_FLAGS_ID;
    @Unique private static final EntityDataAccessor<Optional<UUID>> CATACLYSM_DATA_OWNERUUID_ID;
    
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    @Inject(method = "remove", at = @At("HEAD"), cancellable = true)
}
```

#### Usage:
1. **Taming**: Right-click wild slime with slimeball (33% success rate)
2. **Commands**: Right-click tamed slime to toggle sit/follow
3. **Behavior**: Tamed slimes follow owner, defend against attackers, won't split when damaged

This implementation showcases advanced Mixin techniques for modifying vanilla entity behavior while maintaining compatibility and following modern modding best practices.
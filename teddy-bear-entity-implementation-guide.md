# Teddy Bear Entity Implementation Guide

Based on the analysis of the Netherite Ministrosity entity, this document outlines the implementation details for creating a Teddy Bear pet entity using the same pattern.

## Overview

The Netherite Ministrosity is a pet entity that demonstrates several key features:

- Tameable companion that follows the player
- Has an inventory system (chest functionality)
- Can be picked up with a bucket
- Multiple animation states (idle, sleep, chest open/close)
- Custom AI behaviors and state management
- Spawns from an item (Netherite Effigy)

## Core Implementation Components

### 1. Entity Class Hierarchy

The Teddy Bear entity should follow this inheritance structure:

```text
TamableAnimal (Minecraft base)
    └── AnimationPet (Custom base for pets)
        └── InternalAnimationPet (Adds attack state management)
            └── Teddy_Bear_Entity (Our implementation)
```

**Key interfaces to implement:**

- `Bucketable` - Allows picking up with bucket
- `ContainerListener` - For inventory management
- `HasCustomInventoryScreen` - Custom inventory GUI
- `IFollower` - Custom follow behavior interface

### 2. Essential Entity Features

#### Animation System

- Uses `AnimationState` objects for client-side animations
- States are synchronized via `EntityDataAccessor` with `SynchedEntityData`
- Animation states include:
  - `idleAnimationState` - Default state
  - `sleepAnimationState` - Resting/inactive state
  - `operationAnimationState` - Awakening animation
  - `chestopenAnimationState` - Opening inventory
  - `chestloopAnimationState` - Inventory open idle
  - `chestcloseAnimationState` - Closing inventory

#### Data Synchronization

Key synced data:

- `FROM_BUCKET` - Whether entity was spawned from bucket
- `IS_AWAKEN` - Whether the pet is active (needs activation item)
- `ATTACK_STATE` - Current animation/behavior state
- `SITTING` - Sitting behavior from parent class
- `COMMAND` - Command mode (0=wander, 1=follow, 2=stay)

#### Inventory System

- Uses `SimpleContainer` with 17 slots
- Implements custom GUI through `MinistrostiyMenu`
- Items persist through NBT save/load
- Drops items on death

### 3. AI Goals Structure

The entity uses a combination of standard and custom goals:

1. **SitWhenOrderedToGoal** (Priority 0) - Vanilla sitting behavior
2. **InternalPetStateGoal** (Priority 1) - Custom state management
3. **TameableAIFollowOwner** (Priority 6) - Following behavior
4. **RandomStrollGoal** (Priority 7) - Wandering
5. **LookAtPlayerGoal** (Priority 8) - Player interaction
6. **RandomLookAroundGoal** (Priority 8) - Idle behavior

### 4. Taming and Activation

Two-step process:

1. **Spawning**: Use effigy item to place dormant entity
2. **Activation**: Use activation item (Lava Power Cell) to:
   - Tame the entity
   - Set `IS_AWAKEN` to true
   - Enable full functionality

### 5. Bucket Functionality

Implements Minecraft's `Bucketable` interface:

- Can be picked up with empty bucket
- Creates custom bucket item with entity data
- Preserves inventory and state when bucketed
- Custom pickup sound effect

### 6. Interaction System

Player interactions:

- **Right-click (owner)**: Open inventory
- **Shift + Right-click (owner)**: Cycle command modes
- **Right-click with activation item**: Tame/activate
- **Right-click with healing item**: Restore health
- **Right-click with bucket**: Pick up entity

### 7. Required Files

#### Entity Files

- `Teddy_Bear_Entity.java` - Main entity class
- `Teddy_Bear_Effigy.java` - Spawning item
- `TeddyBearBucket.java` - Bucket item implementation

#### Client-Side Files

- `Teddy_Bear_Model.java` - 3D model definition
- `Teddy_Bear_Renderer.java` - Rendering logic
- `Teddy_Bear_Layer.java` - Optional glow/overlay effects
- `Teddy_Bear_Animation.java` - Animation definitions

#### GUI Files

- `TeddyBearMenu.java` - Container menu
- `TeddyBearInventoryScreen.java` - Client GUI
- `MessageTeddyInventory.java` - Network packet

#### Registration

- Add to `ModEntities.java` - Entity type registration
- Add to `ModItems.java` - Effigy and bucket items
- Add to `CMModelLayers.java` - Model layer registration

### 8. Key Implementation Details

#### Attributes

```java
public static AttributeSupplier.Builder teddy_bear() {
    return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 50.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 0.3D)
        .add(Attributes.ARMOR, 2D)
        .add(Attributes.FOLLOW_RANGE, 32.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.35F);
}
```

#### State Management

- Use `InternalPetStateGoal` for animation state transitions
- States are numbered (0=idle, 1=sleep, 2=operation, etc.)
- State changes broadcast entity events for client sync

#### Special Behaviors

- Immune to fall damage
- Can breathe underwater
- Fire resistant (if desired)
- Custom step height for climbing
- Smart body rotation control

### 9. Network Communication

Custom packets needed:

- Inventory sync packet (like `MessageMiniinventory`)
- State update packets handled by entity events

### 10. Configuration Integration

Use CMConfig for:

- Health multiplier
- Damage multiplier
- Enable/disable features

## Implementation Recommendations

1. **Start Simple**: Begin with basic entity without inventory
2. **Add Features Incrementally**:
   - Basic movement and AI
   - Taming mechanics
   - Animation states
   - Inventory system
   - Bucket functionality
3. **Test Frequently**: Each feature should be tested before adding the next
4. **Reuse Existing Code**: Copy and modify Ministrosity files as templates
5. **Custom Behaviors**: Design unique teddy bear behaviors (hugging, playing, etc.)

## Unique Teddy Bear Features to Consider

- Comfort mechanic: Provides buffs when nearby
- Toy storage: Special inventory for "toy" items
- Sleep schedule: Different behavior day/night
- Cuddle animation: Special interaction animation
- Repair with wool/string instead of coal
- Different sizes/colors based on crafting materials
- Protection mode: Defends owner's bed/base

# Teddy Bear Entity Implementation Prompts for Claude Code

This document provides a series of phased prompts to guide the implementation of a new Teddy Bear pet entity. Each phase builds upon the last, following the patterns established in the `Netherite_Ministrosity_Entity`.

---

### Phase 1: Core Entity and Registration

**Goal:** Create the basic `Teddy_Bear_Entity` class and register it so it can be spawned in-game with the `/summon` command.

**Prompt:**

"Based on the existing entity `Netherite_Ministrosity_Entity`, create a new entity named `Teddy_Bear_Entity`.

1.  **Create the Entity Class:**
    *   Create a new file: `src/main/java/com/github/L_Ender/cataclysm/entity/Pet/Teddy_Bear_Entity.java`.
    *   The class `Teddy_Bear_Entity` should extend `InternalAnimationPet`. For now, you can leave the class body mostly empty, but include the constructor.
    *   Add a static method `teddy_bear_attributes()` that returns an `AttributeSupplier.Builder` with the following attributes:
        *   `MAX_HEALTH`: 50.0D
        *   `KNOCKBACK_RESISTANCE`: 0.3D
        *   `ARMOR`: 2.0D
        *   `FOLLOW_RANGE`: 32.0D
        *   `MOVEMENT_SPEED`: 0.35F

2.  **Register the Entity:**
    *   In `ModEntities.java`, create a new `RegistryObject` for the `Teddy_Bear_Entity` named `TEDDY_BEAR`.
    *   The entity ID should be `teddy_bear`.
    *   Set its `MobCategory` to `CREATURE` and its size to `sized(0.8F, 1.2F)`.
    *   In the `initializeAttributes` method, register the attributes for the `TEDDY_BEAR` using the `teddy_bear_attributes()` method you created."

---

### Phase 2: Taming and Basic AI

**Goal:** Implement taming mechanics and basic AI goals for following, sitting, and wandering.

**Prompt:**

"Now, let's add taming and basic AI to the `Teddy_Bear_Entity`. Use `Netherite_Ministrosity_Entity` as a reference for the implementation.

1.  **Implement Taming:**
    *   In `Teddy_Bear_Entity.java`, override the `mobInteract` method.
    *   Add logic to tame the entity when a player right-clicks it with a `HONEYCOMB`.
    *   Upon taming, set the owner and broadcast the taming entity event.

2.  **Implement Commands:**
    *   In `mobInteract`, add a check so that if the owner shift-right-clicks the tamed entity, it cycles through commands (0: wander, 1: follow, 2: sit).
    *   Display the current command to the player using `player.displayClientMessage`.
    *   Ensure the `setOrderedToSit()` method is called correctly based on the command.

3.  **Register AI Goals:**
    *   Override the `registerGoals` method.
    *   Add the following goals with appropriate priorities, referencing `Netherite_Ministrosity_Entity`:
        *   `SitWhenOrderedToGoal`
        *   `TameableAIFollowOwner`
        *   `RandomStrollGoal`
        *   `LookAtPlayerGoal`
        *   `RandomLookAroundGoal`"

---

### Phase 3: Client-Side Model and Renderer

**Goal:** Create and register the model and renderer to give the Teddy Bear its appearance in the game.

**Prompt:**

"Create the necessary client-side components for the `Teddy_Bear_Entity`.

1.  **Create the Model Class:**
    *   Create `src/main/java/com/github/L_Ender/cataclysm/client/model/entity/Teddy_Bear_Model.java`.
    *   This class should extend `AdvancedEntityModel<Teddy_Bear_Entity>`.
    *   For now, you can create a simple box model. Define the root, body, head, arms, and legs as `ModelPart` objects.

2.  **Create the Renderer Class:**
    *   Create `src/main/java/com/github/L_Ender/cataclysm/client/render/entity/Teddy_Bear_Renderer.java`.
    *   This class should extend `MobRenderer<Teddy_Bear_Entity, Teddy_Bear_Model>`.
    *   In the constructor, pass a new instance of `Teddy_Bear_Model`.
    *   Define the texture location as `cataclysm:textures/entity/teddy_bear.png`.

3.  **Register Client-Side Components:**
    *   In `CMModelLayers.java`, register a new `ModelLayerLocation` for the `TEDDY_BEAR`.
    *   In the main mod class (`Cataclysm.java`), in the `clientSetup` method, register the renderer using `EntityRenderers.register(ModEntities.TEDDY_BEAR.get(), Teddy_Bear_Renderer::new);`.
    *   In the `onClientSetup` event subscriber, register the model layer definition."

---

### Phase 4: Inventory System

**Goal:** Implement the inventory container, menu, and screen.

**Prompt:**

"Implement an inventory for the `Teddy_Bear_Entity`, following the pattern of `Netherite_Ministrosity_Entity`.

1.  **Update Entity Class:**
    *   In `Teddy_Bear_Entity.java`, implement `ContainerListener` and `HasCustomInventoryScreen`.
    *   Add a `SimpleContainer` named `inventory` with a size of 9.
    *   Implement `createInventory()`, `addAdditionalSaveData()`, and `readAdditionalSaveData()` to manage the inventory's state.
    *   Override `openCustomInventoryScreen` to open the custom menu.
    *   In `mobInteract`, call `openCustomInventoryScreen` when the owner right-clicks it without sneaking.

2.  **Create Menu and Screen:**
    *   Create `TeddyBearMenu.java` extending `AbstractContainerMenu`, using `MinistrostiyMenu` as a template.
    *   Create `TeddyBearScreen.java` extending `AbstractContainerScreen`, using a relevant existing screen as a template.
    *   Create a network message `MessageTeddyInventory` to sync inventory opening, similar to `MessageMiniinventory`.

3.  **Register Menu:**
    *   In a `ModContainers` class (create it if it doesn't exist), register the `TeddyBearMenu`."

---

### Phase 5: Advanced Animations and State Machine

**Goal:** Implement the full animation state machine for actions like sleeping and opening the inventory.

**Prompt:**

"Let's implement the animation state machine for the `Teddy_Bear_Entity`.

1.  **Add Animation States:**
    *   In `Teddy_Bear_Entity.java`, add `AnimationState` fields for: `idleAnimationState`, `sleepAnimationState`, `chestopenAnimationState`, `chestloopAnimationState`, and `chestcloseAnimationState`.

2.  **Sync Animation State:**
    *   Use the existing `ATTACK_STATE` `EntityDataAccessor` to represent the current animation state (0 for idle, 1 for sleep, 3 for chest_open, etc.).
    *   Override `onSyncedDataUpdated` to check for changes to `ATTACK_STATE` and start/stop the corresponding `AnimationState` on the client side.

3.  **Manage State Transitions:**
    *   Add an `InternalPetStateGoal` to manage transitions. For example, when the inventory is closed, it should transition from state 4 (chest_loop) to 5 (chest_close).
    *   In `mobInteract`, when the inventory is opened, set the attack state to 3 (`chestopen`).
    *   In the `TeddyBearMenu`, override the `removed` method to set the entity's attack state to 5 (`chestclose`) when the player closes the inventory screen."

---

### Phase 6: Bucket Functionality

**Goal:** Allow the player to pick up and place the Teddy Bear using a bucket.

**Prompt:**

"Implement the `Bucketable` functionality for the `Teddy_Bear_Entity`.

1.  **Update Entity Class:**
    *   Ensure `Teddy_Bear_Entity` implements the `Bucketable` interface.
    *   Add the `FROM_BUCKET` `EntityDataAccessor`.
    *   Implement all methods from the `Bucketable` interface: `fromBucket`, `setFromBucket`, `saveToBucketTag`, `loadFromBucketTag`, `getBucketItemStack`, and `getPickupSound`. Use `Netherite_Ministrosity_Entity` as a direct reference.

2.  **Create Bucket Item:**
    *   In `ModItems.java`, create a new `RegistryObject` for `TEDDY_BEAR_BUCKET`. It should be a new `ForgeSpawnEggItem` with a custom implementation that handles placing the entity from the bucket. A better approach would be to create a `TeddyBearBucketItem` class extending `Item` and handle placement logic there. Let's do that. Create a `TeddyBearBucketItem` class.

3.  **Handle Interaction:**
    *   In `Teddy_Bear_Entity.java`'s `mobInteract` method, add the logic to pick up the entity when a player uses an empty bucket on it."

---

### Phase 7: Spawn Item (Effigy)

**Goal:** Create a custom item that spawns a dormant Teddy Bear, which must then be "awakened" by the player.

**Prompt:**

"Create a spawn item for the `Teddy_Bear_Entity`.

1.  **Create the Effigy Item:**
    *   In `ModItems.java`, register a new item named `TEDDY_BEAR_EFFIGY`.
    *   Create a new class `Teddy_Bear_Effigy_Item` that extends `Item`.
    *   In this class, override the `useOn` method to spawn a `Teddy_Bear_Entity` at the clicked location.

2.  **Implement Dormant State:**
    *   In `Teddy_Bear_Entity.java`, add the `IS_AWAKEN` `EntityDataAccessor` (if not already present).
    *   When spawned from the effigy, the teddy bear should have `IS_AWAKEN` set to `false`.
    *   A dormant teddy bear should be inactive (perhaps using the `sleepAnimationState`).
    *   Modify the taming logic in `mobInteract`: interacting with a dormant teddy bear with a `HONEYCOMB` should now also set `IS_AWAKEN` to `true`.

3.  **Register Item and Model:**
    *   Ensure the `TEDDY_BEAR_EFFIGY` item is registered.
    *   Add a model file for it in `src/main/resources/assets/cataclysm/models/item/teddy_bear_effigy.json`."

---

### Phase 8: Polishing (Sounds and Config)

**Goal:** Add sound effects and integrate with the mod's configuration system.

**Prompt:**

"Finally, let's polish the `Teddy_Bear_Entity` by adding sounds and configuration options.

1.  **Add Sound Events:**
    *   In `ModSounds.java`, register new `SoundEvent`s for the teddy bear: `TEDDY_BEAR_AMBIENT`, `TEDDY_BEAR_HURT`, `TEDDY_BEAR_DEATH`, and `TEDDY_BEAR_PICKUP_BUCKET`.
    *   In `sounds.json`, define the sound files for these events.

2.  **Implement Sounds in Entity:**
    *   In `Teddy_Bear_Entity.java`, override `getAmbientSound`, `getHurtSound`, `getDeathSound`, and reference the new sounds.
    *   Use the `getPickupSound` from the `Bucketable` interface to return the custom pickup sound.

3.  **Integrate with Config:**
    *   In `CMConfig.java`, add configuration options for the teddy bear's health and movement speed.
    *   In `Teddy_Bear_Entity.java`'s constructor, use `setConfigattribute` to apply the health multiplier from the config.
    *   In the `teddy_bear_attributes` method, reference the config value for movement speed."

---

### Phase 9: Artwork and Loot

**Goal:** Create placeholder artwork and a loot table for the Teddy Bear.

**Prompt:**

"To complete the Teddy Bear entity, we need to create placeholder artwork and a loot table. Use the assets from `Netherite_Ministrosity` as a template.

1.  **Create Placeholder Textures:**
    *   Copy `netherite_ministrosity.png` to `teddy_bear.png` in the same directory (`src/main/resources/assets/cataclysm/textures/entity/monstrosity/`).
    *   Copy `netherite_ministrosity_layer.png` to `teddy_bear_layer.png` in the same directory.
    *   Copy `netherite_ministrosity_bucket.png` to `teddy_bear_bucket.png` in `src/main/resources/assets/cataclysm/textures/item/`.

2.  **Create Item Models:**
    *   Copy `netherite_ministrosity_bucket.json` to `teddy_bear_bucket.json` in `src/main/resources/assets/cataclysm/models/item/`.
    *   In the new file, update the texture path to point to `item/teddy_bear_bucket`.
    *   Copy `netherite_ministrosity_spawn_egg.json` to `teddy_bear_effigy.json` in the same directory. Update the texture path to point to `item/teddy_bear_effigy`.

3.  **Create Loot Table:**
    *   Copy `netherite_ministrosity.json` to `teddy_bear.json` in `src/main/resources/data/cataclysm/loot_tables/entities/`.
    *   Modify the loot table to drop a `HONEYCOMB` and a `LEATHER` when the teddy bear is killed."
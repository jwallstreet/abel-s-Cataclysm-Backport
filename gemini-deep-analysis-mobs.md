# Creating a New Mob in Cataclysm: A Deep Dive

This document provides a detailed analysis of the process for creating a new mob in the Cataclysm mod, using the `Ender_Golem_Entity` as a reference.

## 1. The Entity Class

The foundation of any new mob is its entity class. This class defines the mob's behavior, attributes, and overall design.

**Key Components:**

*   **Inheritance:** The choice of the base class is crucial. For boss-like mobs, `LLibrary_Boss_Monster` is a good starting point, as it provides built-in support for complex animations and behaviors. For standard mobs, `Monster` or other vanilla entity classes are more appropriate.
*   **Animations:** The Cataclysm mod uses a custom animation system. Animations are defined as static `Animation` objects and are triggered by setting the animation on the entity.
*   **Attributes:** A static method (e.g., `ender_golem()`) is used to create an `AttributeSupplier.Builder` that defines the mob's core attributes, such as `MAX_HEALTH`, `ATTACK_DAMAGE`, and `MOVEMENT_SPEED`.
*   **AI Goals:** The `registerGoals()` method is where the mob's AI is defined. This includes a combination of standard goals (e.g., `LookAtPlayerGoal`) and custom goals specific to the mod.
*   **Data Syncing:** `EntityDataAccessor` is used to synchronize data between the server and client. This is essential for ensuring that client-side animations and effects are displayed correctly.
*   **Core Logic:** The `tick()` method contains the mob's main logic, including its attack patterns, special abilities, and state management.

**Example: `Ender_Golem_Entity.java`**

```java
public class Ender_Golem_Entity extends LLibrary_Boss_Monster {

    // Animation definitions
    public static final Animation ANIMATION_ATTACK1 = Animation.create(25);
    public static final Animation ANIMATION_ATTACK2 = Animation.create(25);
    // ...

    // Attribute definition
    public static AttributeSupplier.Builder ender_golem() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28F)
                // ...
    }

    // AI goal registration
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new CmAttackGoal(this, 1.0D));
        // ...
    }

    // Core logic
    public void tick() {
        super.tick();
        // ...
    }
}
```

## 2. Entity Registration

Once the entity class is created, it needs to be registered with the game. This is handled in the `ModEntities` class.

**Key Components:**

*   **`DeferredRegister`:** A `DeferredRegister` is used to queue the registration of all entity types.
*   **`RegistryObject`:** Each entity has a `RegistryObject` that handles the actual registration. This includes defining the entity's name, class, mob category, and other properties.
*   **`EntityAttributeCreationEvent`:** The entity's attributes are registered in the `initializeAttributes` method, which is subscribed to the `EntityAttributeCreationEvent`.
*   **Spawn Placements:** The `initializeAttributes` method is also used to define the entity's spawn placements, which determine where and how it can spawn in the world.

**Example: `ModEntities.java`**

```java
@Mod.EventBusSubscriber(modid = Cataclysm.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Cataclysm.MODID);

    public static final RegistryObject<EntityType<Ender_Golem_Entity>> ENDER_GOLEM = ENTITY_TYPE.register("ender_golem", () -> EntityType.Builder.of(Ender_Golem_Entity::new, MobCategory.MONSTER)
            .sized(2.5F, 3.5F)
            .fireImmune()
            .build(Cataclysm.MODID + ":ender_golem"));

    @SubscribeEvent
    public static void initializeAttributes(EntityAttributeCreationEvent event) {
        event.put(ENDER_GOLEM.get(), Ender_Golem_Entity.ender_golem().build());
        // ...
    }
}
```

## 3. Client-Side Components

To bring the mob to life, several client-side components are required.

**Key Components:**

*   **Model:** The mob's 3D model is defined in a class that extends `AdvancedEntityModel`. This class also handles the animations.
*   **Renderer:** The renderer is responsible for rendering the mob in the world. It links the model to the entity and specifies its texture.
*   **Layer:** An optional layer can be added to the renderer for extra effects, such as glowing textures.

**Example Files:**

*   `Ender_Golem_Model.java`
*   `Ender_Golem_Renderer.java`
*   `Ender_Golem_Layer.java`

## 4. Resources

The final step is to create the necessary resources for the mob.

**Key Components:**

*   **Textures:** The mob's texture is a PNG file located in `src/main/resources/assets/cataclysm/textures/entity/`.
*   **Sounds:** Sound events are defined in the `ModSounds` class and have corresponding entries in `sounds.json`.
*   **Loot Tables:** The mob's loot table is a JSON file located in `src/main/resources/data/cataclysm/loot_tables/entities/`.

By following these steps and using the existing code as a reference, you can create a new mob that is consistent with the design and style of the Cataclysm mod.

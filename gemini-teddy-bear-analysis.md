# Creating a Teddy Bear Pet: A Deep Dive

This document provides a detailed analysis of the process for creating a new teddy bear pet entity, using the `Netherite_Ministrosity_Entity` as a reference.

## 1. The Entity Class

The entity class is the core of the teddy bear, defining its behavior, interactions, and appearance.

**Key Components:**

*   **Inheritance:** The class should extend `InternalAnimationPet`, which provides a foundation for pets with custom animations and behaviors. It should also implement `Bucketable`, `ContainerListener`, and `HasCustomInventoryScreen` to support the bucket and inventory features.
*   **Taming:** The taming process is initiated by interacting with the teddy bear with a specific item (e.g., a honeycomb). Once tamed, the teddy bear will be owned by the player and will follow them.
*   **Inventory:** The teddy bear will have a custom inventory that can be accessed by the player. The inventory size and behavior are defined in the entity class.
*   **AI Goals:** The AI goals will control the teddy bear's behavior, including following its owner, sitting when ordered, and wandering around.
*   **Data Syncing:** `EntityDataAccessor` will be used to synchronize data between the server and client, such as the taming status, awaken state, and whether it's from a bucket.

**Example: `Netherite_Ministrosity_Entity.java`**

```java
public class Netherite_Ministrosity_Entity extends InternalAnimationPet implements Bucketable, ContainerListener, HasCustomInventoryScreen {

    // Data accessors for syncing data
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Netherite_Ministrosity_Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_AWAKEN = SynchedEntityData.defineId(Netherite_Ministrosity_Entity.class, EntityDataSerializers.BOOLEAN);

    // Inventory management
    public SimpleContainer miniInventory;

    // AI goal registration
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(6, new TameableAIFollowOwner(this, 1.3D, 6.0F, 2.0F, true));
        // ...
    }

    // Interaction handling
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        // ...
    }
}
```

## 2. Interactions

The `mobInteract` method is the heart of the teddy bear's interactions with the player.

**Key Interactions:**

*   **Taming:** When the player interacts with an untamed teddy bear with the taming item, the teddy bear becomes tamed and is assigned to the player.
*   **Inventory Access:** When the player interacts with a tamed teddy bear while not sneaking, the teddy bear's inventory is opened.
*   **Commands:** When the player interacts with a tamed teddy bear while sneaking, the teddy bear cycles through its commands (e.g., sit, follow).
*   **Bucket:** The teddy bear can be picked up in a bucket, which is handled by the `emptybucketMobPickup` method.

## 3. Client-Side Components

The client-side components are responsible for the teddy bear's appearance and animations.

**Key Components:**

*   **Model:** A custom model will be created for the teddy bear, with animations for idling, sleeping, and other actions.
*   **Renderer:** The renderer will link the model to the entity and specify its texture.
*   **Animations:** The animations will be defined as `AnimationState` objects and will be triggered based on the teddy bear's state.

## 4. Items

The teddy bear will be associated with several items.

**Key Items:**

*   **Taming Item:** An item used to tame the teddy bear (e.g., a honeycomb).
*   **Bucket Item:** A custom bucket item that can be used to pick up and place the teddy bear.

By following these guidelines and using the `Netherite_Ministrosity_Entity` as a reference, we can create a new teddy bear pet that is both functional and engaging.

package com.github.L_Ender.cataclysm.structures.jisaw;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class CataclysmPoolElement extends SinglePoolElement {
    public static final Codec<CataclysmPoolElement> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(
                    templateCodec(),
                    processorsCodec(),
                    projectionCodec(),
                    BoundsParams.CODEC.optionalFieldOf("bounds", new BoundsParams(false, BlockPos.ZERO, BlockPos.ZERO, BlockPos.ZERO, Optional.empty(), Optional.empty(), Optional.empty(), BlockPos.ZERO, BlockPos.ZERO, true, false, Optional.empty(), Optional.empty())).forGetter(element -> element.bounds),
                    ConditionsParams.CODEC.optionalFieldOf("conditions", new ConditionsParams(-1, -1, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Collections.emptyList(), 1)).forGetter(element -> element.conditions),
                    TagsParams.CODEC.optionalFieldOf("tags", new TagsParams(Collections.emptyList(), false, Optional.empty(), 1)).forGetter(element -> element.tags),
                    Codec.BOOL.optionalFieldOf("two_way", false).forGetter(element -> element.twoWay),
                    Codec.INT.optionalFieldOf("gen_order", 0).forGetter(element -> element.genOrder),
                    Codec.INT.optionalFieldOf("priority", 0).forGetter(element -> element.priority)
            ).apply(builder, CataclysmPoolElement::new));

    /**
     * Bounds related parameters
     */
    public final BoundsParams bounds;

    /**
     * Generation conditions parameters
     */
    public final ConditionsParams conditions;

    /**
     * Tag related parameters
     */
    public final TagsParams tags;

    /**
     * Whether this piece's horizontal jigsaw blocks can connect in both directions
     */
    public final boolean twoWay;

    /**
     * Control the order in which pieces generate. Higher numbers generate last.
     */
    public final int genOrder;

    /**
     * Control the order in which elements are selected. Lower numbers are tested first.
     */
    public final int priority;

    protected CataclysmPoolElement(Either<ResourceLocation, StructureTemplate> p_210415_, Holder<StructureProcessorList> p_210416_, StructureTemplatePool.Projection p_210417_,
                                   BoundsParams bounds, ConditionsParams conditions, TagsParams tags,
                                   boolean twoWay, int genOrder, int priority) {
        super(p_210415_, p_210416_, p_210417_);
        this.bounds = bounds;
        this.conditions = conditions;
        this.tags = tags;
        this.twoWay = twoWay;
        this.genOrder = genOrder;
        this.priority = priority;
    }

    public static boolean canAttachTwoWays(StructureTemplate.StructureBlockInfo p_54246_, StructureTemplate.StructureBlockInfo p_54247_) {
        Direction direction = JigsawBlock.getFrontFacing(p_54246_.state);
        Direction direction1 = JigsawBlock.getFrontFacing(p_54247_.state);
        Direction direction2 = JigsawBlock.getTopFacing(p_54246_.state);
        Direction direction3 = JigsawBlock.getTopFacing(p_54247_.state);
        JigsawBlockEntity.JointType jigsawblockentity$jointtype = JigsawBlockEntity.JointType.byName(p_54246_.nbt.getString("joint")).orElseGet(() -> {
            return direction.getAxis().isHorizontal() ? JigsawBlockEntity.JointType.ALIGNED : JigsawBlockEntity.JointType.ROLLABLE;
        });
        boolean flag = jigsawblockentity$jointtype == JigsawBlockEntity.JointType.ROLLABLE;
        return direction == direction1 && (flag || direction2 == direction3) && p_54246_.nbt.getString("target").equals(p_54247_.nbt.getString("name"));
    }

    public boolean ignoresBounds() {
        return this.bounds.ignoreBounds;
    }

    public boolean placeBounds() {
        return this.bounds.placeBounds;
    }

    public boolean twoWay() {
        return this.twoWay;
    }

    public Vec3i offset() {
        return new Vec3i(bounds.offset.getX(), bounds.offset.getY(), bounds.offset.getZ());
    }

    public boolean checkCriteria(CataclysmJigsawManager.PieceState pieceState, CataclysmJigsawManager.Placer placer) {
        int maxDepth = this.conditions.maxDepth;
        if (maxDepth != -1 && pieceState.depth > maxDepth) return false;

        int minDepth = this.conditions.minDepth;
        if (minDepth != -1 && pieceState.depth < minDepth) return false;

        CataclysmJigsawManager.PieceState parent = pieceState;
        for (int i = 0; i < this.conditions.forbiddenParentsDepth; i++) {
            String parentName = parent.piece.getElement().toString().split("[\\[\\]]")[2];
            if (this.conditions.forbiddenParents.contains(parentName)) {
                return false;
            }
            parent = parent.parent;
        }

        if (this.tags.needsTag.isPresent()) {
            parent = pieceState;
            boolean foundTag = false;
            for (int i = 0; i < this.tags.needsTagDepth; i++) {
                if (this.tags.needsTag.get().equals(parent.tag)) {
                    foundTag = true;
                    break;
                }
                parent = parent.parent;
            }
            if (!foundTag) return false;
        }

        if (!(placer instanceof CataclysmJigsawManager.FallbackPlacer)) {
            if (this.conditions.minRequiredPaths.isPresent() && placer.numPaths < this.conditions.minRequiredPaths.get())
                return false;
            if (this.conditions.maxAllowedPaths.isPresent() && placer.numPaths > this.conditions.maxAllowedPaths.get())
                return false;
        }
        return true;
    }

    public String getRandomTag(RandomSource random) {
        if (tags.tags.isEmpty()) return null;

        int total = 0;
        for (Tag tag : this.tags.tags) {
            total += tag.weight;
        }

        float rand = random.nextFloat() * total;
        total = 0;
        for (Tag tag : this.tags.tags) {
            total += tag.weight;
            if (total >= rand) return tag.tag;
        }
        return null;
    }

    @Override
    public BoundingBox getBoundingBox(StructureTemplateManager structureManager, BlockPos blockPos, Rotation rotation) {
        StructureTemplate structuretemplate = this.getTemplate(structureManager);

        Vec3i sizeVec = structuretemplate.getSize().offset(-1, -1, -1);
        BlockPos blockpos = StructureTemplate.transform(BlockPos.ZERO.offset(bounds.boundsMinOffset), Mirror.NONE, rotation, BlockPos.ZERO);
        BlockPos blockpos1 = StructureTemplate.transform(BlockPos.ZERO.offset(sizeVec).offset(bounds.boundsMaxOffset), Mirror.NONE, rotation, BlockPos.ZERO);
        return BoundingBox.fromCorners(blockpos, blockpos1).move(blockPos);
    }

    public BoundingBox getCheckBoundingBox(StructureTemplateManager structureManager, BlockPos blockPos, Rotation rotation) {
        StructureTemplate structuretemplate = this.getTemplate(structureManager);

        Vec3i sizeVec = structuretemplate.getSize().offset(-1, -1, -1);
        BlockPos blockpos = StructureTemplate.transform(BlockPos.ZERO.offset(bounds.boundsMinOffset).offset(bounds.checkBoundsMinOffset), Mirror.NONE, rotation, BlockPos.ZERO);
        BlockPos blockpos1 = StructureTemplate.transform(BlockPos.ZERO.offset(sizeVec).offset(bounds.boundsMaxOffset).offset(bounds.checkBoundsMaxOffset), Mirror.NONE, rotation, BlockPos.ZERO);
        return BoundingBox.fromCorners(blockpos, blockpos1).move(blockPos);
    }

    public BoundingBox getInteriorBoundingBox(StructureTemplateManager structureManager, BlockPos blockPos, Rotation rotation) {
        if (bounds.interiorBoundsMaxOffset.isEmpty() && bounds.interiorBoundsMinOffset.isEmpty()) return null;
        BlockPos interiorBoundsMinOffset = BlockPos.ZERO;
        BlockPos interiorBoundsMaxOffset = BlockPos.ZERO;
        if (bounds.interiorBoundsMinOffset.isPresent()) interiorBoundsMinOffset = bounds.interiorBoundsMinOffset.get();
        if (bounds.interiorBoundsMaxOffset.isPresent()) interiorBoundsMaxOffset = bounds.interiorBoundsMaxOffset.get();
        StructureTemplate structuretemplate = this.getTemplate(structureManager);

        Vec3i sizeVec = structuretemplate.getSize().offset(-1, -1, -1);
        BlockPos blockpos = StructureTemplate.transform(BlockPos.ZERO.offset(bounds.boundsMinOffset).offset(interiorBoundsMinOffset), Mirror.NONE, rotation, BlockPos.ZERO);
        BlockPos blockpos1 = StructureTemplate.transform(BlockPos.ZERO.offset(sizeVec).offset(bounds.boundsMaxOffset).offset(interiorBoundsMaxOffset), Mirror.NONE, rotation, BlockPos.ZERO);
        return BoundingBox.fromCorners(blockpos, blockpos1).move(blockPos);
    }

    public static class BoundsParams {
        public static final Codec<BoundsParams> CODEC = RecordCodecBuilder.create((builder) -> builder
                .group(
                        Codec.BOOL.optionalFieldOf("ignore_bounds", false).forGetter(element -> element.ignoreBounds),
                        BlockPos.CODEC.optionalFieldOf("bounds_min_offset", BlockPos.ZERO).forGetter(element -> element.boundsMinOffset),
                        BlockPos.CODEC.optionalFieldOf("bounds_max_offset", BlockPos.ZERO).forGetter(element -> element.boundsMaxOffset),
                        BlockPos.CODEC.optionalFieldOf("offset", BlockPos.ZERO).forGetter(element -> element.offset),
                        Codec.STRING.optionalFieldOf("special_bounds").forGetter(element -> element.specialBounds),
                        Codec.STRING.optionalFieldOf("needs_overlap_bounds").forGetter(element -> element.needsOverlapBounds),
                        Codec.STRING.optionalFieldOf("forbidden_overlap_bounds").forGetter(element -> element.forbiddenOverlapBounds),
                        BlockPos.CODEC.optionalFieldOf("check_bounds_min_offset", BlockPos.ZERO).forGetter(element -> element.checkBoundsMinOffset),
                        BlockPos.CODEC.optionalFieldOf("check_bounds_max_offset", BlockPos.ZERO).forGetter(element -> element.checkBoundsMaxOffset),
                        Codec.BOOL.optionalFieldOf("place_bounds", true).forGetter(element -> element.placeBounds),
                        Codec.BOOL.optionalFieldOf("ignore_parent_bounds", false).forGetter(element -> element.ignoreParentBounds),
                        BlockPos.CODEC.optionalFieldOf("interior_bounds_min_offset").forGetter(element -> element.interiorBoundsMinOffset),
                        BlockPos.CODEC.optionalFieldOf("interior_bounds_max_offset").forGetter(element -> element.interiorBoundsMaxOffset)
                ).apply(builder, BoundsParams::new));

        /**
         * Whether this piece should ignore the usual piece boundary checks.
         * Enabling this allows this piece to spawn while overlapping other pieces.
         */
        public final boolean ignoreBounds;

        /**
         * Adjust the piece's bounds on all 6 sides
         */
        public final BlockPos boundsMinOffset;
        public final BlockPos boundsMaxOffset;

        /**
         * Offset the piece's location
         */
        public final BlockPos offset;

        /**
         * Name of a special bounding box this contributes to
         */
        public final Optional<String> specialBounds;

        /**
         * Name of a special bounding box this piece needs to overlap with
         */
        public final Optional<String> needsOverlapBounds;

        /**
         * Name of a special bounding box this piece is not allowed to overlap with
         */
        public final Optional<String> forbiddenOverlapBounds;

        /**
         * Adjust the bounding box the piece uses to check other pieces. Often larger than its own bounding box.
         */
        public final BlockPos checkBoundsMinOffset;
        public final BlockPos checkBoundsMaxOffset;

        /**
         * Set to false to prevent this piece from subtracting from free space
         */
        public final boolean placeBounds;

        /**
         * Set to true to make a piece ignore its parent's bounds while checking for free space
         */
        public final boolean ignoreParentBounds;

        /**
         * Interior space for furniture and etc
         */
        public final Optional<BlockPos> interiorBoundsMinOffset;
        public final Optional<BlockPos> interiorBoundsMaxOffset;

        private BoundsParams(boolean ignoreBounds, BlockPos boundsMinOffset, BlockPos boundsMaxOffset, BlockPos offset, Optional<String> specialBounds, Optional<String> needsOverlapBounds, Optional<String> forbiddenOverlapBounds, BlockPos checkBoundsMinOffset, BlockPos checkBoundsMaxOffset, boolean placeBounds, boolean ignoreParentBounds, Optional<BlockPos> interiorBoundsMinOffset, Optional<BlockPos> interiorBoundsMaxOffset) {
            this.ignoreBounds = ignoreBounds;
            this.boundsMinOffset = boundsMinOffset;
            this.boundsMaxOffset = boundsMaxOffset;
            this.offset = offset;
            this.specialBounds = specialBounds;
            this.forbiddenOverlapBounds = forbiddenOverlapBounds;
            this.needsOverlapBounds = needsOverlapBounds;
            this.checkBoundsMinOffset = checkBoundsMinOffset;
            this.checkBoundsMaxOffset = checkBoundsMaxOffset;
            this.placeBounds = placeBounds;
            this.ignoreParentBounds = ignoreParentBounds;
            this.interiorBoundsMinOffset = interiorBoundsMinOffset;
            this.interiorBoundsMaxOffset = interiorBoundsMaxOffset;
        }
    }

    public static class ConditionsParams {
        public static final Codec<ConditionsParams> CODEC = RecordCodecBuilder.create((builder) -> builder
                .group(
                        Codec.INT.optionalFieldOf("min_depth", -1).forGetter(element -> element.minDepth),
                        Codec.INT.optionalFieldOf("max_depth", -1).forGetter(element -> element.maxDepth),
                        Codec.INT.optionalFieldOf("min_height").forGetter(element -> element.minHeight),
                        Codec.INT.optionalFieldOf("max_height").forGetter(element -> element.maxHeight),
                        Codec.INT.optionalFieldOf("min_required_paths").forGetter(element -> element.minRequiredPaths),
                        Codec.INT.optionalFieldOf("max_allowed_paths").forGetter(element -> element.maxAllowedPaths),
                        Codec.INT.optionalFieldOf("num_paths_override").forGetter(element -> element.numPathsOverride),
                        Codec.STRING.listOf().optionalFieldOf("forbidden_parents", Collections.emptyList()).forGetter(element -> element.forbiddenParents),
                        Codec.INT.optionalFieldOf("forbidden_parents_depth", 1).forGetter(element -> element.forbiddenParentsDepth)
                ).apply(builder, ConditionsParams::new));

        /**
         * Maximum iteration depth at which this piece can be chosen
         */
        public final int minDepth;
        public final int maxDepth;

        /**
         * Distances to stop the piece from placing too high or too low down.
         */
        public final Optional<Integer> minHeight;
        public final Optional<Integer> maxHeight;

        /**
         * Only allow the piece to spawn when this many active paths are generating
         */
        public final Optional<Integer> minRequiredPaths;
        public final Optional<Integer> maxAllowedPaths;
        public final Optional<Integer> numPathsOverride;

        /**
         * Prevent pieces from generating with certain parent pieces
         */
        public final List<String> forbiddenParents;
        public final int forbiddenParentsDepth;

        private ConditionsParams(
                int minDepth, int maxDepth,
                Optional<Integer> minHeight, Optional<Integer> maxHeight,
                Optional<Integer> minRequiredPaths, Optional<Integer> maxAllowedPaths, Optional<Integer> numPathsOverride,
                List<String> forbiddenParents, int forbiddenParentsDepth
        ) {
            this.minDepth = minDepth;
            this.maxDepth = maxDepth;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
            this.minRequiredPaths = minRequiredPaths;
            this.maxAllowedPaths = maxAllowedPaths;
            this.numPathsOverride = numPathsOverride;
            this.forbiddenParents = forbiddenParents;
            this.forbiddenParentsDepth = forbiddenParentsDepth;
        }
    }

    public record Tag(String tag, int weight) {
        public static final Codec<Tag> CODEC = RecordCodecBuilder.create((builder) -> builder
                .group(
                        Codec.STRING.fieldOf("tag").forGetter(element -> element.tag),
                        Codec.INT.optionalFieldOf("weight", 1).forGetter(element -> element.weight)
                ).apply(builder, Tag::new));

    }

    public static class TagsParams {
        public static final Codec<TagsParams> CODEC = RecordCodecBuilder.create((builder) -> builder
                .group(
                        Tag.CODEC.listOf().optionalFieldOf("possible_tags", Collections.emptyList()).forGetter(element -> element.tags),
                        Codec.BOOL.optionalFieldOf("inherits_tag", false).forGetter(element -> element.inheritsTag),
                        Codec.STRING.optionalFieldOf("needs_tag").forGetter(element -> element.needsTag),
                        Codec.INT.optionalFieldOf("needs_tag_depth", 1).forGetter(element -> element.needsTagDepth)
                ).apply(builder, TagsParams::new));

        /**
         * List of tags and their weights
         */
        public final List<Tag> tags;

        /**
         * Inherit tag from parent piece.
         */
        public final boolean inheritsTag;

        /**
         * Needs a parent piece to have this tag in order to generate.
         */
        public final Optional<String> needsTag;

        /**
         * Search through this many parents to find the needed tag.
         */
        public final int needsTagDepth;

        private TagsParams(
                List<Tag> tags, boolean inheritsTag, Optional<String> needsTag, int needsTagDepth
        ) {
            this.tags = tags;
            this.inheritsTag = inheritsTag;
            this.needsTag = needsTag;
            this.needsTagDepth = needsTagDepth;
        }
    }
}

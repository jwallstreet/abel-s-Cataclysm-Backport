package com.github.L_Ender.cataclysm.blocks;

import org.jetbrains.annotations.Nullable;

import com.github.L_Ender.cataclysm.blockentities.Door_Of_Seal_BlockEntity;
import com.github.L_Ender.cataclysm.init.ModBlocks;
import com.github.L_Ender.cataclysm.init.ModTileentites;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Door_of_Seal_Block extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private static final VoxelShape CLOSED_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);


    public Door_of_Seal_Block() {
        super(Properties.of(Material.METAL).color(MaterialColor.METAL)
                .noOcclusion()
                .dynamicShape()
                .strength(-1.0F, 3600000.0F)
                .noLootTable()
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)).setValue(OPEN, Boolean.valueOf(false)));
    }


    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49751_) {
        p_49751_.add(FACING,OPEN,LIT);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new Door_Of_Seal_BlockEntity(pos, state);
    }


    public InteractionResult use(BlockState p_49722_, Level p_49723_, BlockPos p_49724_, Player p_49725_, InteractionHand p_49726_, BlockHitResult p_49727_) {
        return this.onHit(p_49723_,p_49722_, p_49727_, p_49725_, true)  ? InteractionResult.sidedSuccess(p_49723_.isClientSide) : InteractionResult.PASS;
    }

    public boolean onHit(Level p_49702_,BlockState blockState, BlockHitResult p_49704_, @javax.annotation.Nullable Player p_49705_, boolean p_49706_) {
        BlockPos blockpos = p_49704_.getBlockPos();
        if (p_49706_) {
            this.attemptToRing(p_49705_, p_49702_,blockState, blockpos);
            return true;
        } else {
            return false;
        }
    }


    public boolean attemptToRing(@javax.annotation.Nullable Entity p_152189_, Level p_152190_,BlockState blockState, BlockPos p_152191_) {
        BlockEntity blockentity = p_152190_.getBlockEntity(p_152191_);
        if (!p_152190_.isClientSide && blockentity instanceof Door_Of_Seal_BlockEntity && !blockState.getValue(LIT)) {
            ((Door_Of_Seal_BlockEntity)blockentity).onHit(p_152190_);
            p_152190_.setBlock(p_152191_, blockState.setValue(LIT, Boolean.valueOf(true)), 3);
          //  p_152190_.playSound((Player)null, p_152191_, ModSounds.MALEDICTUS_SHORT_ROAR.get(), SoundSource.BLOCKS, 2.0F, 1.0F);
            p_152190_.gameEvent(p_152189_, GameEvent.BLOCK_CHANGE, p_152191_);
            return true;
        } else {
            return false;
        }
    }



    @javax.annotation.Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152194_, BlockState p_152195_, BlockEntityType<T> p_152196_) {
        return createTickerHelper(p_152196_, ModTileentites.DOOR_OF_SEAL.get(), Door_Of_Seal_BlockEntity::tick);
    }


    public VoxelShape getShape(BlockState p_49755_, BlockGetter p_49756_, BlockPos p_49757_, CollisionContext p_49758_) {
        return CLOSED_SHAPE;
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public VoxelShape getVisualShape(BlockState p_48735_, BlockGetter p_48736_, BlockPos p_48737_, CollisionContext p_48738_) {
        return Shapes.empty();
    }


    public VoxelShape getBlockSupportShape(BlockState p_253862_, BlockGetter p_254569_, BlockPos p_254197_) {
        if (p_253862_.getValue(OPEN)) {
            return Shapes.empty();
        } else {
            return CLOSED_SHAPE;
        }
    }

    public VoxelShape getCollisionShape(BlockState p_53396_, BlockGetter p_53397_, BlockPos p_53398_, CollisionContext p_53399_) {
        if (p_53396_.getValue(OPEN)) {
            return Shapes.empty();
        } else {
            return CLOSED_SHAPE;
        }
    }

    public VoxelShape getOcclusionShape(BlockState p_53401_, BlockGetter p_53402_, BlockPos p_53403_) {
        return Shapes.empty();
    }


    public boolean isPathfindable(BlockState p_49717_, BlockGetter p_49718_, BlockPos p_49719_, PathComputationType p_49720_) {
        return false;
    }

    private boolean doesGongFitInDirection(BlockPos pos, Direction direction, Level level) {
        for (int i = 0; i <= 7; i++) {
            BlockPos abovePos = pos.above(i);
            BlockPos blockpos1 = abovePos.relative(direction.getClockWise());
            BlockPos blockpos2 = abovePos;
            BlockPos blockpos3 = abovePos.relative(direction.getCounterClockWise());
            BlockPos blockpos4 = abovePos.relative(direction.getClockWise(),2);
            BlockPos blockpos5 = abovePos.relative(direction.getCounterClockWise(),2);
            BlockPos[] toBreakPoses = {blockpos1, blockpos2, blockpos3,blockpos4,blockpos5};
            for (BlockPos toBreakPos : toBreakPoses) {
                BlockState blockstate = level.getBlockState(toBreakPos);
                if (!blockstate.getMaterial().isReplaceable()) return false;
            }
        }
        return true;
    }

    @javax.annotation.Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockPos blockpos = context.getClickedPos();
        Direction.Axis direction$axis = direction.getAxis();
        if (direction$axis == Direction.Axis.Y) {
            Direction dir = context.getHorizontalDirection();
            BlockState blockstate = this.defaultBlockState().setValue(FACING, dir);
            if (blockstate.canSurvive(context.getLevel(), blockpos) && doesGongFitInDirection(blockpos, dir, context.getLevel())) {
                return blockstate;
            }
        } else {
            Direction dir = direction.getOpposite();
            BlockState blockstate1 = this.defaultBlockState().setValue(FACING, dir);
            if (blockstate1.canSurvive(context.getLevel(), context.getClickedPos()) && doesGongFitInDirection(context.getClickedPos(), dir, context.getLevel())) {
                return blockstate1;
            }
        }

        return null;
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @javax.annotation.Nullable LivingEntity entity, ItemStack itemStack) {
        super.setPlacedBy(level, pos, state, entity, itemStack);
        if (!level.isClientSide) {
            for (int i = 0; i < 8; i++) {
                BlockPos abovePos = pos.above(i);

                BlockPos blockpos1 = abovePos.relative(state.getValue(FACING).getClockWise());
                BlockPos blockpos2 = abovePos;
                BlockPos blockpos3 = abovePos.relative(state.getValue(FACING).getCounterClockWise());
                BlockPos blockpos4 = abovePos.relative(state.getValue(FACING).getClockWise(),2);
                BlockPos blockpos5 = abovePos.relative(state.getValue(FACING).getCounterClockWise(),2);


                BlockState defaultGongPart = ModBlocks.DOOR_OF_SEAL_PART.get().defaultBlockState();
                level.setBlock(blockpos1, defaultGongPart.setValue(FACING, state.getValue(FACING)).setValue(Door_Of_Seal_Part_Block.PART, Door_Of_Seal_Part.SIDE_LEFT).setValue(Door_Of_Seal_Part_Block.Y_OFFSET, i), 3);
                level.setBlock(blockpos3, defaultGongPart.setValue(FACING, state.getValue(FACING)).setValue(Door_Of_Seal_Part_Block.PART, Door_Of_Seal_Part.SIDE_RIGHT).setValue(Door_Of_Seal_Part_Block.Y_OFFSET, i), 3);
                level.setBlock(blockpos4, defaultGongPart.setValue(FACING, state.getValue(FACING)).setValue(Door_Of_Seal_Part_Block.PART, Door_Of_Seal_Part.END_LEFT).setValue(Door_Of_Seal_Part_Block.Y_OFFSET, i), 3);
                level.setBlock(blockpos5, defaultGongPart.setValue(FACING, state.getValue(FACING)).setValue(Door_Of_Seal_Part_Block.PART, Door_Of_Seal_Part.END_RIGHT).setValue(Door_Of_Seal_Part_Block.Y_OFFSET, i), 3);
                if (blockpos2 != pos) {
                    level.setBlock(blockpos2, defaultGongPart.setValue(FACING, state.getValue(FACING)).setValue(Door_Of_Seal_Part_Block.PART, Door_Of_Seal_Part.CENTER).setValue(Door_Of_Seal_Part_Block.Y_OFFSET, i), 3);
                }
                level.blockUpdated(abovePos, Blocks.AIR);
                state.updateNeighbourShapes(level, abovePos, 3);
            }
        }

    }

    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            for (int i = 0; i <= 7; i++) {
                BlockPos abovePos = pos.above(i);
                BlockPos blockpos1 = abovePos.relative(state.getValue(FACING).getClockWise());
                BlockPos blockpos2 = abovePos;
                BlockPos blockpos3 = abovePos.relative(state.getValue(FACING).getCounterClockWise());
                BlockPos blockpos4 = abovePos.relative(state.getValue(FACING).getClockWise(),2);
                BlockPos blockpos5 = abovePos.relative(state.getValue(FACING).getCounterClockWise(),2);
                BlockPos[] toBreakPoses = {blockpos1, blockpos2, blockpos3,blockpos4,blockpos5};
                for (BlockPos toBreakPos : toBreakPoses) {
                    BlockState blockstate = level.getBlockState(toBreakPos);
                    if (blockstate.is(ModBlocks.DOOR_OF_SEAL_PART.get())) {
                        level.setBlock(toBreakPos, Blocks.AIR.defaultBlockState(), 35);
                        level.levelEvent(player, 2001, toBreakPos, Block.getId(blockstate));
                    }
                }
            }
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    public enum Door_Of_Seal_Part implements StringRepresentable {
        SIDE_LEFT("side_left"),
        SIDE_RIGHT("side_right"),
        END_LEFT("end_left"),
        END_RIGHT("end_right"),
        CENTER("center");

        private final String name;

        private Door_Of_Seal_Part(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String getSerializedName() {
            return this.name;
        }
    }

    public static class Door_Of_Seal_Part_Block extends HorizontalDirectionalBlock {

        public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
        public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
        public static final EnumProperty<Door_Of_Seal_Part> PART = EnumProperty.create("door_part", Door_Of_Seal_Part.class);
        public static final IntegerProperty Y_OFFSET = IntegerProperty.create("y_offset", 0, 7);

        public Door_Of_Seal_Part_Block(Properties properties) {
            super(properties);
            this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, Boolean.valueOf(false)).setValue(PART, Door_Of_Seal_Part.CENTER).setValue(Y_OFFSET, 0));
        }

        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49751_) {
            p_49751_.add(FACING,OPEN, PART, Y_OFFSET);
        }

        public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
            BlockPos basePos = getBasePos(state, pos);
            BlockState baseState = level.getBlockState(basePos);
            if (baseState.is(ModBlocks.DOOR_OF_SEAL.get())) {
                BlockHitResult baseHitResult = new BlockHitResult(hitResult.getLocation().add(basePos.getX() - pos.getX(), basePos.getY() - pos.getY(), basePos.getZ() - pos.getZ()), hitResult.getDirection(), basePos, hitResult.isInside());
                return baseState.getBlock().use(baseState, level, pos, player, hand, baseHitResult);
            }
            return super.use(state, level, pos, player, hand, hitResult);
        }

        private BlockPos getBasePos(BlockState state, BlockPos pos) {
            BlockPos toReturn = pos.below(state.getValue(Y_OFFSET));
            if (state.getValue(PART) == Door_Of_Seal_Part.SIDE_LEFT) {
                toReturn = toReturn.relative(state.getValue(FACING).getCounterClockWise());
            }
            else if (state.getValue(PART) == Door_Of_Seal_Part.SIDE_RIGHT) {
                toReturn = toReturn.relative(state.getValue(FACING).getClockWise());
            }
            if (state.getValue(PART) == Door_Of_Seal_Part.END_LEFT) {
                toReturn = toReturn.relative(state.getValue(FACING).getCounterClockWise(),2);
            }
            else if (state.getValue(PART) == Door_Of_Seal_Part.END_RIGHT) {
                toReturn = toReturn.relative(state.getValue(FACING).getClockWise(),2);
            }
            return toReturn;
        }

        @Override
        public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
            BlockPos basePos = getBasePos(state, pos);
            BlockState baseState = level.getBlockState(basePos);
            if (baseState.is(ModBlocks.DOOR_OF_SEAL.get())) {
                level.setBlock(basePos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, basePos, Block.getId(state));
            }
        }

        public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor level, BlockPos pos, BlockPos pos1) {
            BlockPos basePos = getBasePos(state, pos);
            BlockState baseState = level.getBlockState(basePos);
            if (!baseState.is(ModBlocks.DOOR_OF_SEAL.get())) {
                return Blocks.AIR.defaultBlockState();
            }
            return super.updateShape(state, direction, state1, level, pos, pos1);
        }


        public boolean isPathfindable(BlockState p_49717_, BlockGetter p_49718_, BlockPos p_49719_, PathComputationType p_49720_) {
            return false;
        }


        public VoxelShape getShape(BlockState p_49755_, BlockGetter p_49756_, BlockPos p_49757_, CollisionContext p_49758_) {
            return CLOSED_SHAPE;
        }

        public RenderShape getRenderShape(BlockState blockState) {
            return RenderShape.MODEL;
        }


        public VoxelShape getBlockSupportShape(BlockState p_253862_, BlockGetter p_254569_, BlockPos p_254197_) {
            if (p_253862_.getValue(OPEN)) {
                return Shapes.empty();
            } else {
                return CLOSED_SHAPE;
            }
        }

        public VoxelShape getCollisionShape(BlockState p_53396_, BlockGetter p_53397_, BlockPos p_53398_, CollisionContext p_53399_) {
            if (p_53396_.getValue(OPEN)) {
                return Shapes.empty();
            } else {
                return CLOSED_SHAPE;
            }
        }

        public VoxelShape getOcclusionShape(BlockState p_53401_, BlockGetter p_53402_, BlockPos p_53403_) {
            return Shapes.empty();
        }

        @Override
        public Item asItem() {
            return ModBlocks.DOOR_OF_SEAL.get().asItem();
        }
    }
}

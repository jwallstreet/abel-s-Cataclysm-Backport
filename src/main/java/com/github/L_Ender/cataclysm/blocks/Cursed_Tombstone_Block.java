package com.github.L_Ender.cataclysm.blocks;


import javax.annotation.Nullable;

import com.github.L_Ender.cataclysm.blockentities.Cursed_tombstone_Entity;
import com.github.L_Ender.cataclysm.init.ModTileentites;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Cursed_Tombstone_Block extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final VoxelShape X_BASE = Block.box(5.0D, 0.0D, 0.0D, 11.0D, 2.0D, 16.0D);
    private static final VoxelShape Z_BASE = Block.box(0.0D, 0.0D, 5.0D, 16.0D, 2.0D, 11.0D);

    private static final VoxelShape X_MID = Block.box(6.0D, 2.0D, 1.0D, 10.0D, 24.0D, 15.0D);
    private static final VoxelShape Z_MID = Block.box(1.0D, 2.0D, 6.0D, 15.0D, 24.0D, 10.0D);

    private static final VoxelShape X_AXIS_AABB = Shapes.or(X_BASE, X_MID);
    private static final VoxelShape Z_AXIS_AABB = Shapes.or(Z_BASE, Z_MID);

    public Cursed_Tombstone_Block() {
        super(Properties.of(Material.STONE)
                .color(MaterialColor.METAL)
                .dynamicShape()
                .strength(-1.0F, 3600000.0F)
                .noLootTable()
                .sound(SoundType.STONE));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)).setValue(POWERED, Boolean.valueOf(false)));
    }


    public BlockState getStateForPlacement(BlockPlaceContext p_48689_) {
        return this.defaultBlockState().setValue(FACING, p_48689_.getHorizontalDirection().getOpposite());
    }


    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }


    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (state.getValue(POWERED)) {
            if (!state.getValue(LIT)) {
                state = state.setValue(LIT, Boolean.valueOf(true));
                worldIn.setBlock(pos, state, 10);
                return InteractionResult.SUCCESS;
            }
        }else{
            player.displayClientMessage(Component.translatable("block.cataclysm.cursed_tombstone.message"), true);
            return InteractionResult.FAIL;
        }
        return InteractionResult.FAIL;
    }



    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_48814_) {
        p_48814_.add(FACING,LIT,POWERED);
    }

    public VoxelShape getShape(BlockState p_48816_, BlockGetter p_48817_, BlockPos p_48818_, CollisionContext p_48819_) {
        Direction direction = p_48816_.getValue(FACING);
        return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new Cursed_tombstone_Entity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152180_, BlockState p_152181_, BlockEntityType<T> p_152182_) {
        return createTickerHelper(p_152182_, ModTileentites.CURSED_TOMBSTONE.get(), Cursed_tombstone_Entity::commonTick);
    }

}
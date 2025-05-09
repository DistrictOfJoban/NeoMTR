package com.lx862.jcm.mod.block.behavior;/*package com.lx862.jcm.mod.block.behavior;

import com.lx862.jcm.mod.util.BlockUtil;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import mtr.mapping.holder.*;

public interface WaterloggableBehavior extends Waterloggable {
    BooleanProperty WATERLOGGED = new BooleanProperty(Properties.WATERLOGGED);

    default FluidState getFluidState(BlockState state) {
        return IBlock.getStatePropertySafe(state, WATERLOGGED) ? new FluidState(Fluids.WATER.getStill(false)) : new FluidState(Fluids.EMPTY.getDefaultState());
    }

    default void updateWaterState(BlockState state, LevelAccessor world, BlockPos pos) {
        if (IBlock.getStatePropertySafe(state, WATERLOGGED)) {
            #if MC_VERSION == "11802"
                world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            #elif MC_VERSION < "11904"
                world.getFluidTickScheduler().schedule(pos.data, Fluids.WATER, Fluids.WATER.getTickRate(world));
            #else
                world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            #endif
        }
    }

    default BlockState getWaterloggedState(BlockState state, Level world, BlockPos pos) {
        if(state == null) return null;
        // TODO Map getFluid?
        return state.setValue(new Property<>(WATERLOGGED), world.getFluidState(pos).getFluid() == Fluids.WATER);
    }
}
*/
package de.ellpeck.actuallyadditions.common.blocks;

import de.ellpeck.actuallyadditions.common.ActuallyAdditions;
import de.ellpeck.actuallyadditions.common.blocks.base.BlockContainerBase;
import de.ellpeck.actuallyadditions.common.inventory.GuiHandler;
import de.ellpeck.actuallyadditions.common.tile.TileEntityDirectionalBreaker;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class BlockDirectionalBreaker extends BlockContainerBase {

    public BlockDirectionalBreaker() {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance(1.5f, 10.0f)
                .harvestTool(ToolType.PICKAXE)
                .sound(SoundType.STONE));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int par2) {
        return new TileEntityDirectionalBreaker();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing par6, float par7, float par8, float par9) {
        if (this.tryToggleRedstone(world, pos, player)) { return true; }
        if (!world.isRemote) {
            TileEntityDirectionalBreaker breaker = (TileEntityDirectionalBreaker) world.getTileEntity(pos);
            if (breaker != null) {
                player.openGui(ActuallyAdditions.INSTANCE, GuiHandler.GuiTypes.DIRECTIONAL_BREAKER.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        }
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
        int rotation = EnumFacing.getDirectionFromEntityLiving(pos, player).ordinal();
        world.setBlockState(pos, this.getStateFromMeta(rotation), 2);

        super.onBlockPlacedBy(world, pos, state, player, stack);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.byIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockDirectional.FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockDirectional.FACING);
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(BlockDirectional.FACING, rot.rotate(state.getValue(BlockDirectional.FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirror) {
        return this.withRotation(state, mirror.toRotation(state.getValue(BlockDirectional.FACING)));
    }
}

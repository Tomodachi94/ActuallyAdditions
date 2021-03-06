package de.ellpeck.actuallyadditions.common.blocks;

import de.ellpeck.actuallyadditions.common.ActuallyAdditions;
import de.ellpeck.actuallyadditions.common.blocks.base.BlockContainerBase;
import de.ellpeck.actuallyadditions.common.inventory.GuiHandler;
import de.ellpeck.actuallyadditions.common.tile.TileEntityMiner;
import de.ellpeck.actuallyadditions.common.util.StringUtil;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMiner extends BlockContainerBase implements IHudDisplay {

    public BlockMiner() {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance(8f, 30f)
                .harvestTool(ToolType.PICKAXE)
                .sound(SoundType.STONE));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing par6, float par7, float par8, float par9) {
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityMiner) {
                player.openGui(ActuallyAdditions.INSTANCE, GuiHandler.GuiTypes.MINER.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityMiner();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void displayHud(Minecraft minecraft, EntityPlayer player, ItemStack stack, RayTraceResult posHit, ScaledResolution resolution) {
        TileEntity tile = minecraft.world.getTileEntity(posHit.getBlockPos());
        if (tile instanceof TileEntityMiner) {
            TileEntityMiner miner = (TileEntityMiner) tile;
            String info = miner.checkY == 0 ? "Done Mining!" : miner.checkY == -1 ? "Calculating positions..." : "Mining at " + (miner.getPos().getX() + miner.checkX) + ", " + miner.checkY + ", " + (miner.getPos().getZ() + miner.checkZ) + ".";
            minecraft.fontRenderer.drawStringWithShadow(info, resolution.getScaledWidth() / 2 + 5, resolution.getScaledHeight() / 2 - 20, StringUtil.DECIMAL_COLOR_WHITE);
        }
    }
}
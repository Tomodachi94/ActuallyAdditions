package de.ellpeck.actuallyadditions.common.blocks;

import de.ellpeck.actuallyadditions.common.blocks.base.BlockContainerBase;
import de.ellpeck.actuallyadditions.common.tile.TileEntityPlayerInterface;
import de.ellpeck.actuallyadditions.common.util.StringUtil;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPlayerInterface extends BlockContainerBase implements IHudDisplay {

    public BlockPlayerInterface() {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance(4.5f, 10.0f)
                .harvestTool(ToolType.PICKAXE)
                .sound(SoundType.STONE));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int par2) {
        return new TileEntityPlayerInterface();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityPlayerInterface) {
            TileEntityPlayerInterface face = (TileEntityPlayerInterface) tile;
            if (face.connectedPlayer == null) {
                face.connectedPlayer = player.getUniqueID();
                face.playerName = player.getName();
                face.markDirty();
                face.sendUpdate();
            }
        }

        super.onBlockPlacedBy(world, pos, state, player, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void displayHud(Minecraft minecraft, EntityPlayer player, ItemStack stack, RayTraceResult posHit, ScaledResolution resolution) {
        TileEntity tile = minecraft.world.getTileEntity(posHit.getBlockPos());
        if (tile != null) {
            if (tile instanceof TileEntityPlayerInterface) {
                TileEntityPlayerInterface face = (TileEntityPlayerInterface) tile;
                String name = face.playerName == null ? "Unknown" : face.playerName;
                minecraft.fontRenderer.drawStringWithShadow("Bound to: " + TextFormatting.RED + name, resolution.getScaledWidth() / 2 + 5, resolution.getScaledHeight() / 2 + 5, StringUtil.DECIMAL_COLOR_WHITE);
                minecraft.fontRenderer.drawStringWithShadow("UUID: " + TextFormatting.DARK_GREEN + face.connectedPlayer, resolution.getScaledWidth() / 2 + 5, resolution.getScaledHeight() / 2 + 15, StringUtil.DECIMAL_COLOR_WHITE);
            }
        }
    }
}

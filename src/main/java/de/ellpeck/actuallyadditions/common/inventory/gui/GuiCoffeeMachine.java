package de.ellpeck.actuallyadditions.common.inventory.gui;

import java.util.Collections;

import de.ellpeck.actuallyadditions.common.ActuallyAdditions;
import de.ellpeck.actuallyadditions.common.inventory.ContainerCoffeeMachine;
import de.ellpeck.actuallyadditions.common.network.PacketHandlerHelper;
import de.ellpeck.actuallyadditions.common.tile.TileEntityBase;
import de.ellpeck.actuallyadditions.common.tile.TileEntityCoffeeMachine;
import de.ellpeck.actuallyadditions.common.util.AssetUtil;
import de.ellpeck.actuallyadditions.common.util.StringUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCoffeeMachine extends GuiWtfMojang {

    private static final ResourceLocation RES_LOC = AssetUtil.getGuiLocation("gui_coffee_machine");
    private final TileEntityCoffeeMachine machine;

    private EnergyDisplay energy;
    private FluidDisplay fluid;

    public GuiCoffeeMachine(InventoryPlayer inventory, TileEntityBase tile) {
        super(new ContainerCoffeeMachine(inventory, tile));
        this.machine = (TileEntityCoffeeMachine) tile;
        this.xSize = 176;
        this.ySize = 93 + 86;
    }

    @Override
    public void initGui() {
        super.initGui();

        GuiButton buttonOkay = new GuiButton(0, this.guiLeft + 60, this.guiTop + 11, 58, 20, StringUtil.localize("info." + ActuallyAdditions.MODID + ".gui.ok"));
        this.buttonList.add(buttonOkay);

        this.energy = new EnergyDisplay(this.guiLeft + 16, this.guiTop + 5, this.machine.storage);
        this.fluid = new FluidDisplay(this.guiLeft - 30, this.guiTop + 1, this.machine.tank, true, false);
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        super.drawScreen(x, y, f);

        String text2 = this.machine.coffeeCacheAmount + "/" + TileEntityCoffeeMachine.COFFEE_CACHE_MAX_AMOUNT + " " + StringUtil.localize("info." + ActuallyAdditions.MODID + ".gui.coffee");
        if (x >= this.guiLeft + 40 && y >= this.guiTop + 25 && x <= this.guiLeft + 49 && y <= this.guiTop + 56) {
            this.drawHoveringText(Collections.singletonList(text2), x, y);
        }

        this.energy.drawOverlay(x, y);
        this.fluid.drawOverlay(x, y);
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        AssetUtil.displayNameString(this.fontRenderer, this.xSize, -10, this.machine);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(AssetUtil.GUI_INVENTORY_LOCATION);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop + 93, 0, 0, 176, 86);

        this.mc.getTextureManager().bindTexture(RES_LOC);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 93);

        if (this.machine.coffeeCacheAmount > 0) {
            int i = this.machine.getCoffeeScaled(30);
            this.drawTexturedModalRect(this.guiLeft + 41, this.guiTop + 56 - i, 192, 0, 8, i);
        }

        if (this.machine.brewTime > 0) {
            int i = this.machine.getBrewScaled(23);
            this.drawTexturedModalRect(this.guiLeft + 53, this.guiTop + 42, 192, 30, i, 16);

            int j = this.machine.getBrewScaled(26);
            this.drawTexturedModalRect(this.guiLeft + 99 + 25 - j, this.guiTop + 44, 192 + 25 - j, 46, j, 12);
        }

        this.energy.draw();
        this.fluid.draw();
    }

    @Override
    public void actionPerformed(GuiButton button) {
        PacketHandlerHelper.sendButtonPacket(this.machine, button.id);
    }
}
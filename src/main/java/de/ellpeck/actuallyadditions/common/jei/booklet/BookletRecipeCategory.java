package de.ellpeck.actuallyadditions.common.jei.booklet;

import java.util.ArrayList;
import java.util.List;

import de.ellpeck.actuallyadditions.common.ActuallyAdditions;
import de.ellpeck.actuallyadditions.common.util.StringUtil;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;

public class BookletRecipeCategory implements IRecipeCategory<BookletRecipeWrapper> {

    public static final String NAME = "actuallyadditions.booklet";

    private final IDrawable background;

    public BookletRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(160, 105);
    }

    @Override
    public String getUid() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return StringUtil.localize("container.nei." + NAME + ".name");
    }

    @Override
    public String getModName() {
        return ActuallyAdditions.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, BookletRecipeWrapper wrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 70, -4);

        List<ItemStack> list = new ArrayList<>();
        wrapper.thePage.getItemStacksForPage(list);
        recipeLayout.getItemStacks().set(0, list);
    }
}

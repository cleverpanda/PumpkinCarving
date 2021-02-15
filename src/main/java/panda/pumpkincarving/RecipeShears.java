package panda.pumpkincarving;

import java.util.List;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeShears implements IRecipeFactory{

	@Override
	public IRecipe parse(JsonContext context, JsonObject json) {
		ShapedOreRecipe recipe = ShapedOreRecipe.factory(context, json);

        ShapedPrimer primer = new ShapedPrimer();
        primer.width = recipe.getRecipeWidth();
        primer.height = recipe.getRecipeHeight();
        primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
        primer.input = recipe.getIngredients();

        return new ShapedShearsRecipe(recipe.getRecipeOutput(), primer);
    }

    public static class ShapedShearsRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    	private final ItemStack recipeOutput;
        public final List<Ingredient> recipeItems;
    	private int recipeWidth = 2;
    	private int recipeHeight = 2;
    	
    	public ShapedShearsRecipe(ItemStack result, ShapedPrimer primer) {
    		this.recipeOutput = result;
            this.recipeItems = primer.input;
        }

        @Nullable
        public ItemStack getRecipeOutput()
        {
            return this.recipeOutput;
        }

        public boolean matches(InventoryCrafting inv, World worldIn)
        {
            for (int i = 0; i <= 3 - this.recipeWidth; ++i)
            {
                for (int j = 0; j <= 3 - this.recipeHeight; ++j)
                {
                    if (this.checkMatch(inv, i, j, true))
                    {
                        return true;
                    }

                    if (this.checkMatch(inv, i, j, false))
                    {
                        return true;
                    }
                }
            }

            return false;
        }

        
        private boolean checkMatch(InventoryCrafting inv, int x, int y, boolean test)
        {
            for (int i = 0; i < 3; ++i)
            {
                for (int j = 0; j < 3; ++j)
                {
                    int k = i - x;
                    int l = j - y;
                    Ingredient ingredient = Ingredient.EMPTY;

                    if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight)
                    {
                        if (test)
                        {
                            ingredient = this.recipeItems.get(this.recipeWidth - k - 1 + l * this.recipeWidth);
                        }
                        else
                        {
                            ingredient = this.recipeItems.get(k + l * this.recipeWidth);
                        }
                    }

                    if (!ingredient.apply(inv.getStackInRowAndColumn(i, j)))
                    {
                        return false;
                    }
                }
            }

            return true;
        }


        @Nullable
        public ItemStack getCraftingResult(InventoryCrafting inv)
        {
            return this.recipeOutput.copy();
        }

        public int getRecipeSize()
        {
            return this.recipeItems.size();
        }
        
    	@Override
    	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
    		
    		NonNullList<ItemStack> aitemstack = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);

            for (int i = 0; i < aitemstack.size(); ++i)
            {
                ItemStack itemstack = inv.getStackInSlot(i);
                if(!itemstack.isEmpty()){
                	if(itemstack.getItem() == Items.SHEARS || OreDictionary.getOres("toolShears").contains(itemstack)){
                    	ItemStack shearscopy = itemstack.copy();

                    	if(shearscopy.attemptDamageItem(1, Minecraft.getMinecraft().world.rand, null)){
                    		ForgeEventFactory.onPlayerDestroyItem(ForgeHooks.getCraftingPlayer(), itemstack, null);
                    		aitemstack.set(i,ItemStack.EMPTY);
                    	}else{
                    		aitemstack.set(i, shearscopy);
                    	}
                }
                }else{
                	aitemstack.set(i, itemstack);
                } 
            }

            return aitemstack;
    	}

    	@Override
        public boolean canFit(int width, int height)
        {
            return width * height >= 1;
        }
    } 
}
package panda.pumpkincarving;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeShears implements IRecipeFactory {

	@Override
	public IRecipe parse(JsonContext context, JsonObject json) {
		ShapedOreRecipe recipe = ShapedOreRecipe.factory(context, json);

		ShapedPrimer primer = new ShapedPrimer();
		primer.width = recipe.getRecipeWidth();
		primer.height = recipe.getRecipeHeight();
		primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
		primer.input = recipe.getIngredients();

		return new ShapedShearsRecipe(null, recipe.getRecipeOutput(), primer);
	}

	public static class ShapedShearsRecipe extends ShapedOreRecipe {
		public ShapedShearsRecipe(ResourceLocation group, @Nonnull ItemStack result, ShapedPrimer primer) {
			super(group, result, primer);
		}

		@Override
		public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {

			NonNullList<ItemStack> aitemstack = NonNullList.<ItemStack>withSize(inv.getSizeInventory(),
					ItemStack.EMPTY);

			for (int i = 0; i < aitemstack.size(); ++i) {
				ItemStack itemstack = inv.getStackInSlot(i);
				if (!itemstack.isEmpty()) {
					if (itemstack.getItem() == Items.SHEARS
							|| OreDictionary.getOres("toolShears").contains(itemstack)) {
						ItemStack shearscopy = itemstack.copy();

						if (shearscopy.attemptDamageItem(1, Minecraft.getMinecraft().world.rand, null)) {
							ForgeEventFactory.onPlayerDestroyItem(ForgeHooks.getCraftingPlayer(), itemstack, null);
							aitemstack.set(i, ItemStack.EMPTY);
						} else {
							aitemstack.set(i, shearscopy);
						}
					}
				} else {
					aitemstack.set(i, itemstack);
				}
			}

			return aitemstack;
		}
	}
}
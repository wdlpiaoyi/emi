package dev.emi.emi.recipe;

import java.util.List;

import com.google.common.collect.Lists;

import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.runtime.EmiLog;
import dev.emi.emi.runtime.ProxyRecipeManager;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.ShapedRecipe;

public class EmiShapedRecipe extends EmiCraftingRecipe {

	public EmiShapedRecipe(ShapedRecipe recipe) {
		super(padIngredients(recipe), EmiStack.of(EmiPort.getOutput(recipe)), ProxyRecipeManager.getId(recipe), false);
		setRemainders(input, recipe);
	}

	public static void setRemainders(List<EmiIngredient> input, CraftingRecipe recipe) {
		try {
			CraftingInventory inv = EmiUtil.getCraftingInventory();
			// Vanilla recipes return a full 3x3 list indexed by slot, custom ones may return a shorter,
			// compactly indexed list (eg. width * height). 3 keeps padded indices for shapeless recipes.
			int recipeWidth = recipe instanceof ShapedRecipe shaped ? shaped.getWidth() : 3;
			for (int i = 0; i < input.size(); i++) {
				if (input.get(i).isEmpty()) {
					continue;
				}
				for (int j = 0; j < input.size(); j++) {
					if (j == i) {
						continue;
					}
					if (!input.get(j).isEmpty()) {
						inv.setStack(j, input.get(j).getEmiStacks().get(0).getItemStack().copy());
					}
				}
				List<EmiStack> stacks = input.get(i).getEmiStacks();
				for (EmiStack stack : stacks) {
					inv.setStack(i, stack.getItemStack().copy());
					List<ItemStack> remainders = recipe.getRemainder(inv);
					int index = remainders.size() >= input.size() ? i : (i / 3) * recipeWidth + (i % 3);
					if (index < remainders.size()) {
						ItemStack remainder = remainders.get(index);
						if (!remainder.isEmpty()) {
							stack.setRemainder(EmiStack.of(remainder));
						}
					}
				}
				inv.clear();
			}
		} catch (Exception e) {
			EmiLog.error("Exception thrown setting remainders for " + ProxyRecipeManager.getId(recipe), e);
		}
	}

	private static List<EmiIngredient> padIngredients(ShapedRecipe recipe) {
		List<EmiIngredient> list = Lists.newArrayList();
		int i = 0;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				if (x >= recipe.getWidth() || y >= recipe.getHeight() || i >= recipe.getIngredients().size()) {
					list.add(EmiStack.EMPTY);
				} else {
					list.add(EmiIngredient.of(recipe.getIngredients().get(i++)));
				}
			}
		}
		return list;
	}
}

package dev.emi.emi.jemi.impl;

import java.util.List;

import org.jetbrains.annotations.Unmodifiable;

import com.google.common.collect.Lists;

import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawablesView;

public class JemiRecipeSlotDrawablesView implements IRecipeSlotDrawablesView {
	public final List<IRecipeSlotDrawable> slots = Lists.newArrayList();

	@Override
	public @Unmodifiable List<IRecipeSlotDrawable> getSlots() {
		return slots;
	}
}

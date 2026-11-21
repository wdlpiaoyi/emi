package dev.emi.emi.jemi.impl;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IJeiKeyMapping;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

public class JemiTooltipBuilder implements ITooltipBuilder {
	public final List<Either<StringVisitable, TooltipData>> lines = Lists.newArrayList();

	@Override
	public void add(StringVisitable component) {
		lines.add(Either.left(component));
	}

	@Override
	public void addAll(Collection<? extends StringVisitable> components) {
		for (StringVisitable v : components) {
			add(v);
		}
	}

	@Override
	public void add(TooltipData data) {
		lines.add(Either.right(data));
	}

	@Override
	public void addKeyUsageComponent(String translationKey, IJeiKeyMapping keyMapping) {
		add(Text.translatable(translationKey, keyMapping.getTranslatedKeyMessage()));
	}

	@Override
	public void setIngredient(ITypedIngredient<?> typedIngredient) {
		// EMI's methods bypass the vanilla tooltip render which accepts a stack, so this will do nothing
	}

	@Override
	public void clear() {
		lines.clear();
	}

	@Override
	public void clearIngredient() {
		// No ingredient is stored
	}

	@Override
	public List<Either<StringVisitable, TooltipData>> getLines() {
		return lines;
	}

	@Override
	public List<Text> toLegacyToComponents() {
		List<Text> legacy = Lists.newArrayList();
		for (Either<StringVisitable, TooltipData> line : lines) {
			if (line.left().orElse(null) instanceof Text text) {
				legacy.add(text);
			}
		}
		return legacy;
	}

	@Override
	public void removeAll(List<Text> components) {
		// EMI does not support tooltip removeal
	}

	/**
	 * Convert the stored lines into components the vanilla tooltip renderer can display.
	 * Derived from {@link #getLines()} so changes made by callbacks are reflected.
	 */
	public List<TooltipComponent> getTooltipComponents() {
		List<TooltipComponent> tooltip = Lists.newArrayList();
		for (Either<StringVisitable, TooltipData> line : lines) {
			line.ifLeft(text -> {
				if (text instanceof Text t) {
					tooltip.add(TooltipComponent.of(t.asOrderedText()));
				}
			});
			line.ifRight(data -> {
				try {
					tooltip.add(TooltipComponent.of(data));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		return tooltip;
	}
}

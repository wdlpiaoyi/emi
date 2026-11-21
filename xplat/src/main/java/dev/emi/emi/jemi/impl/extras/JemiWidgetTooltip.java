package dev.emi.emi.jemi.impl.extras;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import dev.emi.emi.jemi.impl.JemiTooltipBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidgetTooltipCallback;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

/**
 * Tooltip state shared by the widget implementations JEMI creates for JEI extras.
 */
public class JemiWidgetTooltip {
	private final List<StringVisitable> lines = Lists.newArrayList();
	private final List<TooltipData> data = Lists.newArrayList();
	private IRecipeWidgetTooltipCallback callback;

	public void add(StringVisitable text) {
		lines.add(text);
	}

	public void addAll(Collection<? extends StringVisitable> texts) {
		lines.addAll(texts);
	}

	public void add(TooltipData component) {
		data.add(component);
	}

	public void callback(IRecipeWidgetTooltipCallback tooltipCallback) {
		callback = tooltipCallback;
	}

	public List<TooltipComponent> build() {
		List<TooltipComponent> tooltip = Lists.newArrayList();
		for (StringVisitable line : lines) {
			if (line instanceof Text text) {
				tooltip.add(TooltipComponent.of(text.asOrderedText()));
			}
		}
		for (TooltipData component : data) {
			try {
				tooltip.add(TooltipComponent.of(component));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (callback != null) {
			try {
				JemiTooltipBuilder builder = new JemiTooltipBuilder();
				callback.onTooltip(builder);
				tooltip.addAll(builder.getTooltipComponents());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tooltip;
	}
}

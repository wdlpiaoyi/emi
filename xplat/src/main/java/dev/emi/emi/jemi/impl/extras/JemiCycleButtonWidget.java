package dev.emi.emi.jemi.impl.extras;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.Lists;

import dev.emi.emi.api.widget.WidgetHolder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.ICycleButtonWidget;
import mezz.jei.api.gui.widgets.IRecipeWidgetTooltipCallback;
import net.minecraft.client.item.TooltipData;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

public class JemiCycleButtonWidget<T> implements ICycleButtonWidget<T>, JemiExtraWidget {
	public int x = 0, y = 0;
	public int width, height;
	public final List<T> values = Lists.newArrayList();
	public T value;
	public final Consumer<T> onValueChanged;
	public Text message;
	public Function<T, Text> messageFunction;
	public IDrawable icon;
	public Function<T, IDrawable> iconFunction;
	public boolean active = true, visible = true;
	private final JemiWidgetTooltip tooltip = new JemiWidgetTooltip();

	public JemiCycleButtonWidget(List<T> values, T initialValue, int width, int height, Consumer<T> onValueChanged) {
		this.values.addAll(values);
		this.value = initialValue;
		this.width = width;
		this.height = height;
		this.onValueChanged = onValueChanged;
	}

	@Override
	public JemiCycleButtonWidget<T> setPosition(int xPos, int yPos) {
		this.x = xPos;
		this.y = yPos;
		return this;
	}

	@Override
	public JemiCycleButtonWidget<T> setPosition(int areaX, int areaY, int areaWidth, int areaHeight,
			HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		this.x = areaX + horizontalAlignment.getXPos(areaWidth, width);
		this.y = areaY + verticalAlignment.getYPos(areaHeight, height);
		return this;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public JemiCycleButtonWidget<T> setMessage(Text message) {
		this.message = message;
		this.messageFunction = null;
		return this;
	}

	@Override
	public JemiCycleButtonWidget<T> setMessage(Function<T, Text> valueText) {
		this.messageFunction = valueText;
		this.message = null;
		return this;
	}

	@Override
	public JemiCycleButtonWidget<T> setIcon(IDrawable icon) {
		this.icon = icon;
		this.iconFunction = null;
		return this;
	}

	@Override
	public JemiCycleButtonWidget<T> setIcon(Function<T, IDrawable> valueIcon) {
		this.iconFunction = valueIcon;
		this.icon = null;
		return this;
	}

	@Override
	public JemiCycleButtonWidget<T> setActive(boolean active) {
		this.active = active;
		return this;
	}

	@Override
	public JemiCycleButtonWidget<T> setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public JemiCycleButtonWidget<T> setValue(T value) {
		if (!values.contains(value)) {
			throw new IllegalArgumentException("Value is not one of the supplied values");
		}
		this.value = value;
		return this;
	}

	@Override
	public JemiCycleButtonWidget<T> setTooltip(StringVisitable tooltip) {
		this.tooltip.add(tooltip);
		return this;
	}

	@Override
	public JemiCycleButtonWidget<T> setTooltip(Collection<? extends StringVisitable> tooltip) {
		this.tooltip.addAll(tooltip);
		return this;
	}

	@Override
	public JemiCycleButtonWidget<T> setTooltip(TooltipData tooltip) {
		this.tooltip.add(tooltip);
		return this;
	}

	@Override
	public JemiCycleButtonWidget<T> setTooltip(IRecipeWidgetTooltipCallback tooltipCallback) {
		this.tooltip.callback(tooltipCallback);
		return this;
	}

	@Override
	public void addWidgets(WidgetHolder holder) {
		if (!visible) {
			return;
		}
		IDrawable shownIcon = iconFunction != null ? iconFunction.apply(value) : icon;
		Text shownMessage = messageFunction != null ? messageFunction.apply(value) : message;
		if (shownIcon != null) {
			holder.addDrawable(x, y, width, height, (raw, mouseX, mouseY, delta) -> {
				shownIcon.draw(raw, x, y);
			});
		} else if (shownMessage != null) {
			holder.addText(shownMessage, x, y, 0xffffffff, true);
		}
		holder.addTooltip((mouseX, mouseY) -> tooltip.build(), x, y, width, height);
	}
}

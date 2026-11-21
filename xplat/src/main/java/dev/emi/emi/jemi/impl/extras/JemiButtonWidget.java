package dev.emi.emi.jemi.impl.extras;

import java.util.Collection;

import dev.emi.emi.api.widget.WidgetHolder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IButtonWidget;
import mezz.jei.api.gui.widgets.IRecipeWidgetTooltipCallback;
import net.minecraft.client.item.TooltipData;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

public class JemiButtonWidget implements IButtonWidget<JemiButtonWidget>, JemiExtraWidget {
	public int x = 0, y = 0;
	public int width, height;
	public Text message;
	public IDrawable icon;
	public boolean active = true, visible = true;
	public final Runnable onPress;
	private final JemiWidgetTooltip tooltip = new JemiWidgetTooltip();

	public JemiButtonWidget(int width, int height, Runnable onPress) {
		this.width = width;
		this.height = height;
		this.onPress = onPress;
	}

	@Override
	public JemiButtonWidget setPosition(int xPos, int yPos) {
		this.x = xPos;
		this.y = yPos;
		return this;
	}

	@Override
	public JemiButtonWidget setPosition(int areaX, int areaY, int areaWidth, int areaHeight,
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
	public JemiButtonWidget setMessage(Text message) {
		this.message = message;
		return this;
	}

	@Override
	public JemiButtonWidget setIcon(IDrawable icon) {
		this.icon = icon;
		return this;
	}

	@Override
	public JemiButtonWidget setActive(boolean active) {
		this.active = active;
		return this;
	}

	@Override
	public JemiButtonWidget setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

	@Override
	public JemiButtonWidget setTooltip(StringVisitable tooltip) {
		this.tooltip.add(tooltip);
		return this;
	}

	@Override
	public JemiButtonWidget setTooltip(Collection<? extends StringVisitable> tooltip) {
		this.tooltip.addAll(tooltip);
		return this;
	}

	@Override
	public JemiButtonWidget setTooltip(TooltipData tooltip) {
		this.tooltip.add(tooltip);
		return this;
	}

	@Override
	public JemiButtonWidget setTooltip(IRecipeWidgetTooltipCallback tooltipCallback) {
		this.tooltip.callback(tooltipCallback);
		return this;
	}

	@Override
	public void addWidgets(WidgetHolder holder) {
		if (!visible) {
			return;
		}
		if (icon != null) {
			holder.addDrawable(x, y, width, height, (raw, mouseX, mouseY, delta) -> {
				// EMI translates the draw context to the widget position, so draw locally
				icon.draw(raw);
			});
		} else if (message != null) {
			holder.addText(message, x, y, 0xffffffff, true);
		}
		holder.addTooltip((mouseX, mouseY) -> tooltip.build(), x, y, width, height);
	}
}

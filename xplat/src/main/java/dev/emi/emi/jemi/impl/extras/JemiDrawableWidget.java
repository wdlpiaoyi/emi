package dev.emi.emi.jemi.impl.extras;

import java.util.Collection;

import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.widget.WidgetHolder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IDrawableWidget;
import mezz.jei.api.gui.widgets.IRecipeWidgetTooltipCallback;
import net.minecraft.client.item.TooltipData;
import net.minecraft.text.StringVisitable;

public class JemiDrawableWidget implements IDrawableWidget, JemiExtraWidget {
	public int x = 0, y = 0;
	public int width, height;
	private final IDrawable drawable;
	private final EmiTexture texture;
	private final EmiTexture animated;
	private final int time;
	private final boolean horizontal, endToStart, fullToEmpty;
	private final JemiWidgetTooltip tooltip = new JemiWidgetTooltip();

	public JemiDrawableWidget(IDrawable drawable) {
		this(drawable, drawable.getWidth(), drawable.getHeight());
	}

	public JemiDrawableWidget(IDrawable drawable, int width, int height) {
		this.drawable = drawable;
		this.texture = null;
		this.animated = null;
		this.time = 0;
		this.horizontal = false;
		this.endToStart = false;
		this.fullToEmpty = false;
		this.width = width;
		this.height = height;
	}

	public JemiDrawableWidget(EmiTexture texture) {
		this(texture, null, 0, false, false, false);
	}

	public JemiDrawableWidget(EmiTexture texture, EmiTexture animated, int time, boolean horizontal, boolean endToStart, boolean fullToEmpty) {
		this.drawable = null;
		this.texture = texture;
		this.animated = animated;
		this.time = time;
		this.horizontal = horizontal;
		this.endToStart = endToStart;
		this.fullToEmpty = fullToEmpty;
		this.width = texture.width;
		this.height = texture.height;
	}

	@Override
	public JemiDrawableWidget setPosition(int xPos, int yPos) {
		this.x = xPos;
		this.y = yPos;
		return this;
	}

	@Override
	public JemiDrawableWidget setPosition(int areaX, int areaY, int areaWidth, int areaHeight,
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
	public JemiDrawableWidget setTooltip(StringVisitable tooltip) {
		this.tooltip.add(tooltip);
		return this;
	}

	@Override
	public JemiDrawableWidget setTooltip(Collection<? extends StringVisitable> tooltip) {
		this.tooltip.addAll(tooltip);
		return this;
	}

	@Override
	public JemiDrawableWidget setTooltip(TooltipData tooltip) {
		this.tooltip.add(tooltip);
		return this;
	}

	@Override
	public JemiDrawableWidget setTooltip(IRecipeWidgetTooltipCallback tooltipCallback) {
		this.tooltip.callback(tooltipCallback);
		return this;
	}

	@Override
	public void addWidgets(WidgetHolder holder) {
		if (animated != null) {
			holder.addTexture(texture, x, y);
			holder.addAnimatedTexture(animated, x, y, time, horizontal, endToStart, fullToEmpty);
		} else if (texture != null) {
			holder.addTexture(texture, x, y);
		} else if (drawable != null) {
			holder.addDrawable(x, y, width, height, (raw, mouseX, mouseY, delta) -> {
				drawable.draw(raw, x, y);
			});
		}
		holder.addTooltip((mouseX, mouseY) -> tooltip.build(), x, y, width, height);
	}
}

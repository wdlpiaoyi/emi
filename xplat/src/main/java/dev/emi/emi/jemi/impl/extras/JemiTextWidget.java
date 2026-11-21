package dev.emi.emi.jemi.impl.extras;

import java.util.Collection;
import java.util.List;

import dev.emi.emi.api.widget.WidgetHolder;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeWidgetTooltipCallback;
import mezz.jei.api.gui.widgets.ITextWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.item.TooltipData;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

public class JemiTextWidget extends JemiPlaceable<ITextWidget> implements ITextWidget, JemiExtraWidget {
	public int color = 0xffffffff;
	public boolean shadow = true;
	public int spacing = 0;
	public HorizontalAlignment horizontal = HorizontalAlignment.LEFT;
	public VerticalAlignment vertical = VerticalAlignment.TOP;
	public List<StringVisitable> text;
	private final JemiWidgetTooltip tooltip = new JemiWidgetTooltip();

	public JemiTextWidget(List<StringVisitable> text, int width, int height) {
		super(width, height);
		this.text = text;
	}

	@Override
	public ITextWidget setFont(TextRenderer font) {
		// Unimplemented
		return this;
	}

	@Override
	public ITextWidget setColor(int color) {
		this.color = color;
		return this;
	}

	@Override
	public ITextWidget setLineSpacing(int spacing) {
		this.spacing = spacing;
		return this;
	}

	@Override
	public ITextWidget setShadow(boolean shadow) {
		this.shadow = shadow;
		return this;
	}

	@Override
	public ITextWidget setTextAlignment(HorizontalAlignment horizontalAlignment) {
		this.horizontal = horizontalAlignment;
		return this;
	}

	@Override
	public ITextWidget setTextAlignment(VerticalAlignment verticalAlignment) {
		this.vertical = verticalAlignment;
		return this;
	}

	@Override
	public ITextWidget setTooltip(StringVisitable tooltip) {
		this.tooltip.add(tooltip);
		return this;
	}

	@Override
	public ITextWidget setTooltip(Collection<? extends StringVisitable> tooltip) {
		this.tooltip.addAll(tooltip);
		return this;
	}

	@Override
	public ITextWidget setTooltip(TooltipData tooltip) {
		this.tooltip.add(tooltip);
		return this;
	}

	@Override
	public ITextWidget setTooltip(IRecipeWidgetTooltipCallback tooltipCallback) {
		this.tooltip.callback(tooltipCallback);
		return this;
	}

	@Override
	public void addWidgets(WidgetHolder holder) {
		if (text == null || text.isEmpty()) {
			return;
		}
		TextRenderer font = MinecraftClient.getInstance().textRenderer;
		int lineHeight = 9 + spacing;
		int totalHeight = text.size() * lineHeight - spacing;
		int yOff = switch (vertical) {
			case TOP -> 0;
			case CENTER -> (height - totalHeight) / 2;
			case BOTTOM -> height - totalHeight;
		};
		for (int i = 0; i < text.size(); i++) {
			StringVisitable line = text.get(i);
			if (!(line instanceof Text textLine)) {
				continue;
			}
			int xOff = switch (horizontal) {
				case LEFT -> 0;
				case CENTER -> (width - font.getWidth(textLine)) / 2;
				case RIGHT -> width - font.getWidth(textLine);
			};
			holder.addText(textLine, x + xOff, y + yOff + i * lineHeight, color, shadow);
		}
		holder.addTooltip((mouseX, mouseY) -> tooltip.build(), x, y, width, height);
	}
}

package dev.emi.emi.jemi.impl.extras;

import dev.emi.emi.api.widget.WidgetHolder;

/**
 * A JEMI extra that can add itself to an EMI recipe screen.
 */
public interface JemiExtraWidget {

	void addWidgets(WidgetHolder holder);
}

package gps;

import java.awt.Font;
import net.runelite.client.config.FontType;
import net.runelite.client.ui.FontManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The directions overlay's three fonts (current step, next step, the rest): the native RuneScape
 * faces by default, or a custom font from RuneLite's font editor on every line with the current
 * step still bold (feature request 2026-11-04; the size presets went with it to save hub tokens).
 */
public class OverlayFontsTest
{
	@Test
	public void theNativeFacesKeepTheirHierarchy()
	{
		Font[] fonts = RouteDirectionsOverlay.fontsFor(null);
		assertEquals(FontManager.getRunescapeBoldFont(), fonts[0]);
		assertEquals(FontManager.getRunescapeFont(), fonts[1]);
		assertEquals(FontManager.getRunescapeSmallFont(), fonts[2]);
	}

	@Test
	public void aCustomFontDrawsEveryLineWithTheCurrentStepBold()
	{
		Font[] fonts = RouteDirectionsOverlay.fontsFor(FontType.REGULAR.withFamily("Dialog").withSize(14));
		assertEquals("Dialog", fonts[0].getFamily());
		assertEquals(14, fonts[0].getSize());
		assertTrue("the current step keeps its bold weight", fonts[0].isBold());
		assertEquals(14, fonts[1].getSize());
		assertEquals(14, fonts[2].getSize());
	}
}

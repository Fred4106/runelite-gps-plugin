package gps;

import java.awt.Font;
import net.runelite.client.config.FontType;
import net.runelite.client.ui.FontManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The directions overlay's three fonts (current step, next step, the rest): the text-size presets
 * map onto the RuneScape faces at sizes that render crisply, and a custom font from RuneLite's
 * font editor replaces them all, with the current step still bold (feature request 2026-11-04).
 */
public class OverlayFontsTest
{
	@Test
	public void presetsKeepTheRuneScapeHierarchy()
	{
		Font[] fonts = RouteDirectionsOverlay.fontsFor(OverlayFontSize.NORMAL, null, false);
		assertEquals(FontManager.getRunescapeBoldFont(), fonts[0]);
		assertEquals(FontManager.getRunescapeFont(), fonts[1]);
		assertEquals(FontManager.getRunescapeSmallFont(), fonts[2]);
		Font[] doubled = RouteDirectionsOverlay.fontsFor(OverlayFontSize.EXTRA_LARGE, null, false);
		assertEquals(32f, doubled[0].getSize2D(), 0f);
	}

	@Test
	public void aCustomFontDrawsEveryLineWithTheCurrentStepBold()
	{
		FontType custom = FontType.REGULAR.withFamily("Dialog").withSize(14);
		Font[] fonts = RouteDirectionsOverlay.fontsFor(OverlayFontSize.EXTRA_LARGE, custom, true);
		assertEquals("Dialog", fonts[0].getFamily());
		assertEquals(14, fonts[0].getSize());
		assertTrue("the current step keeps its bold weight", fonts[0].isBold());
		assertEquals(14, fonts[1].getSize());
		assertEquals(14, fonts[2].getSize());
	}

	@Test
	public void theCustomFontIsIgnoredWhileTheSwitchIsOff()
	{
		FontType custom = FontType.REGULAR.withFamily("Dialog").withSize(14);
		Font[] fonts = RouteDirectionsOverlay.fontsFor(OverlayFontSize.NORMAL, custom, false);
		assertEquals(FontManager.getRunescapeBoldFont(), fonts[0]);
	}
}

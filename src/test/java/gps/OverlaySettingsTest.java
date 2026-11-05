package gps;

import java.awt.Color;
import net.runelite.client.config.FontType;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.lenient;

/**
 * Plan step L24: the overlays' display settings as one snapshot, out of the plugin class. The
 * snapshot copies the config, a plugin-message override wins over it, the font size is never
 * overridable, and a snapshot taken earlier does not change under a later override.
 */
@RunWith(MockitoJUnitRunner.class)
public class OverlaySettingsTest
{
	@Mock
	ShortestPathConfig config;

	@Before
	@After
	public void clean()
	{
		ConfigOverrides.clear();
		TestConfigKeys.declare();
	}

	@Before
	public void configure()
	{
		lenient().when(config.drawMap()).thenReturn(true);
		lenient().when(config.colourPath()).thenReturn(Color.BLUE);
		lenient().when(config.unreachableText()).thenReturn("Unreachable");
		lenient().when(config.arrivalDismissSeconds()).thenReturn(8);
		lenient().when(config.directionsSteps()).thenReturn(5);
		lenient().when(config.compactOverlay()).thenReturn(true);
		lenient().when(config.useCustomOverlayFont()).thenReturn(true);
		lenient().when(config.overlayFont()).thenReturn(FontType.BOLD);
	}

	@Test
	public void theSnapshotCopiesTheConfig()
	{
		OverlaySettings settings = OverlaySettings.from(config);
		assertTrue(settings.drawMap);
		assertTrue("compact overlay (no header row)", settings.compactOverlay);
		assertTrue(settings.useCustomOverlayFont);
		assertEquals(FontType.BOLD, settings.overlayFont);
		assertEquals(Color.BLUE, settings.colourPath);
		assertEquals("Unreachable", settings.unreachableText);
		assertEquals(8, settings.arrivalDismissSeconds);
		assertEquals("the directions overlay's line budget (a 15-step route took the screen)", 5, settings.directionsSteps);
	}

	@Test
	public void anOverrideWinsAndAnEarlierSnapshotStands()
	{
		OverlaySettings before = OverlaySettings.from(config);
		ConfigOverrides.apply(Map.of("drawMap", false, "colourPath", Color.RED, "arrivalDismissSeconds", 2));
		OverlaySettings after = OverlaySettings.from(config);
		assertFalse(after.drawMap);
		assertEquals(Color.RED, after.colourPath);
		assertEquals(2, after.arrivalDismissSeconds);
		assertEquals("the overlay font is display-only, never overridden", FontType.BOLD, after.overlayFont);
		assertTrue("an earlier snapshot is immutable", before.drawMap);
	}
}

package gps;

import java.util.Set;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Template-only zones (destination-remaps.tsv): a target inside pure-scenery geometry — like
 * Iban's Temple's visible interior, whose real version is instanced — snaps to the zone's
 * anchor tile instead of erroring as unreachable. Clue steps and Quest Helper tiles land on
 * these regularly (gps-capture-20260724-212231 was a map pin on the temple's well platform).
 */
public class DestinationRemapTest
{
	private static final int TEMPLE_DOORS = WorldPointUtil.packWorldPoint(2145, 4647, 1);

	@Test
	public void templeInteriorSnapsToTheDoors()
	{
		int wellPlatform = WorldPointUtil.packWorldPoint(2139, 4647, 1);
		assertEquals(TEMPLE_DOORS, Destinations.remapTemplateOnlyZones(wellPlatform));
		// walkableTargets applies the remap before its ring logic, so every routing flow
		// (map pin, Quest Helper, panel search) gets the anchor as its target set.
		assertEquals(Set.of(TEMPLE_DOORS), Destinations.walkableTargets(null, wellPlatform));
	}

	/**
	 * The TzHaar Fight Cave's arena is instanced; its template lies south-west of Mor Ul Rek and
	 * the "Fight Cave" destination used to point into it, so a search for it walked "as close as
	 * possible" and stopped 33 tiles short (capture 2026-11-07). The zone snaps to the tile north
	 * of the cave entrance (object 11833 at 2437 5166).
	 */
	@Test
	public void fightCaveArenaSnapsToTheCaveEntrance()
	{
		int entrance = WorldPointUtil.packWorldPoint(2438, 5168, 0);
		assertEquals(entrance, Destinations.remapTemplateOnlyZones(WorldPointUtil.packWorldPoint(2400, 5092, 0)));
		assertEquals(entrance, Destinations.remapTemplateOnlyZones(entrance));
	}

	@Test
	public void boxEdgesAndOtherPlanesPassThrough()
	{
		int antechamber = WorldPointUtil.packWorldPoint(2145, 4647, 1);
		assertEquals(antechamber, Destinations.remapTemplateOnlyZones(antechamber));
		int belowTemple = WorldPointUtil.packWorldPoint(2139, 4647, 0);
		assertEquals(belowTemple, Destinations.remapTemplateOnlyZones(belowTemple));
	}
}

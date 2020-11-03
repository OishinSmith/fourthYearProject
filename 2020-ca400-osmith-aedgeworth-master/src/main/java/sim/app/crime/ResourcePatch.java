package sim.app.crime;

import java.awt.*;
import sim.field.grid.*;
import sim.portrayal.*;
import sim.portrayal.grid.*;
import java.awt.geom.*;
import sim.util.*;

/**
 * A special portrayal for drawing part of the maze. The way this portion is
 * drawn (as a curved or straight line) depends in part on neighboring parts of
 * the maze, making the portrayal a bit more complex. We use the new *location*
 * field in DrawInfo2D to determine where we are in the maze, and hence what our
 * neighbors are. Drawing is then done using Java2D operators.
 */

public class ResourcePatch {

	private static final int MAPWIDTH = 600;
	private static final int MAPHEIGHT = 600;

	private static final long serialVersionUID = 1;

	DoubleGrid2D patch = new DoubleGrid2D(MAPWIDTH, MAPHEIGHT, 0.0);

	public DoubleGrid2D getPatch() {
		return patch;
	}

	public void setPatch(DoubleGrid2D patch) {
		this.patch = patch;
	}

	Int2D location;

	public ResourcePatch(Int2D location, int size) {
		this.location = location;
		
		paint(size);
		
		
	}

	private void paint(int size) {
		for (int x = location.getX(); x < (location.getX() + size); x++) {
			for (int y = location.getY(); y < (location.getY() + size); y++) {
				this.getPatch().set(x, y, 1.0);

			}

		}

	}

}
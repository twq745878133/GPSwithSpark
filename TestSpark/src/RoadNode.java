import java.io.Serializable;

import com.esri.core.geometry.Point;

/**
 * @author Z.Y.T
 * 
 *         2014年10月27日上午10:38:17
 */
@SuppressWarnings("serial")
public class RoadNode implements Serializable {

	int id;
	int orientation; // distinguish from the Car's direction;
	Point start;
	Point end;
	double carSpeed;

	public RoadNode(int id, Point point1, Point point2, int orientation) {
		this.id = id;
		this.start = point1;
		this.end = point2;
		this.orientation = orientation;
	}

	// @Override
	// public boolean equals(Object obj) {
	// // TODO Auto-generated method stub
	// RoadNode rn = (RoadNode) obj;
	// return this.id == rn.id && this.orientation == rn.orientation;
	// }

	public double length() {
		return PathGraph.disSquare(start, end);
	}
}

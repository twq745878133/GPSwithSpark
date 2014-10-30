import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;

import com.esri.core.geometry.Point;

//路径库

public class PathGraph implements Serializable {
	static Point pt1, pt2;

	static ArrayList<RoadNode> roadNodes = null;
	static TreeSet<Car> cars = new TreeSet<>();

	public void initRoadNodes(String fileName) {
		if (roadNodes != null)
			return;
		roadNodes = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			br.readLine();
			String value = br.readLine();
			// if id is even,then the road node is forward; otherwise, it is
			// reverse.
			int id = 0;
			while (value != null) {
				Point start = null;
				Point end = null;
				for (int i = 0; i < 2; i++) {
					String[] temp = value.split(",");
					double lon = Double.parseDouble(temp[4]);
					double lat = Double.parseDouble(temp[5]);
					if (i == 0)
						start = new Point(lon, lat);
					else
						end = new Point(lon, lat);
					value = br.readLine();
				}
				RoadNode rn0 = new RoadNode(id++, start, end, 0);
				RoadNode rn1 = new RoadNode(id++, start, end, 1);
				roadNodes.add(rn0);
				roadNodes.add(rn1);
			}
			System.out.println(roadNodes.size());
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int getNodeID(Car car) // 计算点属于哪个节点
	{
		double a2; // 点到线段前一个node的距离平方
		double b2; // 点到线段后一个node的距离平方
		double c2; // 线段长度平方
		int value;
		int key;

		// 先比较第一个node.
		b2 = disSquare(car.point, roadNodes.get(0));
		key = roadNodes.get(0).id;
		// value = key + "	" + car.number + "	" + car.speed;
		value = key;
		double dist2 = b2;

		for (int i = 2; i < roadNodes.size(); i += 2) {
			c2 = roadNodes.get(i).length();
			a2 = b2;
			b2 = disSquare(car.point, roadNodes.get(i));

			if (c2 + b2 < a2) // 角A是钝角或直角，则b为点与线段的距离，比较
			{
				if (b2 < dist2) {
					dist2 = b2;
					key = roadNodes.get(i).id;
					// value = key + "	" + car.number + "	" + car.speed;
					value = key;
				}
				pt1 = pt2;
				continue;
			}

			if (c2 + a2 < b2) // 角B是钝角或直角，则a为点与线段的距离，已经比较过了
			{
				pt1 = pt2;
				continue;
			}

			double t = (b2 + c2 - a2);
			t = (t * t) / (4 * c2);

			if (t <= b2 + 0.00001) // + 1.0 是因为 t <= b2 有误差
			{ // c2值有效，用(b2-t)计算距离
				if ((b2 - t) < dist2) {
					dist2 = (b2 - t);
					key = roadNodes.get(i).id;
					// value = key + "	" + car.number + "	" + car.speed;
					value = key;
				}

			} else { // c2值太小，用min(a2,b2)计算距离
				if (a2 < dist2) {
					dist2 = a2;
					key = roadNodes.get(i).id;
					// value = key + "	" + car.number + "	" + car.speed;
					value = key;
				}
				if (b2 < dist2) {
					dist2 = b2;
					key = roadNodes.get(i).id;
					// value = key + "	" + car.number + "	" + car.speed;
					value = key;
				}
			}
			pt1 = pt2;
		}
		return value;
	}

	public static double disSquare(Point point1, Point point2) {
		return (point1.getX() - point2.getX())
				* (point1.getX() - point2.getX())
				+ (point1.getY() - point2.getY())
				* (point1.getY() - point2.getY());
	}

	public static double disSquare(Point point, RoadNode node) {
		pt1 = node.start;
		pt2 = node.end;
		double disPt1 = disSquare(point, pt1);
		double disPt2 = disSquare(point, pt2);
		double c2 = disSquare(pt1, pt2);
		double t = (disPt1 + c2 - disPt2);
		return (t * t) / (4 * c2);
	}

	public String computeRoadNode(String carInfo) {
		// 1. Split the carInfo line and generate a Car instance.
		String[] array = carInfo.split("\t");
		String id = array[0];
		String carNumber = array[0];
		String date = array[3];
		String time = array[4];

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date1 = null;
		try {
			date1 = sdf.parse(date + " " + time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double lon = Double.parseDouble(array[1]);
		double lat = Double.parseDouble(array[2]);
		double speed = Double.parseDouble(array[5]);
		int orient = Integer.parseInt(array[6].trim());
		Car car = new Car(id, carNumber, date1, new Point(lon, lat), speed,
				orient);

		// 2. Compute the car belongs to which roadNode:
		// if the car's speed > 0, then first, judge out the roadNode name;
		// second, judge out the direction.
		// if the car's speed = 0, then first, find the last position in the
		// cars TreeSet; second, judge out the roadNode.
		int nodeId = getNodeID(car);
		int orientation = getNodeOrientation(car, roadNodes.get(nodeId));
		if (orientation == 1)
			nodeId++;

		// 3. Check whether the new Car instance has existed in the cars
		// TreeSet.If so, replace the old one with this new one , otherwise add
		// this new one.
		if (cars.contains(car)) {
			cars.remove(car);
			cars.add(car);
		} else {
			cars.add(car);
		}

		// 4. Remove the "dead car"(don't send info over 10 minutes) in the
		// TreeSet
		boolean flag = true;
		while (flag) {
			Car firstCar = cars.first();
			Car lastCar = cars.last();
			long firstTime = firstCar.date.getTime();
			long lastTime = lastCar.date.getTime();
			if (lastTime - firstTime > 10 * 60 * 1000) {
				firstCar=cars.pollFirst();
//				System.out.println("remove one dead car : " + firstCar);
				System.out.println("remove one dead car");
			} else
				flag = false;
		}

		return nodeId + "	" + car.number + "	" + car.speed;
	}

	public int getNodeOrientation(Car car, RoadNode node) {
		// the azimuth of the road node;
		double rodeDirection = this.azimuth(node.start, node.end);
		double radDiff = 0;
		if (car.speed != 0) {
			radDiff = Math.abs(rodeDirection - car.getRad());
		} else {
			Car lastCar = car;
			Iterator<Car> carList = cars.iterator();
			while (carList.hasNext()) {
				Car car1 = carList.next();
				if (car.equals(car1)) {
					lastCar = car1;
					break;
				}
			}
			double carDirection = this.azimuth(car.point, lastCar.point);
			radDiff = Math.abs(rodeDirection - carDirection);
		}
		if (radDiff < Math.PI / 2 || radDiff > 1.5 * Math.PI)
			return 0; // the car runs in the forward direction.
		else
			return 1; // the car runs in the reverse direction.
	}

	// get the azimuth(方位角) of two points;
	public double azimuth(Point point1, Point point2) {
		double tandegree = (point1.getY() - point2.getY())
				/ (point1.getX() - point2.getX());
		return Math.atan(tandegree);
	}
}

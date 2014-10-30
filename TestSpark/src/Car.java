import java.util.Date;

import com.esri.core.geometry.Point;

/**
 * @author Z.Y.T
 * 
 *         2014年10月27日上午10:35:15
 */
public class Car implements Comparable<Car> {
	String id; // 数据id
	String number; // 车牌号
	String date1; // 日期
	String time; // 时间
	Date date;
	Point point; // 经纬度
	double speed; // 速度
	int direction; // 方向

	public Car(String id, String number, Date date, Point point, double speed,
			int orient) {
		this.id = id;
		this.number = number;
		this.date = date;
		this.point = point;
		this.speed = speed;
		this.direction = orient;
	}

	public Car(String id, String number, String date, String time, Point point,
			double speed, int orient) {
		this.id = id;
		this.number = number;
		this.date1 = date;
		this.time = time;
		this.point = point;
		this.speed = speed;
		this.direction = orient;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (this.number.equals(((Car) obj).number))
			return true;
		return false;
	}

	@Override
	public int compareTo(Car o) {
		// TODO Auto-generated method stub
		if (this.date.before(o.date))
			return -1;
		if (this.date.after(o.date))
			return 1;
		else
			return 0;
	}

	// transform from 180 to PI;
	public double getRad() {
		if (this.speed > 0)
			return this.direction / 180.0 * Math.PI;
		else
			return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.number + " " + this.date;
	}

	// record the last modified time;
	public void update(Date date) {
		this.date = date;
	}

	public static void main(String[] args) {
		// TreeSet<Car> cars = new TreeSet<>();
		// cars.add(new Car("1111", "10", new Date(200000)));
		// cars.add(new Car("1231", "11", new Date(100000)));
		// cars.add(new Car("121", "12", new Date(500000)));
		// cars.add(new Car("11", "13", new Date(600000)));
		// cars.add(new Car("12331", "14", new Date(700000)));
		// cars.add(new Car("1231", "15", new Date(1000000000)));
		// System.out.println(cars);
		// System.out.println(cars.ceiling(new Car("12312312312", "13", new
		// Date(
		// 500))));
		// HashSet<Car> cars2 = new HashSet<>();
		// cars2.add(new Car("1111", "10", new Date(200000)));
		// cars2.add(new Car("1231", "11", new Date(100000)));
		// cars2.add(new Car("121", "12", new Date(500000)));
		// cars2.add(new Car("11", "13", new Date(600000)));
		// cars2.add(new Car("12331", "14", new Date(700000)));
		// cars2.add(new Car("1231", "15", new Date(1000000000)));
		// // Collections.binarySearch(cars, new Car("12312312312", "13", new
		// Date(
		// // 500)));
		//
		// System.out.println(cars2);
		// Car car1 = new Car("123", "111", new Date());
		// Car car2 = new Car("134", "111", new Date());
		// System.out.println(car1 == car2);
		// System.out.println(car1.equals(car2));
	}
}
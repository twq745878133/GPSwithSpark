import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;

/**
 * @author Administrator
 *
 * 2014-10-16 上午11:37:06
 */
public class Instances {
	String[] array;
	List<Double> list = new ArrayList<Double>(); // 存放经纬度的集合

	Polyline pRoad = new Polyline(); // 道路
	String path;

	public Polyline instancesRoad(String path) throws IOException {
		// 初始化道路
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		br.readLine();
		String value = br.readLine();
		array = value.split(",");
		pRoad.startPath(Double.parseDouble(array[2]), Double.parseDouble(array[3]));
		
		while ((value = br.readLine()) != null) {
			array = value.split(",");
			pRoad.lineTo(Double.parseDouble(array[2]),Double.parseDouble(array[3]));
		}
		br.close();
		return pRoad;
		// 记录下pt.ID, pt.getX(), pt.getY(), nPtIndex、bOnPoint

	}

	public List<RoadNode> instanceNode(Polyline pRoad) {
		int key = 0;
		Point point1;
		Point point2;
		List<RoadNode> listNode = new ArrayList<RoadNode>();
		point1 = pRoad.getPoint(0);

		for(int i = 1;i < pRoad.getPointCount();i++) {
			point2 = pRoad.getPoint(i);
			listNode.add(new RoadNode(key, point1, point2,0));
			point1 = point2;
			key++;

		}
		return listNode;
	}
}

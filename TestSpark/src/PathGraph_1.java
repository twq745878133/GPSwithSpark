import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;

//路径库
public class PathGraph_1 implements Serializable{
	static Polyline pRoad; // 道路
	static int nPtIndex = -1; // 车对道路进行垂直投影，其垂足在道路上节点的序列号
	static boolean bOnPoint = false; // 车对道路的垂足是否在节点上

	public void initRoad(File file) {
		// ArrayList<Double> xy = pg.readFromFile(file);
		// // for (int i = 0; i < xy.size(); i++) {
		// // System.out.println(xy.get(i));
		// // }
		//
		// // 初始化道路
		// int xyCount = 100;// 点数
		// double xyPairs[] = new double[xyCount * 2];// 为xyPairs[]赋值
		// //
		// pRoad = new Polyline();
		// // pRoad.startPath(xyPairs[0], xyPairs[1]);
		// // for (int i = 2; i < xyCount * 2; i += 2) {
		// // pRoad.lineTo(xyPairs[i], xyPairs[i + 1]);
		// // }
		// pRoad.startPath(xy.get(0), xy.get(1));
		// for (int i = 2; i < xy.size(); i += 2) {
		// pRoad.lineTo(xy.get(i), xy.get(i + 1));
		// }

		ArrayList<Double> xy = this.readFromFile(file);
		pRoad = new Polyline();
		pRoad.startPath(xy.get(0), xy.get(1));
		for (int i = 2; i < xy.size(); i += 2) {
			pRoad.lineTo(xy.get(i), xy.get(i + 1));
		}
	}

	public ArrayList<Double> readFromFile(File file) {
		BufferedReader br;
		ArrayList<Double> xyPairs = new ArrayList<Double>();
		try {
			br = new BufferedReader(new FileReader(file));
			String str = br.readLine();
			while (str != null) {
				if (str.contains("[") || str.contains("]")) {
					str = br.readLine();
					continue;
				}
				if (str.endsWith(",")) {
					str = str.substring(0, str.length() - 1);
				}
				xyPairs.add(Double.parseDouble(str));
				str = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xyPairs;

	}

	public static void main(String[] args) {
		File file = new File("I:\\zyt\\Desktop\\Road.txt");
		PathGraph_1 pg = new PathGraph_1();
		pg.initRoad(file);

		// 循环进行计算、存储
		Point pt = new Point(1, 1);
		double dist2 = pg.getDistNearPointIndex(pt, pRoad);
		// 记录下pt.ID, pt.getX(), pt.getY(), nPtIndex、bOnPoint

	}

	public double getDistNearPointIndex(Point pPt, Polyline pLine) // 点到线段距离的平方
	{
		double a2; // 点到线段起点的距离平方
		double b2; // 点到线段终点的距离平方
		double c2; // 线段长度平方

		Point pt1, pt2; // 当前点；
		// 先比较第一个点
		pt1 = pLine.getPoint(0);
		b2 = (pPt.getX() - pt1.getX()) * (pPt.getX() - pt1.getX())
				+ (pPt.getY() - pt1.getY()) * (pPt.getY() - pt1.getY());
		nPtIndex = 0;
		bOnPoint = true;
		double dist2 = b2;

		for (int i = 1; i < pLine.getPointCount(); i++) {
			pt2 = pLine.getPoint(i);
			c2 = (pt1.getX() - pt2.getX()) * (pt1.getX() - pt2.getX())
					+ (pt1.getY() - pt2.getY()) * (pt1.getY() - pt2.getY());

			a2 = b2;
			b2 = (pPt.getX() - pt2.getX()) * (pPt.getX() - pt2.getX())
					+ (pPt.getY() - pt2.getY()) * (pPt.getY() - pt2.getY());

			if (c2 + b2 < a2) // 角A是钝角或直角，则b为点与线段的距离，比较
			{
				if (b2 < dist2) {
					dist2 = b2;
					nPtIndex = i;
					bOnPoint = true;
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
				}

			} else { // c2值太小，用min(a2,b2)计算距离
				if (a2 < dist2) {
					dist2 = a2;
				}
				if (b2 < dist2) {
					dist2 = b2;
				}
			}
			nPtIndex = i - 1;
			bOnPoint = false;
			pt1 = pt2;

			// 构成锐角三角形，求距离；
			/*
			 * if (pt1.x == pt2.x) dist2 = (pPt.x - pt1.x) * (pPt.x - pt1.x);
			 * else if (pt1.y == pt2.y) dist2 = (pPt.y - pt1.y) * (pPt.y -
			 * pt1.y); else { double k = (pt2.y - pt1.y) / (pt2.x - pt1.x);
			 * double dDif = (pPt.y - pt1.y) - (pPt.x - pt1.x) * k; dDif =
			 * Math.cos(Math.atan(k)) * dDif; dist2 = dDif * dDif; } nPtIndex =
			 * i - 1; bOnPoint = false; pt1 = pt2;
			 */
		}
		return dist2;
	}

}

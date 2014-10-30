import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 */

/**
 * @author Z.Y.T
 * 
 *         2014年10月27日下午10:30:48
 */
public class Road {

	public void initRoad(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			br.readLine();
			String value = br.readLine();
			while (value != null) {
				String[] temp = value.split("\t");
				String id = temp[0];
				String lon = temp[1];
				String lat = temp[2];
				String date = temp[3];
				String time = temp[4];
				String speed = temp[5];
				String direction = temp[6];

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

	}
}

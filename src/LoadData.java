import processing.data.Table;
import processing.data.TableRow;


public class LoadData {
	float[] copies;
	float[] duration;
	float[] popularity;

	public void loadCSVData(Table table){
		copies = new float[table.getRowCount()];
		duration = new float[table.getRowCount()];
		popularity = new float[table.getRowCount()];
		int itr = 0;
		for (TableRow row: table.rows()){
			int c = row.getInt("copies");
			int d = row.getInt("duration");
			int p = row.getInt("popularity");
			copies[itr] = c;
			duration[itr] = d;
			popularity[itr] = p;
			itr++;
		}

	}
	

}

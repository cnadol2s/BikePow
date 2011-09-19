package bike.pow;
import java.util.Calendar;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{
	 
	private static String DB_PATH = "/sdcard/";
    private static String DB_NAME = "BikePowDBv2.db";
    private static String DB_TABLE = "Testdrives";
    private SQLiteDatabase myDataBase; 
    private final Context myContext;
 
    public DataBaseHelper(Context context) {
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
    
    public void insertData(
    		String lat,
    		String lng,
    		String alt,
    		String accuracy,
    		String velocity,
    		String headwind,
    		String hillgraderaw,
    		String hillgradeavg,
    		String hillgrademed,
    		String bearing,
    		String windspeed,
    		String winddegrees,
    		String force,
    		String poweroutput,
    		String a1,
    		String a2,
    		String r,
    		String t,
    		String wc,
    		String wm) throws SQLException {

    	String timestamp = this.getCurrentTimeStamp();
    	
    	this.myDataBase.execSQL("INSERT INTO " + DB_TABLE
				+ " VALUES ("
				+ null + ", '"
				+ timestamp + "', '"
				+ lat + "', '"
				+ lng + "', '"
				+ alt + "', '"
				+ accuracy + "', '"
				+ velocity + "', '"
				+ headwind + "', '"
				+ hillgraderaw + "', '"
				+ hillgradeavg + "', '"
				+ hillgrademed + "', '"
				+ bearing + "', '"
				+ windspeed + "', '"
				+ winddegrees + "', '"
				+ force + "', '"
				+ poweroutput + "', '"
				+ a1 + "', '"
				+ a2 + "', '"
				+ r + "', '"
				+ t + "', '"
				+ wc + "', '"
				+ wm + "');");
    }
 
    @Override
	public synchronized void close() {
    	    if(myDataBase != null) {
    	    	myDataBase.close();
    	    }
    	    super.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) { }
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
	
	public String getCurrentTimeStamp() {
		Calendar cal = Calendar.getInstance();
		String year = Integer.toString(cal.get(Calendar.YEAR));
		String month = Integer.toString(cal.get(Calendar.MONTH) + 1);
		String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		String minute = Integer.toString(cal.get(Calendar.MINUTE));
		String second = Integer.toString(cal.get(Calendar.SECOND));
		return year + "-" + month + "-" + day + "_" + hour + "-" + minute + "-" + second; 
	}
}
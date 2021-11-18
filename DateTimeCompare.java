import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeCompare {
    public static void main(String[] args){

        Calendar now = Calendar.getInstance();
//        System.out.println(now);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println(sdf.format(now.getTime())); 

        now.add(Calendar.DATE, -1);
        System.out.println(sdf.format(now.getTime())); 

        //引数
        String argDate = "20211025";
        String constTime = "1755";
        Calendar cal1 = Calendar.getInstance();
        int year  = Integer.parseInt(argDate.substring(0,4));
        int month = Integer.parseInt(argDate.substring(4,6));
        int day   = Integer.parseInt(argDate.substring(6,8));
        int hour  = Integer.parseInt(constTime.substring(0,2));
        int minute= Integer.parseInt(constTime.substring(2,4));
    
        System.out.println(year);
        System.out.println(month);
        System.out.println(day);
        System.out.println(hour);
        System.out.println(minute);
        
        cal1.set(Calendar.YEAR,year); 
        //月の指定は0から始まるため-1
        cal1.set(Calendar.MONTH,month-1); 
        cal1.set(Calendar.DATE,day); 
        cal1.set(Calendar.HOUR_OF_DAY,hour);
        cal1.set(Calendar.MINUTE,minute);
        cal1.set(Calendar.SECOND,0);
        


        System.out.println(sdf.format(cal1.getTime())); 


    }
    
}

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeCompare {
    public static void main(String[] args){

        //現在日時取得
        Calendar calNow = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println("現在日時　　：" + sdf.format(calNow.getTime())); 

        //引数で渡された日付(支払予定日)
        String argDate = "20211120";
        String constTime = "1755";
        Calendar calPaymentSchedule = Calendar.getInstance();
        int year  = Integer.parseInt(argDate.substring(0,4));
        int month = Integer.parseInt(argDate.substring(4,6));
        int day   = Integer.parseInt(argDate.substring(6,8));
        int hour  = Integer.parseInt(constTime.substring(0,2));
        int minute= Integer.parseInt(constTime.substring(2,4));
    
        //支払日の17:55を作成
        calPaymentSchedule.set(Calendar.YEAR,year); 
        calPaymentSchedule.set(Calendar.MONTH,month-1);//月の指定は0から始まるため-1
        calPaymentSchedule.set(Calendar.DATE,day); 
        calPaymentSchedule.set(Calendar.HOUR_OF_DAY,hour);
        calPaymentSchedule.set(Calendar.MINUTE,minute);
        calPaymentSchedule.set(Calendar.SECOND,0);
        //System.out.println("支払日　　　：" + sdf.format(calPaymentSchedule.getTime())); 

        //作成した日時の前日
        calPaymentSchedule.add(Calendar.DATE, -1);
        System.out.println("支払日の前日：" + sdf.format(calPaymentSchedule.getTime())); 

        int diff = calNow.compareTo(calPaymentSchedule);
        System.out.println("比較結果：" + diff); 

        if (diff == 0){
            System.out.println("現在日時と支払予定日は同じ日時です");
          }else if (diff > 0){
            System.out.println("現在日時は支払予定日を過ぎています");
          }else{
            System.out.println("現在日時は支払予定日より前です");
          }

    }
    
}

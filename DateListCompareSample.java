import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * 
 * 
 */
public class DateListCompareSample{
    public static void main(String[] args){

        List<Date> list = new ArrayList<Date>();
        list.add(Date.valueOf("2021-03-01"));
        list.add(Date.valueOf("2021-04-01"));
        list.add(Date.valueOf("2021-03-02"));
        list.add(Date.valueOf("2021-03-03"));
        list.add(Date.valueOf("2021-03-04"));
        list.add(Date.valueOf("2021-03-05"));
        list.add(Date.valueOf("2021-03-07"));
        list.add(Date.valueOf("2021-03-06"));
        list.add(Date.valueOf("2021-03-09"));
        list.add(Date.valueOf("2021-03-08"));

        System.out.println(Collections.min(list));
        System.out.println(Collections.max(list));
        
    }
}

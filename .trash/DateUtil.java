package com.bizpack.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	/**
	 * 오늘날짜를 패턴에 맞게 가져오기
	 * @param pattern yyyy-MM-dd : 2019-02-28 , yyyy-MM-dd HH:mm:ss.SSS : 2019-02-28 01:59:28.002
	 * @return 날짜
	 */
	public static String getDatePattern(String pattern){
		String rtnStr = null;
	   	try {
	   		java.text.SimpleDateFormat sdfCurrent = new java.text.SimpleDateFormat(pattern, Locale.KOREA);
	   	    java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
	   	    rtnStr = sdfCurrent.format(ts.getTime());
	   	} catch (IllegalArgumentException e) {
	   		LogUtil.exlog(new Object().getClass(), e);
	   	}
	   	return rtnStr;
	}

	/**
	 * 입력된 날짜를 패턴에 맞게 가져온다
	 * @param pattern (yyyy-MM-dd : 2019-02-28 , yyyy-MM-dd HH:mm:ss.SSS : 2019-02-28 01:59:28.002
	 * @return 날짜
	 */
	public static String printDatePattern(String date, String pattern){
		if(date == null) return "";
		String rtnStr = null;
		Date date1 = null;
		Calendar cal1 = Calendar.getInstance();
		java.text.SimpleDateFormat sdfCurrent = new java.text.SimpleDateFormat(pattern, Locale.KOREA);
		if(checkDate(date,pattern)){
			try {
				date1 = sdfCurrent.parse(date);
				rtnStr = sdfCurrent.format(date1);
			} catch (ParseException e) {
				LogUtil.exlog(new Object().getClass(), e);
			} catch (IllegalArgumentException e) {
				LogUtil.exlog(new Object().getClass(), e);
			}
		}

		if(date1 == null && checkDate2(date)){
			java.text.SimpleDateFormat sdfCurrent2 = new java.text.SimpleDateFormat("yyyy-M-d.HH.mm.ss.S", Locale.KOREA);
			try {
				date1 = sdfCurrent2.parse(date);
				rtnStr = sdfCurrent.format(date1);
			} catch (ParseException e) {
				LogUtil.exlog(new Object().getClass(), e);
			} catch (IllegalArgumentException e) {
				LogUtil.exlog(new Object().getClass(), e);
			}
		}
		if(date1 == null) return date;
		cal1.setTime(date1);
		return sdfCurrent.format(new java.sql.Timestamp(cal1.getTimeInMillis()));
	}

	/**
	 * 문자열이 패턴과 일치한 날짜인지 확인하는 메소드
	 * @param str 문자열
	 * @param pattern 날짜 패턴
	 * @return 날짜가 아니거나 패턴과 일치하지 않으면 false / 그외 true
	 */
	public static boolean checkDate(String str, String pattern){

		boolean dateValidity = true;
		SimpleDateFormat df = new SimpleDateFormat(pattern,Locale.KOREA); //20041102101244
		df.setLenient(true); // false 로 설정해야 엄밀한 해석을 함.
		try {
			Date dt = df.parse(str);
		}catch(ParseException pe){
			dateValidity = false;
		}

		return dateValidity;
	}

	/**
	 * 문자열이 날짜인지 확인하는 메소드
	 * @param str 문자열
	 * @return 날짜가 아니면 false / 그외 true
	 */
	public static boolean checkDate2(String str){
		boolean dateValidity = true;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d.HH.mm.ss.S",Locale.KOREA); //20041102101244
		df.setLenient(false); // false 로 설정해야 엄밀한 해석을 함.
		try {
			Date dt = df.parse(str);
		}catch(ParseException pe){
			dateValidity = false;
		}catch(IllegalArgumentException ae){
			dateValidity = false;
		}
		return dateValidity;
	}

	/**
	 * date형식을 가진 String value의 패턴을 변경한다.
	 * @param stringDate date형식을 가진 String value(yyyy-MM-dd : 2019-02-28 , yyyy-MM-dd HH:mm:ss.SSS : 2019-02-28 01:59:28.002
	 * @param old_pattern 이전 패턴
	 * @param new_pattern 변경할 패턴
	 * @return stringDate
	 */
	public static String convertDatePattern(String stringDate, String old_pattern, String new_pattern){
		SimpleDateFormat formatter = new SimpleDateFormat(old_pattern,new Locale("en", "US"));
		try {
			Date time = formatter.parse(stringDate);
			return new SimpleDateFormat(new_pattern).format(time);
		} catch (ParseException e) {
			LogUtil.exlog(new Object().getClass(), e);
		}
		return stringDate;
	}

	/**
	 * 입력된 날짜(yyyy-MM-dd)형식이 유효한 날짜인지를 검사한다.
	 * @param dt yyyy-MM-dd 형식의 날짜형식 문자열
	 * @return 유효한 날짜 true, 아니면 false
	 */
	public static boolean isDate(String dt){
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			format.setLenient(false);
			format.parse(dt);
		} catch (ParseException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		}
       return true;
	}

	/**
	 * 입력된 날짜(yyyy-MM-dd)형식이 유효한 날짜인지를 검사한다.
	 * @param m : 월, d : 일, y : 연
	 * @return 유효한 날짜 true, 아니면 false
	 */
	public static boolean isDate(int m, int d, int y)
    {
        m -= 1;
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        try{
                c.set(y,m,d);
                java.util.Date dt = c.getTime();
        }catch (IllegalArgumentException e){
                return false;
        }
        return true;
    }

	/**
	 * 입력된 값과 현재 시간의 차이를   이전인지 현재인지 지났는지 검사
	 * 입력날짜기 기준(strDate - nowDate)
	 * @param strDate yyyy-MM-dd 형식으로 날짜만
	 * @return 1: 전, 0:오늘날짜, -1:지난날짜
	 */
	public static int dateDiff(String strDate){
		return dateDiff(strDate, "yyyy-MM-dd");
	}

	/**
	 * 입력된 값과 현재 시간의 차이를   이전인지 현재인지 지났는지 검사
	 * 입력날짜기 기준(strDate - nowDate)
	 * @param strDate 날짜형 문자열
	 * @param pattern 날짜 패턴
	 * @return 1: 전, 0:오늘날짜, -1:지난날짜
	 */
	public static int dateDiff(String strDate, String pattern){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
//		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern, Locale.KOREA);

		Date date1 = null;
		try {
			date1 = sdf.parse(strDate);
		} catch (ParseException e) {
			LogUtil.exlog(new Object().getClass(), e);
		}
		Date date2 = null;

		try {
			date2 = sdf.parse(getDatePattern(pattern));
		} catch (ParseException e) {
			LogUtil.exlog(new Object().getClass(), e);
		}

		cal1.setTime(date1);
		cal2.setTime(date2);
		int byInt = 60*60*24*1000;
		if(pattern.indexOf("HH") > -1) byInt = 60*60*1000;
		if(pattern.indexOf("mm") > -1) byInt = 60*1000;
		if(pattern.indexOf("ss") > -1) byInt = 1000;
		int diff = (int)((cal1.getTimeInMillis() - cal2.getTimeInMillis())/(long)(byInt));
		if(diff > 0){
			return 1;
		}else if(diff < 0){
			return -1;
		}else{
			return 0;
		}
	}
	/**
	 * 두 날짜의 차이를 설정한 type에 맞게 가져온다. 주의할것은 strDate1 - strDate2 이므로 양,음수를 잘 구분해야함
	 * @param strDate1 첫번째 날짜
	 * @param strDate2 두번째 날짜
	 * @param pattern 날짜 패턴 두 날짜가 패턴과 일치해야함 (yyyy-MM-dd HH:mm:ss) 형식
	 * @param type 리턴받는 두 날짜의 차이를 일(d), 시간(h), 분(m) 단위로 선택함
	 * @return
	 */
	public static int dateDiff(String strDate1, String strDate2, String pattern, String type){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern, Locale.KOREA);

		Date date1 = null;
		Date date2 = null;
		try {
			date1 = sdf.parse(strDate1);
			date2 = sdf.parse(strDate2);
		} catch (ParseException e) {
			LogUtil.exlog(new Object().getClass(), e);
		}

		cal1.setTime(date1);
		cal2.setTime(date2);
		long divide = 0;
		if("d".equals(type)){
			divide = 60*60*24*1000;
		}else if("h".equals(type)){
			divide = 60*60*1000;
		}else{
			divide = 60 * 1000;
		}
		return (int)((cal1.getTimeInMillis() - cal2.getTimeInMillis())/(divide));

	}

	/**
	 * 해당 날짜를 원하는 날수 만큼 증가, 감소 시킨다.
	 * @param strDate 기준 날짜, 날짜 형식으로 입력.
	 * @param intIncrease 증가 또는 감소 하고자 하는 수치
	 * @return 변경된 날짜 문자열
	 */
	public static String dateAdd(String strDate, int intIncrease){
		Calendar cal1 = Calendar.getInstance();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
		Date date1 = null;
		try {
			date1 = sdf.parse(strDate);
		} catch (ParseException e) {
			LogUtil.exlog(new Object().getClass(), e);
		}
		cal1.setTime(date1);
		long addTimeStamp = ((long)(60*60*24*1000) * intIncrease);
		cal1.setTimeInMillis(cal1.getTimeInMillis() + addTimeStamp);

		return sdf.format(new java.sql.Timestamp(cal1.getTimeInMillis()));
	}

	/**
	 * 해당 날짜를 패턴을 지정하여 원하는 날수 만큼 증가, 감소 시킨다.
	 * @param strDate 기준 날짜, 날짜 형식으로 입력.
	 * @param intIncrease 증가 또는 감소 하고자 하는 수치
	 * @param pattern (yyyy-MM-dd : 2019-02-28 , yyyy-MM-dd HH:mm:ss.SSS : 2019-02-28 01:59:28.002)
	 * @return 변경된 날짜 문자열
	 */
	public static String dateAdd(String strDate, int intIncrease, String pattern){
		Calendar cal1 = Calendar.getInstance();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern, Locale.KOREA);
		Date date1 = null;
		try {
			date1 = sdf.parse(strDate);
		} catch (ParseException e) {
			LogUtil.exlog(new Object().getClass(), e);
		}
		cal1.setTime(date1);
		long addTimeStamp = ((long)(60*60*24*1000) * intIncrease);
		cal1.setTimeInMillis(cal1.getTimeInMillis() + addTimeStamp);

		return sdf.format(new java.sql.Timestamp(cal1.getTimeInMillis()));
	}

	/**
	 * 오늘을 기준으로 원하는 날수 만큼 증가, 감소 시킨다.
	 * @param intString 증가 또는 감소 하고자 하는 수치
	 * @param pattern 반환받고자 하는 날짜 패턴
	 * @return 변경된 날짜 문자열
	 */
	public static String todayAdd(String intString, String pattern){
		int intIncrease = 0;
		if(ObjectUtil.notEmpty(intIncrease)){
			intIncrease = Integer.parseInt(intString);
		}
		Calendar cal1 = Calendar.getInstance();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern, Locale.KOREA);
		Date date1 = new Date();
		cal1.setTime(date1);
		long addTimeStamp = ((long)(60*60*24*1000) * intIncrease);
		cal1.setTimeInMillis(cal1.getTimeInMillis() + addTimeStamp);

		return sdf.format(new java.sql.Timestamp(cal1.getTimeInMillis()));
	}

	/**
	 * 특정 날짜를 기준으로 원하는 만큼 달을 증가, 감소 시킨다.
	 * @param date 변경할 날짜
	 * @param months 증가 또는 감소 하고자 하는 수치
	 * @return 변경된 날짜
	 */
    public static Date addMonth(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

	/**
	 * 요청하는 날짜 패턴에 맞춰 오늘 날짜 반환
	 * @param schOption 요청자
	 * @return HOUR : yyyy-MM-dd
	 * 		   DAY_M : yyyy-MM
	 * 		   MONTH : yyyy
	 * 		   YEAR : ""
	 */
	public static String getTodayforStat(String schOption) {
		String today="";
		if("HOUR".equals(schOption)){
			today = getDatePattern("yyyy-MM-dd");
		}else if("DAY_M".equals(schOption)){
			today = getDatePattern("yyyy-MM");
		}else if("MONTH".equals(schOption)){
			today = getDatePattern("yyyy");
		}else if("YEAR".equals(schOption)){
			today= "";
		}
		return today;
	}

	/**
	 * 요청하는 날짜 패턴에 맞춰 요청 날짜 반환
	 * @param Date 요청 날짜
	 * @param schOption 요청자
	 * @return HOUR : yyyy-MM-dd
	 * 		   DAY_W : yyyy-MM
	 * 		   DAY_M : yyyy-MM
	 * 		   MONTH : yyyy
	 * 		   YEAR : yyyy
	 */
	public static String getDateforStat(String Date, String schOption) {
		String today="";
		if("HOUR".equals(schOption)){
			today = printDatePattern(Date,"yyyy-MM-dd");
		}else if("DAY_W".equals(schOption)){
			today = printDatePattern(Date,"yyyy-MM");
		}else if("DAY_M".equals(schOption)){
			today = printDatePattern(Date,"yyyy-MM");
		}else if("MONTH".equals(schOption)){
			today = printDatePattern(Date,"yyyy");
		}else if("YEAR".equals(schOption)){
			today = printDatePattern(Date,"yyyy");
		}
		return today;
	}

    /**
     * 특정 날짜에 대하여 요일을 구함(일 ~ 토)
     * @param date
     * @param datePattern
     * @return
     * @throws IOException
     */
    public static String getDateDay(String date, String datePattern) throws IOException, ParseException {

        String[] week = {"일", "월", "화", "수", "목", "금", "토"};
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern) ;
        Date nDate = dateFormat.parse(date) ;
        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);
        int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;

        return week[dayNum+1] ;
    }
}

package com.kk.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

public class AppDateUtil {

  public static final String CHINA_DATE_FORMAT = "yyyy年MM月dd日";
  public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String TIME_FORMAT = "HH:mm:ss";

  public static String getDateStr(Date date, String format) {
    return new SimpleDateFormat(format).format(new Date(date.getTime()));
  }

  public static Map<String, Object> getQueryDate(String type) {
    Map<String, Object> map = new HashMap<String, Object>();
    if (type.equals("today")) {
      // 今天
      map.put("sdate", getDateStr(getDayBegin(), "yyyy-MM-dd HH:mm:ss"));
      map.put("edate", getDateStr(getDayEnd(), "yyyy-MM-dd HH:mm:ss"));
    } else if (type.equals("yesterday")) {
      // 昨天
      map.put("sdate", getDateStr(getBeginDayOfYesterday(), "yyyy-MM-dd HH:mm:ss"));
      map.put("edate", getDateStr(getEndDayOfYesterDay(), "yyyy-MM-dd HH:mm:ss"));
    } else if (type.equals("week")) {
      // 本周
      map.put("sdate", getDateStr(getBeginDayOfWeek(), "yyyy-MM-dd HH:mm:ss"));
      map.put("edate", getDateStr(getEndDayOfWeek(), "yyyy-MM-dd HH:mm:ss"));
    } else if (type.equals("month")) {
      // 本月
      map.put("sdate", getDateStr(getBeginDayOfMonth(), "yyyy-MM-dd HH:mm:ss"));
      map.put("edate", getDateStr(getEndDayOfMonth(), "yyyy-MM-dd HH:mm:ss"));
    } else if (type.equals("year")) {
      // 本年
      map.put("sdate", getDateStr(getBeginDayOfYear(), "yyyy-MM-dd HH:mm:ss"));
      map.put("edate", getDateStr(getEndDayOfYear(), "yyyy-MM-dd HH:mm:ss"));
    }
    return map;
  }

  // 获取当天的开始时间
  public static java.util.Date getDayBegin() {
    Calendar cal = new GregorianCalendar();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return new Timestamp(cal.getTimeInMillis());
  }
  // 获取当天的结束时间
  public static Date getDayEnd() {
    Calendar cal = new GregorianCalendar();
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    return new Timestamp(cal.getTimeInMillis());
  }
  // 获取昨天的开始时间
  public static Date getBeginDayOfYesterday() {
    Calendar cal = new GregorianCalendar();
    cal.setTime(getDayBegin());
    cal.add(Calendar.DAY_OF_MONTH, -1);
    return new Timestamp(cal.getTimeInMillis());
  }
  // 获取昨天的结束时间
  public static Date getEndDayOfYesterDay() {
    Calendar cal = new GregorianCalendar();
    cal.setTime(getDayEnd());
    cal.add(Calendar.DAY_OF_MONTH, -1);
    return new Timestamp(cal.getTimeInMillis());
  }

  // 获取本周的开始时间
  public static Date getBeginDayOfWeek() {
    Date date = new Date();
    if (date == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
    if (dayofweek == 1) {
      dayofweek += 7;
    }
    cal.add(Calendar.DATE, 2 - dayofweek);
    return getDayStartTime(cal.getTime());
  }
  // 获取本周的结束时间
  public static Date getEndDayOfWeek() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(getBeginDayOfWeek());
    cal.add(Calendar.DAY_OF_WEEK, 6);
    Date weekEndSta = cal.getTime();
    return getDayEndTime(weekEndSta);
  }

  /**
   * 获取本月的开始时间
   * @return
   */
  public static Date getBeginDayOfMonth(int year, int month) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month - 1, 1);
    return getDayStartTime(calendar.getTime());
  }

  /**
   * 获取本月的开始时间
   * @return
   */
  public static Date getBeginDayOfMonth(int month) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(getNowYear(), month - 1, 1);
    return getDayStartTime(calendar.getTime());
  }

  /**
   * 获取本月的开始时间
   * @return
   */
  public static Date getBeginDayOfMonth() {
    return getBeginDayOfMonth(getNowYear(), getNowMonth());
  }

  /**
   * 获取本月的结束时间
   * @return
   */
  public static Date getEndDayOfMonth(int year, int month) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month - 1, 1);
    int day = calendar.getActualMaximum(5);
    calendar.set(year, month - 1, day);
    return getDayEndTime(calendar.getTime());
  }

  /**
   * 获取本月的结束时间
   * @return
   */
  public static Date getEndDayOfMonth(int month) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(getNowYear(), month - 1, 1);
    int day = calendar.getActualMaximum(5);
    calendar.set(getNowYear(), month - 1, day);
    return getDayEndTime(calendar.getTime());
  }

  /**
   * 获取上个月
   * @return
   */
  public static String getLastMonth() {
    LocalDate today = LocalDate.now();
    today = today.minusMonths(1);
    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM");
    return formatters.format(today);
  }

  /**
   * 获取上个月
   * @return
   */
  public static int getLastMonthValue() {
    LocalDate today = LocalDate.now();
    today = today.minusMonths(1);
    return today.getMonthValue();
  }

  /**
   * 获取上个月开始的日期，和结束日期
   * @param month 当前的月份
   */
  public static Map<String, String> getLastMonthBegin(int month) {
    Map<String, String> map = new HashMap<>();
    LocalDate today = LocalDate.now();
    int year = today.getYear();
    // 如果是一月，就查12月的
    if(month == 1){
      month = 12;
      year = year - 1;
    }else{
      month = month - 1;
    }
    map.put("startday", buildDateToStr(getBeginDayOfMonth(year, month)));
    map.put("endday", buildDateToStr(getEndDayOfMonth(year, month)));
    return map;
  }

  /**
   * 获取本月的结束时间
   * @return
   */
  public static Date getEndDayOfMonth() {
    return getEndDayOfMonth(getNowYear(), getNowMonth());
  }

  /**
   * 获取本年的开始时间
   * @return
   */
  public static Date getBeginDayOfYear() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, getNowYear());
    // cal.set
    cal.set(Calendar.MONTH, Calendar.JANUARY);
    cal.set(Calendar.DATE, 1);

    return getDayStartTime(cal.getTime());
  }

  /**
   * 获取本年的结束时间
   * @return
   */
  public static Date getEndDayOfYear() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, getNowYear());
    cal.set(Calendar.MONTH, Calendar.DECEMBER);
    cal.set(Calendar.DATE, 31);
    return getDayEndTime(cal.getTime());
  }

  /**
   * 获取今年是哪一年
   * @return
   */
  public static Integer getNowYear() {
    Date date = new Date();
    GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
    gc.setTime(date);
    return Integer.valueOf(gc.get(1));
  }

  /**
   * 获取本月是哪一月
   * @return
   */
  public static int getNowMonth() {
    Date date = new Date();
    GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
    gc.setTime(date);
    return gc.get(2) + 1;
  }

  /**
   * 获取某个日期的结束时间
   * @param d
   * @return
   */
  public static Timestamp getDayEndTime(Date d) {
    Calendar calendar = Calendar.getInstance();
    if (null != d) {
      calendar.setTime(d);
    }
    calendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            23,
            59,
            59);
    calendar.set(Calendar.MILLISECOND, 999);
    return new Timestamp(calendar.getTimeInMillis());
  }

  /**
   * 获取某个日期的开始时间
   * @param d
   * @return
   */
  public static Timestamp getDayStartTime(Date d) {
    Calendar calendar = Calendar.getInstance();
    if (null != d) {
      calendar.setTime(d);
    }
    calendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            0,
            0,
            0);
    calendar.set(Calendar.MILLISECOND, 0);
    return new Timestamp(calendar.getTimeInMillis());
  }

  // -----------------字符串和日期的转换--------------------------------------
  /**
   * 格式化字符串为日期
   *
   * @param date 日期字符串
   * @param format 转换格式
   * @return
   */
  public static Date parseDate(String date, String format) {
    try {
      return new SimpleDateFormat(format).parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Date parseDate(String date) {
    return parseDate(date, DATE_FORMAT);
  }

  public static Date parseChinaDate(String date) {
    return parseDate(date, CHINA_DATE_FORMAT);
  }

  public static Date parseDateTime(String date) {
    return parseDate(date, DATETIME_FORMAT);
  }

  public static Date parseTime(String date) {
    return parseDate(date, TIME_FORMAT);
  }

  /**
   * 日期转换成时间字符串
   *
   * @return
   */
  public static String buildDateToStr(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateString = formatter.format(date);
    return dateString;
  };

  // --------------获取星期几---------------------------------------------------
  /**
   * 获取星期
   *
   * @param date
   * @return
   */
  public static int getWeek(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.get(Calendar.DAY_OF_WEEK);
  }
  /**
   * 获取星期几
   *
   * @param strDate
   * @return
   */
  public static String getWeekDayName(String strDate) {
    String[] mName = {"日", "一", "二", "三", "四", "五", "六"};
    Date date = parseDate(strDate);
    int week = getWeek(date);
    return "星期" + mName[week];
  }

  public static String getWeekDayName(Date date) {
    String[] mName = {"日", "一", "二", "三", "四", "五", "六"};
    int week = getWeek(date);
    return "星期" + mName[week];
  }

  /**
   * 一年中的星期几
   *
   * @return
   */
  public static int getWeekNumOfYear(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.WEEK_OF_YEAR);
  }

  public static int getWeekNumOfYear(String date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(parseDate(date, DATE_FORMAT));
    return calendar.get(Calendar.WEEK_OF_YEAR);
  }

  /**
   * 获取本周星期一的日期
   *
   * @param yearNum
   * @param weekNum
   * @return
   * @throws ParseException
   */
  public static String getYearWeekFirstDay(int yearNum, int weekNum) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, yearNum);
    cal.set(Calendar.WEEK_OF_YEAR, weekNum);
    cal.set(Calendar.DAY_OF_WEEK, 2);
    String tempYear = Integer.toString(yearNum);
    String tempMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
    String tempDay = Integer.toString(cal.get(Calendar.DAY_OF_MONTH) - 1);
    return tempYear + "-" + tempMonth + "-" + tempDay;
  }

  /**
   * 获取本周星期天的日期
   *
   * @param yearNum
   * @param weekNum
   * @return
   * @throws ParseException
   */
  public static String getYearWeekEndDay(int yearNum, int weekNum) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, yearNum);
    cal.set(Calendar.WEEK_OF_YEAR, weekNum + 1);
    cal.set(Calendar.DAY_OF_WEEK, 1);

    String tempYear = Integer.toString(yearNum);
    String tempMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
    String tempDay = Integer.toString(cal.get(Calendar.DAY_OF_MONTH) - 1);
    return tempYear + "-" + tempMonth + "-" + tempDay;
  }

  /**
   * Excel导入的时候。需要转换一下时间
   *
   * @param dateStr 时间字符串
   * @return Date类型数据
   * @throws ParseException
   */
  public static Date buildStrToDate(String dateStr) throws ParseException {
    // 格式化方法
    SimpleDateFormat format = null;
    Date date = null;
    // 判断是什么格式的时间
    if (dateStr.indexOf("月") > 0) {
      // 取年
      String yeah = dateStr.substring(0, 4);
      // 取月
      String yue = dateStr.substring(dateStr.indexOf("年") + 1, dateStr.indexOf("月"));
      // 取日
      String ri = dateStr.substring(dateStr.indexOf("月") + 1, dateStr.indexOf("日"));
      String buildType = "yyyy年";
      // 2018年7月10日16:50:21类型的
      if (yue.length() > 1) {
        buildType = buildType + "MM月";
      } else {
        buildType = buildType + "M月";
      }
      ;
      // 判断天
      if (ri.length() > 1) {
        buildType = buildType + "dd日";
      } else {
        buildType = buildType + "d日";
      }
      if (dateStr.indexOf(" ") > 0) {
        format = new SimpleDateFormat(buildType + " HH:mm:ss");
      } else {
        format = new SimpleDateFormat(buildType + "HH:mm:ss");
      }
      date = format.parse(dateStr);
    } else if (dateStr.indexOf("-") > 0) {
      // 英文格式时间
      format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      date = format.parse(dateStr);
    } else if (dateStr.indexOf("/") > 0) {
      // 英文格式时间
      format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      date = format.parse(dateStr);
    }
    return date;
  }

  /**
   * 提取时间中的年月日
   *
   * @param dateStr 时间字符串
   * @return
   */
  public static String splitZhDate(String dateStr) {
    // 取年
    String yeah = dateStr.substring(0, 4);
    // 取月
    String yue = dateStr.substring(dateStr.indexOf("年") + 1, dateStr.indexOf("月"));
    // 取日
    String ri = dateStr.substring(dateStr.indexOf("月") + 1, dateStr.indexOf("日"));
    // 取时间
    return yeah + "-" + yue + "-" + ri;
  }

  /**
   * 转换成可以保存到数据库的时间
   *
   * @param date 时间对象
   * @return
   */
  public static Timestamp getTimestamp(Date date) {
    return new Timestamp(date.getTime());
  }

  /**
   * * 将传递过来的字符串转化为Timestamp类型
   *
   * @param s
   * @return
   */
  public static Timestamp toDate(String s) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date d = null;
    try {
      d = sdf.parse(s);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return new Timestamp(d.getTime());
  }

  // 01. Date --> java.time.LocalDateTime
  public void UDateToLocalDateTime() {
    Date date = new Date();
    Instant instant = date.toInstant();
    ZoneId zone = ZoneId.systemDefault();
    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
  }

  // 02. Date --> java.time.LocalDate
  public void UDateToLocalDate() {
    Date date = new Date();
    Instant instant = date.toInstant();
    ZoneId zone = ZoneId.systemDefault();
    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
    LocalDate localDate = localDateTime.toLocalDate();
  }

  // 03. Date --> java.time.LocalTime
  public void UDateToLocalTime() {
    Date date = new Date();
    Instant instant = date.toInstant();
    ZoneId zone = ZoneId.systemDefault();
    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
    LocalTime localTime = localDateTime.toLocalTime();
  }

  // 04. java.time.LocalDateTime --> Date
  public void LocalDateTimeToUdate() {
    LocalDateTime localDateTime = LocalDateTime.now();
    ZoneId zone = ZoneId.systemDefault();
    Instant instant = localDateTime.atZone(zone).toInstant();
    Date date = Date.from(instant);
  }

  // 05. java.time.LocalDate --> Date
  public void LocalDateToUdate() {
    LocalDate localDate = LocalDate.now();
    ZoneId zone = ZoneId.systemDefault();
    Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
    Date date = Date.from(instant);
  }

  // 06. java.time.LocalTime --> Date
  public void LocalTimeToUdate() {
    LocalTime localTime = LocalTime.now();
    LocalDate localDate = LocalDate.now();
    LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
    ZoneId zone = ZoneId.systemDefault();
    Instant instant = localDateTime.atZone(zone).toInstant();
    Date date = Date.from(instant);
  }


  /**
   * 判断传递进来的时间，是否小于相差时间
   * @param startTime 开始时间
   * @param hourNum 小时数
   * @return 如果小于则true，否则false
   */
  public static boolean diffStartTimeBy(String startTime, int hourNum){
    // 无效的时间
    if("".equals(startTime)){
      return false;
    }
    // 一天的毫秒数
    long nh = 1000 * 60 * 60;
    // 毫秒数转时间
    Long time = Long.valueOf(startTime);
    // 今天减去开始时间
    Long diffTime = new Date().getTime() - time;
    // 计算差多少小时
    long hour = diffTime / nh;
    return (hour <= hourNum);
  }
  //获取上周的开始时间
  public static Date getBeginDayOfLastWeek() {
    Date date = new Date();
    if (date == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
    if (dayofweek == 1) {
      dayofweek += 7;
    }
    cal.add(Calendar.DATE, 2 - dayofweek - 7);
    return getDayStartTime(cal.getTime());
  }

  // 获取上周的结束时间
  public static Date getEndDayOfLastWeek() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(getBeginDayOfLastWeek());
    cal.add(Calendar.DAY_OF_WEEK, 6);
    Date weekEndSta = cal.getTime();
    return getDayEndTime(weekEndSta);
  }

  //获取三十天前的日期
  public static String getThirtydaysBefore(Date today) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar theCa = Calendar.getInstance();
    theCa.setTime(today);
    theCa.add(theCa.DATE, -30);//最后一个数字30可改，30天的意思
    Date start = theCa.getTime();
    return sdf.format(start);
  }
}

package com.nighthawk.spring_portfolio.mvc.calendar;

/** Simple POJO 
 * Used to Interface with APCalendar
 * The toString method(s) prepares object for JSON serialization
 * Note... this is NOT an entity, just an abstraction
 */
class Year {
   private int year;
   private int month;
   private int day;
   private boolean isLeapYear;
   private int firstDayOfYear;
   private int dayOfYear;

   // zero argument constructor
   public Year() {} 

   /* year getter/setters */
   public int getYear() {
      return year;
   }
   public void setYear(int year) {
      this.year = year;
      this.setIsLeapYear(year);
      this.setfirstDayOfYear(year);
   }

   public int getDate() {
      return year;
   }

   public void setDate(int month, int day, int year) {
      this.month = month;
      this.day = day;
      this.year = year;
      this.setIsLeapYear(year);
      this.setDayOfYear(month, day, year);
   }

   private void setDayOfYear(int month2, int day2, int year2) {
   }

   /* isLeapYear getter/setters */
   public boolean getIsLeapYear(int year) {
      return APCalendar.isLeapYear(year);
   }
   public int getfirstDayOfYear(int year) {
      return APCalendar.firstDayOfYear(year);
   }
   private void setfirstDayOfYear(int year) {
      this.firstDayOfYear = APCalendar.firstDayOfYear(year);
   }
   private void setIsLeapYear(int year) {  // this is private to avoid tampering
      this.isLeapYear = APCalendar.isLeapYear(year);
   }

   /* isLeapYearToString formatted to be mapped to JSON */
   public String isLeapYearToString(){
      return ( "{ \"year\": "  +this.year+  ", " + "\"isLeapYear\": "  +this.isLeapYear+ " }" );
   }	
   
   public String firstDayOfYearToString(){
      return ( "{ \"year\": "  +this.year+  ", " + "\"firstDayOfYear\": "  +this.firstDayOfYear+ " }" );
   }	

   /* firstDayOfYearToString formatted to be mapped to JSON */
   public String dayOfYearToString() {
      return ("{ \"month\": " + this.month + ", " + "\"day\": " + this.day + ", " + "\"year\": " + this.year + ", "
            + "\"dayOfYear\": " + this.dayOfYear + " }");
   }
   /* standard toString placeholder until class is extended */
   public String toString() { 
      return isLeapYearToString(); 
   }

   public static void main(String[] args) {
      Year year = new Year();
      year.setYear(2022);
      System.out.println(year);
   }
}
package me.CodeingBoy.timetable2googlecalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Course {
    private static Calendar week1Date = Calendar.getInstance();

    private String name;
    private String teacher;
    private String classroom;
    private Calendar start, end;

    public Course(String name, String teacher, String classroom, Calendar start, Calendar end) {
        this.name = name;
        this.teacher = teacher;
        this.classroom = classroom;
        this.start = start;
        this.end = end;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public static Course[] parseCourse(String inf) throws IllegalArgumentException {
        try {
            ArrayList<Course> courses = new ArrayList<>();

            String[] strings = inf.split("\n");

            int weekDay = Integer.parseInt(strings[0].split(" ")[0]);
            int courseNum = Integer.parseInt(strings[0].split(" ")[1]);

            String name = strings[1];
            String times = strings[2];
            String classroom = strings[3];
            String teacher = strings[4];

            for (String t : times.split(",")) {
                // get start and end week information
                int weekStart = Integer.parseInt(t.split("-")[0]);
                int weekEnd = t.split("-").length == 2 ? Integer.parseInt(t.split("-")[1]) : weekStart;

                // calculate date
                for (int i = weekStart; i < weekEnd + 1; i++) {
                    Calendar startTime = weekToDate(i, weekDay);

                    switch (courseNum) {
                        case 1:
                            startTime.set(Calendar.HOUR_OF_DAY, 8);
                            startTime.set(Calendar.MINUTE, 40);
                            break;
                        case 2:
                            startTime.set(Calendar.HOUR_OF_DAY, 10);
                            startTime.set(Calendar.MINUTE, 25);
                            break;
                        case 3:
                            startTime.set(Calendar.HOUR_OF_DAY, 14);
                            startTime.set(Calendar.MINUTE, 15);
                            break;
                        case 4:
                            startTime.set(Calendar.HOUR_OF_DAY, 16);
                            startTime.set(Calendar.MINUTE, 00);
                            break;
                        case 5:
                            startTime.set(Calendar.HOUR_OF_DAY, 19);
                            startTime.set(Calendar.MINUTE, 00);
                            break;
                    }

                    Calendar endTime = (Calendar) startTime.clone();
                    endTime.add(Calendar.MINUTE, 95);
                    if (courseNum == 4) {
                        endTime.add(Calendar.MINUTE, -15);
                    }

                    courses.add(new Course(name, teacher, classroom, startTime, endTime));
                }

            }
            return courses.toArray(new Course[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Occurred error while reading " + inf);
        }
    }

    public static Calendar getWeek1Date() {
        return week1Date;
    }

    public static void setWeek1Date(Calendar week1Date) {
        Course.week1Date = week1Date;
    }

    private static Calendar weekToDate(int week, int weekday) {
        Calendar newDate = (Calendar) week1Date.clone();

        newDate.add(Calendar.WEEK_OF_MONTH, week - 1);
        newDate.add(Calendar.DAY_OF_WEEK, weekday - 1);

        return newDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        final StringBuffer sb = new StringBuffer();
        sb.append("课程名：").append(name).append('\n');
        sb.append("教师：").append(teacher).append('\n');
        sb.append("课室：").append(classroom).append('\n');
        sb.append("开始时间：").append(dateFormat.format(start.getTime())).append('\n');
        sb.append("结束时间：").append(dateFormat.format(end.getTime())).append('\n');
        return sb.toString();
    }

    public String toGoogleCalendarString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        String startDate = dateFormat.format(start.getTime()),
                startTime = timeFormat.format(start.getTime());
        String endDate = dateFormat.format(end.getTime()),
                endTime = timeFormat.format(end.getTime());

        String result = String.format("%s,%s,%s,%s,%s,False,\"%s\"\n", name, startDate, startTime, endDate, endTime, toString());

        return result;
    }
}
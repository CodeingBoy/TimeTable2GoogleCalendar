package me.CodeingBoy.timetable2googlecalendar;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Main {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("File not found.\nUsage: input output week1Date");
        }

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar week1 = Calendar.getInstance();
            week1.setTime(dateFormat.parse(args[2]));
            Course.setWeek1Date(week1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        File file = new File(args[0]);
        StringBuffer stringBuffer = new StringBuffer();
        try {
            FileReader reader = new FileReader(file);

            while (reader.ready()) {
                stringBuffer.append((char) reader.read());
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] courseStrings = stringBuffer.toString().split("\n\n");

        ArrayList<Course> courses = new ArrayList();
        for (String c : courseStrings) {
            for (Course course : Course.parseCourse(c)) {
                courses.add(course);
            }
        }

        File output = new File(args[1]);
        if (!output.exists()) {
            try {
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter outputWriter = new FileWriter(output);
            outputWriter.write("Subject,Start Date,Start Time,End Date,End Time,All Day Event,Description\n");

            for (Course course : courses) {
                System.out.println(course);
                outputWriter.write(course.toGoogleCalendarString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Courses information had been written to " + args[1]);
    }
}

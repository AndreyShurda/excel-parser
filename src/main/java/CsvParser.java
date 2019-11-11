import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

public class CsvParser {

    public static void main(String[] args) throws Exception {
//        File file = new File("LicenseRegistry20191011-2.csv");
//        InputStream targetStream = new FileInputStream(file);
//        readAllLines(targetStream);

        Set<Person> set = new TreeSet<>();

        LocalDate date = LocalDate.now();
        LocalDate date2 = LocalDate.of(2018,5,12);
        LocalDate date3 = LocalDate.of(2019,5,12);


        set.add(new Person("test",date));
        set.add(new Person("test",date3));
        set.add(new Person("test",date2));

        System.out.println(set);

    }

    public static void writeTofile(List<String> lines, String filename) throws IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "WINDOWS-1251"));
        for (String unique : lines) {
            writer.write(unique);
            writer.newLine();
        }
        writer.close();
    }


    public static void readAllLines(InputStream in) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    @Data
    @AllArgsConstructor
    static
    class Person implements Comparable<Person>{
        private String name;
        private LocalDate date;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return Objects.equals(date, person.date);
        }

        @Override
        public int hashCode() {
            return Objects.hash(date);
        }

        @Override
        public int compareTo(Person o) {
            return this.date.compareTo(o.date);
        }
    }
}

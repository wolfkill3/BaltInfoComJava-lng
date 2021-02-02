import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class mymain {
    public static void main(String[] args) {
        Path path = Path.of("D:\\JavaTest\\BaltInfoComTest\\asd.csv");
        var splitedList = new HashSet<String[]>();
        var splitedHashMap = new HashMap<String,Set<String[]>>();
        try {
            List<String> lines = Files.readAllLines(path)
                    .stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
            lines.forEach(t->{
                var splitedString = t.split(";");
                splitedList.add(splitedString);
            });
            splitedList.forEach(t->{
                System.out.println();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

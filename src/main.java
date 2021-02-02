import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class main {
    public static void main(String[] args) {
        Path path = Path.of("D:\\JavaTest\\BaltInfoComTest\\asd.csv");
        var columns = new ArrayList<HashMap<String, Set<Integer>>>();
        var finalGroups = new ArrayList<Set<Integer>>();
        for (int i = 0; i < 3; i++) {
            columns.add(new HashMap<>());
        }
        try {
            var lines = Files.readAllLines(path);
            var setStrings = new HashSet<String>();


            for (int i = 0; i < lines.size(); i++) {
                System.out.println("sorted " + (i+1) + " in " + lines.size());
                var t = lines.get(i);
                var rowValues = t.split(";");
                if (rowValues.length != 3) continue;

                if (i == 0) {

                    setStrings.add(t);
                    var group = new HashSet<Integer>();
                    group.add(0);
                    finalGroups.add(group);



                    for (int j = 0; j < rowValues.length; j++) {
                        columns.get(j).put(rowValues[j], group);
                    }
                } else if (!setStrings.contains(t)) {
                    setStrings.add(t);
                    var matchedGroups = new ArrayList<Set<Integer>>();
                    for (int j = 0; j < rowValues.length; j++) {
                        var columnsGroup = columns.get(j);
                        var columnsGroupGet = columnsGroup.get(rowValues[j]);
                        matchedGroups.add(columnsGroupGet);
                    }
                    var unicGroups = matchedGroups.stream()
                            .filter(Objects::nonNull)
                            .distinct()
                            .collect(Collectors.toList());

                    if (unicGroups.size() == 0) {
                        var group = new HashSet<Integer>();
                        group.add(i);
                        for (int j = 0; j < rowValues.length; j++) {
                            columns.get(j).put(rowValues[j], group);
                        }
                        finalGroups.add(group);
                    } else if (unicGroups.size() == 1) {
                        var matchedGroup = unicGroups.get(0);
                        matchedGroup.add(i);
                        for (int j = 0; j < rowValues.length; j++) {
                            columns.get(j).put(rowValues[j], matchedGroup);
                        }

                    } else {
                        var combinedGroup = new HashSet<Integer>();
                        for (Set<Integer> unicGroup : unicGroups) {
                            combinedGroup.addAll(unicGroup);
                            finalGroups.remove(unicGroup);
                        }
                        for (int j = 0; j < combinedGroup.size(); j++) {
                            var rowValuesTwo = t.split(";");
                            for (int k = 0; k < rowValuesTwo.length; k++) {
                                columns.get(k).put(rowValues[k], combinedGroup);
                            }

                        }
                        finalGroups.add(combinedGroup);
                    }
                }


            }
            System.out.println(finalGroups.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

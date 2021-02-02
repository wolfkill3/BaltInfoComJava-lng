import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class test {
    public static void main(String[] args) {
        Path path = Path.of("D:\\JavaTest\\BaltInfoComTest\\lng.csv");
        Path path2 = Path.of("D:\\JavaTest\\BaltInfoComTest\\te2.txt");
        List<String> lines;
        List<List<String>> newFile;
        int count = 1;
        int countOfBigGroups = 0;
        try {
            lines = Files.readAllLines(path);
            System.out.println(lines.size());
            newFile = findLineGroups(lines.stream().distinct().collect(Collectors.toList()));
            System.out.println(newFile.size());
            Files.createFile(path2);

            Collections.sort(newFile, (o1, o2) -> o2.size() - o1.size());

            for (List<String> group : newFile) {
                if (group.size() > 1) {
                    countOfBigGroups++;
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(new File(String.valueOf(path2)));
            fileOutputStream.write(("Количество групп с более чем 1 элементом: " + countOfBigGroups + "\n").getBytes());

            for (List<String> group : newFile) {
                fileOutputStream.write(("Group: " + count + "\n").getBytes());
                for (String strings : group) {
                    fileOutputStream.write((strings + "\n").getBytes());
                }
                count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<List<String>> findLineGroups(List<String> lines) {
        class NewLineElement {
            private String lineElement;
            private int columnNum;

            private NewLineElement(String lineElement, int columnNum) {
                this.lineElement = lineElement;
                this.columnNum = columnNum;
            }
        }

        if (lines == null)
            return Collections.emptyList();

        List<List<String>> linesGroups = new ArrayList<>();
        if (lines.size() < 2) {
            linesGroups.add(lines);
            return linesGroups;
        }

        List<Map<String, Integer>> columns = new ArrayList<>();
        Map<Integer, Integer> unitedGroups = new HashMap<>();
        for (String line : lines) {
            String[] lineElements = line.split(";");
            if (lineElements.length != 3) {
                continue;
            }
            TreeSet<Integer> groupsWithSameElems = new TreeSet<>();
            List<NewLineElement> newElements = new ArrayList<>();

            for (int elmIndex = 0; elmIndex < lineElements.length; elmIndex++) {
                String currLnElem = lineElements[elmIndex];
                if (columns.size() == elmIndex)
                    columns.add(new HashMap<>());
                if ("".equals(currLnElem.replaceAll("\"", "").trim()))
                    continue;

                Map<String, Integer> currCol = columns.get(elmIndex);
                Integer elemGrNum = currCol.get(currLnElem);
                if (elemGrNum != null) {
                    while (unitedGroups.containsKey(elemGrNum))
                        elemGrNum = unitedGroups.get(elemGrNum);
                    groupsWithSameElems.add(elemGrNum);
                } else {
                    newElements.add(new NewLineElement(currLnElem, elmIndex));
                }
            }
            int groupNumber;
            if (groupsWithSameElems.isEmpty()) {
                linesGroups.add(new ArrayList<>());
                groupNumber = linesGroups.size() - 1;
            } else {
                groupNumber = groupsWithSameElems.first();
            }
            for (NewLineElement newLineElement : newElements) {
                columns.get(newLineElement.columnNum).put(newLineElement.lineElement, groupNumber);
            }
            for (int matchedGrNum : groupsWithSameElems) {
                if (matchedGrNum != groupNumber) {
                    unitedGroups.put(matchedGrNum, groupNumber);
                    linesGroups.get(groupNumber).addAll(linesGroups.get(matchedGrNum));
                    linesGroups.set(matchedGrNum, null);
                }
            }
            linesGroups.get(groupNumber).add(line);
        }
        linesGroups.removeAll(Collections.singleton(null));
        return linesGroups;
    }
}

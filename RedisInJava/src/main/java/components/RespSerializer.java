package components;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class RespSerializer {

    public List<String[]> deserialize(byte[] command) {
        String dataString = new String(command, StandardCharsets.UTF_8);
        char[] dataArray = dataString.toCharArray();
        List<String[]> resultList = new ArrayList<>();
        int i = 0;
        while (i < dataArray.length) {
            char currentChar = dataArray[i];
            if (currentChar == '*') {
                StringBuilder arrLength = new StringBuilder();
                while (i < dataArray.length && Character.isDigit(dataArray[i]))
                    arrLength.append(dataArray[i++]);
                i += 2;
                if (dataArray[i] == '*') {
                    for (int j = 0; j < Integer.parseInt(arrLength.toString()); j++) {
                        StringBuilder nestedLength = new StringBuilder();
                        i++;
                        while (i < dataArray.length && Character.isDigit(dataArray[i])) {
                            nestedLength.append(dataArray[i++]);
                        }
                        i += 2;
                        String[] subArray = new String[Integer.parseInt(nestedLength.toString())];
                        i = getParts(dataArray, i, subArray);
                        resultList.add(subArray);
                    }
                } else {
                    String[] subArray = new String[Integer.parseInt(arrLength.toString())];
                    i = getParts(dataArray, i, subArray);
                    resultList.add(subArray);
                }
            }
        }
        return resultList;
    }

    private int getParts(char[] dataArray, int i, String[] subArray) {
        int j = 0;
        while (i < dataArray.length && j < subArray.length) {
            if (dataArray[i] == '$') {
                i++;
                StringBuilder partLength = new StringBuilder();
                while (i < dataArray.length && Character.isDigit(dataArray[i])) {
                    partLength.append(dataArray[i++]);
                    i++;
                }
                i += 2;
                StringBuilder subParts = new StringBuilder();
                for (int k = 0; k < Integer.parseInt(partLength.toString()); k++)
                    subParts.append(dataArray[i++]);
                i += 2;
                subArray[j++] = subParts.toString();
            }
        }
        return i;
    }
}

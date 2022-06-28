package dh.realestate.service.molit;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Scanner;

@Component
public class MolitCode {

    public static String codeSearch(String region)
            throws FileNotFoundException, UnsupportedEncodingException {

        var filePath = "src/main/resources/rs_code.txt";
        var doc = new InputStreamReader(new FileInputStream(filePath),"EUC-KR");
        Scanner obj = new Scanner(doc);
        String line, code;
        String[] arr;

        while (obj.hasNextLine()) {
            line = obj.nextLine();
            arr = line.split("\t", 3)[1].split(" ");

            if (arr.length == 2 && arr[1].equals(region)) {
                code = line.split("\t", 3)[0].substring(0,5);
                return code;
            }
        }

        return null;
    }
}

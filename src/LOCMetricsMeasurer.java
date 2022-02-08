import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class LOCMetricsMeasurer{
    
    private String blockCommentStart;
    private String blockCommentEnd;;
    private String inlineCommentStart;
    //TODO: should we handle documentation comment blocks seperately?
    
    public LOCMetricsMeasurer(){
        //TODO: write constructor if necessary
    }

    public LOCMetricsMeasurer(String blockCommentStart, String blockCommentEnd, String inlineCommentStart){
        this.blockCommentStart = blockCommentStart;
        this.blockCommentEnd = blockCommentEnd;
        this.inlineCommentStart = inlineCommentStart;
    }

    public String readFile(String path){
        String content = "";
        try {
            BufferedReader filein = new BufferedReader(new FileReader(new File(path)));
            FileReader fileIn = new FileReader(path);

            while ((content = filein.readLine()) != null){
                content = filein.readLine();
                System.out.println(content);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }


    public LOCMetrics measureClassLOCMetrics(String classFileName){
        //TODO: change so this throws an exception and caller has to handle file error
        int loc = 0;
        int cloc = 0;
        float wmc = 1;
        String filecontent = readFile(classFileName);



            Scanner scanner = new Scanner(filecontent);
            Boolean insideBlockComment = false;
            //Boolean insideMethod = false;
            int nb_methods=0;
            int nb_predicates = 0;
            //(?!if|while|for|catch|do|new|return)^(public\s+|private\s+|protected\s+).+(.)\s{$
            //above is regex for matching function definition from:
            // https://stackoverflow.com/questions/47387307/regular-expression-that-matches-java-method-definition
            Pattern method_def = Pattern.compile("(?!if|while|for|catch|do|new|return)^(public\\s+|private\\s+|protected\\s+).+(.)\\s?\\{$", Pattern.CASE_INSENSITIVE);
            Matcher matcher;
            while (scanner.hasNextLine()){
                String line = scanner.nextLine().trim(); // please remember
                matcher = method_def.matcher(line);
                //System.out.println(line);
                if (line.isEmpty()){
                    continue;
                    // } else if ((line.startsWith("public") ||
                    //             line.startsWith("private") ||
                    //             line.startsWith("protected") ) &&
                    //             !line.contains("class")){
                    //                 insideMethod = true;
                } if (matcher.find()){
                    //insideMethod = true;
                    nb_methods++;
                } else if (insideBlockComment){
                    loc++;
                    cloc++;
                    if (line.endsWith(blockCommentEnd)) insideBlockComment = false;
                } else if (line.contains(inlineCommentStart)){
                    loc++;
                    cloc++;
                } else if (line.startsWith(blockCommentStart)){
                    insideBlockComment = true;
                    loc++;
                    cloc++;
                } else {
                    loc++;
                }

                if (line.contains("if") ||
                        line.contains("else") ||
                        line.contains("while") ||
                        line.contains("for")){
                    nb_predicates++;
                }
            }
            wmc = (nb_methods + nb_predicates)/ (float)nb_methods;
            scanner.close();

        return new LOCMetrics(loc, cloc);
    }

    public LOCMetrics computePackageLOCMetrics(ArrayList<LOCMetrics> childrenMetrics){
       //TODO: implement
        return new LOCMetrics();
    }
}
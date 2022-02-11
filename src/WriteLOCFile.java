import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class WriteLOCFile {

    ArrayList<String> pathList = new ArrayList<>();
    ArrayList<String> csvLign  = new ArrayList<>();
    ArrayList<String> packages = new ArrayList<>();


    public String classCSVLign(String path, LOCMetricsMeasurer measurer){
        LOCMetrics lm = measurer.measureClassLOCMetrics(path); //TODO: mettre dans fichier externe
        int loc = lm.getLoc();
        int cloc = lm.getCloc();
        float dc = lm.getDc();
        int wmc = lm.getWmc();
        float bc = lm.getBc();

        String javaString = path.substring(path.lastIndexOf("/")+1,path.length());

        return path + ","+ javaString +","+ String.valueOf(loc) + "," + String.valueOf(cloc) + "," + String.valueOf(dc) + ","
            + String.valueOf(wmc) + "," + String.valueOf(bc);
    }

//inspiré de : https://stackoverflow.com/questions/3332486/program-to-get-all-files-within-a-directory-in-java
//mais inversé la logique du programme et adapté à nos besoins spécifiques
    public void getFiles(File f, String target) {
        File files[];
        if (f.isDirectory()){
            files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                getFiles(files[i], target);
            }
        }
        else if(f.isFile() && f.getAbsolutePath().indexOf(".java") != -1){
            String absPath = f.getAbsolutePath();
            pathList.add(absPath.substring(absPath.indexOf(target),absPath.length()));
            packages.add(absPath.substring(absPath.indexOf(target),absPath.lastIndexOf("/")));
        }
    }

    public void application(LOCMetricsMeasurer measurer){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("classes.csv")));
            bw.write("chemin, class, classe_LOC, classe_CLOC, classe_DC, WMC, classe_BC\n");
            for(int i=0 ; i<pathList.size();i++){
                csvLign.add(classCSVLign(pathList.get(i), measurer));
            }
            for(int i=0;i<csvLign.size();i++){
                bw.write(csvLign.get(i)+"\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMap(){
        HashMap<String,LOCMetrics> path2Mesures = new HashMap<>();
        ArrayList<String> lignes = new ArrayList<>();

        for(int i =0;i<packages.size();i++){
            int loc = Integer.parseInt(csvLign.get(i).split(",")[2]);
            int cloc = Integer.parseInt(csvLign.get(i).split(",")[3]);
            if(!path2Mesures.containsKey(packages.get(i))){
                path2Mesures.put(packages.get(i),new LOCMetrics(packages.get(i),true,loc,cloc,0));
            }
            else{
                int demo = path2Mesures.get(packages.get(i)).getLoc() + loc;
                path2Mesures.get(packages.get(i)).setLoc(demo);

                int demo2 = path2Mesures.get(packages.get(i)).getCloc() + cloc;
                path2Mesures.get(packages.get(i)).setCloc(cloc);
            }
        }
        try {
            BufferedWriter bwriter = new BufferedWriter(new FileWriter(new File("paquets.csv")));
            bwriter.write("chemin, paquet, paquet_LOC, paquet_CLOC, paquet_DC, WMP, paquet_BC\n");
            for (int i = 0; i < packages.size(); i++) {
                LOCMetrics cur_element = path2Mesures.get(packages.get(i));
                String oneLine = packages.get(i) + "," + packages.get(i).replace("/", ".") + "," + cur_element.getLoc() + "," + cur_element.getCloc() + ","
                    + cur_element.getDc() + "," + cur_element.getWmc() +"," + cur_element.getBc();

                if (!lignes.contains(oneLine)) {
                    lignes.add(oneLine);
                    bwriter.write(oneLine + "\n");
                }
            }
            bwriter.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        LOCMetricsMeasurer measurer = new LOCMetricsMeasurer("/*", "*/", "//");
        WriteLOCFile wlf = new WriteLOCFile();
        wlf.getFiles(new File(System.getProperty("user.dir")+"/" + args[0]), args[0]);
        wlf.application(measurer);
        wlf.setMap();
    }
}

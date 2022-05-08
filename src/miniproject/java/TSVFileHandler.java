package miniproject.java;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class TSVFileHandler {

    private String filename;

    //Regex to match the URL from the TSV
    //This is for future use
    //private static final String URL_PATTERN = "(http|ftp|https):\\/\\/([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:\\/~+#-]*[\\w@?^=%&\\/~+#-])";
    //Since the starting dataset is too large, I will focus on the domain the occurs the most Dailymail.co.uk
    private static final String URL_DAILYMAIL_PATTERN = "(http|ftp|https):\\/\\/([\\w\\d]+\\.)?dailymail\\.co\\.uk(\\/.*)?";
    private static final String PARAGRAPH = "<p>(.?)</p>";
    private static Pattern urlPattern;

    /**
     * Create the tsv file
     * @return
     */
    public String CreateTSV(){
        String filePath = null;
        try{
            filePath = "Data_Out\\" + "1" +this.filename;
            File tsvModel = new File(filePath);
            if(tsvModel.createNewFile()){
                System.out.println("File created: "+ tsvModel.getName());
            }else{
                System.out.println("File already exists");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return filePath;
    }

    /**
     * Constructor method
     */
    public TSVFileHandler() {
        urlPattern = Pattern.compile(URL_DAILYMAIL_PATTERN);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateStr = dateFormat.format(cal.getTime());
        this.filename = dateStr+ ".csv"; //yyyyMMdd.csv
    }

    /**
     * Getter method
     * @return
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Setter method
     * @param filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Read the file and create a file from the data
     * @param filename
     * @throws IOException
     */
    public void read(String filename) throws IOException {
        long startTime = System.nanoTime();

        String line;
        File file = new File("Data_In\\" + filename);
        System.out.println("Reading file: "+ file.getName());
        BufferedReader br = new BufferedReader(new FileReader(file));
        while((line = br.readLine()) != null){
            Matcher match = urlPattern.matcher(line);
            if(match.find()){
                AppendToFile("Data_Out\\" + this.filename,match.group() + "\n" );
            }
        }
        stripDuplicatesFromFile("Data_Out\\" + this.filename);

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in seconds: " + timeElapsed / 1000000000);
    }

    /**
     * Take the string and append it to the end of a given file
     * @param filePath
     * @param str
     */
    void AppendToFile(String filePath, String str){
        //Name of the file
        //String FilePath = "Data_Out\\" + this.filename;
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
            out.write(str);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This goes through a file and deletes all the duplicates
     * @param filename
     * @throws IOException
     */
    private void stripDuplicatesFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        Set<String> lines = new HashSet<String>(10000); // maybe should be bigger
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (String unique : lines) {
            writer.write(unique);
            writer.newLine();
        }
        writer.close();
    }


}

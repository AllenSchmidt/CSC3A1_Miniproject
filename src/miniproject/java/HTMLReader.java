package miniproject.java;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * This Class is responsible for creating instances of each given html and pull the paragraph data,
 * for use by the Entityfinder
 *
 */
public class HTMLReader{
    //Get the HTML's and create a TSV FILE with headers
    //ID    Domain  Title   Article_body
    private int id;
    WebClient client;
    TSVFileHandler tsvExtractedData;
    private List<String> ListOfNames = new ArrayList<>(10000);
    private List<List<String>> ListOfCombinations = new ArrayList<>(10000);
    private List<ArticleData> Articles = new ArrayList<>(10000);
    private List<String> ArticleTitle = new ArrayList<>(10000);
    /**Setup the webclient and instantiate the TSVFilehandler
     *
     */
    public HTMLReader() {
        client = new WebClient();
        tsvExtractedData = new TSVFileHandler();
        //Create initial state of the client
        client.getOptions().setUseInsecureSSL(true);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getOptions().setPrintContentOnFailingStatusCode(false);
    }

    /**This reads from the list of URLs
     * extracts article data and place it in a tsv file
     *
     * @param filename
     * @throws FileNotFoundException
     */
    public void ReadFromWebsite(String filename) throws Exception {
        long startTime = System.nanoTime();
        System.out.printf("Reading file: %s\n", filename);
        File file = new File(filename );
        BufferedReader br = new BufferedReader(new FileReader(file));

        List<String> ArticleData = new ArrayList<>();
        try{
            String line;
            String filePath = tsvExtractedData.CreateTSV();
            while((line = br.readLine()) !=null){
                HtmlPage htmlPage = client.getPage(line);
                String title = htmlPage.getTitleText();
                this.ArticleTitle.add(title);
                List<DomNode> para = htmlPage.getByXPath("//*[@class='mol-para-with-font']");
                String concatenateString = "";
                for(DomNode node : para){
                   concatenateString += node.asNormalizedText();
                   concatenateString.replaceAll("\\r|\\n", "");
                }
                String tsvLine = title + '\t' + concatenateString + '\n';
                ArticleData.add(tsvLine);
                tsvExtractedData.AppendToFile(filePath, tsvLine);
            }

        }catch (IOException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
            br.close();
        }

        TimeUnit.SECONDS.sleep(5);
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in seconds: " + timeElapsed / 1000000000);

        System.out.println("Reading Articles...");
        EntityFinder(ArticleData);
    }

    /**
     * Find the entities in a given list
     * @param data
     * @throws Exception
     */
    private void EntityFinder(List<String> data) throws Exception {
        //Start time of the entityfinder
        long startTime = System.nanoTime();

        System.out.println("Reading tokenizer model...");

        //Loading the tokenizer model
        InputStream inputStreamTokenizer = new FileInputStream("model/en-token.bin");
        TokenizerModel tokenModel = new TokenizerModel(inputStreamTokenizer);
        TokenizerME tokenME = new TokenizerME(tokenModel);

        System.out.println("Tokenizer model loaded...");
        System.out.println("Finding entities...");
        //List of entities
        List<String> ListOfNames = null;
        //Make sure \r \n 's and . are absent from the articledata
        //Load the NER-person model
        InputStream isNameFinder = new FileInputStream("model/en-ner-person.bin");
        TokenNameFinderModel model = new TokenNameFinderModel(isNameFinder);

        for(int i=0; i < data.size(); i++){
            ListOfNames = new ArrayList<>();
            //This was removed earliers, but to make sure that there aren't any straglers
            data.get(i).replaceAll("\r", "");
            data.get(i).replaceAll("\n", "");
            data.get(i).replaceAll("'s", "");
            data.get(i).replaceAll(".", "");

            String tokens[] = tokenME.tokenize(data.get(i));

            //Namefinder Class
            NameFinderME nameFinder = new NameFinderME(model);
            Span names[] = nameFinder.find(tokens);
            //Add the names to the list of names
            for(Span s : names){
                ListOfNames.add(tokens[s.getStart()]);
            }
            //Create the combinations of the names for the graph ADT
            List<List<String>> ListOfCombinations = new ArrayList<>();
            for(List<String> combination : new CombinatoricIterable<>(removeDuplicate(ListOfNames), 2)){
                ListOfCombinations.add(combination);
            }
            this.Articles.add(new ArticleData(this.ArticleTitle.get(i), removeDuplicate(ListOfNames), ListOfCombinations));
        }
        isNameFinder.close();

        //End time of the entityFinder
        TimeUnit.SECONDS.sleep(5);
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in seconds: " + timeElapsed / 1000000000);
    }

    /**
     * Return the Articles
     * @return
     */
    public List<ArticleData> getArticles(){return this.Articles;}

    private List<String> getArticleTitles(){return this.ArticleTitle;}

    /**
     * Remove duplicates from a list with a hashSet
     * @param temp
     * @return
     */
    private List<String> removeDuplicate(List<String> temp){
        return new ArrayList<>(new HashSet<>(temp));
    }
}

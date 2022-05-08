package miniproject.java;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class SocialGraphNetwork extends Application {

    public static void main(String[] args) throws Exception {launch(args);}

    @Override
    public void start(Stage ignored) throws Exception {
        long startTime = System.nanoTime();

        TSVFileHandler tsvFile = new TSVFileHandler();
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------------------------------------------------");
        System.out.println("Enter the name of the file downloaded from the Gdelt database");
        System.out.println("It is in the form %DATE%.export.csv:");
        String filename = sc.nextLine();
        System.out.println("-------------------------------------------------------------");
        tsvFile.read(filename);

        HTMLReader htmlFile = new HTMLReader();
        htmlFile.ReadFromWebsite("Data_Out\\" + tsvFile.getFilename());

        Graph<String, String> g = buildGraph(htmlFile.getArticles());

        TimeUnit.SECONDS.sleep(5);
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.printf("Datasets Creation time in seconds: %d\n", timeElapsed / 1000000000);

        System.out.println("Creating stage for graph");
        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, strategy);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Social Network visualization");
        stage.setMinHeight(500);
        stage.setMaxHeight(800);
        stage.setScene(new Scene(new SmartGraphDemoContainer(graphView), 1024, 768));
        stage.show();

        TimeUnit.SECONDS.sleep(5);
        endTime = System.nanoTime();
        timeElapsed = endTime - startTime;
        System.out.printf("Stage created: %d\n", timeElapsed / 1000000000);

        graphView.init();

    }

    /**
     * Build the graph that will be displayed by the SmartGraph library
     * @param Articles
     * @return
     */
    private Graph<String, String> buildGraph(List<ArticleData> Articles){
        Graph<String, String> graph = new DigraphEdgeList<>();
        List<String> tempNames = new ArrayList<>();
        List<List<String>> tempCombinations = new ArrayList<>();

        int t = 0;
        // From the whole set of articles jsut pick and display 10 random articles and their connections
        System.out.println("-------------------------------------------------------------");
        while(t < 7){
            System.out.printf("Article %d: %s\n", t+1, Articles.get(t).getArticleTitle());
            int randomArticleChoice = getRandomNumber(1, Articles.size());
            List<String> names = Articles.get(randomArticleChoice).getListOfNames();
            for(int i=0; i < names.size(); i++){
                tempNames.add(names.get(i));
            }
            List<List<String>> comps = Articles.get(randomArticleChoice).getListOfCombinations();
            for(int i=0; i < comps.size(); i++){
                tempCombinations.add(comps.get(i));
            }
            t++;
        }

        List<String> NoDuplicates = new ArrayList<>(new HashSet<>(tempNames));
        tempNames = NoDuplicates;

        for(int i=0; i< tempNames.size(); i++) {
            graph.insertVertex(tempNames.get(i));
        }

        for(int i=0; i< tempCombinations.size(); i++){
                graph.insertEdge(tempCombinations.get(i).get(0), tempCombinations.get(i).get(1), String.valueOf(i));
        }
        //Print the amount of articles
        System.out.printf("There are %d Articles available in the Dataset\n", Articles.size());
        System.out.println("-------------------------------------------------------------");
        return graph;
    }

    /**
     * Creates a random number within a range
     * @param min
     * @param max
     * @return
     */
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

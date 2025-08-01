import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.DataModelBuilder;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProductRecommendationSystem {

    public static void main(String[] args) {
        try {
            // Load data from a CSV file in the format userID,itemID,preference
            File dataFile = new File("data/dataset.csv");
            DataModel model = new FileDataModel(dataFile);

            // Use Pearson Correlation to find similarity between users
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

            // Define the user neighborhood (e.g., top 2 similar users)
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);

            // Build the recommender
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            // Recommend items for a specific user
            int userID = 1;
            List<RecommendedItem> recommendations = recommender.recommend(userID, 3);

            // Display the recommended items
            System.out.println("Recommended items for user ID " + userID + ":");
            for (RecommendedItem recommendation : recommendations) {
                System.out.println("Item ID: " + recommendation.getItemID() + ", Score: " + recommendation.getValue());
            }

        } catch (IOException | TasteException e) {
            e.printStackTrace();
        }
    }
}

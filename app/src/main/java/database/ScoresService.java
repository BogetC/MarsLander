package database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import models.Score;

public class ScoresService {
    private static final String COLLECTION_NAME = "scores";

    public static CollectionReference getScoresCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Task<QuerySnapshot> getScores(){
        return getScoresCollection().get();
    }

    public static Task<DocumentReference> createScore(Score score) {
        return getScoresCollection().add(score);
    }
}

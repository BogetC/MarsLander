package helloandroid.m2dl.marslander;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import database.ScoresService;
import models.Score;

public class ScoreActivity extends Activity implements View.OnClickListener {
    private final Score score = new Score();
    private List<Score> scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Intent intent = getIntent();
        // int scoreValue = (int) intent.getSerializableExtra("score");
        int scoreValue = 744;
        this.score.setUsername("You");
        this.score.setScore(scoreValue);
        this.scores = new ArrayList<>();
        this.scores.add(this.score);
        TextView scoreLabel = findViewById(R.id.scoreLabel);
        scoreLabel.setText(scoreValue + "s");
        ScoreActivity self = this;
        ScoresService.getScores().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Score scoreI = new Score(document.getData());
                        self.scores.add(scoreI);
                    }
                    self.scores.sort(Comparator.comparing(Score::getScore).reversed());
                    int scoresSize = self.scores.size();
                    if (scoresSize > 0) {
                        TextView hs1 = findViewById(R.id.hs1Label);
                        hs1.setText(self.scores.get(0).getUsername() + ": " + self.scores.get(0).getScore());
                    }
                    if (scoresSize > 1) {
                        TextView hs2 = findViewById(R.id.hs2Label);
                        hs2.setText(self.scores.get(1).getUsername() + ": " + self.scores.get(1).getScore());
                    }
                    if (scoresSize > 2) {
                        TextView hs3 = findViewById(R.id.hs3Label);
                        hs3.setText(self.scores.get(2).getUsername() + ": " + self.scores.get(2).getScore());
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        TextView feedBack = findViewById(R.id.feedbackLabel);
        EditText editText = findViewById(R.id.usernameInput);
        String username = editText.getText().toString();
        if ("".equals(username)) {
            feedBack.setText("Username required !");
        } else {
            this.score.setUsername(username);
            ScoresService.createScore(this.score)
                    .addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            feedBack.setText("Score saved !");
                            feedBack.setTextColor(Color.GREEN);
                            editText.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            feedBack.setText(e.toString());
                            feedBack.setTextColor(Color.RED);
                        }
                    });
        }
    }
}

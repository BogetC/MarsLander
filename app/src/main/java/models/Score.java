package models;

import java.util.Map;

public class Score {
    private String username;
    private long score;

    public Score() { }

    public Score(Map<String, Object> score) {
        this.username = (String) score.get("username");
        this.score = (long) score.get("score");
    }

    public String getUsername() {
        return username;
    }

    public long getScore() {
        return score;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(long score) {
        this.score = score;
    }
}

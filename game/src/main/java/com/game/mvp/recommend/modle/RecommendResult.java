package com.game.mvp.recommend.modle;

import com.game.mvp.model.Game;

import java.util.List;

/**
 * Created by 74234 on 2017/5/18.
 */

public class RecommendResult {
    private List<SubjectList> adList;
    private List<RecBlock> recBlock;
    private  List<Game> recGame;
    private  List<RecBlock> recPoster;
    private List<SuggestWords> suggestWords;

    public List<SubjectList> getAdList() {
        return adList;
    }

    public void setAdList(List<SubjectList> adList) {
        this.adList = adList;
    }

    public List<RecBlock> getRecBlock() {
        return recBlock;
    }

    public void setRecBlock(List<RecBlock> recBlock) {
        this.recBlock = recBlock;
    }

    public List<Game> getRecGame() {
        return recGame;
    }

    public void setRecGame(List<Game> recGame) {
        this.recGame = recGame;
    }

    public List<RecBlock> getRecPoster() {
        return recPoster;
    }

    public void setRecPoster(List<RecBlock> recPoster) {
        this.recPoster = recPoster;
    }


    public TaskInstallGame getTaskInstallGame() {
        return taskInstallGame;
    }

    public void setTaskInstallGame(TaskInstallGame taskInstallGame) {
        this.taskInstallGame = taskInstallGame;
    }
    public List<SuggestWords> getSuggestWords() {
        return suggestWords;
    }

    public void setSuggestWords(List<SuggestWords> suggestWords) {
        this.suggestWords = suggestWords;
    }

    private  TaskInstallGame taskInstallGame;
    public class SuggestWords
    {
        private String word;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }
    }

    public class TaskInstallGame {
        private String  posRelateId;
        private  String position;

        public String getPosRelateId() {
            return posRelateId;
        }

        public void setPosRelateId(String posRelateId) {
            this.posRelateId = posRelateId;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }
    }
    public class RecPoster
    {
        private String id;
        private  String name;
        private  String poster;
        private  int type;
        private  String url;
        private String startFlag;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPoster() {
            return poster;
        }

        public void setPoster(String poster) {
            this.poster = poster;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getStartFlag() {
            return startFlag;
        }

        public void setStartFlag(String startFlag) {
            this.startFlag = startFlag;
        }
    }
}

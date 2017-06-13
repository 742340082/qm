package com.game.mvp.recommend.modle;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.game.adapter.RecommendAdapter;
import com.game.mvp.model.Game;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/5/19.
 */

public class SubjectGame extends DataSupport implements MultiItemEntity{
    private  SubjectList subjectList;
    private Game game;
    private  int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SubjectList getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(SubjectList subjectList) {
        this.subjectList = subjectList;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public int getItemType() {
        if (subjectList!=null)
        {
            int type = subjectList.getType();
            int itemTypeID=0;
            switch (type)
            {
                case 1:
                case 13:
                    itemTypeID= RecommendAdapter.GAME_RECOMMEND_SUBJECT1;
                    break;
                case 15:
                    itemTypeID= RecommendAdapter.GAME_RECOMMEND_SUBJECT2;
                    break;
                case 4:
                case 6:
                case 3:
                case 7:
                    //活动
                    itemTypeID= RecommendAdapter.GAME_RECOMMEND_SUBJECT3;
                    break;
                case 2:
                    itemTypeID= RecommendAdapter.GAME_RECOMMEND_SUBJECT4;
                    break;
            }
            return itemTypeID;
        }else
        {
            return RecommendAdapter.GAME_RECOMMEND_NORMAL;
        }
    }
}

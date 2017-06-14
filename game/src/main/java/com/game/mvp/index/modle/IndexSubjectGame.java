package com.game.mvp.index.modle;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.game.adapter.IndexAdapter;
import com.game.mvp.model.Game;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/5/19.
 */

public class IndexSubjectGame extends DataSupport implements MultiItemEntity{
    private IndexSubjectList subjectList;
    private Game game;
    private  int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public IndexSubjectList getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(IndexSubjectList subjectList) {
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
                    itemTypeID= IndexAdapter.GAME_RECOMMEND_SUBJECT1;
                    break;
                case 15:
                    itemTypeID= IndexAdapter.GAME_RECOMMEND_SUBJECT2;
                    break;
                case 4:
                case 6:
                case 3:
                case 7:
                    //活动
                    itemTypeID= IndexAdapter.GAME_RECOMMEND_SUBJECT3;
                    break;
                case 2:
                    itemTypeID= IndexAdapter.GAME_RECOMMEND_SUBJECT4;
                    break;
            }
            return itemTypeID;
        }else
        {
            return IndexAdapter.GAME_RECOMMEND_NORMAL;
        }
    }
}

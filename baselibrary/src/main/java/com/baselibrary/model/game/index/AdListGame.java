package com.baselibrary.model.game.index;

import com.baselibrary.model.game.Game;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/5/19.
 */

public class AdListGame extends DataSupport implements MultiItemEntity{
    private IndexAdList subjectList;
    private Game game;
    private  int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public IndexAdList getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(IndexAdList subjectList) {
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
                    itemTypeID= 2;
                    break;
                case 15:
                    itemTypeID= 3;
                    break;
                case 4:
                case 6:
                case 3:
                case 7:
                    //活动
                    itemTypeID= 4;
                    break;
                case 2:
                    itemTypeID=5;
                    break;
            }
            return itemTypeID;
        }else
        {
            return 1;
        }
    }
}

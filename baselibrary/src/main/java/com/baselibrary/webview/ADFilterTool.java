package com.baselibrary.webview;

import android.content.Context;
import android.content.res.Resources;

import com.baselibrary.R;

/**
 * Created by 74234 on 2017/5/13.
 */

public class ADFilterTool {
    public static String getClearAdDivJs(Context context){
        String js = "javascript:";
        Resources res = context.getResources();
        String[] adDivs = res.getStringArray(R.array.adBlockDiv);
        for(int i=0;i<adDivs.length;i++){
            //去掉class元素带有广告的标签
//            js +="document.querySelector('."+adDivs[i]+"').style.display=\"none\";";
////            js+="document.querySelector('广告块的选择器').style.display=\"none\""
//            js += "var adDiv"+i+"= document.getElementsByClassName("+adDivs[i]+");" +
//                    "if(adDiv"+i+" != null)adDiv"+i+".parentNode.removeChild(adDiv"+i+");";
            js += "var adDiv"+i+"= $(."+adDivs[i]+");" +
                    "if(adDiv"+i+" != null)adDiv"+i+".parentNode.removeChild(adDiv"+i+");";
        }
        return js;
    }
}

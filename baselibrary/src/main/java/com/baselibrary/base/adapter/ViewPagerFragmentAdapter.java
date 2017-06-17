package com.baselibrary.base.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.baselibrary.utils.Logger;

import java.util.ArrayList;

public class ViewPagerFragmentAdapter
        extends FragmentPagerAdapter {
    private ArrayList<FragmentInfo> mFragmentInfoS = new ArrayList();
    private ArrayList<String> mTitleS = new ArrayList();

    public ViewPagerFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }
//    @Override
//    public Parcelable saveState() {
//        return null;
//    }
    @Override
    public void finishUpdate(ViewGroup container) {
        try{
            super.finishUpdate(container);
        } catch (NullPointerException nullPointerException){
            System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
        }
    }


    public void addFragement(Fragment fragment) {
        this.mFragmentInfoS.add(new FragmentInfo(fragment));
    }
    public void addFragement(Fragment fragment,Bundle bundle) {
        this.mFragmentInfoS.add(new FragmentInfo(fragment,bundle));
    }

    public void addTabPage(String data, Fragment fragment, Bundle bundle) {
        this.mTitleS.add(data);
        this.mFragmentInfoS.add(new FragmentInfo(fragment, bundle));
    }

    public void addTabPage(String data, Fragment fragment) {
        this.mTitleS.add(data);
        this.mFragmentInfoS.add(new FragmentInfo(fragment, null));
    }
    @Override
    public int getCount() {
        return this.mFragmentInfoS.size();
    }
    @Override
    public Fragment getItem(int postion) {
        Fragment fragment = mFragmentInfoS.get(postion).getFragment();
        if(mFragmentInfoS.get(postion).getBuild()!=null) {
            fragment.setArguments(mFragmentInfoS.get(postion).getBuild());
        }
        Logger.e("TAG",fragment.toString());
        return fragment;
    }
@Override
    public CharSequence getPageTitle(int position) {
        return (CharSequence) this.mTitleS.get(position);
    }


    class FragmentInfo {
        private Bundle build;
        private Fragment fragment;

        public FragmentInfo(Fragment fragment) {
            this.fragment = fragment;
        }

        public FragmentInfo(Fragment fragment, Bundle bundle) {
            this.build = bundle;
            this.fragment = fragment;
        }

        public Bundle getBuild() {
            return this.build;
        }

        public Fragment getFragment() {
            return this.fragment;
        }

        public void setBuild(Bundle paramBundle) {
            this.build = paramBundle;
        }

        public void setFragment(Fragment paramFragment) {
            this.fragment = paramFragment;
        }
    }

}

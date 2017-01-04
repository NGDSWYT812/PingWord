package com.ping.pingword.base;

import android.util.SparseArray;
import android.view.View;

public class ViewHolder {
	private ViewHolder(){};

	public static <T extends View> T get(View view,int id){
		SparseArray<View> sa = (SparseArray<View>) view.getTag();
		if(sa==null){
			sa = new SparseArray<View>();
			view.setTag(sa);
		}
		View v = sa.get(id);
		if(v==null){
			v = view.findViewById(id);
			sa.put(id, v);
		}

		return (T)v;
	}
}

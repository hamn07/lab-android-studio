package com.example.explistest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
	private ExpandableListView elist;
	private LinkedList<HashMap<String, String>> gList;
	private String[] gFrom = { "gtitle" };
	private int[] gTo = { R.id.gtitle };
	private String[] cFrom = { "ctitle", "cdesc", "cimg" };
	private int[] cTo = { R.id.ctitle, R.id.cdesc, R.id.cimg };

	private LinkedList<LinkedList<HashMap<String, Object>>> cList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		elist = (ExpandableListView) findViewById(R.id.elist);

		gList = new LinkedList<HashMap<String, String>>();
		cList = new LinkedList<LinkedList<HashMap<String, Object>>>();

		HashMap<String, String> gitem00 = new HashMap<String, String>();
		gitem00.put(gFrom[0], "中餐類");
		gList.add(gitem00);

		LinkedList<HashMap<String, Object>> gcList0 = new LinkedList<HashMap<String, Object>>();

		HashMap<String, Object> citem00 = new HashMap<String, Object>();
		citem00.put(cFrom[0], "京醬肉絲");
		citem00.put(cFrom[1], "京醬肉絲---1111");
		citem00.put(cFrom[2], R.drawable.ic_launcher);
		gcList0.add(citem00);

		HashMap<String, Object> citem01 = new HashMap<String, Object>();
		citem01.put(cFrom[0], "宮保雞丁");
		citem01.put(cFrom[1], "宮保雞丁---2222");
		citem01.put(cFrom[2], R.drawable.ic_launcher);
		gcList0.add(citem01);

		cList.add(gcList0);
		// -------------------------------------------------------------
		 HashMap<String, String> gitem01 = new HashMap<String, String>();
		 gitem01.put(gFrom[0], "異國料理");
		 gList.add(gitem01);
		
		 LinkedList<HashMap<String,Object>> gcList1 =
		 new LinkedList<HashMap<String,Object>>();
		
		 HashMap<String, Object> citem10 = new HashMap<String, Object>();
		 citem10.put(cFrom[0], "披薩");
		 citem10.put(cFrom[1], "披薩---1111");
		 citem10.put(cFrom[2], R.drawable.ic_launcher);
		 gcList1.add(citem10);
		
		 HashMap<String, Object> citem11 = new HashMap<String, Object>();
		 citem11.put(cFrom[0], "漢堡");
		 citem11.put(cFrom[1], "漢堡---2222");
		 citem11.put(cFrom[2], R.drawable.ic_launcher);
		 gcList1.add(citem11);
		
		 HashMap<String, Object> citem12 = new HashMap<String, Object>();
		 citem12.put(cFrom[0], "生魚片");
		 citem12.put(cFrom[1], "生魚片---2222");
		 citem12.put(cFrom[2], R.drawable.ic_launcher);
		 gcList1.add(citem12);
		
		 cList.add(gcList1);
		// -------------------------------------------------------------

		 HashMap<String, String> gitem02 = new HashMap<String, String>();
		 gitem02.put(gFrom[0], "路邊攤類");
		 gList.add(gitem02);
		
		 LinkedList<HashMap<String,Object>> gcList2 =
		 new LinkedList<HashMap<String,Object>>();
		
		 HashMap<String, Object> citem20 = new HashMap<String, Object>();
		 citem20.put(cFrom[0], "蚵仔煎");
		 citem20.put(cFrom[1], "蚵仔煎---1111");
		 citem20.put(cFrom[2], R.drawable.ic_launcher);
		 gcList2.add(citem20);
		
		 HashMap<String, Object> citem21 = new HashMap<String, Object>();
		 citem21.put(cFrom[0], "臭豆腐");
		 citem21.put(cFrom[1], "臭豆腐---2222");
		 citem21.put(cFrom[2], R.drawable.ic_launcher);
		 gcList2.add(citem21);
		
		 cList.add(gcList2);

		MyExpandableListAdapter adapter = new MyExpandableListAdapter(this, gList, R.layout.gitem, gFrom, gTo,
				cList, R.layout.citem, cFrom, cTo);

		elist.setAdapter(adapter);

	}

	/** 
     * A simple adapter which allows you to bind data to specific 
     * Views defined within the layout of an Expandable Lists children 
     * (Implement getGroupView() to define the layout of parents) 
     */ 
    public class MyExpandableListAdapter extends SimpleExpandableListAdapter { 
            private List<? extends List<? extends Map<String, ?>>> mChildData; 
            private String[] mChildFrom; 
            private int[] mChildTo; 

            public MyExpandableListAdapter(Context context, 
                            List<? extends Map<String, ?>> groupData, int groupLayout, 
                            String[] groupFrom, int[] groupTo, 
                            List<? extends List<? extends Map<String, ?>>> childData, 
                            int childLayout, String[] childFrom, int[] childTo) { 
                    super(context, groupData, groupLayout, groupFrom, groupTo, 
                                    childData, childLayout, childFrom, childTo); 
                    mChildData = childData; 
                    mChildFrom = childFrom; 
                    mChildTo = childTo; 
            } 

            public View getChildView(int groupPosition, int childPosition, 
                            boolean isLastChild, View convertView, ViewGroup parent) { 
                    View v; 
                    if (convertView == null) { 
                            v = newChildView(isLastChild, parent); 
                    } else { 
                            v = convertView; 
                    } 
                    
                    bindView(v, mChildData.get(groupPosition).get(childPosition), 
mChildFrom, 
                                    mChildTo, groupPosition, childPosition); 
                    return v; 
            } 

            // This method binds my data to the Views specified in the child xml layout 
            private void bindView(View view, Map<String, ?> data, 
                            String[] from, int[] to, int groupPosition, int childPosition) { 
                    int len = to.length - 1; 
                    // Apply TextViews 
                    for (int i = 0; i < len; i++) { 
                            TextView v = (TextView) view.findViewById(to[i]); 
                            if (v != null) { 
                                    v.setText((String) data.get(from[i])); 
                            } 

                    } 
                    // Apply ImageView 
                    ImageView imgV = (ImageView) view.findViewById(to[2]); 
                    if (imgV != null) { 
                    	imgV.setImageResource(R.drawable.ic_launcher);;
//                            if(childPosition % 2 == 0) imgV.setImageDrawable(albumCovers.get 
//(0)); 
//                            else imgV.setImageDrawable(albumCovers.get(1)); 
                    } 
            } 
    }

}

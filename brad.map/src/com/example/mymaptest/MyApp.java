package com.example.mymaptest;

import android.app.Application;

public class MyApp extends Application{
	private MapHandler mapHandler;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mapHandler = new MapHandler();
		
		
	}
	MapHandler getMapHandler(){ return mapHandler;}

}

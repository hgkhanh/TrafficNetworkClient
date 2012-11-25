package org.k2htm.tnc;

import android.app.Application;
import android.content.res.Configuration;

public class TrafficNetworkClient extends Application {
	private int timeFilter;
	private String user;
	public static final int DEFAULT_TIME_FILTER = 60;
	public static final String DEFAULT_USER = "Guest";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		timeFilter = DEFAULT_TIME_FILTER;
		user = DEFAULT_USER;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getTimeFilter() {
		return timeFilter;
	}

	public void setTimeFilter(int time_filter) {
		this.timeFilter = time_filter;
	}

}

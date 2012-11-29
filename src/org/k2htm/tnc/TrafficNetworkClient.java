package org.k2htm.tnc;

import java.util.ArrayList;

import android.app.Application;
import android.util.Log;
import edu.k2htm.datahelper.Report;

public class TrafficNetworkClient extends Application {
	private int timeFilter;
	private String user;
	private ArrayList<Report> reportList;
	public static final String TAG = "Application Object";

	public ArrayList<Report> getReportList() {
		return reportList;
	}

	public void setReportList(ArrayList<Report> reportList) {
		this.reportList = reportList;
	}

	public static final int DEFAULT_TIME_FILTER = -1;
	public static final String DEFAULT_USER = "Guest";
	public static final String ADDRESS = "192.168.0.109:8080";

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

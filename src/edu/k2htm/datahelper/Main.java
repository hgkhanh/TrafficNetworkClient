package edu.k2htm.datahelper;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated constructor stub
		
		//test report 
		ArrayList<Report> reports = new ArrayList<Report>();
		Report report1= new Report("khanh",(long)12345,2106535,105325564,"",(short)1,"");
		Report report2= new Report("khiem",(long)9995,74545,77777,"tac roi",(short)2,"pic2.jpg");
		reports.add(report1);
		reports.add(report2);
		ReportGetter repGeter = new ReportGetter(reports);
		String outputXML="";
		try {
			outputXML = repGeter.getReportAsXML(10);
			System.out.println("gen");
			System.out.println(outputXML);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("read");
		repGeter = new ReportGetter(outputXML);
		System.out.println(repGeter.toString());
		
		
		//test comment
		ArrayList<Comment> comments = new ArrayList<Comment>();
		Comment comment1= new Comment("khanh",3,"coment vao day",(long)12343124);
		Comment comment2= new Comment("khiem",2,"chem gio, co tai nan dau",(long)9999);
		comments.add(comment1);
		comments.add(comment2);
		CommentGetter commentGetter = new CommentGetter(comments);
		outputXML="";
		try {
			outputXML = commentGetter.getCommentsAsXML(1);
			System.out.println("gen");
			System.out.println(outputXML);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("read");
		commentGetter = new CommentGetter(outputXML);
		System.out.println(commentGetter.toString());
	}
}

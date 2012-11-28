package org.k2htm.tnc;

import java.util.ArrayList;

import edu.k2htm.datahelper.Comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentItemAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ViewHolder holder;
	private ArrayList<Comment> comments;
	
	
	
	public CommentItemAdapter(Context context, ArrayList<Comment> comments) {
		super();
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.comments = comments;
		holder = new ViewHolder();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.comment_item_listview, null);
			convertView.setTag(holder);
		} else {
			convertView.getTag();
		}
		holder.tvUserComment = (TextView) convertView.findViewById(R.id.tvUserComment);
		holder.tvUserComment.setText(comments.get(position).getCommenter());
		holder.tvDateComment = (TextView) convertView.findViewById(R.id.tvTimeComment);
		holder.tvDateComment.setText(comments.get(position).getTime()+"");
		holder.tvContentComment = (TextView) convertView.findViewById(R.id.tvContentComment);
		holder.tvContentComment.setText(comments.get(position).getComment());
		
		return convertView;
	}

	private class ViewHolder{
		TextView tvUserComment;
		TextView tvDateComment;
		TextView tvContentComment;
	}
}

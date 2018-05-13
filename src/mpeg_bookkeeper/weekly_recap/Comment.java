package mpeg_bookkeeper.weekly_recap;

public class Comment {
	private String comment;
	private String date;
	private String initials;

	public Comment(String initials, String date, String comment) {
		this.comment = comment;
		this.date = date;
		this.initials = initials;
	}

	public String getComment() {
		return "<" + this.initials + " " + this.date + "> " + this.comment;
	}
}
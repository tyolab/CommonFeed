package au.com.tyo.common.feed;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class Channel implements Iterable<Item> {
	
	private String title;
	
	private String description;
	
	private String link;
	
	private Calendar  lastBuildDate;
	
	private String lastBuildDateStr;
	
	private Calendar  pubDate;
	
	private long ttl;
	
	private ArrayList<Item> items;
	
	public Channel() {
		items = new ArrayList<Item>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Calendar getLastBuildDate() {
		return lastBuildDate;
	}

	public void setLastBuildDate(Calendar lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}

	public String getLastBuildDateStr() {
		return lastBuildDateStr;
	}

	public void setLastBuildDateStr(String lastBuildDateStr) {
		this.lastBuildDateStr = lastBuildDateStr;
	}

	public Calendar getPubDate() {
		return pubDate;
	}

	public void setPubDate(Calendar pubDate) {
		this.pubDate = pubDate;
	}

	public long getTtl() {
		return ttl;
	}

	public void setTtl(long ttl) {
		this.ttl = ttl;
	}

	public void addItem(Item item) {
		items.add(item);
	}

	public int getItemCount() {
		return items.size();
	}

	@Override
	public Iterator<Item> iterator() {
		return items.iterator();
	}
}

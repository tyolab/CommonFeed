package au.com.tyo.common.feed;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import au.com.tyo.parser.Sgml;
import au.com.tyo.parser.SgmlNode;
import au.com.tyo.parser.XML;
import au.com.tyo.services.HttpPool;
import au.com.tyo.utils.SimpleDateUtils;
import au.com.tyo.utils.StringUtils;


/*
 list of element for the RSS feed ( "*" indicates that an element must be provided except for Atom elements "author" and "link" which are only required under certain conditions.):
 
author
category
channel
copyright
description
description*
generator
guid
image
item
lastBuildDate (in channel)
link*
managingEditor
pubDate
title*
ttl

 */
public class RSS extends Feed implements Iterable<Channel> {
	
	public static String TAG_AUTHOR = "author";
	public static String TAG_CATEGORY = "category";
	public static String TAG_CHANNEL = "channel";
	public static String TAG_COPYRIGHT = "copyright";
	public static String TAG_DESCRIPTION = "description";
	public static String TAG_ITEM_DESCRIPTION = "description";
	public static String TAG_GENERATOR = "generator";
	public static String TAG_GUID = "guid";
	public static String TAG_IMAGE = "image";
	public static String TAG_ITEM = "item";
	public static String TAG_LASTBUILDDATE = "lastBuildDate";
	public static String TAG_LINK = "link";
	public static String TAG_MANAGINGEDITOR = "managingEditor";
	public static String TAG_PUBDATE = "pubDate";
	public static String TAG_TITLE = "title";
	public static String TAG_TTL = "ttl";

	
	private ArrayList<Channel> channels;
	
	private boolean needsOnlyLatest;
	
	public RSS() {
		needsOnlyLatest = false;
	}
	
//	public Channel getChannel() {
//		return channel;
//	}
//
//	public void setChannel(Channel channel) {
//		this.channel = channel;
//	}

	public boolean isNeedsOnlyLatest() {
		return needsOnlyLatest;
	}

	public void setNeedsOnlyLatest(boolean needsOnlyLatest) {
		this.needsOnlyLatest = needsOnlyLatest;
	}

	public static RSS newFeed(String html) {
		RSS rss = new RSS();
		rss.parse(html);
		return rss;
	}
	
	public static RSS newFeedFromUrl(String url) {

		String html = null;
		try {
			html = HttpPool.getInstance().getConnection().get(url);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return newFeed(html);
	}

	private void parse(String html) {
		Sgml parser = new Sgml();
		byte[] htmlBytes = html.getBytes();
//		count = 0;
		
		SgmlNode rootNode = parser.parse(htmlBytes, 0);
		
		if (rootNode != null) {
			SgmlNode childNode = null;
			
			for (int i = 0; i < rootNode.countChildren(); ++i) 
				childNode = rootNode.getChild(i);
				if (childNode != null && childNode.getName().equalsIgnoreCase(TAG_CHANNEL))
					for (int k = 0; k < childNode.countChildren(); ++k) {
					
						Channel channel = new Channel();
						
						SgmlNode targetNode = childNode.getChild(k);
						if (targetNode != null && targetNode.getName().equalsIgnoreCase(TAG_ITEM)) {
		//						++count;	
							Item item = new Item();
							
							for (int j = 0; j < targetNode.countChildren(); ++j) {
								SgmlNode infoNode = targetNode.getChild(j);
								
								if (infoNode.getName().equalsIgnoreCase("title")) {
									String title = StringUtils.unescapeHtml(infoNode.getText()).trim();
									item.setTitle(title);
	//								item.setKeyWords(DoodleRegex.removePatterns(title));
	//								item.getPage().setTitle(item.getKeyWords());
								}
								else if (infoNode.getName().equalsIgnoreCase("link")) {
									String url = infoNode.getText();
									item.setLink(url);
	//								item.setUniqueId(url.hashCode());
	//								if (cache != null) {
	//									Item archivedDoodle = cache.loadCache(url);
	//									if (archivedDoodle != null) {
	//										item = archivedDoodle;
	//										break;
	//									}
	//								}
								}
								else if (infoNode.getName().equalsIgnoreCase("description")) {				
									String description;
									if (infoNode.countChildren() > 0) {
										int start = 0;
										int end = 0;
										start = infoNode.getChild(0).getStart();
										end = infoNode.getChild(infoNode.countChildren() - 1).getEnd();
										int length = end - start;
										byte[] tmp = new byte[length];
										System.arraycopy(htmlBytes, start, tmp, 0, length);
										description = new String(tmp); // can't use substring, because the offset is based on bytes./
									}
									else {
										description = infoNode.getText();								
									}
									item.setDescription(XML.unXMLify(description));
	//								item.processDescription();
									
									if (needsOnlyLatest) {
										Calendar yesterday = SimpleDateUtils.getGmtCalendar000();
										yesterday.setTimeInMillis(yesterday.getTimeInMillis() - SimpleDateUtils.ONE_DAY_IN_MILLIS);
										if (item.getPubDate().before(yesterday))
											break;
									}
								}
							}
							
							channel.addItem(item);
		//						item.setIndex(channel.getItemCount() - 1);
						}
					} // childNote
		}
	}

	@Override
	public Iterator<Channel> iterator() {
		return channels == null ? null : channels.iterator();
	}
}

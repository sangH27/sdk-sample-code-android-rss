package com.inmobi.banner.sample;

/**
 * Created by rahul.raja on 1/23/17.
 */

public class Constants {

//    public static final String FEED_URL = "https://api.rss2json.com/v1/api.json?rss_url=http://rss.donga.com/total.xml"
    public static final String FEED_URL = "https://api.rss2json.com/v1/api.json?rss_url=http://feed43.com/1801458831546005.xml";
                                           //https://api.rss2json.com/v1/api.json?rss_url=http://sanga5a5a5a5.tistory.com/rss
    //https://api.rss2json.com/v1/api.json?rss_url=http://feed43.com/8755466222260574.xml
    public static final String FALLBACK_IMAGE_URL = "https://s3-ap-southeast-1.amazonaws.com/inmobi-surpriseme/notification/notif2.jpg";

    public interface FeedJsonKeys {
        String FEED_LIST = "items";
        String CONTENT_TITLE = "title";
//        String CONTENT_ENCLOSURE = "title"; //enclosure -> title
        String CONTENT_LINK = "link";
        String FEED_CONTENT = "content";

    }
}

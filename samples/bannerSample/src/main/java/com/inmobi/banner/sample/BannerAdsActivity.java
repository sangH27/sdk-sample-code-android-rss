package com.inmobi.banner.sample;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiBanner;
import com.inmobi.ads.InMobiInterstitial;
import com.inmobi.sdk.InMobiSdk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.inmobi.banner.sample.Constants.FALLBACK_IMAGE_URL;


public class BannerAdsActivity extends AppCompatActivity {

    private static final String TAG = BannerAdsActivity.class.getSimpleName();
    //07-31 14:16:05.091 19140-19140/com.inmobi.banner.sample D/BannerAdsActivity:

    private ListView mNewsListView;
    private WebView mWeb;
    private InMobiInterstitial mInterstitialAd;
    private int position_state;
    private InMobiBanner mBannerAd;
    private String TAG1 = "InMobiBanner";
    private String TAG2 = "InMobiInterstitial";


    @NonNull
    private final Handler mHandler = new Handler(); //하위 스레드
    private List<NewsSnippet> mItemList = new ArrayList<>();
    private NewsFeedAdapter mAdapter;


    public interface OnHeadlineSelectedListener {
        void onArticleSelected(int position);
    }

    private OnHeadlineSelectedListener mCallback = new OnHeadlineSelectedListener() {
        @Override
        public void onArticleSelected(int position) {
            //일반 클릭 이벤트 처리 구현
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

        setContentView(R.layout.activity_banner_ads);

        setupListView();
        getHeadlines();

        InMobiSdk.init(this, "1234567890qwerty0987654321qwerty12345");
        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);

        Log.d(TAG,"is TAG");
        setupBannerAd();
        setupInterstitial();
    }


    private void setupListView() {
        mNewsListView = (ListView) findViewById(R.id.lvNewsContainer);
        //    <ListView
        // android:id="@+id/lvNewsContainer"
        // activity_banner_ads.xml

        mAdapter = new NewsFeedAdapter(this, mItemList);
        // private List<NewsSnippet> mItemList = new ArrayList<>(); // mItemList is dataset

        mNewsListView.setAdapter(mAdapter);
        mNewsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        /*long 클릭 이벤트 처리*/
        mNewsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position, final long id) {
                AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(BannerAdsActivity.this);
                confirmationDialog.setTitle("Delete Item?");
                confirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewsSnippet newsSnippet = mItemList.get(position);
                        mItemList.remove(newsSnippet);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                confirmationDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                confirmationDialog.show();
                return true;
            }
        });

        /*일반적인 클릭 이벤트 처리*/
        mNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                position_state = position;
                mInterstitialAd.show();
                mAdapter.notifyDataSetChanged(); //업댑터는 여기서 변경된 데이터를 읽고 뷰를 생성하여 ListView로 전달
            }
        });
    }

    private void getHeadlines() { //public class DataFetcher-interface OnFetchCompletedListener-String readStream(InputStream in)
                                  //public void getFeed(final String urlString, final OnFetchCompletedListener onFetchCompletedListener)
        new DataFetcher().getFeed(Constants.FEED_URL, new DataFetcher.OnFetchCompletedListener() { //http 연결하고 스트림으로 읽어오고 결과는 String data로 반환하고
            @Override
            public void onFetchCompleted(@Nullable final String data, @Nullable final String message) {
                if (null != data) {
                    mHandler.post(new Runnable() { //만약에 데이터가 있다면 하위 스레드에서 run() 실행. run() -> loadHeadlines(data);
                        @Override
                        public void run() {
                            loadHeadlines(data); //만약에 데이터가 있다면 하위 스레드에서 run() 실행. run() -> loadHeadlines(data);
                        }
                    });
                }
            }
        });
    }


    private void loadHeadlines(String data) {
        try {
            JSONArray feed = new JSONObject(data).
                    getJSONArray(Constants.FeedJsonKeys.FEED_LIST);//FEED_LIST=items; "item"별로 읽어와서 JSONArray에 저장. 반환되는 값 JSON!.
            for (int i = 0; i < feed.length(); i++) {              //JSONArray.length() gets length of JSONArray object
                JSONObject item = feed.getJSONObject(i);
                Log.v("여기가 JSON Object item 출력", item.toString());
                NewsSnippet feedEntry = new NewsSnippet();

                String temp_imgsrc=item.getString(Constants.FeedJsonKeys.CONTENT_TITLE);
                Log.v("여기가 imgsrc",temp_imgsrc);


                    feedEntry.imageUrl = (String) temp_imgsrc;


                    if (temp_imgsrc==null) {
                        feedEntry.imageUrl = FALLBACK_IMAGE_URL;
                        Log.v("여기가 enclosure/imageUrl", feedEntry.imageUrl);
                    }



//                        feedEntry.imageUrl = item.getJSONObject(Constants.FeedJsonKeys.CONTENT_ENCLOSURE).
//                                getString(Constants.FeedJsonKeys.CONTENT_LINK);
//                        Log.v("여기가enclosure/imageUrl", feedEntry.imageUrl);


                    /*CONTENT_ENCLOSURE = "enclosure"; 티스토리 블로그 RSS에서 enclosure 객체를 반환하지 않음*/
                    /*
                    JSONObject enclosureObject = item.getJSONObject(Constants.FeedJsonKeys.CONTENT_ENCLOSURE);


                    if (!enclosureObject.isNull(Constants.FeedJsonKeys.CONTENT_LINK)) {
                        feedEntry.imageUrl = item.getJSONObject(Constants.FeedJsonKeys.CONTENT_ENCLOSURE).
                                getString(Constants.FeedJsonKeys.CONTENT_LINK);
                        Log.v("여기가enclosure/imageUrl", feedEntry.imageUrl);
                    } else {
                        feedEntry.imageUrl = FALLBACK_IMAGE_URL;
                        Log.v("여기가enclosure/imageUrl", feedEntry.imageUrl);
                    }
                    */

                    feedEntry.landingUrl = item.getString(Constants.FeedJsonKeys.CONTENT_LINK);
                    Log.d("여기가 LandingUrl", feedEntry.landingUrl);
                    String temp = item.getString(Constants.FeedJsonKeys.FEED_CONTENT);
                    Log.d("FEED_CONTENT치환전", temp);
                    feedEntry.content = temp.replace("<p><sub><i>-- Delivered by <a href=\"http://feed43.com/\">Feed43</a> service</i></sub></p>", "");
                    Log.d("여기가content", feedEntry.content);

                    /*feed43에서 최종적으로 적용한 Item search pattern*/
/*
                    <div class="list_content">{*}
                    <a href="{%}"{*}class="{*}">{*}
                    <strong class="tit_post">{%}</strong>{*}
                    <p class="txt_post">{%}</p>{*}
                    </a>{*}
                    </div>
*/

                    feedEntry.isSponsored = false;
                    mItemList.add(feedEntry);//보여주는 뷰리스트 객체의 원본 데이터를 수정하면 viewList에서도 반영 -> 완료
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.d(TAG, "JSONException for loadHeadlines", e);
        }
    }


    public void setupInterstitial() {
        Long placementId = 1475973082314L;
        mInterstitialAd = new InMobiInterstitial(this, placementId,
                new InMobiInterstitial.InterstitialAdListener2() {
                    @Override
                    public void onAdLoadFailed(InMobiInterstitial inMobiInterstitial, InMobiAdRequestStatus inMobiAdRequestStatus) {
                        Log.d(TAG2, "Unable to load interstitial ad (error message: " +
                                inMobiAdRequestStatus.getMessage());
                        mInterstitialAd.load();

                    }

                    @Override
                    public void onAdReceived(InMobiInterstitial inMobiInterstitial) {
                        Log.d(TAG2, "onAdReceived");
                    }

                    @Override
                    public void onAdLoadSucceeded(InMobiInterstitial inMobiInterstitial) {
                        Log.d(TAG2, "onAdLoadSuccessful");
                    }

                    @Override
                    public void onAdRewardActionCompleted(InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {
                        Log.d(TAG2, "onAdRewardActionCompleted ");
                        webview_call();
                    }

                    @Override
                    public void onAdDisplayFailed(InMobiInterstitial inMobiInterstitial) {
                        Log.d(TAG2, "onAdDisplayFailed " + "FAILED");
                        webview_call();
                    }

                    @Override
                    public void onAdWillDisplay(InMobiInterstitial inMobiInterstitial) {
                        Log.d(TAG2, "onAdWillDisplay ");
                    }

                    @Override
                    public void onAdDisplayed(InMobiInterstitial inMobiInterstitial) {
                        Log.d(TAG2, "onAdDisplayed ");
                    }

                    @Override
                    public void onAdInteraction(InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {
                        Log.d(TAG2, "onAdInteraction ");
                    }

                    @Override
                    public void onAdDismissed(InMobiInterstitial inMobiInterstitial) {
                        Log.d(TAG2, "onAdDismissed ");
//                        webview_call();
                    }

                    @Override
                    public void onUserLeftApplication(InMobiInterstitial inMobiInterstitial) {
                        Log.d(TAG2, "onUserWillLeaveApplication ");
                    }
                });
        mInterstitialAd.load();
    }


    public void webview_call() {
        mWeb = (WebView) findViewById(R.id.webView);
        NewsSnippet newsSnippet = mItemList.get(position_state);
        String temp_url = newsSnippet.landingUrl;
        mWeb.loadUrl(temp_url);
        mInterstitialAd.load();
    }


    private void setupBannerAd() {
        mBannerAd = new InMobiBanner(BannerAdsActivity.this, PlacementId.YOUR_PLACEMENT_ID);
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.ad_container);
        mBannerAd.setAnimationType(InMobiBanner.AnimationType.ROTATE_HORIZONTAL_AXIS);
        mBannerAd.setListener(new InMobiBanner.BannerAdListener() {
            @Override
            public void onAdLoadSucceeded(InMobiBanner inMobiBanner) {
                Log.d(TAG1, "onAdLoadSucceeded");
            }

            @Override
            public void onAdLoadFailed(InMobiBanner inMobiBanner,
                                       InMobiAdRequestStatus inMobiAdRequestStatus) {
                Log.d(TAG1, "Banner ad failed to load with error: " +
                        inMobiAdRequestStatus.getMessage());
            }

            @Override
            public void onAdDisplayed(InMobiBanner inMobiBanner) {
                Log.d(TAG1, "onAdDisplayed");
            }

            @Override
            public void onAdDismissed(InMobiBanner inMobiBanner) {
                Log.d(TAG1, "onAdDismissed");
            }

            @Override
            public void onAdInteraction(InMobiBanner inMobiBanner, Map<Object, Object> map) {
                Log.d(TAG1, "onAdInteraction");
            }

            @Override
            public void onUserLeftApplication(InMobiBanner inMobiBanner) {
                Log.d(TAG1, "onUserLeftApplication");
            }

            @Override
            public void onAdRewardActionCompleted(InMobiBanner inMobiBanner, Map<Object, Object> map) {
                Log.d(TAG1, "onAdRewardActionCompleted");
            }
        });
        setBannerLayoutParams();
        adContainer.addView(mBannerAd);
        mBannerAd.load();
    }


    private void setBannerLayoutParams() {
        int width = toPixelUnits(320);
        int height = toPixelUnits(50);
        RelativeLayout.LayoutParams bannerLayoutParams = new RelativeLayout.LayoutParams(width, height);
        bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bannerLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mBannerAd.setLayoutParams(bannerLayoutParams);
    }


    private int toPixelUnits(int dipUnit) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dipUnit * density);
    }

}
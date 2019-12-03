# sdk-sample-code-android-rss
   <head>
      <meta content="text/html; charset=UTF-8" http-equiv="content-type">
   </head>
      <p class="c3"><span class="c6">Article collection sample app using RSS feeds</span></p>
      <p class="c4"><span class="c2">1. Introduction</span></p>
      <p class="c4"><span class="c2">First, articles in a blog (which you can think of a backend DB) are converted to RSS Feed and the Feed is transferred to the mobile application in xml format. When mobile application received the Feed from a blog, it lists and sorts the articles by parsing each elements such as title, image file to update/show the list of articles when ListView object is renewed(called). In this sample application, we integrated an advertising SDK and set up different event listeners to show different types of ads under each event/situation. For instance, when webview is called SDK delivers a interstitial ad, when the application opens SDK delivers a regular small banner in the article component. Advertising SDK is InMobi Android 6.2.0 and blog takes place of a server which contains articles.
</span></p>
      <p class="c4"><span class="c2">2. Data structure and flow</span></p>
      <p class="c4"><span class="c2">Data and data flow used in three sections: Blog, RSS Feed, and Application</span></p>
      <p class="c4"><span class="c2">2-1. Data structure</span></p>
      <p class="c4"><span class="c2">1) 1) Defining a data prototype NewsSnippet 2) Declaring a private list <NewsSnippet> as a data prototype as a data type 3) Declaring a ListVie to show a list of articles 4) List <NewsSnippet> and ListView connected to a custom adapter 5) getView () function ( Whenever an item of ListView is updated on the user screen, a function called) is set so that the data of List <NewsSnippet> can be updated in the ListView through the corresponding adapter.</span></p>
      <p class="c4"><span class="c2">2-2. Data Flow </span></p>
      <p class="c4"><span class="c2">1) Write an article in the blog 2) Create a blog article as an RSS Feed 3) Issue an RSS Feed URL and save the URL in the application Parsing according to Json type 6) It saves in prototype data (NewsSnippet).</span></p>
      <p class="c4 c7"><span class="c2"></span></p>
      <p></p>
      <p class="c3" align="center"><span style="overflow: hidden; display: inline-block; margin: 0.00px 0.00px; border: 0.00px solid #000000; transform: rotate(0.00rad) translateZ(0px); -webkit-transform: rotate(0.00rad) translateZ(0px); width: 601.33px; height: 241.47px;"><img alt="" src="images/image1.png" width= 901.995px height= 362.205px></span></p>
      <p class="c0" align="center"><span class="c2">Fig1. Data flow seen from class level</span></p>
      <p></p>
      <p class="c3" align="center"><span style="overflow: hidden; display: inline-block; margin: 0.00px 0.00px; border: 0.00px solid #000000; transform: rotate(0.00rad) translateZ(0px); -webkit-transform: rotate(0.00rad) translateZ(0px); width: 601.33px; height: 97.27px;"><img alt="" src="images/image3.png" width= 901.995px height=145.905px></span></p>
      <p></p>
      <p class="c0" align="center"><span class="c2">Fig2. RSS Feed - Json object Fetch/Store</span></p>
      <p></p>
      <p class="c1"><span class="c2">2-3. RSS Feed generation</span></p>
      <p class="c1"><span class="c2">Create a feed in XML format using Feed43. At http://feed43.com, you can output a specific string via a pattern (Extraction Rules: {*} is all parts, {%} is the string / part you want to print). The following is the source entered in Feed43.</span></p>
      <p class="c0"><span style="overflow: hidden; display: inline-block; margin: 0.00px 0.00px; border: 0.00px solid #000000; transform: rotate(0.00rad) translateZ(0px); -webkit-transform: rotate(0.00rad) translateZ(0px); width: 616.73px; height: 411.47px;"><img alt="" src="images/image2.png" width= 616.73px height= 411.47px></span><span class="c2">Fig3. Feed43 format</span></p>
      <p class="c1"><span class="c2">2-4. Data Fetch : RSS Feed to Application</span></p>
      <p class="c1"><span class="c2">Step A. HttpURLConnection with URL<br>Step C. inputstream = BufferedInputStream with B &nbsp;<br>Step D. readStream with inputstream which step C returns <br>Step E. Returns data which is prototype of RSS feed</span></p>
      <p class="c1"><span class="c2">The fetched data is encoded in UTF-8 and the result is shown below.</span></p>

   <head>
      <meta content="text/html; charset=UTF-8" http-equiv="content-type">
   </head>
      <p class="c1 c7"><span class="c2"></span></p>
      <p class="c1 c7"><span class="c2"></span></p>
      <p class="c1"><span class="c2">4. Results</span></p>
      <p class="c1" align="center"><span style="overflow: hidden; display: inline-block; margin: 0.00px 0.00px; border: 0.00px solid #000000; transform: rotate(0.00rad) translateZ(0px); -webkit-transform: rotate(0.00rad) translateZ(0px); width: 284.48px; height: 463.84px;"><img alt="" src="images/image5.png" width= 284.48px height= 463.84px></span><span>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span><span style="overflow: hidden; display: inline-block; margin: 0.00px 0.00px; border: 0.00px solid #000000; transform: rotate(0.00rad) translateZ(0px); -webkit-transform: rotate(0.00rad) translateZ(0px); width: 268.38px; height: 477.01px;"><img alt="" src="images/image4.jpg" width= 268.38px height= 477.01px></span></p>
      <p class="c1 c8" align="center"><span class="c2">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Fig4. ListVeiw &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Fig5. Banner</span></p>

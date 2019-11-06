package com.ahnsafety.ex59rssfeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter adapter;

    ArrayList<Item> items= new ArrayList<>();

    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView= findViewById(R.id.recycler);
        adapter= new MyAdapter(items, this);
        recyclerView.setAdapter(adapter);

        //리사이클러의 배치관리자 설정
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //대량의 데이터 추가작업
        readRss();

        //스와이프 갱신 하기
        refreshLayout= findViewById(R.id.layout_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Toast.makeText(MainActivity.this, "aaa", Toast.LENGTH_SHORT).show();
                items.clear();
                adapter.notifyDataSetChanged();
                readRss();
            }
        });

    }//onCreate Method..


    //rss xml문서 읽어와서 파싱하는 작업 메소드
    void readRss(){

        try {
            URL url= new URL("http://rss.hankyung.com/new/news_main.xml");

            //실디바이스 중에서 oreo버전 이상에서는 보안강화로 인해 https만 허용하도록..함.
            url= new URL("https://rss.blog.naver.com/kim97o_o.xml");

            //스트림역결하여 데이터 읽어오기 : 인터넷 작업은 반드시 퍼미션 작성해야함
            //Network작업은 반드시 별도의 Thread만 할 수 있음.
            //별도의 Thread객체 생성
            RssFeedTask task= new RssFeedTask();
            task.execute(url);//doInBackground()메소드가 발동[thread의 start()와 같은 역할]

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }//readRss Method...

    //이너클래스
    class RssFeedTask extends AsyncTask<URL, Void, String> {

        //Thread의 run()메소드와 같은 역할
        @Override
        protected String doInBackground(URL... urls) {
            //전달받은 URL객체
            URL url= urls[0];

            //해임달(URL)에게 무지개로드(stream) 열도록..
            try {
                InputStream is= url.openStream();

                //읽어온 xml를 파싱(분석)해주는 객체 생성
                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
                XmlPullParser xpp= factory.newPullParser();

                //utf-8은 한글도 읽어오기 위한 인코딩 방식
                xpp.setInput(is,"utf-8");

                int eventType= xpp.getEventType();

                Item item= null;
                String tagName= null;

                while (eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG:
                            tagName= xpp.getName();
                            Log.i("TAG",tagName);

                            if(tagName.equals("item")){
                                item= new Item();
                            }else if(tagName.equals("title")){
                                xpp.next();
                                if(item!=null) item.setTitle(xpp.getText());
                            }else if(tagName.equals("link")){
                                xpp.next();
                                if(item!=null) item.setLink(xpp.getText());
                            }else if(tagName.equals("description")){
                                xpp.next();
                                if(item!=null) item.setDesc(xpp.getText());
                            }else if(tagName.equals("image")){
                                xpp.next();
                                if(item!=null) item.setImgUrl(xpp.getText());
                            }else if(tagName.equals("pubDate")){
                                xpp.next();
                                if(item!=null) item.setDate(xpp.getText());
                            }
                            break;

                        case XmlPullParser.TEXT:
                            break;

                        case XmlPullParser.END_TAG:
                            tagName= xpp.getName();
                            if(tagName.equals("item")){

                                Log.i("SSS",item.getTitle());

                                //읽어온 기사 한개를 대량의 데이터에 추가
                                items.add(item);
                                item=null;

                                //리사이클러의 아답터에게 데이터가
                                //변경되었다는 것을 통지(화면갱신)
                                //UI변경작업을 하고 싶다면..
                                publishProgress();//onProgressUpdate()라는 메소드 실행

//                                try { //차례대로 나오는 순서를 보기위한 시간조절
//                                    Thread.sleep(1000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                            }
                            break;
                    }

                    eventType= xpp.next();
                }//while

                //파싱작업이 완료되었다!!
                //토스트 보이기..단, 별도 스레드는
                //UI작업이 불가!
                //그래서 runOnUiThread()를 이용했었음.
                //이 UI작업을 하는 별도의 메소드로
                //결과를 리턴하는 방식을 사용

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }


            return "파싱종료";
        }//doInBackground()

        //doInBackground()작업 도중에
        //publishProgress()라는 메소드를
        //호출하면 자동으로 실행되는 메소드

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            //이 곳에서도 UI변경작업이 기능함
            adapter.notifyItemInserted(items.size());
        }

        //doInBackground메소드가 종료된 후
        //UI작업을 위해 자동 실행되는 메소드
        //runOnUiThread()와 비슷한 역할
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //스와이프 아이콘 제거하기
            refreshLayout.setRefreshing(false);

            //리사이클러에서 보여주는 데이터를 가진
            //아답터에게 데이터가 변경되었다고 공지
            //adapter.notifyDataSetChanged(); 용량이 커짐으로 인한 자리이동


            //이 메소드 안에서는 UI변경작업 가능
             Toast.makeText(MainActivity.this, s+":"+items.size(), Toast.LENGTH_SHORT).show();
        }
    }//RssFeedTask 클래스..

}//MainActivity class..

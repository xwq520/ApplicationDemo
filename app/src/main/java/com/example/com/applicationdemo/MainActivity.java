package com.example.com.applicationdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.com.application.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // 生产发送http网络请求的 请求队列
    private RequestQueue requestQueue = null;
    private   TextView txt = null;
    private NetworkImageView networkImageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
    }

    private void init(){
        // 生产发送http网络请求的 请求队列
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        networkImageView  = (NetworkImageView)findViewById(R.id.networkImageView);
        txt = (TextView)findViewById(R.id.txt_ShowRespones);
    }

    /*
    * 利用NetworkImageView显示网络图片

     * 利用Volley异步加载图片
     *
     * 注意方法参数: getImageListener(ImageView view, int defaultImageResId, int
     * errorImageResId) 第一个参数:显示图片的ImageView 第二个参数:默认显示的图片资源 第三个参数:加载错误时显示的图片资源
     */
    private void showImageByNetworkImageView(){
        String imageUrl="http://avatar.csdn.net/6/6/D/1_lfdfhl.jpg";

        // LruCache 保存一个强引用来限制内容数量，每当Item被访问的时候，此Item就会移动到队列的头部。
        //  当cache已满的时候加入新的item时，在队列尾部的item会被回收。
        //  如果你cache的某个值需要明确释放，重写entryRemoved()
        // 如果key相对应的item丢掉啦，重写create().这简化了调用代码，即使丢失了也总会返回。
        // 默认cache大小是测量的item的数量，重写sizeof计算不同item的 大小。  int cacheSize = 4 * 1024 * 1024; // 4MiB
        //  * 不允许key或者value为null
        //  当get（），put（），remove（）返回值为null时，key相应的项不在cache中

        // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
        // LruCache通过构造函数传入缓存值，以KB为单位。 一般来说最大值的1/8左右就可以了。
        int maxMemory = ((int) (Runtime.getRuntime().maxMemory() / 1024));
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(2 * 1024 * 1024){
            protected int sizeOf(String key, Bitmap value) {
                            return value.getByteCount();
                      }
         };
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);

 /*       ImageLoader.ImageListener listener = ImageLoader.getImageListener(networkImageView,
                R.mipmap.ic_launcher,);
        imageLoader.get(imageUrl, listener);
*/
        networkImageView.setTag("url");
        networkImageView.setImageUrl(imageUrl, imageLoader);
        networkImageView.setErrorImageResId(R.mipmap.error);
        networkImageView.setDefaultImageResId(R.mipmap.ic_launcher);

        // 情况缓存 evictAll
        // lruCache.evictAll();

        txt.setText("已缓存："+lruCache.size()+"=maxMemory:"+maxMemory);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            StringBuilder httpurl = new StringBuilder("http://gc.ditu.aliyun.com/geocoding?a=%E6%9D%AD%E5%B7%9E%E5%B8%82");
            // http请求的参数
            Map<String, String> map = new HashMap<String, String>();
            map.put("name1", "value1");
            map.put("name2", "value2");
            JSONObject jsonObject = new JSONObject(map);
            JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, httpurl.toString(), jsonObject,
                    new Response.Listener<JSONObject>() {
                          @Override
                        public void onResponse(JSONObject response) {
                            Log.d("TAG", response.toString());
                            txt.setText("请求后响应的JSON_DATA=" + response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            //超时时间10s,最大重连次数2次
            // jsonRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
            // 开启缓存，默认是开启缓存
            jsonRequest.setShouldCache(true);
            // 默认的是5M磁盘缓存
            requestQueue.add(jsonRequest);


           /* {
                //注意此处override的getParams()方法,在此处设置post需要提交的参数根本不起作用
                //必须象上面那样,构成JSONObject当做实参传入JsonObjectRequest对象里
                //所以这个方法在此处是不需要的
//    @Override
//    protected Map<String, String> getParams() {
//          Map<String, String> map = new HashMap<String, String>();
//            map.put("name1", "value1");
//            map.put("name2", "value2");

//        return params;
//    }
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    headers.put("Content-Type", "application/json; charset=UTF-8");

                    return headers;
                }
            };*/


        } else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(), "nav_gallery", Toast.LENGTH_LONG).show();
            txt.setText("");
        } else if (id == R.id.nav_slideshow) {
            showImageByNetworkImageView();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

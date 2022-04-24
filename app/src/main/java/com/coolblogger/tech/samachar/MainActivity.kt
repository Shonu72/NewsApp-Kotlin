package com.coolblogger.tech.samachar


import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest



class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)
         fetchData()
         mAdapter = NewsListAdapter(this)
        recyclerview.adapter = mAdapter
    }
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.drnews)
            .setTitle("SamaChar App")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes"
            ) { dialog, which ->
                finishAffinity();
                System.exit(0);
            }
            .setNegativeButton("No", null)
            .show()
    }
private fun fetchData(){
    val url =  "https://newsapi.org/v2/top-headlines?country=in&apiKey=f3df3a0baa174da5aa81321db0abf505"
    val jsonObjectRequest = object : JsonObjectRequest(
        Request.Method.GET,
        url,
        null,
        {
            Log.e("TAG", "fetchData: $it")
            val newsJsonArray = it.getJSONArray("articles")
            val newsArray = ArrayList<News>()
            for (i in 0 until newsJsonArray.length()) {
                val newsJsonObject = newsJsonArray.getJSONObject(i)
                val news = News(
                    newsJsonObject.getString("title"),
                    newsJsonObject.getString("author"),
                    newsJsonObject.getString("url"),
                    newsJsonObject.getString("urlToImage")
                )
                newsArray.add(news)
            }

            mAdapter.updatedNews(newsArray)
        },

        {
            Log.d("Error occur", "Try again..." + it.networkResponse.statusCode)
        }
    ) {
        override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            headers["User-Agent"] = "Mozilla/5.0"
            return headers
        }
    }
    MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
}
    override fun onItemClicked(item: News   ) {

        val builder  =  CustomTabsIntent.Builder()
       val  CustomTabsIntent  = builder.build();
        CustomTabsIntent.launchUrl(this, Uri.parse(item.url));


    }
}
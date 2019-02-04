package com.example.progressproject.ui.home

import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.example.progressproject.R
import com.example.progressproject.adapters.ArticleListAdapter
import com.example.progressproject.common.AppInjector
import com.example.progressproject.data.models.Doc
import com.example.progressproject.di.activity.ActivityComponent
import com.example.progressproject.di.activity.ActivityModule
import com.example.progressproject.di.application.ApplicationComponent
import com.example.progressproject.ui.detail.ArticleDetailActivity
import com.example.progressproject.ui.detail.ArticleDetailFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ArticleListAdapter.OnItemClickedListener {

    @Inject lateinit var articleListViewModel: ArticleListViewModel
    @Inject lateinit var activityComponent: ActivityComponent

    private lateinit var adapter: ArticleListAdapter
    private var docList = ArrayList<Doc>()
    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependancies()
        docList = ArrayList()
        linearLayoutManager = LinearLayoutManager(this)
        rvArticle.layoutManager = linearLayoutManager
        rvArticle.addItemDecoration(DividerItemDecoration(this, linearLayoutManager.orientation))
    }

    fun searchArticle(view: View) {
        val searchQuery: String = etSearchQuery.text.toString()
        articleListViewModel.getSearch(searchQuery).observe(this, Observer {
            rvArticle.adapter = ArticleListAdapter( it?.docs!!, this)
            docList = it.docs as ArrayList<Doc>
        })
    }

    private fun injectDependancies(){
        activityComponent =
                (application as AppInjector).applicationComponent.newActivityComponent(ActivityModule(this))
        activityComponent.injectHomeScreen(this)
    }

    /**
     * perform your click actions here
     */
    override fun onItemClicked(position: Int) {
        startActivity(Intent(this, ArticleDetailActivity::class.java).also {
            it.putExtra(ArticleDetailFragment.WEB_URL, docList[position].webUrl)
        })
    }
}

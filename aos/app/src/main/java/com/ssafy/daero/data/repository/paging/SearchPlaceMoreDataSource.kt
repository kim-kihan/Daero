package com.ssafy.daero.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.ssafy.daero.data.dto.search.ArticleMoreItem
import com.ssafy.daero.data.remote.SnsApi
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchPlaceMoreDataSource(private val snsApi: SnsApi, private val searchKeyword: String) : RxPagingSource<Int, ArticleMoreItem>() {
    override fun getRefreshKey(state: PagingState<Int, ArticleMoreItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ArticleMoreItem>> {
        val page = params.key ?: 1
        return snsApi.searchPlaceMore(searchKeyword, page)
            .subscribeOn(Schedulers.io())
            .map{
                PagingSource.LoadResult.Page(
                    data = it.results,
                    prevKey = null,
                    nextKey = if (page >= it.total_page) null else page + 1
                ) as PagingSource.LoadResult<Int, ArticleMoreItem>
            }
            .onErrorReturn {
                PagingSource.LoadResult.Error(it)
            }
    }

}
package com.ssafy.daero.ui.root.sns

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.daero.R
import com.ssafy.daero.application.App
import com.ssafy.daero.base.BaseFragment
import com.ssafy.daero.databinding.FragmentArticleWriteThumbnailBinding
import com.ssafy.daero.ui.adapter.sns.ArticleWriteThumbnailAdapter
import com.ssafy.daero.ui.root.RootFragment
import com.ssafy.daero.utils.constant.*
import com.ssafy.daero.utils.file.deleteCache
import com.ssafy.daero.utils.view.toast

class ArticleWriteThumbnailFragment :
    BaseFragment<FragmentArticleWriteThumbnailBinding>(R.layout.fragment_article_write_thumbnail) {

    private val articleWriteViewModel: ArticleWriteViewModel by viewModels({ requireParentFragment() })
    private lateinit var thumbnailAdapter: ArticleWriteThumbnailAdapter

    override fun init() {
        initViews()
        initAdapter()
        observeData()
        setOnClickListeners()
        setOtherListeners()
    }

    private fun initViews() {
        articleWriteViewModel.articleWriteRequest?.let {
            binding.editTextArticleWriteTitle.setText(it.title)
            binding.editTextCommentAddComment.setText(it.tripComment)
            binding.switchArticleWriteExpose.isChecked = it.expose == 'y'
        }
    }

    private val itemCheckListener: (Int) -> Unit = { position ->
        articleWriteViewModel.linearTripStamps[position].isThumbnail =
            !articleWriteViewModel.linearTripStamps[position].isThumbnail
        articleWriteViewModel.linearTripStamps.forEachIndexed { index, tripStampDto ->
            if (index != position) {
                tripStampDto.isThumbnail = false
            }
        }
        thumbnailAdapter.tripStamps = articleWriteViewModel.linearTripStamps
        thumbnailAdapter.notifyDataSetChanged()
    }

    private fun initAdapter() {
        thumbnailAdapter = ArticleWriteThumbnailAdapter(itemCheckListener).apply {
            tripStamps = articleWriteViewModel.linearTripStamps
        }
        binding.recyclerArticleWriteThumbnailTripStamp.adapter = thumbnailAdapter
    }

    private fun observeData() {
        articleWriteViewModel.postState.observe(viewLifecycleOwner) {
            when(it) {
                SUCCESS -> {
                    toast("???????????? ??????????????? ????????? ???????????????.")

                    // Room ??????
                    articleWriteViewModel.deleteAllTripRecord()

                    // ArticleWriteViewModel ?????? ?????????
                    articleWriteViewModel.finishWriteArticle()

                    // ?????? ???????????? ?????? ??????
                    deleteCache(requireContext())

                    // Prefs ?????????
                    App.prefs.initTrip()
                    App.prefs.tripState = TRIP_BEFORE

                    // fragment type ?????????
                    RootFragment.selectPosition = R.id.TripFragment

                    // RootFragment ??? ??????
                    findNavController().navigate(R.id.action_articleWriteThumbnailFragment_to_rootFragment)

                    articleWriteViewModel.postState.value = DEFAULT
                }
                FAIL -> {
                    toast("????????? ???????????? ?????????????????????.")
                    articleWriteViewModel.postState.value = DEFAULT
                }
            }
        }
        articleWriteViewModel.showProgress.observe(viewLifecycleOwner) {
            binding.progressBarArticleWritingLoading.isVisible = it
        }
    }

    private fun setOnClickListeners() {
        binding.buttonArticleWrite.setOnClickListener {
            val thumbnailIndex = articleWriteViewModel.getThumbnailIndex()
            if(thumbnailIndex < 0) {
                toast("????????? ????????? ??????????????????.")
                return@setOnClickListener
            }

            if(binding.editTextArticleWriteTitle.text.toString().isBlank()) {
                toast("????????? ??????????????????.")
                return@setOnClickListener
            }

            if(binding.editTextCommentAddComment.text.toString().isBlank()) {
                toast("????????? ??????????????????.")
                return@setOnClickListener
            }

            articleWriteViewModel.articleWriteRequest?.let {
                it.thumbnailIndex = articleWriteViewModel.getThumbnailIndex()
                it.title = binding.editTextArticleWriteTitle.text.toString()
                it.tripComment = binding.editTextCommentAddComment.text.toString()
            }

            articleWriteViewModel.postArticle()
        }

        binding.imgArticleWriteBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setOtherListeners() {
        binding.switchArticleWriteExpose.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                articleWriteViewModel.articleWriteRequest?.expose = 'y'
            } else {
                articleWriteViewModel.articleWriteRequest?.expose = 'n'
            }
        }
    }
}
package com.ssafy.daero.ui.root.sns

import androidx.lifecycle.MutableLiveData
import com.ssafy.daero.base.BaseViewModel
import com.ssafy.daero.data.dto.article.ReportRequestDto
import com.ssafy.daero.data.repository.SnsRepository
import com.ssafy.daero.utils.constant.FAIL
import com.ssafy.daero.utils.constant.SUCCESS

class ReportViewModel : BaseViewModel() {
    private val snsRepository = SnsRepository.get()

    val reportState = MutableLiveData<Int>()

    fun reportArticle(articleSeq: Int, report_seq: Int) {
        addDisposable(
            snsRepository.reportArticle(articleSeq, ReportRequestDto(report_seq))
                .subscribe(
                    {
                        reportState.postValue(SUCCESS)
                    },
                    { throwable ->
                        reportState.postValue(FAIL)
                    }
                )
        )
    }

    fun reportComment(replySeq: Int, report_seq: Int) {
        addDisposable(
            snsRepository.reportComment(replySeq, ReportRequestDto(report_seq))
                .subscribe(
                    {
                        reportState.postValue(SUCCESS)
                    },
                    { throwable ->
                        reportState.postValue(FAIL)
                    }
                )
        )
    }

    fun reportUser(userSeq: Int, report_seq: Int) {
        addDisposable(
            snsRepository.reportUser(userSeq, ReportRequestDto(report_seq))
                .subscribe(
                    {
                        reportState.postValue(SUCCESS)
                    },
                    { throwable ->
                        reportState.postValue(FAIL)
                    }
                )
        )
    }
}
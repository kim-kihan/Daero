package com.ssafy.daero.ui.root.trip


import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.daero.R
import com.ssafy.daero.application.App
import com.ssafy.daero.base.BaseFragment
import com.ssafy.daero.databinding.FragmentTripNextBinding
import com.ssafy.daero.ui.adapter.TripNearByAdapter
import com.ssafy.daero.ui.adapter.TripUntilNowAdapter
import com.ssafy.daero.utils.constant.*
import com.ssafy.daero.utils.view.toast


class TripNextFragment : BaseFragment<FragmentTripNextBinding>(R.layout.fragment_trip_next) {

    private val tripNextViewModel: TripNextViewModel by viewModels()
    private lateinit var tripNearByAdapter: TripNearByAdapter
    private lateinit var tripUntilNowAdapter: TripUntilNowAdapter

    override fun init() {
        setTripState()
        initAdapter()
        observeData()
        setOnClickListeners()
        getTripStamps()
        getAroundTrips()
    }

    private fun setTripState() {
        // 다음 여행지 추천 화면으로 온경우 다음 여행지 추천상태로 변경
        App.prefs.isFirstTrip = false
    }

    private fun initAdapter() {
        tripNearByAdapter = TripNearByAdapter().apply {
            onItemClickListener = nearByTripPlaceClickListener
        }
        binding.recyclerTripNextNearBy.adapter = tripNearByAdapter

        tripUntilNowAdapter = TripUntilNowAdapter().apply {
            onItemClickListener = tripStampClickListener
        }
        binding.recyclerTripNextNow.adapter = tripUntilNowAdapter
    }

    private val nearByTripPlaceClickListener: (View, Int) -> Unit = { _, tripPlaceSeq ->
        findNavController().navigate(R.id.action_rootFragment_to_tripInformationFragment,
        bundleOf(PLACE_SEQ to tripPlaceSeq, IS_RECOMMEND to false))
    }

    private val tripStampClickListener: (View, Int) -> Unit = { _, tripStampId ->
        findNavController().navigate(R.id.action_rootFragment_to_tripStampFragment,
            bundleOf(TRIP_STAMP_ID to tripStampId, IS_TRIP_STAMP_UPDATE to true))
    }

    private val applyOptions: (Int, String) -> Unit = { time, transportation ->
        App.prefs.tripTime = time
        App.prefs.tripTransportation = transportation
        tripNextViewModel.recommendNextPlace(time, transportation)
    }

    private fun setOnClickListeners() {
        binding.apply {

            buttonTripNextNextTripRecommend.setOnClickListener {
                TripNextBottomSheetFragment(applyOptions).show(
                    childFragmentManager,
                    "TripNextBottomSheetFragment"
                )
            }

            // TODO: 여행 그만두기 기능 = 게시글 작성
            buttonTripNextStop.setOnClickListener {

            }

            imageTripNextNotification.setOnClickListener {
                findNavController().navigate(R.id.action_rootFragment_to_notificationFragment)
            }
        }
    }

    private fun getTripStamps() {
        tripNextViewModel.getTripStamps()
    }

    private fun getAroundTrips() {
        tripNextViewModel.getAroundTrips(App.prefs.curPlaceSeq)
    }

    private fun observeData() {
        tripNextViewModel.aroundTrips.observe(viewLifecycleOwner) {
            tripNearByAdapter.apply {
                tripPlaces = it
                notifyDataSetChanged()
            }
        }
        tripNextViewModel.tripStamps.observe(viewLifecycleOwner) {
            tripUntilNowAdapter.apply {
                tripStamps = it
                notifyDataSetChanged()
            }
        }
        tripNextViewModel.showProgress.observe(viewLifecycleOwner) {
            binding.progressBarTripNextLoading.isVisible = it
        }
        tripNextViewModel.nextTripRecommendResponseDto.observe(viewLifecycleOwner) { placeSeq ->
            if(placeSeq > 0) {
                findNavController().navigate(
                    R.id.action_rootFragment_to_tripInformationFragment, bundleOf(
                        PLACE_SEQ to placeSeq
                    )
                )
                tripNextViewModel.initNextTripRecommend()
            }
        }
        tripNextViewModel.nextTripRecommendState.observe(viewLifecycleOwner) {
            when(it) {
                FAIL -> {
                    toast("여행지 추천을 받는데 실패했습니다.")
                    tripNextViewModel.nextTripRecommendState.value = DEFAULT
                }
                EMPTY -> {
                    toast("해당 조건에 맞는 여행지가 없습니다.")
                    tripNextViewModel.nextTripRecommendState.value = DEFAULT
                }
            }
        }
    }
}
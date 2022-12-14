package com.ssafy.daero.data.remote

import com.ssafy.daero.data.dto.article.ArticleWriteRequestDto
import com.ssafy.daero.data.dto.common.PagingResponseDto
import com.ssafy.daero.data.dto.trip.*
import com.ssafy.daero.data.dto.user.ImageUploadResponseDto
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface TripApi {
    /**
     * 트립스탬프 상세 조회
     * */
    @GET("trips/trip-stamp/{trip_stamp_seq}")
    fun getTripStampDetail(
        @Path("trip_stamp_seq") tripStampSeq: Int
    ): Single<Response<TripStampDetailResponseDto>>

    /**
     * 내 여정 조회
     */
    @GET("trips/my/{user_seq}/journey")
    fun getMyJourney(
        @Path("user_seq") user_seq: Int,
        @Query("start-date") startDate: String,
        @Query("end-date") endDate: String
    ): Single<Response<List<List<MyJourneyResponseDto>>>>

    /**
     * 여정 조회
     */
    @GET("trips/user/{user_seq}/journey")
    fun getJourney(
        @Path("user_seq") user_seq: Int,
        @Query("start-date") startDate: String,
        @Query("end-date") endDate: String
    ): Single<Response<List<List<MyJourneyResponseDto>>>>

    /**
     * 내 여행 앨범 조회
     */
    @GET("trips/my/{user_seq}/album")
    fun getMyAlbum(
        @Path("user_seq") userSeq: Int,
        @Query("page") page: Int
    ): Single<PagingResponseDto<TripAlbumItem>>

    /**
     * 여행 앨범 조회
     */
    @GET("trips/user/{user_seq}/album")
    fun getAlbum(
        @Path("user_seq") userSeq: Int,
        @Query("page") page: Int
    ): Single<PagingResponseDto<TripAlbumItem>>

    /**
     * 첫 여행지 추천
     */
    @POST("trips/recommend")
    fun getFirstTripRecommend(
        @Body firstTripRecommendRequestDto: FirstTripRecommendRequestDto
    ): Single<Response<FirstTripRecommendResponseDto>>

    /**
     * 여행지 정보
     */
    @GET("trips/info/{trip_places_seq}")
    fun getTripInformation(@Path("trip_places_seq") placeSeq: Int): Single<Response<TripInformationResponseDto>>

    /**
     * 다음 여행지 추천
     * */
    @GET("trips/recommend")
    fun recommendNextPlace(
        @Query("place-seq") placeSeq: Int,
        @Query("time") time: Int,
        @Query("transportation") transportation: String
    ): Single<Response<NextPlaceRecommendResponseDto>>

    /**
     * 인기있는 여행지
     */
    @GET("trips/popular")
    fun getPopularTrips(): Single<Response<List<TripPopularResponseDto>>>

    /**
     * 내 주변 여행지
     */
    @GET("trips/nearby")
    fun getAroundTrips(@Query("place-seq") placeSeq: Int): Single<Response<List<TripPopularResponseDto>>>

    /**
     * 여행 마무리하기
     */
    @Multipart
    @POST("trips")
    fun postArticle(
        @Part files: List<MultipartBody.Part>,
        @Part("json") json: RequestBody
    ): Single<Response<Unit>>
}
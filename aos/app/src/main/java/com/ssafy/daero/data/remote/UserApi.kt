package com.ssafy.daero.data.remote


import com.ssafy.daero.data.dto.login.*
import com.ssafy.daero.data.dto.resetPassword.ResetPasswordRequestDto
import com.ssafy.daero.data.dto.resetPassword.ResetPasswordResponseDto
import com.ssafy.daero.data.dto.signup.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface UserApi {
    @POST("users/login")
    fun emailLogin(@Body loginRequestDto: LoginRequestDto): Single<LoginResponseDto>

    @GET("users/{email_address}")
    fun findID(@Path("email_address") email: String): Single<FindIDResponseDto>

    @PUT("users/{user_seq}/profile")
    fun editProfile(
        @Path("user_seq") userSeq: Int,
        @Body profileEditRequest: ProfileEditRequest
    ): Single<Void>

    @POST("users/reset-password")
    fun findPassword(@Body loginRequestDto: FindPasswordRequestDto): Single<FindPasswordResponseDto>

    @GET("users/{user_email}/reset-password")
    fun emailCheck(@Path("user_email") email: String): Single<FindPasswordCheckEmailResponseDto>

    @POST("users/{password_reset_seq}/reset-password")
    fun findPasswordModify(
        @Body findPasswordModifyRequestDto: FindPasswordModifyRequestDto,
        @Path("password_reset_seq") reset_seq: String
    ): Single<FindPasswordModifyResponseDto>

    @POST("users/email")
    fun verifyEmail(@Body signupEmailRequestDto: SignupEmailRequestDto): Single<SignupEmailResponseDto>

    @GET("users/{user_seq}/verified")
    fun verifyUserEmail(@Path("user_seq") user_seq: Int): Single<VerifyUserEmailResponseDto>

    @POST("users/nickname")
    fun verifyNickname(@Body signupNicknameRequestDto: SignupNicknameRequestDto): Single<SignupNicknameResponseDto>

    @POST("users/signup")
    fun signup(@Body signupRequestDto: SignupRequestDto): Single<Void>

    @GET("users/{user_seq}/preference")
    fun getPreferences(@Path("user_seq") userSeq: Int): Single<MutableList<TripPreferenceResponseDto>>

    /**
      선호도 조사 결과 전송
     * */
    @POST("users/{user_seq}/preference")
    fun postPreferencse(
        @Path("user_seq") userSeq: Int,
        @Body preferenceList: List<Int>
    ): Single<Void>

    @POST("users/{user_seq}/password")
    fun confirmPassword(
        @Path("user_seq") userSeq: Int,
        @Body resetPasswordRequestDto: ResetPasswordRequestDto
    ): Single<ResetPasswordResponseDto>

    @PUT("users/{user_seq}/password")
    fun updatePassword(
        @Path("user_seq") userSeq: Int,
        @Body resetPasswordRequestDto: ResetPasswordRequestDto
    ): Single<ResetPasswordResponseDto>

}
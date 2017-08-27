package com.dharanaditya.bcards.api;

import com.dharanaditya.bcards.model.AccessCredentials;
import com.dharanaditya.bcards.model.BCard;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by dharan1011 on 27/8/17.
 */

public interface LinkedInService {
    String REQUEST_AUTH_CODE_URL = "https://www.linkedin.com/oauth/v2/authorization";
    String REQUEST_ACCESS_TOKEN_URL = "https://www.linkedin.com";
    String API_BASE_URL = "https://api.linkedin.com";
    String PARAM_RESPONSE_TYPE = "response_type";
    String PARAM_REDIRECT_URL = "redirect_uri";
    String PARAM_CLIENT_ID = "client_id";
    String PARAM_GRANT_TYPE = "grant_type";
    String PARAM_CODE = "code";
    String PARAM_CLIENT_SECRET = "client_secret";
    String PARAM_FORMAT = "json";

    @FormUrlEncoded
    @POST("oauth/v2/accessToken")
    Call<AccessCredentials> getAccessToken(
            @Field(PARAM_GRANT_TYPE) String grantType,
            @Field(PARAM_CODE) String code,
            @Field(PARAM_REDIRECT_URL) String redirectUrl,
            @Field(PARAM_CLIENT_ID) String clientId,
            @Field(PARAM_CLIENT_SECRET) String clientSecret
    );


    @GET("/v1/people/~:(id,email-address,first-name,last-name,public-profile-url,picture-url,headline)")
    Call<BCard> getBCardData(@Header("Authorization") String accessToken,
                             @Query("format") String format);
}

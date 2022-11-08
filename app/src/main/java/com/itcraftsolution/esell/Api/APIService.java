package com.itcraftsolution.esell.Api;

import com.itcraftsolution.esell.Notifications.MyResponse;
import com.itcraftsolution.esell.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    // Firebase Notification Key
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA42xtKnc:APA91bFZqdu2tUzjeA7QBWgvNColWIZl6nqT3tcIvZv3IYjhxhUddPMEsPL6PGz2Xz-ru7uaW90-5OWThjr4ycjSPR8_h-b5mH4RshoNvUGc30MWEVScgeSjl3annmgjnJDIH_rnpryF"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}

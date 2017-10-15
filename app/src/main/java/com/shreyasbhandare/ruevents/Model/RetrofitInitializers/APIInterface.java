package com.shreyasbhandare.ruevents.Model.RetrofitInitializers;

import com.shreyasbhandare.ruevents.Model.POJO.EventsPerPage;
import com.shreyasbhandare.ruevents.Model.POJO.PageList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    //https://graph.facebook.com/v2.10/search?fields=cover,name,id&access_token=1963239783915001|va9v9A46La360H7g-H3bn65SYxg&limit=400&q=rutgers&type=page
    @GET("/v2.10/search")
    Call<PageList> getPageList(@Query("fields") String fields,
                               @Query("access_token") String access_token,
                               @Query("limit") String limit,
                               @Query("q") String q,
                               @Query("type") String page);

    //https://graph.facebook.com/v2.10/993599127355768/events?fields=cover,start_time,name&access_token=1963239783915001|va9v9A46La360H7g-H3bn65SYxg
    @GET("/v2.10/{page_id}/events")
    Call<EventsPerPage> getEventPerPageList(@Path("page_id") String page_id,
                                            @Query("fields") String fields,
                                            @Query("access_token") String access_token,
                                            @Query("since") String since);

}
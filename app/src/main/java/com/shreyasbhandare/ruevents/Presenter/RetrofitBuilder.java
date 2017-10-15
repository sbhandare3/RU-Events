package com.shreyasbhandare.ruevents.Presenter;

import com.shreyasbhandare.ruevents.Model.RetrofitInitializers.APIClient;
import com.shreyasbhandare.ruevents.Model.RetrofitInitializers.APIInterface;

public interface RetrofitBuilder {
    APIInterface apiInterface = APIClient.getAPIClient().create(APIInterface.class);
    String ACCESS_TOKEN = "1963239783915001|va9v9A46La360H7g-H3bn65SYxg";
}

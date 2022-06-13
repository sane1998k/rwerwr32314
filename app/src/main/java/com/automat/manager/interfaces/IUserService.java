package com.automat.manager.interfaces;

import com.automat.manager.requests.CheckBalloonRequest;
import com.automat.manager.requests.CreateOrderRequest;
import com.automat.manager.requests.CreateOrderRequest2;
import com.automat.manager.requests.CreateReturnBalloonByNameDriverRequest;
import com.automat.manager.requests.CreateReturnBalloonRequest;
import com.automat.manager.requests.CreateReturnRequest;
import com.automat.manager.requests.LoginRequest;
import com.automat.manager.requests.PutOrdersRequest;
import com.automat.manager.responses.GetAllGottenBalloons;
import com.automat.manager.responses.GetBalloonById;
import com.automat.manager.responses.GetCarResponse;
import com.automat.manager.responses.GetCounterpartyResponse;
import com.automat.manager.responses.GetCreateOrdersResponse;
import com.automat.manager.responses.GetCreateReturnBalloonResponse;
import com.automat.manager.responses.GetDriversResponse;
import com.automat.manager.responses.GetGasTypesResponse;
import com.automat.manager.responses.GetReturnOrdersResponse;
import com.automat.manager.responses.GetReturningsBody;
import com.automat.manager.responses.GetUserResponse;
import com.automat.manager.responses.PidorasObj1;
import com.automat.manager.responses.ReturnBalloonResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IUserService {
    @Headers("Content-Type: application/json")
    @POST("api/v1/LogIn/")
    Call<String> userLogin(@Body LoginRequest loginRequest);

    @POST("api/v1/Orders/")
    Call<GetCreateOrdersResponse> getCreateOrdersResponse(@Header("Authorization") String authToken, @Body CreateOrderRequest createOrderRequest);

    @POST("api/v1/Orders/Detail/{orderDetailId}/Balloons")
    Call<GetCreateReturnBalloonResponse> createReturnBalloon(@Header("Authorization") String authToken, @Body CreateReturnBalloonRequest createReturnBalloonRequest, @Path("orderDetailId") String orderDetailId);

    @GET("api/v1/GetUserInfo")
    Call<GetUserResponse> getUserInfo(@Header("Authorization") String authToken);

    @GET("api/v1/ReferenceBook/Counterparty")
    Call<ArrayList<GetCounterpartyResponse>> getCounterparty(@Header("Authorization") String authToken);

    @GET("api/v1/ReferenceBook/GasTypes")
    Call<ArrayList<GetGasTypesResponse>> getGasTypes(@Header("Authorization") String authToken);
    @GET("api/v1/ReferenceBook/Drivers")
    Call<ArrayList<GetDriversResponse>> getDrivers(@Header("Authorization") String authToken);
    @GET("api/v1/ReferenceBook/Cars")
    Call<ArrayList<GetCarResponse>> getCars(@Header("Authorization") String authToken);
    @GET("api/v1/Balloons/{id}")
    Call<GetBalloonById> getBalloonById(@Header("Authorization") String authToken, @Path("id") String id);
    @GET("api/v1/Orders/New")
    Call<ArrayList<PidorasObj1>> getNewOrders(@Header("Authorization") String authToken);
    @GET("/api/v1/Orders/Returnings/Collected")
    Call<ArrayList<GetReturnOrdersResponse>> getReturnOrder(@Header("Authorization") String authToken);

    @GET("api/v1/Orders/Collected")
    Call<ArrayList<PidorasObj1>> getCollectedOrder(@Header("Authorization") String authToken);

    @GET("api/v1/Orders/")
    Call<ArrayList<GetCreateOrdersResponse>> getOrders(@Header("Authorization") String authToken);

    @Headers({"Content-Type: application/json"})
    @PUT("api/v1/Orders/{id}")
    Call<PutOrdersRequest> updateOrders(@Header("Authorization") String authToken, @Path("id") String id, @Body PutOrdersRequest putOrdersRequest);
    @PUT("/api/v1/Orders/Returnings/{returningId}/Balloons/{id}/Chеck")
    Call<Void> checkReturnBalloon(@Header("Authorization") String authToken, @Path("returningId") String returningId, @Path("id") String id, @Body CheckBalloonRequest putOrdersRequest);
    @PUT("/api/v1/Orders/{orderId}/Balloons/{id}/Chеck")
    Call<Void> checkOrderBalloon(@Header("Authorization") String authToken, @Path("orderId") String orderId,  @Path("id") String id, @Body CheckBalloonRequest putOrdersRequest);
    @DELETE("api/v1/Orders/Balloons/{id}")
    Call<Void> deleteReturnedBalloon(@Header("Authorization") String authToken, @Path("id") String id);
    @PUT("/api/v1/Orders/Details/{detailsId}/ToComplete")
    Call<Void> setCompleteOrder(@Header("Authorization") String authToken, @Path("detailsId") String detailsId);
    @PUT("/api/v1/Orders/ToDone/{id}")
    Call<Void> toDoneOrder(@Header("Authorization") String authToken, @Path("id") String id);
    @GET("api/v1/Orders/Detail/{orderDetailId}/Balloons")
    Call<ArrayList<GetAllGottenBalloons>> getAllGottenBalloons(@Header("Authorization") String authToken, @Path("orderDetailId") String orderDetailId);

    @GET("/api/v1/Orders/{orderId}/Balloons")
    Call<ArrayList<GetAllGottenBalloons>> getAllGottenBalloons2(@Header("Authorization") String authToken, @Path("orderId") String orderId);

    @DELETE("/api/v1/Orders/Returnings/Balloons/{id}")
    Call<Void> deleteReturnBalloonDriver(@Header("Authorization") String authToken, @Path("id") String id);

    @POST("/api/v1/Orders/Returnings/{returningId}/Balloons")
    Call<GetCreateOrdersResponse> createReturnBalloon1(@Header("Authorization") String authToken, @Path("returningId") String returningId, @Body ReturnBalloonResponse returnBalloonResponse);

    @PUT("/api/v1/Orders/Returnings/{returningId}/Balloons/{balloonName}/ChеckByName")
    Call<Void> checkBalloonByNameCollector(@Header("Authorization") String authToken, @Path("returningId") String returningId, @Path("balloonName") String balloonName, @Body CheckBalloonRequest checkBalloonRequest);

    @PUT("/api/v1/Orders/{orderId}/Balloons/{id}/ChеckByName")
    Call<Void> checkBalloonByNameDriver(@Header("Authorization") String authToken, @Path("orderId") String orderId, @Query("name") String name, @Body CheckBalloonRequest checkBalloonRequest);

    @GET("/api/v1/Orders/Returnings/{returningId}/Balloons")
    Call<ArrayList<GetAllGottenBalloons>> createReturnBalloon2(@Header("Authorization") String authToken, @Path("returningId") String returningId);

    @POST("/api/v1/Orders/{orderId}/Returnings")
    Call<GetReturningsBody> createReturn(@Header("Authorization") String authToken, @Path("orderId") String orderId, @Body CreateReturnRequest createReturnRequest);

    @POST("/api/v1/Orders/Returnings/{returningId}/BalloonsByName")
    Call<Void> createReturnBalloonByNameDriver(@Header("Authorization") String authToken, @Path("returningId") String returningId, @Body CreateReturnBalloonByNameDriverRequest createReturnBalloonByNameDriverRequest);

    @GET("/api/v1/Orders/{orderId}/Returnings")
    Call<ArrayList<GetReturningsBody>> getCreateReturn(@Header("Authorization") String authToken, @Path("orderId") String orderId);

    @PUT("/api/v1/Orders/Returnings/ToComplete/{id}")
    Call<Void> returnToComplete(@Header("Authorization") String authToken, @Path("id") String id);

    @PUT("/api/v1/Orders/Returnings/ToDone/{id}")
    Call<Void> toDoneReturnOrder(@Header("Authorization") String authToken, @Path("id") String id);
}

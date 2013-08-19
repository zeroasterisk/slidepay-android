package com.slidepay.resthandler;

import org.json.JSONObject;

/**
 * Created by Alex on 8/15/13.
 */
public abstract class ResponseHandler {
    /**
     * The *Handler classes in the resthandler package all make remote requests. When invoking the
     * various methods in these classes, you usually supply an anonymous instance of this class (with
     * onSuccess and onFailure overriden) to receive the results of those requests.
     */

    /**
     * @param response The result of the API request. The actual type of the response is detailed
     *                 in the corresponding RestHandler subclass. For example, the OrderHandler class specifies
     *                 that getSummaries(...) populates response with an ArrayList<Order>.
     */
    public abstract void onSuccess(Object response);//, Class<?> type);

    /**
     * Your anonymous instance will override this method to be notified of failed API requests.
     * @param e Network requests can result in a ton of exceptions. If e is null, then we've received something in the 200 range, but
     *          the request didn't actually succeed. Check the response field for details.
     * @param response         if a response could be parsed, we throw it into a JSON object
     * @param errorCode        The error code, if present, that was returned by the server.
     * @param errorDescription if we get a human readable error response from the server, we pass it along to you. If not, this will be null
     */
    public abstract void onFailure(Throwable e, JSONObject response, int errorCode, String errorDescription);


    /**
     * Error Codes:
     *
     */

}

package com.gracecode.iZhihu.Tasks;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FetchQuestion extends BaseTasks<Boolean, Void, Void> {
    private final static int MAX_FETCH_TIMES = 3600 * 1000 * 2; // 2 hours

    public FetchQuestion(Context context, Callback callback) {
        super(context, callback);
    }

    @Override
    protected Void doInBackground(Boolean... booleans) {
        try {
            for (Boolean focus : booleans) {
                if (!focus && System.currentTimeMillis() - requester.getLastRequestTimeStamp() < MAX_FETCH_TIMES) {
                    return null;
                }

                int startId = database.getStartId();
                JSONArray fetchedData = requester.fetch(startId);
                for (int i = 0, length = fetchedData.length(); i < length; i++) {
                    JSONObject item = (JSONObject) fetchedData.get(i);
                    database.insertSingleQuestion(item);
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NetworkErrorException e) {
            e.printStackTrace();
        }
        return null;
    }
}

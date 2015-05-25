/*
 * Copyright (c) 2015 Coder.HanXin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coderhanxin.xpride.ui.timeline;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.Toast;

import com.coderhanxin.xpride.R;
import com.coderhanxin.xpride.ui.base.BaseActivity;
import com.coderhanxin.xpride.ui.custom_views.StatusCardLayout;
import com.coderhanxin.xpride.utils.TimeUtils;
import com.coderhanxin.xpride.weibo.AccessTokenKeeper;
import com.coderhanxin.xpride.weibo.Constants;
import com.orhanobut.logger.Logger;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.squareup.picasso.Picasso;

/**
 * Created by CoderHanXin on 2015/05/24.
 */
public class FriendsTimelineActivity extends BaseActivity {

    private StatusCardLayout mCardLayout;

    /**
     * 当前 Token 信息
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 用于获取微博信息流等操作的API
     */
    private StatusesAPI mStatusesAPI;
    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                Logger.json(response);
                if (response.startsWith("{\"statuses\"")) {
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    StatusList statuses = StatusList.parse(response);
                    Status status = statuses.statusList.get(0);
                    mCardLayout.setName(status.user.screen_name);
                    mCardLayout.setCreateTime(TimeUtils.instance(FriendsTimelineActivity.this).buildTimeString(status.created_at));
                    mCardLayout.setSource(Html.fromHtml(status.source).toString());
                    mCardLayout.setText(status.text);
                    Picasso.with(FriendsTimelineActivity.this).load(status.user.avatar_large).into(mCardLayout.getmProfileImageView());

                    if (statuses != null && statuses.total_number > 0) {
                        Toast.makeText(FriendsTimelineActivity.this,
                                "获取微博信息流成功, 条数: " + statuses.statusList.size(),
                                Toast.LENGTH_LONG).show();
                    }
                } else if (response.startsWith("{\"created_at\"")) {
                    // 调用 Status#parse 解析字符串成微博对象
                    Status status = Status.parse(response);
                    Toast.makeText(FriendsTimelineActivity.this,
                            "发送一送微博成功, id = " + status.id,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FriendsTimelineActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Logger.e(e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(FriendsTimelineActivity.this, info.toString(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 对statusAPI实例化
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
        mStatusesAPI.friendsTimeline(0L, 0L, 10, 1, false, 0, false, mListener);

    }

    @Override
    public void setContentView() {

        setContentView(R.layout.activity_friends_timeline);
    }

    @Override
    public void findViews() {

        mCardLayout = (StatusCardLayout) findViewById(R.id.card);
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }
}

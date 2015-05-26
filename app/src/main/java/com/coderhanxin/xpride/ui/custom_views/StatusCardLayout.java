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

package com.coderhanxin.xpride.ui.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.coderhanxin.xpride.R;

/**
 * Created by CoderHanXin on 2015/05/25.
 */
public class StatusCardLayout extends FrameLayout {

    private ImageView mProfileImageView;
    private TextView mNameTextView;
    private TextView mCreateTimeTextView;
    private TextView mSourceTextView;
    private TextView mText;

    public StatusCardLayout(Context context) {
        this(context, null);
    }

    public StatusCardLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.timeline_item_card, this, true);
        mProfileImageView = (ImageView) findViewById(R.id.profile_image);
        mNameTextView = (TextView) findViewById(R.id.screen_name);
        mCreateTimeTextView = (TextView) findViewById(R.id.create_time);
        mSourceTextView = (TextView) findViewById(R.id.source);
        mText = (TextView) findViewById(R.id.text);
    }

    public void setName(String name) {
        mNameTextView.setText(name);
    }

    public void setCreateTime(String createTime) {
        mCreateTimeTextView.setText(createTime);
    }

    public void setSource(String source) {
        mSourceTextView.setText(source);
    }

    public void setText(String text) {
        mText.setText(text);
    }

    public ImageView getProfileImageView() {
        return mProfileImageView;
    }
}

package net.qiujuer.italker.common.widget.adapter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public abstract class TextWatcherAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

package com.vary.UI;

import com.vary.Models.CategoryModel;

import java.util.List;

public interface SetDataCallback {
    void onLoaded(List<CategoryModel> categories);
    void onLoaded(Integer version);
    void onLoaded(Throwable t);
}

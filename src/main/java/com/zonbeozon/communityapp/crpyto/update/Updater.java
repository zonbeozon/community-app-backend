package com.zonbeozon.communityapp.crpyto.update;

import com.zonbeozon.communityapp.crpyto.execute.ExecuteType;

import java.util.Set;

public interface Updater {
    void update();
    Set<ExecuteType> getExecuteTypes();
}

package com.augurit.aplanmis.data.exchange.convert;

import com.augurit.aplanmis.data.exchange.convert.datatree.DataTree;

import java.util.List;

public interface DataTreeCovert<K, V extends DataTree> {
    V convert(K k);

    List<V> convert(List<K> ks);
}

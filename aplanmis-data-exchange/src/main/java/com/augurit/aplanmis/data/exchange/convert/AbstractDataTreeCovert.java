package com.augurit.aplanmis.data.exchange.convert;

import com.alibaba.fastjson.JSON;
import com.augurit.aplanmis.data.exchange.convert.datatree.DataTree;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractDataTreeCovert<K, V extends DataTree> implements DataTreeCovert<K, V> {
    abstract String getId(K k);

    abstract String getParentId(K k);

    @Override
    public V convert(K k) {
        V dataTree;
        try {
            // 通过反射获取model的真实类型
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class<V> clazz = (Class<V>) pt.getActualTypeArguments()[1];
            // 通过反射创建model的实例
            dataTree = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        BeanUtils.copyProperties(k, dataTree);
        dataTree.setId(this.getId(k));
        return dataTree;
    }

    @Override
    public List<V> convert(List<K> ks) {
        List<K> roots = this.getRoots(ks);
        List<V> DataTrees = new ArrayList();
        for (K k : roots) {
            V v = this.convert(k);
            addChild(v, ks);
            DataTrees.add(v);
        }
        return DataTrees;
    }

    protected List<K> getRoots(List<K> ks) {
        List<K> roots = new ArrayList();
        Iterator<K> iterator = ks.iterator();
        while (iterator.hasNext()) {
            K root = iterator.next();
            if (this.isRoot(root)) {
                roots.add(root);
                iterator.remove();
            }
        }
        return roots;
    }

    protected boolean isRoot(K k) {
        return this.getId(k).equals(this.getParentId(k));
    }

    protected void addChild(DataTree root, List<K> ks) {
        List<DataTree> childData = new ArrayList();
        Iterator<K> iterator = ks.iterator();
        while (iterator.hasNext()) {
            K child = iterator.next();
            if (root.getId().equals(this.getParentId(child))) {
                DataTree convert = this.convert(child);
                childData.add(convert);
                iterator.remove();
            }
        }
        if (childData.size() > 0) {
            root.setChildren(childData);
            root.setHasChildren(true);
            /*for (DataTree c : childData) {
                this.addChild(c, ks);
            }*/
        }
    }
}

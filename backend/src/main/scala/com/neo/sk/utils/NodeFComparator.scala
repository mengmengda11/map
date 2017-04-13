package com.neo.sk.utils

/**
  * Created by zm on 2017/4/13.
  */
class NodeFComparator {
  @Override
  def compare(o1:NodeX,o2:NodeX): Unit ={
    return o1.getF-o2.getF
  }

}

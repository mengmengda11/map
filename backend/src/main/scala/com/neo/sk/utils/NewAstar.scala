package com.neo.sk.utils

/**
  * Created by zm on 2017/4/17.
  */

import java.util.ArrayList
import java.util.List

import scala.collection.mutable.ListBuffer

object NewAstar {
  var NODES: Array[Array[Int]] =null
//    Array(Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
//      Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
//      Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
//      Array(0, 0, 0, 1, 0, 0, 0, 0, 0),
//      Array(0, 0, 0, 1, 0, 0, 0, 0, 0),
//      Array(0, 0, 0, 1, 0, 0, 0, 0, 0),
//      Array(0, 0, 0, 1, 0, 0, 0, 0, 0),
//      Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
//      Array(0, 0, 0, 0, 0, 0, 0, 0, 0))
  val STEP: Int = 10

  def main(nodeArray: Array[Array[Int]],start:NewAstar.Node,end:NewAstar.Node ):Array[Array[Int]]= {
    NODES=nodeArray
    //val startNode: NewAstar.Node = new NewAstar.Node(5, 1)
    //val endNode: NewAstar.Node = new NewAstar.Node(5, 5)
    val startNode=start
    val endNode=end
    var parent: NewAstar.Node = new NewAstar().findPath(startNode,endNode)
    var endNodeArray:Array[Array[Int]]=nodeArray


    for(i<- 0 to NODES.length-1){
      for(j<- 0 to NODES(0).length-1){
        System.out.print(NODES(i)(j) + ", ")
      }
      System.out.println
    }
    val arrayList: ListBuffer[NewAstar.Node] = new ListBuffer[NewAstar.Node]
    while (parent != null) {
      {
        arrayList.append(new NewAstar.Node(parent.x, parent.y))
        parent = parent.parent
      }
    }
    System.out.println("\n")

    for(i<- 0 to NODES.length-1){
      for(j<- 0 to NODES(0).length-1){
        if (exists(arrayList, i, j)) {
          System.out.print("@, ")
          endNodeArray(i)(j)=6
        }
        else {
          System.out.print(NODES(i)(j) + ", ")
        }
      }
      System.out.println
    }

   return endNodeArray
  }




  def find(nodes: ListBuffer[NewAstar.Node], point: NewAstar.Node): NewAstar.Node = {
    for (n <- nodes) if ((n.x == point.x) && (n.y == point.y)) {
      return n
    }
    return null
  }

  def exists(nodes: ListBuffer[NewAstar.Node], node: NewAstar.Node): Boolean = {
    for (n <- nodes) {
      if ((n.x == node.x) && (n.y == node.y)) {
        return true
      }
    }
    return false
  }

  def exists(nodes: ListBuffer[NewAstar.Node], x: Int, y: Int): Boolean = {
    for (n <- nodes) {
      if ((n.x == x) && (n.y == y)) {
        return true
      }
    }
    return false
  }

  class Node(var x: Int, var y: Int) {
    var F: Int = 0
    var G: Int = 0
    var H: Int = 0

    def calcF {
      this.F = this.G + this.H
    }

    var parent: NewAstar.Node = null
  }

}



class NewAstar {
  private val openList: ListBuffer[NewAstar.Node] = new ListBuffer[NewAstar.Node]
  private val closeList: ListBuffer[NewAstar.Node] = new ListBuffer[NewAstar.Node]

  def findMinFNodeInOpneList: NewAstar.Node = {
    var tempNode: NewAstar.Node = openList(0)

    for (node <- openList) {
      if (node.F < tempNode.F) {
        tempNode = node
      }
    }
    return tempNode
  }

  def findMinFNodeInOpneNumber: Int = {
    var tempNode:Int =0

    for (a <- 0 to  openList.length-1) {
      if (openList(a).F < openList(tempNode).F) {
        tempNode = a
      }
    }
    return tempNode
  }

  def findNeighborNodes(currentNode: NewAstar.Node): ListBuffer[NewAstar.Node] = {
    val arrayList: ListBuffer[NewAstar.Node] = new ListBuffer[NewAstar.Node]
    val topX: Int = currentNode.x
    val topY: Int = currentNode.y - 1
    if (canReach(topX, topY) && !NewAstar.exists(closeList, topX, topY)) {
      arrayList.append(new NewAstar.Node(topX, topY))
    }
    val bottomX: Int = currentNode.x
    val bottomY: Int = currentNode.y + 1
    if (canReach(bottomX, bottomY) && !NewAstar.exists(closeList, bottomX, bottomY)) {
      arrayList.append(new NewAstar.Node(bottomX, bottomY))
    }
    val leftX: Int = currentNode.x - 1
    val leftY: Int = currentNode.y
    if (canReach(leftX, leftY) && !NewAstar.exists(closeList, leftX, leftY)) {
      arrayList.append(new NewAstar.Node(leftX, leftY))
    }
    val rightX: Int = currentNode.x + 1
    val rightY: Int = currentNode.y
    if (canReach(rightX, rightY) && !NewAstar.exists(closeList, rightX, rightY)) {
      arrayList.append(new NewAstar.Node(rightX, rightY))
    }
    return arrayList
  }

  def canReach(x: Int, y: Int): Boolean = {
    if (x >= 0 && x < NewAstar.NODES.length && y >= 0 && y < NewAstar.NODES(0).length) {
      return NewAstar.NODES(x)(y) == 0
    }
    return false
  }

  def findPath(startNode: NewAstar.Node, endNode: NewAstar.Node): NewAstar.Node = {
    openList.append(startNode)
    while (openList.size > 0) {
      {
        val currentNode: NewAstar.Node = findMinFNodeInOpneList
        val currentNodeNumber: Int =findMinFNodeInOpneNumber
        openList.remove(currentNodeNumber)
        closeList.append(currentNode)
        val neighborNodes: ListBuffer[NewAstar.Node] = findNeighborNodes(currentNode)
        for (node <- neighborNodes) {
          if (NewAstar.exists(openList, node)) {
            foundPoint(currentNode, node)
          }
          else {
            notFoundPoint(currentNode, endNode, node)
          }
        }
        if (NewAstar.find(openList, endNode) != null) {
          return NewAstar.find(openList, endNode)
        }
      }
    }
    return NewAstar.find(openList, endNode)
  }

  private def foundPoint(tempStart: NewAstar.Node, node: NewAstar.Node) {
    val G: Int = calcG(tempStart, node)
    if (G < node.G) {
      node.parent = tempStart
      node.G = G
      node.calcF
    }
  }

  private def notFoundPoint(tempStart: NewAstar.Node, end: NewAstar.Node, node: NewAstar.Node) {
    node.parent = tempStart
    node.G = calcG(tempStart, node)
    node.H = calcH(end, node)
    node.calcF
    openList.append(node)
  }

  private def calcG(start: NewAstar.Node, node: NewAstar.Node): Int = {
    val G: Int = NewAstar.STEP
    val parentG: Int = if (node.parent != null) node.parent.G
    else 0
    return G + parentG
  }

  private def calcH(end: NewAstar.Node, node: NewAstar.Node): Int = {
    val step: Int = Math.abs(node.x - end.x) + Math.abs(node.y - end.y)
    return step * NewAstar.STEP
  }
}

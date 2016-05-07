package com.trietree;

import java.util.LinkedList;
import java.util.List;

public class Node {
	char content; // 该节点存放的字母
	boolean isEnd;// 是否是单词的最后一个字母
	int count; // 包含该字母的所有单词的个数
	LinkedList<Node> childList; //子串
	List<Document> docList;//存储对应的文档信息
	
	public Node(char c) {
		childList = new LinkedList<Node>();
		isEnd = false;
		content = c;
		count = 0;
	}

	public Node subNode(char c) {
		if (childList != null) {
			for (Node eachChild : childList) {
				if (eachChild.content == c) {
					return eachChild;
				}
			}
		}
		return null;
	}
}

//package com.trietree;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//
//import com.newone.Document;
//
//public class Main {
//	static final String ProcessPath="D:\\Workspace\\SearchEngine\\data\\processed";
//	static final String QueryPath="D:\\Workspace\\SearchEngine\\data\\query.txt";
//	static final String ResultPath="D:\\Workspace\\SearchEngine\\data\\result.txt";
//	static double Avgdl;
//	public static Trie mytrie = new Trie();
//	//所有的不重复的单词构成词表
//	//public static HashSet<String> KeyWords= new HashSet<String>();
//	//索引index为Map型。记录了词在文档中出现的次数+文档的长度+文档的名字
//	//public static HashMap<String,List<Document>> Index=new HashMap<String,List<Document>>();
//	//记录每篇文档的长度
//	public static HashMap<String,Integer> docLen=new HashMap<String,Integer>();
//	//记录query每行的关键词
//	public static List<String[]> myquery=new ArrayList<String[]>();
//	//记录得分的HashMap<String,Double>
//	public static HashMap<String,Double> Score=new HashMap<String,Double>();
//	// 获得文件路径
//	public static List<File> getFileList(String dirPath) {
//		File[] files = new File(dirPath).listFiles();
//		List<File> fileList = new ArrayList<File>();
//		for (File file : files) {
//			fileList.add(file);
//		}
//		return fileList;
//	}
//	// 读取process中的所有文件，构造词表
//	public static void Vocabulary(List<File> fileList) {
//		for (File list : fileList) {
//			readTxt(list.getName());
//		}
//	}
//	//按行读取txt(xml)文件的内容+完成索引的创建
//	public static void readTxt(String fileName) {
//		String fullPath=ProcessPath+"\\"+fileName;
//		File file = new File(fullPath);
//		BufferedReader reader = null;
//		//记录文档的长度
//		int documentLength=0;
//		//存储每篇文档的词典
//		HashSet<String> dictionary= new HashSet<String>();
//		//记录词和出现的次数的Map对
//		HashMap<String,Integer> wordAndFreq=new HashMap<String,Integer>();
//		try {
//			//System.out.println("以行为单位读取文件内容，一次读一行");
//			reader = new BufferedReader(new FileReader(file));
//			String tempString = null;
//			int line = 1;
//			// 一次读一行，读入null时文件结束
//			while ((tempString = reader.readLine()) != null) {
//				// 把当前行号显示出来
//				//System.out.println("line " + line + ": " + tempString);
//				if(line==1){
//					line++;
//					continue;
//				}else{
//					String[] tmp = tempString.split(" ");
//					documentLength=documentLength+tmp.length;
//					for(String s:tmp)
//					{
//						if(dictionary.contains(s)){
//							wordAndFreq.put(s, wordAndFreq.get(s)+1);
//						}else{
//							wordAndFreq.put(s, 0);
//							dictionary.add(s);
//						}
//					}
//				}
//			}
//			//读完一篇文档之后进行索引的存储
//			for (String word:dictionary){
//				Document doc=new Document();
//				List<Document> myList=new ArrayList<Document>();
//				doc.freq=wordAndFreq.get(word);
//				doc.length=documentLength;
//				doc.ID=fileName.replace(".xml","");
//				myList.add(doc);
//				if(mytrie.search(word)){
//					Index.get(word).add(doc);
//				}else{
//					Index.put(word,myList);
//				}
//				docLen.put(doc.ID, doc.length);
//			}
//			reader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (reader != null) {
//				try {
//					reader.close();
//				} catch (IOException e1) {
//				}
//			}
//		}
//	}
//	//BM25得分计算
//	public static double BM25(double tf,int n_qi,int dl){
//		int N=docLen.size();
//		double IDF=Math.log((N-n_qi+0.5)/(n_qi+0.5));
//		int k1=2;
//		double b=0.75;
//		double K=k1*(1-b+b*dl/avgLength());
//		double R=tf*(k1+1)/(tf+K);
//		double score=IDF*R;
//		return score;
//	}
//	//单纯地按行读取query中txt(xml)文件的内容
//	public static void readTxtOnly() {
//		String fullPath=QueryPath;
//		File file = new File(fullPath);
//		BufferedReader reader = null;
//		try {
//			reader = new BufferedReader(new FileReader(file));
//			String tempString = null;
//			int line = 101;
//			// 一次读一行，读入null时文件结束
//			while ((tempString = reader.readLine()) != null) {
//				//记录每个关键词返回的文档得分
//				HashMap<String,Double> Score=new HashMap<String,Double>();
//				String[] tmp = tempString.split(" ");
//				myquery.add(tmp);
//			}
//			reader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (reader != null) {
//				try {
//					reader.close();
//				} catch (IOException e1) {
//				}
//			}
//		}
//	}
//
//	// 针对query进行查询
//	public static void doResearch() throws IOException {
//		int line = 101;
//		System.out.println("开始执行查询了");
//		int flag=1;
//		for (String[] t : myquery) {
//			long startTime=System.currentTimeMillis();   //获取查询开始时间
//			System.out.println("查询序号："+line);
//			// 针对某个词进行得分计算
//			for (int i = 0; i < t.length; i++) {
//				if (Index.containsKey(t[i])) {
//					for (Document dd : Index.get(t[i])) {
//						System.out.println(flag++);
//						double tf = (double) dd.freq / (double) dd.length;
//						int dl = dd.length;
//						int n_qi = Index.get(t[i]).size();
//						double score = BM25(tf, n_qi, dl);
//						if (Score.containsKey(dd.ID)) {
//							Score.put(dd.ID, score + Score.get(dd.ID));
//						} else {
//							Score.put(dd.ID, score);
//						}
//					}
//				}
//			}
//			System.out.println("第"+line+"个查询结束了");
//			long endTime=System.currentTimeMillis(); 	//获取结束时间
//			long runtime=(endTime-startTime)/1000;
//			System.out.println("查询第"+line+"个query时间："+runtime+"s");
//			long startTime_sort=System.currentTimeMillis();   //获取排序开始时间
//			List<Map.Entry<String, Double>> list_Data = sortByValue(Score);
//			long endTime_sort=System.currentTimeMillis(); 	//获取排序结束时间
//			long runtime_sort=(endTime_sort-startTime_sort);
//			System.out.println("排序第"+line+"个query时间："+runtime_sort+"ms");
//			long startTime_write=System.currentTimeMillis();   //获取写入txt开始时间
//			for (int i = 0; i < 1000; i++) {
//				String resultText = line + " " + list_Data.get(i).getValue() + " " + list_Data.get(i).getKey();
//				WriteStringToFile(ResultPath, resultText);
//			}
//			long endTime_write=System.currentTimeMillis(); 	//获取结束时间
//			long runtime_write=(endTime_write-startTime_write);
//			System.out.println("写入第"+line+"个query结果的时间："+runtime_write+"ms");
//			Score.clear();
//			line++;
//		}
//	}
//	//将String类型写入txt文件中
//	public static void WriteStringToFile(String filePath,String resultText) throws IOException {
//		BufferedWriter bw = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (filePath, true), "UTF-8"));
//		bw.write (resultText);
//		bw.write("\r\n");
//		bw.close();
//    }
//
//	// java清空txt文档内容
//	public static void ClearTxt(String path) {
//		try {
//			File f = new File(path);
//			FileWriter fw = new FileWriter(f);
//			BufferedWriter bw = new BufferedWriter(fw);
//			bw.write("");
//		} catch (Exception e) {
//		}
//	}
//	//计算整个文档集的平均长度
//	public static double avgLength() {
//		int length_all = 0;
//		for (String dd : docLen.keySet()) {
//			length_all = length_all + docLen.get(dd);
//		}
//		Avgdl = (double) (length_all) / docLen.size();
//		return Avgdl;
//	}
//
//	// 对一个Map型的数据按照Value值由大到小排序
//	public static List<Map.Entry<String, Double>> sortByValue(HashMap<String, Double> score) {
//		List<Map.Entry<String, Double>> list_Data = new ArrayList<Map.Entry<String, Double>>(score.entrySet());
//		Collections.sort(list_Data, new Comparator<Map.Entry<String, Double>>() {
//			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
//				if ((o2.getValue() - o1.getValue()) > 0)
//					return 1;
//				else if ((o2.getValue() - o1.getValue()) == 0)
//					return 0;
//				else
//					return -1;
//			}
//		});
//		return list_Data;
//
//	}
//	public static void main(String[] args) throws IOException {
//		// TODO Auto-generated method stub
//		ClearTxt(ResultPath);
//		long startTime_index=System.currentTimeMillis();   //获取开始时间
//		List<File> fileList=getFileList(ProcessPath);
//		
//		long endTime_index=System.currentTimeMillis(); 	//获取结束时间
//		long runtime_index=(endTime_index-startTime_index)/1000;
//		System.out.println("索引建立时间"+runtime_index+"s");
//		avgLength();
//		long startTime_search=System.currentTimeMillis();   //获取开始时间
//		readTxtOnly();
//		doResearch();
//		long endTime_search=System.currentTimeMillis(); 	//获取结束时间
//		long runtime_search=(endTime_search-startTime_search)/1000;
//		System.out.println("查询时间"+runtime_search+"s");
//	}
//
//}

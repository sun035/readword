package cn.wt.readword;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;



public class PoiReadWordTest {
	static String wordPath;
	@Before
	public void before(){
		wordPath="C:\\Users\\thinkpad\\Desktop\\工作报告合并\\差异化防雷技术支持-工作总结.doc";
	}
  
  @Test
  public  void testWord() {
      try {
               FileInputStream in = new FileInputStream(wordPath);// 载入文档
                POIFSFileSystem pfs = new POIFSFileSystem(in);
               HWPFDocument hwpf = new HWPFDocument(pfs);
               Range range = hwpf.getRange();// 得到文档的读取范围
               TableIterator it = new TableIterator(range);
              // 迭代文档中的表格
             while (it.hasNext()) {
                    Table tb = (Table) it.next();
                   // 迭代行，默认从0开始
                   for (int i = 0; i < tb.numRows(); i++) {
                            TableRow tr = tb.getRow(i);
                             // 迭代列，默认从0开始
                            for (int j = 0; j < tr.numCells(); j++) {
                                TableCell td = tr.getCell(j);// 取得单元格
                                // 取得单元格的内容
                                for (int k = 0; k < td.numParagraphs(); k++) {
                                       Paragraph para = td.getParagraph(k);
                                       String s = para.text().trim();
                                       System.out.println(s);
                                  } 
                            }
                     } 
               } 
         } catch (Exception e) {
             e.printStackTrace();
         }
      }
  
  @Test
  public  void testWord2() {//Microsoft Word 97 - 2003 文档 (.doc)
	  WordExtractor ex = null;
	  InputStream is =null;
	  try {
	  is = new FileInputStream(new File(wordPath));
	  ex = new WordExtractor(is);
	  String[] text = ex.getParagraphText();
	  for(String str:text){
		  System.out.println("----------------------------------");
		  System.out.println(str);
		  System.out.println("----------------------------------");
	  }
	 
	  } catch (Exception e) {
	  e.printStackTrace();
	  }finally{
		  if (is != null ) {
			  try {
				is.close();
				ex.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
	  }
	  }
  
  @Test
  public  void testWord3() {//Microsoft Word 97 - 2003 文档 (.doc)
	  
	  WordExtractor ex = null;
	  InputStream is =null;
	  try {
	  is= getClass().getClassLoader().getResourceAsStream("工作报告.doc");
	  ex = new WordExtractor(is);
	  StringArrayReader str=new StringArrayReader(ex.getParagraphText());
	  SwitchStateMachine s=new SwitchStateMachine(str,new WordEntity());
	  try {
          while (true){
        	  s.process();
          }
      } catch (StringArrayReader.EOFException e) {
      }
	  WordEntity word = s.getWord();
	  System.out.println(JSON.toJSONString(word));
	  } catch (Exception e) {
		  e.printStackTrace();
	  }finally{
		  if (is != null ) {
			  try {
				is.close();
				ex.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
	  }
	  }
  
  
  
}

package cn.wt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;


public class PoiWordUtil {
	
	/**
	 *  合并word文件   .docx
	 * @param first  空白文件
	 * @param files  将集合中的文件输出到空白文件中
	 * 
	 * 对于空白文件需要注意每次都需要进行重新生成
	 * 否则同一个空白文件会造成多线程覆盖
	 * 
	 */
	public static  void mergeWord(File first,File...files) {
		OutputStream dest = null;
		OPCPackage src1Package;
		XWPFDocument src1Document = null;
		try {
			dest = new FileOutputStream(first);
			src1Package = OPCPackage.open(new FileInputStream(files[0]));
			src1Document = new XWPFDocument(src1Package);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		//获取将要进行文件合并的第一个文件的内容
		CTBody src1Body = src1Document.getDocument().getBody();
		String srcString = src1Body.xmlText();
		//获取第一个内容的前缀
		String prefix = srcString.substring(0,srcString.indexOf(">")+1);
		//主体内容
		String mainPart = srcString.substring(srcString.indexOf(">")+1,srcString.lastIndexOf("<"));
		//后缀
		String sufix = srcString.substring( srcString.lastIndexOf("<") );
		//拼接其他文件的主体内容
		StringBuffer otherPart=new StringBuffer();
		XWPFDocument otherDocument = null;
		for(int i=1;i<files.length;i++){
			OPCPackage otherPackage;
			try {
				otherPackage = OPCPackage.open(new FileInputStream(files[i]));
				otherDocument = new XWPFDocument(otherPackage);
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			CTBody  otherBody =  otherDocument.getDocument().getBody();
			addPart(otherPart, otherBody);
		}
		CTBody makeBody;
		try {
			makeBody = CTBody.Factory.parse(prefix+mainPart+otherPart.toString()+sufix);
			src1Body.set(makeBody);
			src1Document.write(dest);
		} catch (IOException e) {
			e.printStackTrace();
		}catch (XmlException e) {
			e.printStackTrace();
		}finally{
			//因需要关闭流，不进行抽取方法
			try {
				if(src1Document!=null)src1Document.close();
				if(otherDocument!=null)otherDocument.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		}
	private static void addPart(StringBuffer otherPart, CTBody otherBody) {
		XmlOptions optionsOuter = new XmlOptions();
		optionsOuter.setSaveOuter();
		String appendString = otherBody.xmlText(optionsOuter);
		String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
		otherPart.append(addPart);
	}

}

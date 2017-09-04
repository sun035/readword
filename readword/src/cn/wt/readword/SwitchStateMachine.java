package cn.wt.readword;

public class SwitchStateMachine  {
	private   StringArrayReader reader;
	private   WordEntity word;
	private   String ch;
    private   StringBuffer str;
    public SwitchStateMachine(StringArrayReader reader, WordEntity word) {
		super();
		this.reader = reader;
		this.word = word;
		str=new StringBuffer();
	}

	public WordEntity getWord() {
		return word;
	}

	enum State {
        Init, AfterDW, AfterName,AfterGZNR,AfterGZYQ,AfterGZJHL,AfterKZQK,AfterKZQK2,
        AfterXMCX,End;
    }

    private State state = State.Init;

    public synchronized void process() throws StringArrayReader.EOFException{
        
        switch (state) {
            case Init:
                ch = reader.read();
                word.setTitle(ch);
                state = State.AfterDW;
                break;
            case AfterDW:
                ch = reader.read();
				int indexOf = ch.indexOf("承担单位");
				if (indexOf!=-1) {
	               word.setDanwei(ch.substring(indexOf+5));
	               state = State.AfterName;
	               break;
	            }else{
	               state = State.AfterDW;
	               break;
	            } 
	                
            case AfterName:
            	 ch = reader.read();
 				int indexOf2 = ch.indexOf("主要完成人");
 				if (indexOf2!=-1) {
 	               word.setNames(ch.substring(indexOf2+6));
 	               state = State.AfterGZNR;
 	              break;
 	            }else{
 	               state = State.AfterName;
 	              break;
 	            } 
            case AfterGZNR:
           	 	ch = reader.read();
				int indexOf3 = ch.indexOf("工作内容");
				if (indexOf3!=-1) {
	               word.setGznr(reader.read());//这里取的是工作内容的下一句话
	               state = State.AfterGZYQ;
	               break;
	            }else{
	               state = State.AfterGZNR;
	               break;
	            }
            case AfterGZYQ:
           	 	ch = reader.read();
				int indexOf4 = ch.indexOf("工作要求");
				if (indexOf4!=-1) {
	               word.setGzyq(reader.read());//这里取的是工作要求的下一句话
	               state = State.AfterGZJHL;
	               break;
	            }else{
	               state = State.AfterGZYQ;
	               break;
	            }
            case AfterGZJHL:
           	 	ch = reader.read();
				int indexOf5 = ch.indexOf("计划工作量");
				if (indexOf5!=-1) {
	               word.setJhgzl(reader.read());//这里取的是计划工作量的下一句话
	               state = State.AfterKZQK;
	               break;
	            }else{
	               state = State.AfterGZJHL;
	               break;
	            }
            case AfterKZQK:
            	//拼接工作开展情况内容
           	 	ch = reader.read();
				int indexOf6 = ch.indexOf("工作开展情况");
				if (indexOf6!=-1) {
					str.append(reader.read());
	                state = State.AfterKZQK2;
	                break;
	            }else{
	               state = State.AfterKZQK;
	               break;
	            }
            case AfterKZQK2:
           	 	ch = reader.read();
				int indexOf7 = ch.indexOf("项目成效");
				if (indexOf7==-1) {
					str.append(ch);
	                state = State.AfterKZQK2;
	                break;
	            }else{
	            	word.setGzkzqk(str.toString());
	                state = State.AfterXMCX;
	                break;
	            }	
            case AfterXMCX:
            	ch = reader.read();
	            word.setXmcx(ch);//这里取的是项目成效的下一句话
	            state = State.End;
	            break;
            case End:
            	ch = reader.read();
				int indexOf8 = ch.indexOf("工作建议");
				if (indexOf8!=-1)
					word.setGzjy(reader.read());
	            break;    
        }
    }

   
}
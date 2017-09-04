package cn.wt.readword;

public class StringArrayReader {

	class EOFException extends Exception {

    }

    private String[] string;

    private int index;

    public StringArrayReader(String[] string) {
        this.string = string;
    }

    public String read() throws EOFException {
        if (index < string.length) {
            return string[index++];
        } else {
        	 throw new EOFException();
        }
    }

 
	

}
